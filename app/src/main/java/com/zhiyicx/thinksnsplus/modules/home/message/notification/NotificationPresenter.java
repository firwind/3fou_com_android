package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NotificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UnReadNotificaitonBean;
import com.zhiyicx.thinksnsplus.data.beans.UnreadCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/8/15 10:45
 * description:
 * version:
 */
@FragmentScoped
public class NotificationPresenter extends AppBasePresenter<NotificationContract.View> implements NotificationContract.Presenter {

    private static final int MAX_USER_NUMS_COMMENT = 2;
    private static final int MAX_USER_NUMS_DIGG = 3;

    @Inject
    MessageRepository mMessageRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    private Subscription mUnreadNotiSub;

    private UnReadNotificaitonBean mUnReadNotificationBean = null;

    @Inject
    public NotificationPresenter(NotificationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        if (mUnreadNotiSub != null && !mUnreadNotiSub.isUnsubscribed()) {
            mUnreadNotiSub.unsubscribe();
        }
        mUnreadNotiSub = Observable.zip(mMessageRepository.getUnreadNotificationData(),
                mUserInfoRepository.getUserAppendFollowerCount(), (t1, t2) -> {

                    this.mUnReadNotificationBean = t1;
                    List<NotificationBean> list = new ArrayList<>();
                    list.add(getSystemNotification(t1, t2));
                    list.add(getCommentNotification(t1, t2));
                    list.add(getDiggNotification(t1, t2));
                    list.add(getReviewNotification(t1, t2));
                    list.add(getGroupReviewNotification(t1,t2));
                    list.add(getFriendReviewNotification(t1,t2));

                    //通知新的粉丝数量
                    EventBus.getDefault().post(t2, EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE);
                    return list;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<NotificationBean>>() {
                    @Override
                    protected void onSuccess(List<NotificationBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                        //检查小红点是否显示
                        checkBottomMessageTip();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<NotificationBean> data, boolean isLoadMore) {
        return false;
    }


    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        Subscription subscribe = Observable.just(true)
                .subscribeOn(Schedulers.io())
                .map(aBoolean -> {
                    // 是否显示底部红点
                    boolean isShowMessgeTip;

                    boolean hasSystemMsg = mRootView.getListDatas().get(0).getUnreadCount() != 0;
                    boolean hasCommentMsg = mRootView.getListDatas().get(1).getUnreadCount() != 0;
                    boolean hasDigMsg = mRootView.getListDatas().get(2).getUnreadCount() != 0;
                    boolean hasReviewMsg = mRootView.getListDatas().get(3).getUnreadCount() != 0;

                    isShowMessgeTip = hasSystemMsg || hasDigMsg || hasCommentMsg || hasReviewMsg;

                    return isShowMessgeTip;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isShowMessgeTip -> {
                    Fragment containerFragment = mRootView.getCurrentFragment().getParentFragment();
                    if (containerFragment != null && containerFragment instanceof MessageContainerFragment) {
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(isShowMessgeTip, 3);
                        //环信消息
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(TSEMHyphenate.getInstance().getUnreadMsgCount() > 0, 2);
                    }
                    boolean messageContainerRedDotIsShow = isShowMessgeTip || TSEMHyphenate.getInstance().getUnreadMsgCount() > 0;
                    EventBus.getDefault().post(messageContainerRedDotIsShow, EventBusTagConfig.EVENT_IM_SET_MESSAGE_TIP_VISABLE);

                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


    }

    /**
     * 组合好友申请通知
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getFriendReviewNotification(UnReadNotificaitonBean t1,UserFollowerCountBean t2){

        NotificationBean friendReview = new NotificationBean();
        friendReview.setTitle("新朋友申请");
        friendReview.setHeadResId(R.mipmap.icon_message_friend_check);
        friendReview.setUnreadCount(t2.getUser().getMutual());
        if(null != t1.getFriendChecks() && t1.getFriendChecks().size() > 0 &&
                null != t1.getFriendChecks().get(0).getUser()){
            friendReview.setNotification(String.format("%s申请加你为好友",t1.getFriendChecks().get(0).getUser().getName()));
            friendReview.setTime(TimeUtils.utc2LocalLong(t1.getFriendChecks().get(0).getTime()));
        }else {
            friendReview.setNotification("暂无审核申请");
            friendReview.setTime(0L);
        }

        return friendReview;
    }

    /**
     * 组合群审核通知
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getGroupReviewNotification(UnReadNotificaitonBean t1, UserFollowerCountBean t2){

        NotificationBean groupReview = new NotificationBean();
        groupReview.setTitle("群组审核");
        groupReview.setHeadResId(R.mipmap.icon_message_group_check);
        groupReview.setUnreadCount(t1.getCounts().getUnread_group_join_count());

        if(null != t1.getGroupChecks() && t1.getGroupChecks().size() > 0
                && null != t1.getGroupChecks().get(0).getUser() && null != t1.getGroupChecks().get(0).getGroup()){

            groupReview.setNotification(String.format("%s申请加入群聊[%s]",
                    t1.getGroupChecks().get(0).getUser().getName(),t1.getGroupChecks().get(0).getGroup().getName()));
            groupReview.setTime(TimeUtils.utc2LocalLong(t1.getGroupChecks().get(0).getTime()));
        }else {
            groupReview.setNotification("暂无审核申请");
            groupReview.setTime(0L);
        }

        return groupReview;
    }

    /**
     * 组合审核通知消息
     *
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getReviewNotification(UnReadNotificaitonBean t1, UserFollowerCountBean t2) {

        //审核通知
        NotificationBean review = new NotificationBean();
        review.setTitle("审核通知");
        review.setHeadResId(R.mipmap.ico_message_check);
        int pinnedNums = 0;
        pinnedNums = t2.getUser().getFeedCommentPinned() + t2.getUser().getNewsCommentPinned()
                + t2.getUser().getGroupJoinPinned() + t2.getUser().getPostCommentPinned() + t2.getUser().getPostPinned();
        review.setUnreadCount(pinnedNums);
        //时间
        String feedTime = t1.getPinneds() != null && t1.getPinneds().getFeeds() != null ? t1.getPinneds().getFeeds().getTime() : null;
        String newTime = t1.getPinneds() != null && t1.getPinneds().getNews() != null ? t1.getPinneds().getNews().getTime() : null;
        String groupPostsTime = t1.getPinneds() != null && t1.getPinneds().getGroupPosts() != null ? t1.getPinneds().getGroupPosts().getTime() : null;
        String groupCommentsTime = t1.getPinneds() != null && t1.getPinneds().getGroupComments() != null ? t1.getPinneds().getGroupComments().getTime() : null;
        long lastTime = 0;
        lastTime = getLastTime(feedTime, lastTime);
        lastTime = getLastTime(newTime, lastTime);
        lastTime = getLastTime(groupPostsTime, lastTime);
        lastTime = getLastTime(groupCommentsTime, lastTime);
        review.setTime(lastTime);

        String reviewTip;
        if (review.getUnreadCount() > 0) {
            reviewTip = mContext.getString(R.string.new_apply_data);
        } else {
            reviewTip = mContext.getString(R.string.no_apply_data);
            review.setTime(0L);
        }
        review.setNotification(reviewTip);

        return review;
    }


    /**
     * 组合收到的赞消息
     *
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getDiggNotification(UnReadNotificaitonBean t1, UserFollowerCountBean t2) {

        //收到的赞
        NotificationBean digg = new NotificationBean();
        digg.setTitle("收到的赞");
        digg.setHeadResId(R.mipmap.ico_message_good);
        digg.setUnreadCount(t2.getUser().getLiked());
        digg.setTime(t1.getLikes() == null || t1.getLikes().isEmpty() ? 0 :
                TimeUtils.utc2LocalLong(t1.getLikes().get(0).getTime()));
        String diggTip = getItemTipStr(t1.getLikes(), MAX_USER_NUMS_DIGG);
        if (!TextUtils.isEmpty(diggTip)) {
            if (t1.getLikes() != null && t1.getLikes().size() > MAX_USER_NUMS_DIGG) {
                diggTip += mContext.getString(R.string.like_me_more);
            } else {
                diggTip += mContext.getString(R.string.like_me);
            }
        } else {
            diggTip = mContext.getString(R.string.has_no_body)
                    + mContext.getString(R.string.like_me);
        }
        digg.setNotification(diggTip);

        return digg;
    }

    /**
     * 组合评论消息
     *
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getCommentNotification(UnReadNotificaitonBean t1, UserFollowerCountBean t2) {

        //收到的评论
        NotificationBean comment = new NotificationBean();
        comment.setHeadResId(R.mipmap.ico_message_comment);
        comment.setTitle("收到的评论");
        comment.setUnreadCount(t2.getUser().getCommented());
        if (null != t1.getComments() && t1.getComments().size() > 0) {
            String commentTip = getItemTipStr(t1.getComments(), MAX_USER_NUMS_COMMENT);
            if (!TextUtils.isEmpty(commentTip)) {
                if (t1.getComments() != null && t1.getComments().size() > MAX_USER_NUMS_COMMENT) {
                    commentTip += mContext.getString(R.string.comment_me_more);
                } else {
                    commentTip += mContext.getString(R.string.comment_me);
                }
            } else {
                commentTip = mContext.getString(R.string.has_no_body)
                        + mContext.getString(R.string.comment_me);
            }
            comment.setNotification(commentTip);
            comment.setTime(TimeUtils.utc2LocalLong(t1.getComments().get(0).getTime()));
        } else {
            comment.setNotification("还没有人评论了我");
            comment.setTime(0L);
        }

        return comment;

    }

    /**
     * 组合系统消息
     *
     * @param t1
     * @param t2
     * @return
     */
    private NotificationBean getSystemNotification(UnReadNotificaitonBean t1, UserFollowerCountBean t2) {

        //系统消息
        NotificationBean system = new NotificationBean();
        system.setHeadResId(R.mipmap.ico_message_systerm);
        system.setTitle("系统消息");
        system.setUnreadCount(t2.getUser().getSystem());
        if (null != t1.getSystem() && null != t1.getSystem().getData()
                && !TextUtils.isEmpty(t1.getSystem().getData().getContent())) {
            system.setNotification(t1.getSystem().getData().getContent());
            system.setTime(TimeUtils.utc2LocalLong(t1.getSystem().getCreated_at()));
        } else {
            system.setNotification("暂无系统消息");
            system.setTime(0L);
        }

        return system;

    }


    private long getLastTime(String newTime, long lastTime) {
        if (newTime != null && TimeUtils.utc2LocalLong(newTime) > lastTime) {
            lastTime = TimeUtils.utc2LocalLong(newTime);
        }
        return lastTime;
    }

    /**
     * 获取用户文字显示  张三、李四评论了我
     */
    private String getItemTipStr(List<UnreadCountBean> commentsNoti, int maxNum) {
        if (commentsNoti == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String dot = mContext.getString(R.string.str_pingyin_dot);
        for (int i = 0; i < commentsNoti.size(); i++) {
            if (i < maxNum) {
                try {
                    if (stringBuilder.toString().contains(commentsNoti.get(i).getUser().getName())) {
                        maxNum++;
                    } else {
                        stringBuilder.append(commentsNoti.get(i).getUser().getName());
                        stringBuilder.append(dot);
                    }
                    // 服务器脏数据导致用户信息为空
                } catch (NullPointerException ignored) {
                }
            } else {
                break;
            }
        }
        String tip = stringBuilder.toString();
        if (tip.endsWith(dot)) {
            tip = tip.substring(0, tip.length() - 1);
        }
        return tip;
    }

    @Override
    public UnReadNotificaitonBean getUnreadNotiBean() {
        return mUnReadNotificationBean;
    }
}
