package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMConnectionEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.widget.BadgeView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterAlertPopWindow;
import com.zhiyicx.baseproject.widget.popwindow.CenterInfoPopWindow;
import com.zhiyicx.baseproject.widget.popwindow.NoticePopupWindow;
import com.zhiyicx.baseproject.widget.recycleview.BlankClickRecycleView;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.GroupAndFriendNotificaiton;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageAdapterV2;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.notification.review.NotificationReviewActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity;
import com.zhiyicx.thinksnsplus.utils.MessageTimeAndStickSort;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_DATA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_STATE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_TYPE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindFragment.DEAL_TYPE_PHONE;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
public class MessageConversationFragment extends TSListFragment<MessageConversationContract.Presenter, MessageItemBeanV2>
        implements MessageConversationContract.View, MessageAdapterV2.OnSwipeItemClickListener,
        OnUserInfoClickListener, BlankClickRecycleView.BlankClickListener, MessageAdapterV2.OnConversationItemLongClickListener {

    @Inject
    MessageConversationPresenter mConversationPresenter;
    @BindView(R.id.searchView)
    TSSearchView mSearchView;
    
    //群通知
    private TextView mTvGroupNotificationTime;
    private TextView mTvGroupNotificationContent;
    private BadgeView mTvGroupNotificationTip;
    //新朋友
    private TextView mTvFriendNotificationTime;
    private TextView mTvFriendNotificationContent;
    private BadgeView mTvFriendNotificationTip;
    
    
    /**
     * 删除确认弹框
     */
    private ActionPopupWindow mCheckSurePop;
    private UserInfoBean userInfoBean;
    //private NotificationUtil mNotificationUtil;
    private CenterAlertPopWindow mBindPop;

    private List<MessageItemBeanV2> mCacheList = new ArrayList<>();

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMessageConversationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageConversationPresenterModule(new MessageConversationPresenterModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initView(View rootView) {// FIXME: 2018/6/27 
        super.initView(rootView);
        mSearchView.setVisibility(View.VISIBLE);
        userInfoBean = AppApplication.getmCurrentLoginAuth().getUser();
//        if (TextUtils.isEmpty(userInfoBean.getPhone())){
//            showBindPopupWindow();
//        }

        //initHeaderView();


    }


    /**
     * 初始化头部view
     */
    private void initHeaderView(){

        View head = LayoutInflater.from(mActivity).inflate(R.layout.view_conversation_header,null);
        head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderAndFooterWrapper.addHeaderView(head);

        head.findViewById(R.id.rl_group).setOnClickListener(v -> 
                NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_GROUP));
        head.findViewById(R.id.rl_friend).setOnClickListener(v -> 
                NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_FRIEND));
        
        //群通知
        mTvGroupNotificationContent = (TextView) head.findViewById(R.id.tv_group_notification_content);
        mTvGroupNotificationTime = (TextView) head.findViewById(R.id.tv_group_notification_time);
        mTvGroupNotificationTip = (BadgeView) head.findViewById(R.id.tv_group_notification_tip);

        //新朋友
        mTvFriendNotificationContent = (TextView) head.findViewById(R.id.tv_friend_notification_content);
        mTvFriendNotificationTime = (TextView) head.findViewById(R.id.tv_friend_notification_time);
        mTvFriendNotificationTip = (BadgeView) head.findViewById(R.id.tv_friend_notification_tip);
        
    }

    /**
     * 更新群通知和新朋友通知
     * @param notificaiton
     */
    private void setupGroupAndFriendNotification(GroupAndFriendNotificaiton notificaiton){

        if(null != notificaiton.group){
            mTvGroupNotificationContent.setText(notificaiton.group.getNotification());
            mTvGroupNotificationTime.setText(TimeUtils.getTimeFriendlyNormal(notificaiton.group.getTime()));
            mTvGroupNotificationTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(notificaiton.group.getUnreadCount())));
        }
        if(null != notificaiton.friend){
            mTvFriendNotificationContent.setText(notificaiton.friend.getNotification());
            mTvFriendNotificationTime.setText(TimeUtils.getTimeFriendlyNormal(notificaiton.friend.getTime()));
            mTvFriendNotificationTip.setBadgeCount(Integer.parseInt(ConvertUtils.messageNumberConvert(notificaiton.friend.getUnreadCount())));
        }

    }



    /**
     * 提示绑定手机号
     */
    private void showBindPopupWindow() {
        if (mBindPop != null) {
            mBindPop.show();
            return;
        }
        mBindPop = CenterAlertPopWindow.builder()
                .titleStr(getString(R.string.tips))
                .desStr(getString(R.string.you_must_bind_phone_hint))
                .itemRight(getString(R.string.go_bind))
                .itemRightColor(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(new CenterAlertPopWindow.CenterPopWindowItemClickListener() {
                    @Override
                    public void onRightClicked() {
                        goBindPhone();
                    }

                    @Override
                    public void onLeftClicked() {
                        mBindPop.hide();
                    }
                })
                .parentView(getView())
                .build();
        mBindPop.show();
    }

    private void goBindPhone() {
        mBindPop.hide();
        Intent intent = new Intent(getActivity(), AccountBindActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_PHONE);
        bundle.putBoolean(BUNDLE_BIND_STATE, !TextUtils.isEmpty(userInfoBean.getPhone()));
        bundle.putParcelable(BUNDLE_BIND_DATA, userInfoBean);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_home_message_list;
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
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected void initData() {
        super.initData();
        initListener();
        //mNotificationUtil = new NotificationUtil(mActivity);
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.refreshSticks(String.valueOf(AppApplication.getMyUserIdWithdefault()));
        refreshConversationInfo();
        //删除所有会话通知
        //mNotificationUtil.cancelAllChatNotification();
    }


    /**
     * 刷新会话信息
     */
    private void refreshConversationInfo() {
        // 刷新信息内容
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
            //mPresenter.refreshConversationReadMessage();该请求已在MessageFragment中实现


        }
        if (getUserVisibleHint() && !TextUtils.isEmpty(mSearchView.getText())) {
            mSearchView.setText("");
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            /*if (mSearchView != null)
                mSearchView.setText("");*/

            //该事件会由以下两种情况出现
            // ①MessageContainerfragment中的viewPager切换
            // ②HomeFragment中的切换 --- MessageContainerFragment中的setUserVisibleHint方法
            //只在 MessageConversationFragment真正可见时调用
            /*if( null != getParentFragment() &&
                    ((MessageContainerFragment)getParentFragment()).getCurrentItem() == 1 )
                refreshConversationInfo();*/
        }

        if (isVisibleToUser && mAdapter != null && ((MessageAdapterV2) mAdapter).hasItemOpend()) {
            ((MessageAdapterV2) mAdapter).closeAllItems();
        }

    }


    @Override
    protected boolean showNoMoreData() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MessageAdapterV2 commonAdapter = new MessageAdapterV2(getActivity(), mListDatas, mPresenter);
        commonAdapter.setOnSwipItemClickListener(this);
        commonAdapter.setOnUserInfoClickListener(this);
        commonAdapter.setOnConversationItemLongClickListener(this);
        return commonAdapter;
    }

    @Override
    public void showMessage(String message) {
        showMessageNotSticky(message);
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
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        onBlickClick();
    }

    /**
     * 进入聊天页
     *
     * @param messageItemBean 当前 item 内容
     * @param position        当前点击位置
     */
    private void toChatV2(MessageItemBeanV2 messageItemBean, int position) {
        if (messageItemBean == null) {
            return;
        }

        if (null == messageItemBean.getConversation()) {
            ChatActivity.startChatActivity(mActivity, messageItemBean.getEmKey()
                    , messageItemBean.getType() == EMConversation.EMConversationType.Chat ? EaseConstant.CHATTYPE_SINGLE :
                            EaseConstant.CHATTYPE_GROUP);
        } else {
            ChatActivity.startChatActivity(mActivity, messageItemBean.getConversation().conversationId()
                    , messageItemBean.getConversation().getType() == EMConversation.EMConversationType.Chat ? EaseConstant.CHATTYPE_SINGLE :
                            EaseConstant.CHATTYPE_GROUP);
        }
    }

    @Override
    public void onUserInfoClick(UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
    }

    @Override
    public void onBlickClick() {
        if (((MessageAdapterV2) mAdapter).hasItemOpend()) {
            ((MessageAdapterV2) mAdapter).closeAllItems();
        }
    }

    @Override
    public void onLeftClick(int position) {
        if(TSEMConstants.EMKEY_GROUP_NOTIFICATION.equals(mListDatas.get(position).getEmKey()) ){//群组通知
            NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_GROUP);
        }else if(TSEMConstants.EMKEY_FRIEND_NOTIFICATION.equals(mListDatas.get(position).getEmKey())){//新朋友
            NotificationReviewActivity.startNotificationReviewActivity(mActivity, IntentKey.NOTIFICATION_REVIEW_FRIEND);
        }else {
            toChatV2(mListDatas.get(position), position);
        }
    }

    @Override
    public void onRightClick(int position) {
        deleteChatConversation(position);
    }

    private void deleteChatConversation(int position) {
        mPresenter.deleteConversation(position);
        refreshData();
    }

    /**
     * 新朋友和群审核通知
     *  notificaiton
     */
    /*@Subscriber(mode = ThreadMode.MAIN,tag = EventBusTagConfig.EVENT_GROUP_AND_FRIEND_NOTIFICATION_LIST)
    public void onTSEMConnectionEventBus(GroupAndFriendNotificaiton notificaiton) {
        try {
            setupGroupAndFriendNotification(notificaiton);
        }catch (Exception e){}
    }*/

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMConnectionEventBus(TSEMConnectionEvent event) {
        LogUtils.d("onTSEMConnectionEventBus");
        switch (event.getType()) {
            case TSEMConstants.TS_CONNECTION_USER_LOGIN_OTHER_DIVERS:
                break;
            case TSEMConstants.TS_CONNECTION_USER_REMOVED:
                break;
            case TSEMConstants.TS_CONNECTION_CONNECTED:
                hideStickyMessage();
                requestNetData(0L, false);
                break;
            case TSEMConstants.TS_CONNECTION_DISCONNECTED:
                showStickyMessage(getString(R.string.chat_unconnected));
                break;
            default:
        }
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMessageEventEventBus(TSEMessageEvent event) {
        LogUtils.d("TSEMessageEvent");
        EMCmdMessageBody body = (EMCmdMessageBody) event.getMessage().getBody();
        String groupId = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_ID, null);
        String groupName = event.getMessage().getStringAttribute(TSEMConstants.TS_ATTR_NAME, null);
        switch (body.action()) {

            case TSEMConstants.TS_ATTR_GROUP_DISBAND:
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                NotificationUtil.showTextNotification(mActivity, groupName + "[群]解散了");
                //mPresenter.updateGroup(groupId);
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                if (mPresenter != null) {
                    mPresenter.deleteGroup(groupId);
                }
                break;
            case TSEMConstants.TS_ATTR_GROUP_LAYOFF:
                if (TextUtils.isEmpty(groupId)) {
                    return;
                }
                NotificationUtil.showTextNotification(mActivity, "你被管理员移出" + groupName + "[群聊]");
                //mPresenter.updateGroup(groupId);
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                if (mPresenter != null) {
                    mPresenter.deleteGroup(groupId);
                }
                break;
            case TSEMConstants.TS_ATTR_GROUP_EXIT:
                //用户主动退出群聊，在退出成功之后，就加了一条本地消息，不需要再次刷新
                NotificationUtil.showTextNotification(mActivity, "你已经退出" + groupName + "[群聊]");
                //mPresenter.updateGroup(groupId);
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                if (mPresenter != null) {
                    mPresenter.deleteGroup(groupId);
                }
                break;

            default:
        }
    }

    private void initListener() {
        mSearchView.setOnSearchClickListener(new TSSearchView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSearchStr(s.toString().trim());
            }
        });
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MessageItemBeanV2> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        if(null != data){
            mCacheList.clear();
            mCacheList.addAll(data);
        }
    }

    /**
     * 本地搜索会话
     * @param str
     */
    private void filterSearchStr(String str){

        if(str.length() == 0){
            mListDatas.clear();
            mListDatas.addAll(mCacheList);
            refreshData();
            return;
        }

        String name = "";
        mListDatas.clear();
        for (MessageItemBeanV2 itemBeanV2:mCacheList) {
            if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat) {
                if (itemBeanV2.getUserInfo() != null) {
                    name = itemBeanV2.getUserInfo().getName();
                }
            } else if(itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.GroupChat){
                if(null != itemBeanV2.getChatGroupBean())
                    name = itemBeanV2.getChatGroupBean().getName();
            }
            if (name.contains(str))
                mListDatas.add(itemBeanV2);
        }
        Collections.sort(mListDatas,new MessageTimeAndStickSort());
        refreshData();
    }

    /**
     * 解绑前的提示选择弹框
     *
     * @param position 被删除项在列表中的位置
     */
    private void initCheckSurePop(int position) {
        String mStickStr = "";
        if(!TSEMConstants.EMKEY_GROUP_NOTIFICATION.equals(mListDatas.get(position).getEmKey())
                && !TSEMConstants.EMKEY_FRIEND_NOTIFICATION.equals(mListDatas.get(position).getEmKey()))
            mStickStr = mListDatas.get(position).getIsStick() == 0 ? getString(R.string.go_top) : getString(R.string.cancel_top);

        mCheckSurePop = ActionPopupWindow
                .builder()
                .item1Str(mStickStr)
                .item2Str(getString(R.string.ts_delete))
                .item2Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(mActivity)
                .item1ClickListener(() -> {
//                    mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
                    mCheckSurePop.hide();
                    //设置置顶
                    mPresenter.setSticks(mListDatas.get(position),
                            String.valueOf(AppApplication.getMyUserIdWithdefault()));

                })
                .item2ClickListener(() -> {
                    deleteChatConversation(position);
                    mCheckSurePop.hide();
                })
                .bottomClickListener(() -> mCheckSurePop.hide())
                .build();
        mCheckSurePop.show();

    }

    @Override
    public void onDestroyView() {
        dismissPop(mCheckSurePop);
        super.onDestroyView();
    }

    @Override
    public void onConversationItemLongClick(int position) {
        initCheckSurePop(position);
    }

    @Override
    public void showNewMessageTip(boolean isShow) {
        if(null != getParentFragment())
            ((MessageContainerFragment)getParentFragment()).setNewMessageState(isShow);
    }
}
