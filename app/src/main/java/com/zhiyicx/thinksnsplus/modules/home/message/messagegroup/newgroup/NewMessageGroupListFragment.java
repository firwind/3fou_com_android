package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseExpandableListAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSExpandListFragment;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMConnectionEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMessageEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupParentBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoActivity;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/7/7 15:17
 * description:
 * version:
 */

public class NewMessageGroupListFragment extends TSExpandListFragment<NewMessageGroupContract.Presenter,GroupParentBean>
        implements NewMessageGroupContract.View{

    @Inject
    public NewMessageGroupPresenter mPresenter;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    public static NewMessageGroupListFragment newInstance(boolean isOnlyOfficial) {
        NewMessageGroupListFragment fragment = new NewMessageGroupListFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentKey.IS_ONLY_OFFICIAL,isOnlyOfficial);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View rootView) {

        DaggerNewMessageGroupComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .newMessageGroupPresenterModule(new NewMessageGroupPresenterModule(this))
                .build()
                .inject(this);

        super.initView(rootView);

        rootView.setBackgroundColor(Color.WHITE);
        mLvList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            String groupId = mListDatas.get(groupPosition).childs.get(childPosition).getId();
            if(null == EMClient.getInstance().groupManager().getGroup(groupId)){
                ChatInfoActivity.startChatInfoActivity(mActivity,groupId,EaseConstant.CHATTYPE_GROUP);
            }else {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(
                        groupId, EMConversation.EMConversationType.GroupChat, true);
                ChatActivity.startChatActivity(mActivity, conversation.conversationId(), EaseConstant.CHATTYPE_GROUP);
            }

            return false;
        });
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_group);
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getGroupListData();
    }

    @Override
    protected BaseExpandableListAdapter getAdapter() {
        ExpandGroupListAdapter mAdapter = new ExpandGroupListAdapter(mActivity,mListDatas);
        return mAdapter;
    }

    /**
     * 获取群列表
     */
    private void getGroupListData() {
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
        }
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
                getGroupListData();
                break;
            case TSEMConstants.TS_CONNECTION_DISCONNECTED:
                showStickyMessage(getString(R.string.chat_unconnected));
                break;
            default:
        }
    }


    @Override
    public boolean isOnlyOfficial() {
        return null != getArguments() && getArguments().getBoolean(IntentKey.IS_ONLY_OFFICIAL,false);
    }
}
