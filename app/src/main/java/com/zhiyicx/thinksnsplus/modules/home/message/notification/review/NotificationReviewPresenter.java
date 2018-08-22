package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import com.zhiyicx.baseproject.em.manager.util.TSEMessageUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

/**
 * author: huwenyong
 * date: 2018/8/16 13:50
 * description:
 * version:
 */

public class NotificationReviewPresenter extends AppBasePresenter<NotificationReviewContract.View>
        implements NotificationReviewContract.Presenter {

    @Inject
    ChatInfoRepository mChatInfoRepository;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public NotificationReviewPresenter(NotificationReviewContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        addSubscrebe((mRootView.isFriendReview() ? mChatInfoRepository.getFriendReviewList(maxId) :
                mChatInfoRepository.getGroupReviewList()).subscribe(
                new BaseSubscribeForV2<List<GroupOrFriendReviewBean>>() {
                    @Override
                    protected void onSuccess(List<GroupOrFriendReviewBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                }));

        //清空未读消息数量
        if(!isLoadMore){
            mUserInfoRepository.clearUserMessageCount(mRootView.isFriendReview()?
                    UserFollowerCountBean.UserBean.MESSAGE_TYPE_FRIEND:
                    UserFollowerCountBean.UserBean.MESSAGE_TYPE_GROUP)
                    .subscribe(new BaseSubscribeForV2<Object>() {
                        @Override
                        protected void onSuccess(Object data) {

                        }
                    });
        }

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupOrFriendReviewBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void requestAgreeOrInjectApply(GroupOrFriendReviewBean bean, boolean isAgree) {
        addSubscrebe((mRootView.isFriendReview() ?
                mChatInfoRepository.reviewFriendApply(bean.getId(), isAgree).map(s -> {
                    if (isAgree) {
                        //创建会话，发送一条消息
                        TSEMessageUtils.sendAgreeFriendApplyMessage(bean.getUser_id());
                    }
                    return s;
                }) : mChatInfoRepository.reviewGroupApply(bean.getId(), isAgree))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        bean.setStatus(isAgree ? 1 : 2);
                        mRootView.refreshData();
                    }
                }));

    }

    @Override
    public void clearApplyList() {

        addSubscrebe((mRootView.isFriendReview() ? mChatInfoRepository.clearFriendApplyList() :
                mChatInfoRepository.clearGroupApply()).doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        mRootView.onNetResponseSuccess(new ArrayList<>(), false);
                    }
                }));
    }
}
