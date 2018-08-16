package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.NotificationBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist.NotificationListActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.notification.review.NotificationReviewActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/15 10:45
 * description:
 * version:
 */

public class NotificationFragment extends TSListFragment<NotificationContract.Presenter,NotificationBean>
        implements NotificationContract.View{


    public static NotificationFragment newInstance(){
        return new NotificationFragment();
    }

    @Inject
    NotificationPresenter mPresenter;

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initView(View rootView) {

        DaggerNotificationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .notificationPresenterModule(new NotificationPresenterModule(this))
                .build()
                .inject(this);

        super.initView(rootView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.requestNetData(0L,false);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(null != mPresenter && isVisibleToUser)
            mPresenter.requestNetData(0L,false);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<NotificationBean> mAdapter = new CommonAdapter<NotificationBean>(mActivity,
                R.layout.item_home_notification,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, NotificationBean notificationBean, int position) {

                holder.getImageViwe(R.id.iv_notification).setImageDrawable(
                        mActivity.getResources().getDrawable(notificationBean.getHeadResId()));
                holder.getTextView(R.id.tv_notification_name).setText(notificationBean.getTitle());
                holder.getTextView(R.id.tv_notification_content).setText(notificationBean.getNotification());
                holder.getTextView(R.id.tv_notification_time).setText(TimeUtils.getTimeFriendlyNormal(notificationBean.getTime()));
                ((BadgeView)holder.getView(R.id.tv_notification_tip)).setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(notificationBean.getUnreadCount())));

            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                switch (position){
                    case 0://系统消息
                        startActivity(new Intent(getContext(), NotificationListActivity.class));
                        break;
                    case 1://评论列表
                        startActivity(new Intent(getActivity(), MessageCommentActivity.class));
                        break;
                    case 2://点赞列表
                        startActivity(new Intent(getActivity(), MessageLikeActivity.class));
                        break;
                    case 3://审核通知
                        Bundle bundle = new Bundle();
                        Intent to = new Intent(getActivity(), MessageReviewActivity.class);
                        if (mPresenter.getUnreadNotiBean() != null && mPresenter.getUnreadNotiBean().getPinneds() != null) {
                            bundle.putParcelable(MessageReviewFragment.BUNDLE_PINNED_DATA, mPresenter.getUnreadNotiBean().getPinneds());
                        }
                        to.putExtras(bundle);
                        startActivity(to);
                        break;
                    case 4://群聊申请
                        NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_GROUP);
                        break;
                    case 5://好友申请
                        NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_FRIEND);
                        break;
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return mAdapter;
    }


    @Override
    public Fragment getCurrentFragment() {
        return this;
    }
}
