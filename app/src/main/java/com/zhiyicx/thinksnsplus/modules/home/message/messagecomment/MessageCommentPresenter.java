package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import com.google.gson.Gson;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CommentRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE;

/**
 * @Describe
 * @Author zl
 * @Date 2017/2/8
 * @Contact master.jungle68@gmail.com
 * 不在使用缓存 CommentedBeanGreenDaoImpl 2018-5-12 10:23:51 by tym
 */
@FragmentScoped
public class MessageCommentPresenter extends AppBasePresenter<MessageCommentContract.View> implements
        MessageCommentContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MessageCommentPresenter(MessageCommentContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        Subscription commentSub = mUserInfoRepository.clearUserMessageCount(UserFollowerCountBean.UserBean.MESSAGE_TYPE_COMMENTED)
                .flatMap((Func1<Object, Observable<List<CommentedBean>>>) o -> mUserInfoRepository.getMyComments(maxId.intValue()))
                .subscribe(new BaseSubscribeForV2<List<CommentedBean>>() {
                    @Override
                    protected void onSuccess(List<CommentedBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(commentSub);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(new ArrayList<>(), true);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CommentedBean> data, boolean isLoadMore) {

        return true;
    }

    @Override
    public void sendComment(int mCurrentPostion, long replyToUserId, String commentContent) {
        CommentedBean currentCommentBean = mRootView.getListDatas().get(mCurrentPostion);
        String path = CommentRepository.getCommentPath(currentCommentBean.getTarget_id(), currentCommentBean.getChannel(), currentCommentBean
                .getSource_id());
        Subscription commentSub = mCommentRepository.sendCommentV2(commentContent, replyToUserId, Long.parseLong(AppApplication
                .getmCurrentLoginAuth().getUser_id() + "" + System.currentTimeMillis()), path)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.comment_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.comment_success));
                        requestNetData(0L, false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.comment_fail));
                    }
                });
        addSubscrebe(commentSub);

    }

    @Override
    public void payNote(int dynamicPosition, long amount, int imagePosition, int note, final boolean isImage) {
        if (handleTouristControl()) {
            return;
        }
//        double amount;
//        if (isImage) {
//            amount = mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition).getAmount();
//        } else {
//            amount = mRootView.getListDatas().get(dynamicPosition).getPaid_node().getAmount();
//        }
        Gson gson = new Gson();
        DynamicDetailBeanV2 dynamicDetailBean = gson.fromJson(gson.toJson(mRootView.getListDatas().get(dynamicPosition).getCommentable()), DynamicDetailBeanV2.class);
        Subscription subscribe = handleIntegrationBlance(amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.ts_pay_check_handle_doing)))
                .flatMap(o -> mCommentRepository.paykNote(note))
                .flatMap(stringBaseJsonV2 -> {
                    if (isImage) {
                        return Observable.just(stringBaseJsonV2);
                    }
                    stringBaseJsonV2.setData(dynamicDetailBean.getFeed_content());
                    return Observable.just(stringBaseJsonV2);
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.hideCenterLoading();
                        mRootView.paySuccess();
                        if (isImage) {
//                            DynamicDetailBeanV2.ImagesBean imageBean = mRootView.getListDatas().get(dynamicPosition).getImages().get(imagePosition);
//                            imageBean.setPaid(true);
//                            int imageWith = imageBean.getCurrentWith();
//                            if (imageWith == 0) {
//                                imageWith = DEFALT_IMAGE_WITH;
//                            }
//                            // 重新给图片地址赋值 ,没付费的图片 w h 都是 0
//                            imageBean.setGlideUrl(ImageUtils.imagePathConvertV2(true, imageBean.getFile(), imageWith, imageWith,
//                                    imageBean.getPropPart(), AppApplication.getTOKEN()));
                        } else {
                            dynamicDetailBean.getPaid_node().setPaid(true);
                            dynamicDetailBean.setFeed_content(data.getData());
                            if (data.getData() != null) {
                                String friendlyContent = data.getData().replaceAll(MarkdownConfig.NETSITE_FORMAT, Link
                                        .DEFAULT_NET_SITE);
                                if (friendlyContent.length() > DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE) {
                                    friendlyContent = friendlyContent.substring(0, DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE) + "...";
                                }
                                dynamicDetailBean.setFriendlyContent(friendlyContent);
                            }
                        }
                        mRootView.refreshData(dynamicPosition);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.transaction_success));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.transaction_fail));
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.hideCenterLoading();
                    }
                });
        addSubscrebe(subscribe);
    }
}
