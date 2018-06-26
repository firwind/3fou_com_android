package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMConnectionEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.recycleview.BlankClickRecycleView;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.OnUserInfoClickListener;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.MessageAdapterV2;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;

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
    /**
     * 删除确认弹框
     */
    private ActionPopupWindow mCheckSurePop;

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
    protected void initView(View rootView) {
        super.initView(rootView);
        mSearchView.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onResume() {
        super.onResume();
        //mPresenter.refreshSticks(String.valueOf(AppApplication.getMyUserIdWithdefault()));
        refreshConversationInfo();
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
            if (mSearchView != null)
                mSearchView.setText("");

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
    public void onNetResponseSuccess(@NotNull List<MessageItemBeanV2> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        hideStickyMessage();
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
    public BaseFragment getCurrentFragment() {
        return this;
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
        if(null == messageItemBean.getConversation()){
            ChatActivity.startChatActivity(mActivity, messageItemBean.getEmKey()
                    , messageItemBean.getType() == EMConversation.EMConversationType.Chat ? EaseConstant.CHATTYPE_SINGLE :
                            EaseConstant.CHATTYPE_GROUP,messageItemBean.getIsStick());
        }else {
            ChatActivity.startChatActivity(mActivity, messageItemBean.getConversation().conversationId()
                    , messageItemBean.getConversation().getType() == EMConversation.EMConversationType.Chat ? EaseConstant.CHATTYPE_SINGLE :
                            EaseConstant.CHATTYPE_GROUP,messageItemBean.getIsStick());
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
        toChatV2(mListDatas.get(position), position);
    }

    @Override
    public void onRightClick(int position) {
        deleteChatConversation(position);
    }

    private void deleteChatConversation(int position) {
        mPresenter.deleteConversation(position);
        refreshData();
    }

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
                EMClient.getInstance().chatManager().deleteConversation(groupId, true);
                if (mPresenter != null) {
                    mPresenter.deleteGroup(groupId);
                }

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
                if (mPresenter == null) {
                    return;
                }
                // 显示搜索结果
                mPresenter.searchList(s.toString().trim());
            }
        });
    }

    /**
     * @return 输入框的内容
     */
    @Override
    public String getsearchKeyWord() {
        return mSearchView.getText().toString().trim();
    }

    /**
     * 设置置顶成功回调
     */
    @Override
    public void setSticksSuccess(String stick_id) {
        refreshConversationInfo();
    }

    /**
     * 获取置顶id列表
     *
     * @param data
     */
    /*@Override
    public void getSticksList(List<StickBean> data) {
        refreshConversationInfo();
    }*/

    /**
     * 获取置顶ID失败，继续执行获取会话列表
     */
    /*@Override
    public void getSticksFailure() {
        refreshConversationInfo();
    }*/

    /**
     * 解绑前的提示选择弹框
     *
     * @param position 被删除项在列表中的位置
     */
    private void initCheckSurePop(int position) {
        String mStickStr = mListDatas.get(position).getIsStick() == 0 ? getString(R.string.go_top) : getString(R.string.cancel_top);
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
                    mPresenter.setSticks(mListDatas.get(position).getEmKey(),
                            String.valueOf(AppApplication.getMyUserIdWithdefault()),mListDatas.get(position).getIsStick());

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
}
