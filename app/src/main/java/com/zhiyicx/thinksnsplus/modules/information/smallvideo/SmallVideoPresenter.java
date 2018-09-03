package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicCommentBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.DynamicCommentBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment.SmallVideoCommentPresenter;
import com.zhiyicx.thinksnsplus.modules.information.smallvideo.comment.SmallVideoCommentView;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.TSShareUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */
@FragmentScoped
public class SmallVideoPresenter extends AppBasePresenter<SmallVideoContract.View> implements 
        SmallVideoContract.Presenter, OnShareCallbackListener,SmallVideoCommentPresenter {

    @Inject
    BaseDynamicRepository mBaseDynamicRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    DynamicCommentBeanGreenDaoImpl mDynamicCommentBeanGreenDao;

    @Inject
    public SharePolicy mSharePolicy;

    /**
     * 评论
     */
    private SmallVideoCommentView mSmallVideoCommentView;

    public void setSmallVideoCommentView(SmallVideoCommentView mSmallVideoCommentView) {
        this.mSmallVideoCommentView = mSmallVideoCommentView;
    }

    @Inject
    public SmallVideoPresenter(SmallVideoContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mBaseDynamicRepository.getSmallVideoList(maxId,mRootView.getUserId())
                .subscribe(new BaseSubscribeForV2<DynamicBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicBeanV2 data) {
                        mRootView.onNetResponseSuccess(data.getFeeds(),isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null,isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

        mRootView.onCacheResponseSuccess(mRootView.getInitVideoList(),isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void handleLike(DynamicDetailBeanV2 bean) {
        bean.setHas_digg(!bean.getHas_digg());
        // 通知服务器
        mBaseDynamicRepository.handleLike(bean.getHas_digg(), bean.getId());
    }

    @Override
    public void handleFollow(UserInfoBean userInfoBean) {
        mUserInfoRepository.handleFollow(userInfoBean);

        //更新列表中所有该用户视频的关注状态
        for (DynamicDetailBeanV2 data:mRootView.getListDatas()) {
            //用==不会进入判断，此类型是  Long  类型
            if(String.valueOf(data.getUserInfoBean().getUser_id()).equals(String.valueOf(userInfoBean.getUser_id()))){
                data.getUserInfoBean().setFollower(userInfoBean.getFollower());
            }
        }
        //mRootView.refreshData();

    }

    @Override
    public void shareDynamic(DynamicDetailBeanV2 dynamicBean, Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(dynamicBean.getFeed_content());
        shareContent.setContent("    ");
        shareContent.setImage(ImageUtils.getVideoUrl(dynamicBean.getVideo().getCover_id()));
        shareContent.setVideoUrl(TSShareUtils.convert2ShareUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean
                .getId()
                == null ? "" : dynamicBean.getId(), mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault())).getUser_code())));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void onStart(Share share) {
    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }

    @Override
    public void requestCommentList(Long maxId, boolean isLoadMore) {
        if(null == mSmallVideoCommentView)
            return;

        Subscription subscribe = mBaseDynamicRepository.getDynamicCommentListV2(mSmallVideoCommentView.getCurrentDynamic().getFeed_mark(),
                mSmallVideoCommentView.getCurrentDynamic().getId(), maxId)
                .subscribe(new BaseSubscribeForV2<List<DynamicCommentBean>>() {
                    @Override
                    protected void onSuccess(List<DynamicCommentBean> data) {
                        mSmallVideoCommentView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mSmallVideoCommentView.onNetResponseError();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mSmallVideoCommentView.onNetResponseError();
                    }
                });
        addSubscrebe(subscribe);
        
    }

    @Override
    public void sendCommentV2(long replyToUserId, String commentContent) {
        if(null == mSmallVideoCommentView || null == mSmallVideoCommentView.getCurrentDynamic())
            return;

        // 生成一条评论
        DynamicCommentBean creatComment = new DynamicCommentBean();
        creatComment.setState(DynamicCommentBean.SEND_ING);
        creatComment.setComment_content(commentContent);
        creatComment.setFeed_mark(mSmallVideoCommentView.getCurrentDynamic().getFeed_mark());
        String comment_mark = AppApplication.getMyUserIdWithdefault() + "" + System
                .currentTimeMillis();
        creatComment.setComment_mark(Long.parseLong(comment_mark));
        creatComment.setReply_to_user_id(replyToUserId);
        //当回复动态的时候
        if (replyToUserId == 0) {
            UserInfoBean userInfoBean = new UserInfoBean();
            userInfoBean.setUser_id(replyToUserId);
            creatComment.setReplyUser(userInfoBean);
        } else {
            creatComment.setReplyUser(mUserInfoBeanGreenDao.getSingleDataFromCache(replyToUserId));
        }
        creatComment.setUser_id(AppApplication.getMyUserIdWithdefault());
        creatComment.setCommentUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()));
        creatComment.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        mDynamicCommentBeanGreenDao.insertOrReplace(creatComment);
        // 处理评论数
        mSmallVideoCommentView.getCurrentDynamic().setFeed_comment_count(mSmallVideoCommentView.getCurrentDynamic()
                .getFeed_comment_count() + 1);

        mSmallVideoCommentView.getListDatas().add(0, creatComment);
        mSmallVideoCommentView.refreshData();

        mBaseDynamicRepository.sendCommentV2(commentContent, mSmallVideoCommentView.getCurrentDynamic().getId(),
                replyToUserId, creatComment.getComment_mark());
        
    }
}
