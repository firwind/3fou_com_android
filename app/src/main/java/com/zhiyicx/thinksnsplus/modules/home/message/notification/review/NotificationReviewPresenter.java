package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/8/16 13:50
 * description:
 * version:
 */

public class NotificationReviewPresenter extends AppBasePresenter<NotificationReviewContract.View>
        implements NotificationReviewContract.Presenter{

    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    public NotificationReviewPresenter(NotificationReviewContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mBaseFriendsRepository.getFriendReviewList(maxId.intValue())
                .subscribe(new BaseSubscribeForV2<List<GroupOrFriendReviewBean>>() {
                    @Override
                    protected void onSuccess(List<GroupOrFriendReviewBean> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupOrFriendReviewBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void requestAgreeOrInjectApply(GroupOrFriendReviewBean bean,boolean isAgree) {
        mBaseFriendsRepository.reviewFriendApply(bean.getId(),isAgree?1:2)
                .observeOn(Schedulers.io())
                .map(s -> {
                    if(isAgree){
                        //创建会话，发送一条消息
                        EMClient.getInstance().chatManager().getConversation(bean.getUser_id(),
                                EMConversation.EMConversationType.Chat, true);
                        EMMessage message = EMMessage.createTxtSendMessage("我们已经成为好友啦~来一起聊天吧", bean.getUser_id());
                        EMClient.getInstance().chatManager().sendMessage(message);
                        EMClient.getInstance().chatManager().saveMessage(message);
                    }
                    return s;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<String>(mRootView){
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        bean.setStatus(isAgree?1:2);
                        mRootView.refreshData();
                    }
                });
    }
}
