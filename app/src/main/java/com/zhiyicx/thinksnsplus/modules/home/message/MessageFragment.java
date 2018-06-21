package com.zhiyicx.thinksnsplus.modules.home.message;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.recycleview.BlankClickRecycleView;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelike.MessageLikeActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist.NotificationActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息页面
 * @Author zl
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class MessageFragment extends TSListFragment<MessageContract.Presenter, MessageItemBean>
        implements MessageContract.View, MessageAdapterV2.OnSwipeItemClickListener,
        OnUserInfoClickListener, BlankClickRecycleView.BlankClickListener {

    private View mHeaderView;

    private static final float MAXWIDTH = 0.65f;

    @Inject
    protected MessagePresenter mMessagePresenter;

    private List<MessageItemBeanV2> messageItemBeanList;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home_message_list;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected int setRightImg() {
        return R.drawable.frame_loading_grey;
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
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMessageComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messagePresenterModule(new MessagePresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View rootView) {
        try {
            super.initView(rootView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initHeaderView();
        rootView.setBackgroundResource(R.color.bgColor);
        ((BlankClickRecycleView) mRvList).setBlankListener(this);

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initData() {
        super.initData();
    }


    @Override
    public void onResume() {
        super.onResume();
        // 刷新信息内容
        if (mPresenter != null) {
            mPresenter.refreshConversationReadMessage();
            updateCommnetItemData(mPresenter.updateCommnetItemData());
            mPresenter.handleFlushMessage();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (mPresenter != null && isVisibleToUser) {
            // 除了通知的未读数用户信息获取
            mPresenter.handleFlushMessage();
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        messageItemBeanList = new ArrayList<>();
        MessageAdapterV2 commonAdapter = new MessageAdapterV2(getActivity(), messageItemBeanList, null);
        commonAdapter.setOnSwipItemClickListener(this);
        commonAdapter.setOnUserInfoClickListener(this);
        return commonAdapter;
    }

    /**
     * 初始化头信息（评论的、赞过的）
     */
    private void initHeaderView() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout
                .view_header_message_list, null);
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
        mRvList.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }


    /**
     * 更新 hederview 数据
     *
     * @param headerview
     */
    private void updateHeaderViewData(View headerview, MessageItemBean systemMsgItemData, MessageItemBean commentItemData,
                                      MessageItemBean likedItemData, MessageItemBean
                                              reviewItemBean) {
        View rlCritical = null;
        View liked;
        View review;

        TextView tvHeaderSystemMsgContent = null;
        TextView tvHeaderSystemMsgTime = null;
        BadgeView tvHeaderSystemMsgTip = null;

        TextView tvHeaderCommentContent = null;
        TextView tvHeaderCommentTime = null;
        BadgeView tvHeaderCommentTip = null;

        TextView tvHeaderLikeContent = null;
        TextView tvHeaderLikeTime = null;
        BadgeView tvHeaderLikeTip = null;

        TextView tvHeaderReviewContent = null;
        TextView tvHeaderReviewTime = null;
        BadgeView tvHeaderReviewTip = null;

        // 系统通知
        RxView.clicks(headerview.findViewById(R.id.rl_system_notify))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
                        ((MessageAdapterV2) mAdapter).closeAllItems();
                        return;
                    }
                    // 跳转系统通知页面
                    toSystemPage();
                    mPresenter.updateSystemMsgItemData().setUnReadMessageNums(0);
                    updateCommnetItemData(mPresenter.updateSystemMsgItemData());
                });

        rlCritical = headerview.findViewById(R.id.rl_critical);
        RxView.clicks(rlCritical)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
                        ((MessageAdapterV2) mAdapter).closeAllItems();
                        return;
                    }
                    toCommentList();
                    mPresenter.updateCommnetItemData().setUnReadMessageNums(0);
                    updateCommnetItemData(mPresenter.updateCommnetItemData());

                });

        liked = headerview.findViewById(R.id.rl_liked);
        RxView.clicks(liked)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
                        ((MessageAdapterV2) mAdapter).closeAllItems();
                        return;
                    }
                    toLikeList();
                    mPresenter.updateLikeItemData().setUnReadMessageNums(0);
                    updateCommnetItemData(mPresenter.updateLikeItemData());
                });

        review = headerview.findViewById(R.id.rl_review);
        RxView.clicks(review)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
                        ((MessageAdapterV2) mAdapter).closeAllItems();
                        return;
                    }
                    toReviewList();
                    updateReviewItemData(mPresenter.updateReviewItemData());
                });

        tvHeaderSystemMsgContent = (TextView) headerview.findViewById(R.id
                .tv_header_notification_content);
        tvHeaderSystemMsgTime = (TextView) headerview.findViewById(R.id.tv_header_notification_time);
        tvHeaderSystemMsgTip = (BadgeView) headerview.findViewById(R.id.tv_header_notification_tip);

        tvHeaderCommentContent = (TextView) headerview.findViewById(R.id
                .tv_header_comment_content);
        tvHeaderCommentTime = (TextView) headerview.findViewById(R.id.tv_header_comment_time);
        tvHeaderCommentTip = (BadgeView) headerview.findViewById(R.id.tv_header_comment_tip);

        tvHeaderLikeContent = (TextView) headerview.findViewById(R.id.tv_header_like_content);
        tvHeaderLikeTime = (TextView) headerview.findViewById(R.id.tv_header_like_time);
        tvHeaderLikeTip = (BadgeView) headerview.findViewById(R.id.tv_header_like_tip);

        tvHeaderReviewContent = (TextView) headerview.findViewById(R.id
                .tv_header_review_content);
        tvHeaderReviewTime = (TextView) headerview.findViewById(R.id.tv_header_review_time);
        tvHeaderReviewTip = (BadgeView) headerview.findViewById(R.id.tv_header_review_tip);

        tvHeaderSystemMsgContent.setText(systemMsgItemData.getConversation().getLast_message().getTxt());
        if (systemMsgItemData.getConversation().getLast_message_time() == 0 || systemMsgItemData
                .getConversation().getLast_message().getTxt().contains(getString(R.string
                        .has_no_body))) {
            tvHeaderSystemMsgTime.setVisibility(View.INVISIBLE);
        } else {
            tvHeaderSystemMsgTime.setVisibility(View.VISIBLE);
            tvHeaderSystemMsgTime.setText(TimeUtils.getTimeFriendlyNormal(systemMsgItemData
                    .getConversation().getLast_message_time()));
        }
        tvHeaderSystemMsgTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert
                (systemMsgItemData.getUnReadMessageNums())));

        float maxW = DeviceUtils.getScreenWidth(mActivity) * MAXWIDTH;
        String commentText = commentItemData.getConversation().getLast_message().getTxt();
        boolean threePeople = commentText.endsWith(mActivity.getString(R.string.comment_me_more));
        tvHeaderCommentContent.setText(DeviceUtils.getSubStringWidth(tvHeaderCommentContent.getPaint(), commentText, maxW, mActivity.getString(threePeople ? R.string.comment_me_more : R.string.comment_me)));

        if (commentItemData.getConversation().getLast_message_time() == 0 || commentItemData
                .getConversation().getLast_message().getTxt().contains(getString(R.string
                        .has_no_body))) {
            tvHeaderCommentTime.setVisibility(View.INVISIBLE);
        } else {
            tvHeaderCommentTime.setVisibility(View.VISIBLE);
            tvHeaderCommentTime.setText(TimeUtils.getTimeFriendlyNormal(commentItemData
                    .getConversation().getLast_message_time()));
        }
        tvHeaderCommentTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert
                (commentItemData.getUnReadMessageNums())));

        String likeText = likedItemData.getConversation().getLast_message().getTxt();
        threePeople = likeText.endsWith(mActivity.getString(R.string.like_me_more));
        tvHeaderLikeContent.setText(DeviceUtils.getSubStringWidth(tvHeaderLikeContent.getPaint(), likeText, maxW, mActivity.getString(threePeople ? R.string.like_me_more : R.string.like_me)));

        if (likedItemData.getConversation().getLast_message_time() == 0 || likedItemData
                .getConversation().getLast_message().getTxt().contains(getString(R.string
                        .has_no_body))) {
            tvHeaderLikeTime.setVisibility(View.INVISIBLE);
        } else {
            tvHeaderLikeTime.setVisibility(View.VISIBLE);
            tvHeaderLikeTime.setText(TimeUtils.getTimeFriendlyNormal(likedItemData
                    .getConversation().getLast_message_time()));
        }
        tvHeaderLikeTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert
                (likedItemData.getUnReadMessageNums())));

        // 审核
        tvHeaderReviewContent.setText(reviewItemBean.getConversation().getLast_message().getTxt());
        if (reviewItemBean.getConversation().getLast_message_time() == 0 || reviewItemBean
                .getConversation().getLast_message().getTxt().contains(getString(R.string
                        .has_no_body))) {
            tvHeaderReviewTime.setVisibility(View.INVISIBLE);
        } else {
            tvHeaderReviewTime.setVisibility(View.VISIBLE);
            tvHeaderReviewTime.setText(TimeUtils.getTimeFriendlyNormal(reviewItemBean
                    .getConversation().getLast_message_time()));
        }
        tvHeaderReviewTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert
                (reviewItemBean.getUnReadMessageNums())));


        refreshData();
    }


    /**
     * 前往系统消息了列表
     */
    private void toSystemPage() {
        startActivity(new Intent(getContext(), NotificationActivity.class));
    }

    /**
     * 前往评论列表
     */
    private void toCommentList() {
        startActivity(new Intent(getActivity(), MessageCommentActivity.class));
    }

    /**
     * 前往点赞列表
     */
    private void toLikeList() {
        startActivity(new Intent(getActivity(), MessageLikeActivity.class));
    }

    /**
     * 前往审核列表
     */
    private void toReviewList() {
        Bundle bundle = new Bundle();
        Intent to = new Intent(getActivity(), MessageReviewActivity.class);
        if (mPresenter.getUnreadNotiBean() != null && mPresenter.getUnreadNotiBean().getPinneds() != null) {
            bundle.putParcelable(MessageReviewFragment.BUNDLE_PINNED_DATA, mPresenter.getUnreadNotiBean().getPinneds());
        }
        to.putExtras(bundle);
        startActivity(to);
    }

    @Override
    public void updateSystemMsgItemData(MessageItemBean messageItemBean) {
        if (messageItemBean == null) {
            return;
        }
        updateHeaderViewData(mHeaderView, mPresenter.updateSystemMsgItemData(), mPresenter.updateCommnetItemData(), mPresenter
                .updateLikeItemData(), mPresenter.updateReviewItemData());
        refreshData();
    }

    @Override
    public void updateCommnetItemData(MessageItemBean messageItemBean) {
        if (messageItemBean == null) {
            return;
        }
        updateHeaderViewData(mHeaderView, mPresenter.updateSystemMsgItemData(), mPresenter.updateCommnetItemData(), mPresenter
                .updateLikeItemData(), mPresenter.updateReviewItemData());
        refreshData();
    }

    @Override
    public void updateLikeItemData(MessageItemBean messageItemBean) {
        updateHeaderViewData(mHeaderView, mPresenter.updateSystemMsgItemData(), mPresenter.updateCommnetItemData(), mPresenter
                .updateLikeItemData(), mPresenter.updateReviewItemData());
        refreshData();
    }

    @Override
    public void updateReviewItemData(MessageItemBean messageItemBean) {
        updateHeaderViewData(mHeaderView, mPresenter.updateSystemMsgItemData(), mPresenter.updateCommnetItemData(), mPresenter
                .updateLikeItemData(), mPresenter.updateReviewItemData());
        refreshData();
    }

    @Override
    public void showTopRightLoading() {
        ((AnimationDrawable) (mToolbarRight.getCompoundDrawables())[2]).start();
        mToolbarRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeTopRightLoading() {
        ((AnimationDrawable) (mToolbarRight.getCompoundDrawables())[2]).stop();
        mToolbarRight.setVisibility(View.GONE);

    }

    @Override
    public BaseFragment getCureenFragment() {
        return this;
    }

    @Override
    public void getMessageListSuccess(List<MessageItemBeanV2> list) {
        messageItemBeanList.clear();
        messageItemBeanList.addAll(list);
        mAdapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public List<MessageItemBeanV2> getRealMessageList() {
        return messageItemBeanList;
    }


    @Override
    public void refreshData() {
        super.refreshData();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void refreshData(int index) {
        mHeaderAndFooterWrapper.notifyItemChanged(index);
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.finishRefresh();
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
    }

    @Override
    public void onLeftClick(int position) {
    }

    @Override
    public void onRightClick(int position) {
        // 减去 header
        position = position - mHeaderAndFooterWrapper.getHeadersCount();
        mPresenter.deletConversation(position);
        refreshData();
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }


    @Override
    public void onPause() {
        super.onPause();
        onBlickClick();
    }

    @Override
    public void onBlickClick() {
        if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
            ((MessageAdapterV2) mAdapter).closeAllItems();
        }
    }
}
