package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMConnectionEvent;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.popwindow.CenterAlertPopWindow;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.GroupItemAdapter;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.SecondaryListAdapter;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:37
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class MessageGroupListFragment extends TSListFragment<MessageGroupContract.Presenter, ChatGroupBean>
        implements MessageGroupContract.View {

    @BindView(R.id.searchView)
    TSSearchView mSearchView;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    private List<ChatGroupBean> cache;

    private static final String IS_ONLY_OFFICIAL_GROUP = "is_only_official_group";
    /**
     * 仅用于构造
     */
    @Inject
    MessageGroupPresenter mMessageGroupPresenter;

    @Override
    protected boolean useEventBus() {
        return true;
    }

    public static MessageGroupListFragment newInstance() {
        MessageGroupListFragment fragment = new MessageGroupListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        DaggerMessageGroupFComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageGroupPresenterModule(new MessageGroupPresenterModule(this))
                .build().inject(this);

        super.initView(rootView);
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getstatusbarAndToolbarHeight() {
        if (setUseSatusbar()) {
            return 0;
        }
        return super.getstatusbarAndToolbarHeight();
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
    public void onResume() {
        super.onResume();

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
    public void onNetResponseSuccess(@NotNull List<ChatGroupBean> data, boolean isLoadMore) {
        boolean notSearch = TextUtils.isEmpty(getsearchKeyWord());
        if (notSearch) {
            cache = new ArrayList<>(data);
            mSearchView.setVisibility(data.isEmpty() ? View.GONE : View.VISIBLE);
        } else {
            mSearchView.setVisibility(View.VISIBLE);
        }

        super.onNetResponseSuccess(data, isLoadMore);
        List<SecondaryListAdapter.DataTree<ChatGroupBean, ChatGroupBean>> datas = new ArrayList<>();

        if (!notSearch && mListDatas.isEmpty()) {
            setEmptyViewVisiable(false);
        }
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        /*if (!isOnlyOfficialGroup()) {
            List<SecondaryListAdapter.DataTree<ChatGroupBean, ChatGroupBean>> datas = new ArrayList<>();
            for (int i = 0; i < mListDatas.size(); i++) {
                ChatGroupBean chatGroupBean = mListDatas.get(i);
//                datas.add(new SecondaryListAdapter.DataTree<ChatGroupBean, ChatGroupBean>(chatGroupBean, chatGroupBean.getTreeBeanList()));
            }
            GroupItemAdapter adapter = new GroupItemAdapter(getContext(), new GroupItemAdapter.OnScrollListener() {
                @Override
                public void checkIsGroup(ChatGroupBean groupBean) {
                    mPresenter.checkGroupExist(groupBean);
                }
            });
            adapter.setData(datas);
            return adapter;
        } else {*/
            CommonAdapter adapter = new CommonAdapter<ChatGroupBean>(getActivity(), R.layout.item_group_list, mListDatas) {
                @Override
                protected void convert(ViewHolder holder, ChatGroupBean chatGroupBean, int position) {
                    holder.setText(R.id.tv_group_name, chatGroupBean.getName());
                    Glide.with(mContext)
                            .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                                    .getGroup_face())
                            .error(R.mipmap.ico_ts_assistant)
                            .placeholder(R.mipmap.ico_ts_assistant)
                            .transform(new GlideCircleTransform(mContext))
                            .into(holder.getImageViwe(R.id.uv_group_head));

                    int resId = ImageUtils.getGroupSignResId(chatGroupBean.getGroup_level());
                    holder.getImageViwe(R.id.iv_group_sign).setVisibility(0 == resId ? View.INVISIBLE : View.VISIBLE);
                    if (0 != resId)
                        holder.getImageViwe(R.id.iv_group_sign).setImageDrawable(mActivity.getResources().getDrawable(resId));
                }
            };
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    ChatGroupBean groupBean = mListDatas.get(position);
                    mPresenter.checkGroupExist(groupBean);
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });

            return adapter;
    }

    @Override
    public void checkGroupExist(String id) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(id, EMConversation.EMConversationType.GroupChat, true);
        ChatActivity.startChatActivity(mActivity, conversation.conversationId(), EaseConstant.CHATTYPE_GROUP);
//            mActivity.finish();
    }
    private CenterAlertPopWindow mGroupFailingPop;
    @Override
    public void addGroupFailing(String msg) {
        if (mGroupFailingPop != null) {
            mGroupFailingPop.show();
            return;
        }
        mGroupFailingPop = CenterAlertPopWindow.builder()
                .titleStr(getString(R.string.tips))
                .desStr(msg)
                .itemRight(getString(R.string.affirm))
                .itemRightColor(R.color.themeColor)
                .isOutsideTouch(true)
                .isFocus(true)
                .animationStyle(R.style.style_actionPopupAnimation)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .buildCenterPopWindowItem1ClickListener(new CenterAlertPopWindow.CenterPopWindowItemClickListener() {
                    @Override
                    public void onRightClicked() {
//                        goBindPhone();
                        mGroupFailingPop.hide();
                    }

                    @Override
                    public void onLeftClicked() {
                        mGroupFailingPop.hide();
                    }
                })
                .parentView(getView())
                .build();
        mGroupFailingPop.show();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist_with_search;
    }

    @Override
    public String getsearchKeyWord() {
        return mSearchView.getText().toString().trim();
    }

    /*@Override
    public boolean isOnlyOfficialGroup() {
        return null == getArguments() ? false : getArguments().getBoolean(IS_ONLY_OFFICIAL_GROUP);
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
                getGroupListData();
                break;
            case TSEMConstants.TS_CONNECTION_DISCONNECTED:
                showStickyMessage(getString(R.string.chat_unconnected));
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
            List<ChatGroupBean> result = new ArrayList<>();
            for (ChatGroupBean sortModel : cache) {
                String name = sortModel.getName();
                boolean isContent = !TextUtils.isEmpty(name) && name.toLowerCase().contains(filterStr.toString().toLowerCase());
                if (isContent) {
                    result.add(sortModel);
                }
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        /*if(isVisibleToUser){
            if (mSearchView != null)
                mSearchView.setText("");
            if( null != getParentFragment() && ( (getParentFragment() instanceof MessageContainerFragment
                    && ((MessageContainerFragment)getParentFragment()).getCurrentItem() == 0)
                    || getParentFragment() instanceof AddressBookFragment) )
                onRefresh(null);
        }*/
    }

}
