package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

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
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
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

    @BindView(R.id.searchView)
    TSSearchView mSearchView;
    private List<GroupParentBean> cache = new ArrayList<>();

    @Inject
    public NewMessageGroupPresenter mPresenter;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    public static NewMessageGroupListFragment newInstance() {
        NewMessageGroupListFragment fragment = new NewMessageGroupListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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

        //去掉自带的箭头
        //mSearchView.setVisibility(View.VISIBLE);
//        mLvList.setGroupIndicator(null);
        mLvList.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
//            mPresenter.checkGroupExist(mListDatas.get(groupPosition).childs.get(childPosition));
            checkGroupExist(mListDatas.get(groupPosition).childs.get(childPosition).getId());
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
    protected void initData() {
        super.initData();
        initListener();
    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        /*if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
            //mPresenter.refreshConversationReadMessage();该请求已在MessageFragment中实现
        }*/
        if (getUserVisibleHint() && !TextUtils.isEmpty(mSearchView.getText())) {
            mSearchView.setText("");
        }
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<GroupParentBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        cache.clear();
        cache.addAll(data);
    }

    @Override
    protected BaseExpandableListAdapter getAdapter() {
        ExpandGroupListAdapter mAdapter = new ExpandGroupListAdapter(mActivity,mListDatas);
        return mAdapter;
    }

    @Override
    public void checkGroupExist(String id) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(id, EMConversation.EMConversationType.GroupChat, true);
        ChatActivity.startChatActivity(mActivity, conversation.conversationId(), EaseConstant.CHATTYPE_GROUP);
//            mActivity.finish();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_expand_tslist_with_search;
    }

    @Override
    public String getsearchKeyWord() {
        return mSearchView.getText().toString().trim();
    }

    /*@Subscriber(mode = ThreadMode.MAIN)
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
    }*/

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMessageEventEventBus(TSEMessageEvent event) {
        EMCmdMessageBody body = (EMCmdMessageBody) event.getMessage().getBody();
        switch (body.action()) {
            case TSEMConstants.TS_ATTR_GROUP_DISBAND:
            case TSEMConstants.TS_ATTR_GROUP_LAYOFF:
                getGroupListData();
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
                if (mPresenter == null) {
                    return;
                }
                filterData(s.toString().trim());
            }
        });
    }

    /**
     * 获取
     *
     * @param filterStr
     */
    private void filterData(CharSequence filterStr) {
        if (cache.isEmpty()) {
            return;
        }
        if (TextUtils.isEmpty(filterStr)) {
            mListDatas.clear();
            mListDatas.addAll(cache);
        } else {
            List<GroupParentBean> result = new ArrayList<>();
            for (GroupParentBean parentBean : cache) {
                List<ChatGroupBean> childResult = new ArrayList<>();
                for (ChatGroupBean sortModel: parentBean.childs) {
                    String name = sortModel.getName();
                    boolean isContent = !TextUtils.isEmpty(name) && name.toLowerCase().contains(filterStr.toString().toLowerCase());
                    if (isContent) {
                        childResult.add(sortModel);
                    }
                }
                String title ;
                if(cache.indexOf(parentBean) == 0){
                    title = "官方群聊";
                }else if(cache.indexOf(parentBean) == 1){
                    title = "热门群聊";
                }else {
                    title = "自建群聊";
                }
                result.add(new GroupParentBean(title,childResult));
            }
            mListDatas.clear();
            mListDatas.addAll(result);
        }
        refreshData();
        if (mListDatas.isEmpty()) {
            setEmptyViewVisiable(false);
        }
    }

    /**
     * 获取群列表
     */
    private void getGroupListData() {
        if (mPresenter != null) {
            mRefreshlayout.autoRefresh(0);
        }
    }

}
