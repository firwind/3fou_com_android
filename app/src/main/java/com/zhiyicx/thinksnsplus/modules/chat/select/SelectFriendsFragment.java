package com.zhiyicx.thinksnsplus.modules.chat.select;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectFriendsAllAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectedFriendsAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.select.organization.SelectOrganizationActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsFragment extends TSListFragment<SelectFriendsContract.Presenter, UserInfoBean>
        implements SelectFriendsContract.View, SelectFriendsAllAdapter.OnUserSelectedListener {

    public static final String BUNDLE_GROUP_EDIT_DATA = "bundle_group_edit_data";
    public static final String BUNDLE_GROUP_IS_DELETE = "bundle_group_is_delete";

    @BindView(R.id.iv_search_icon)
    ImageView mIvSearchIcon;
    @BindView(R.id.rv_select_result)
    RecyclerView mRvSelectResult;
    @BindView(R.id.edit_search_friends)
    AppCompatEditText mEditSearchFriends;
    @BindView(R.id.ll_search)
    LinearLayout mLinearLayout;

    private List<UserInfoBean> mSelectedList;
    private List<UserInfoBean> mSearchResultList;
    private SelectedFriendsAdapter mSelectedFriendsAdapter;

    /**
     * 群信息
     */
    private ChatGroupBean mChatGroupBean;

    /**
     * 是否是删除用户  如果是删除 那么则不用去请求好友列表
     */
    private boolean mIsDeleteMember;

    public static SelectFriendsFragment instance(Bundle bundle) {
        SelectFriendsFragment friendsFragment = new SelectFriendsFragment();
        friendsFragment.setArguments(bundle);
        return friendsFragment;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SelectFriendsAllAdapter(getContext(), mListDatas, this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setLoadMorNodataTipText(getString(R.string.no_more_firends));

        mSelectedList = new ArrayList<>();
        mSearchResultList = new ArrayList<>();

        if (mChatGroupBean != null) {
            setCenterText(getString(!mIsDeleteMember ? R.string.chat_edit_group_add_member : R.string.chat_edit_group_remove_member));
        }
        setLeftTextColor(R.color.themeColor);
        initListener();
    }

    private void initListener() {
        RxTextView.textChanges(mEditSearchFriends)
                .subscribe(charSequence -> {
                    // 搜索
                    mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
                });
    }


    /**
     * 获取传递过来的数据，如果是创建会话，那么则不会有传递过来的数据
     */
    private void getIntentData() {
        if (getArguments() != null) {
            mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_EDIT_DATA);
            mIsDeleteMember = getArguments().getBoolean(BUNDLE_GROUP_IS_DELETE);
        }
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return !mIsDeleteMember;
    }

    @Override
    protected void initData() {
        super.initData();
        // 选中结果
        LinearLayoutManager selectManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvSelectResult.setLayoutManager(selectManager);
        mSelectedFriendsAdapter = new SelectedFriendsAdapter(getContext(), mSelectedList);
        mRvSelectResult.setAdapter(mSelectedFriendsAdapter);
        mRvSelectResult.addItemDecoration(new LinearDecoration(0, 0, getResources().getDimensionPixelOffset(R.dimen.spacing_small), 0));

        checkData();
    }

    @Override
    public String getSearchKeyWord() {
        return mEditSearchFriends.getText().toString().trim();
    }

    @Override
    public void nextCreateGroup(List<UserInfoBean> list) {
        SelectOrganizationActivity.startSelectOrganizationActivity(getContext(),list);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.select_friends_right_title_default);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.select_friends_center_title);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected void setLeftClick() {
        getActivity().finish();
    }

    @Override
    protected void setRightClick() {
        // 发起聊天
        if (mSelectedList.size() > 0) {
            if (mChatGroupBean != null && !TextUtils.isEmpty(mChatGroupBean.getId())) {
                mPresenter.dealGroupMember(mSelectedList);
            } else {
                // 创建群
                List<UserInfoBean> list = new ArrayList<>();
                list.addAll(mSelectedList);
                if (mChatGroupBean != null && TextUtils.isEmpty(mChatGroupBean.getId())) {
                    list.addAll(mChatGroupBean.getAffiliations());
                }
                mPresenter.createConversation(list);

            }
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_select_friends;
    }

    /**
     * 聊天按钮是否可以点击
     */
    private void checkData() {
        mToolbarRight.setEnabled(mSelectedList.size() != 0);
        mIvSearchIcon.setVisibility(mSelectedList.size() > 0 ? View.GONE : View.VISIBLE);
        if (mSelectedList.size() > 0) {

            if (mChatGroupBean == null) {
                if (mSelectedList.size()>1){
                    setRightText(String.format(getString(R.string.select_friends_right_next_title), mSelectedList.size()));
                }else {
                    setRightText(String.format(getString(R.string.select_friends_right_title), mSelectedList.size()));
                }
            } else {
                setRightText(String.format(getString(mIsDeleteMember ? R.string.chat_edit_group_remove_d : R.string.chat_edit_group_add_d),
                        mSelectedList.size()));
            }

            mToolbarRight.setTextColor(getColor(R.color.themeColor));
        } else {
            if (mChatGroupBean == null) {
                setRightText(getString(R.string.select_friends_right_title_default));
            } else {
                setRightText(String.format(getString(mIsDeleteMember ? R.string.chat_edit_group_remove : R.string.chat_edit_group_add),
                        mSelectedList.size()));
            }
            mToolbarRight.setTextColor(getColor(R.color.normal_for_disable_button_text));
        }
        mSelectedFriendsAdapter.notifyDataSetChanged();
        if (!mSelectedList.isEmpty()) {
            mRvSelectResult.smoothScrollToPosition(mSelectedFriendsAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onUserSelected(UserInfoBean userInfoBean) {
        if (userInfoBean.getIsSelected() == -1){
            ToastUtils.showToast("已经是群成员，不能重复选择");
            return;
        }
        // 选中的列表中，如果是选中 那么直接加
        if (userInfoBean.getIsSelected() == 1) {
            mSelectedList.add(userInfoBean);
        } else {
            Iterator<UserInfoBean> userIterator = mSelectedList.iterator();
            while (userIterator.hasNext()) {
                UserInfoBean data = userIterator.next();
                if (data.getUser_id().equals(userInfoBean.getUser_id())) {
                    // 列表中已经有这个用户了->取消选中->直接移除这个人，这里因为有搜索列表，所以不能直接remove
                    userIterator.remove();
                }
            }
        }

        // 处理全部列表, 搜索有数据，则表示此时为搜索(在隐藏搜索列表时清空了列表)
        if (mSearchResultList.size() > 0 && mEditSearchFriends.hasFocus()) {
            int position = 0;
            List<UserInfoBean> newList = new ArrayList<>();
            for (UserInfoBean userInfoBean1 : mListDatas) {
                if (userInfoBean1.getUser_id().equals(userInfoBean.getUser_id())) {
                    position = mListDatas.indexOf(userInfoBean1);
                    userInfoBean1.setIsSelected(userInfoBean.getIsSelected());
                    newList.add(userInfoBean1);
                    break;
                }
            }
            mListDatas.remove(position);
            mListDatas.addAll(position, newList);
            mAdapter.notifyDataSetChanged();
        }
        mEditSearchFriends.setText("");
        checkData();
    }

    @Override
    public void getFriendsListByKeyResult(List<UserInfoBean> userInfoBeans) {
        checkUserIsSelected(userInfoBeans);
        mSearchResultList.clear();
        mSearchResultList.addAll(userInfoBeans);
    }

    @Override
    public void createConversionResult(List<ChatUserInfoBean> list, EMConversation.EMConversationType type, int chatType, String id) {
        if (type == EMConversation.EMConversationType.Chat) {
            EMClient.getInstance().chatManager().getConversation(id, type, true);
        } else {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(id);
            if (group == null) {
                showSnackErrorMessage(getString(R.string.create_fail));
                return;
            }
        }
        ChatActivity.startChatActivity(mActivity, id, chatType);
        getActivity().finish();
    }

    @Override
    public boolean getIsDeleteMember() {
        return mIsDeleteMember;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }

    @Override
    public void dealGroupMemberResult() {
        getActivity().finish();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        // 当好友没有时隐藏搜索
        mLinearLayout.setVisibility(TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && data.isEmpty() ? View.GONE : View.VISIBLE);
        checkUserIsSelected(data);
        super.onNetResponseSuccess(data, isLoadMore);
        if (!TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && mListDatas.isEmpty()) {
            setEmptyViewVisiable(false);
        }
    }

    @Override
    public void onCacheResponseSuccess(List<UserInfoBean> data, boolean isLoadMore) {
        /*mLinearLayout.setVisibility(TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && data.isEmpty() ? View.GONE : View.VISIBLE);
        checkUserIsSelected(data);
        super.onCacheResponseSuccess(data, isLoadMore);
        if (!TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && mListDatas.isEmpty()) {
            setEmptyViewVisiable(false);
        }*/
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }

    /**
     * 获取网络数据后，调用此方法来判断是否已选中
     *
     * @param list data
     */
    private void checkUserIsSelected(List<UserInfoBean> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        for (UserInfoBean userInfoBean : list) {
            // 给已经选中的用户手动设置
            if (mSelectedList.size() > 0) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (userInfoBean.getUser_id().equals(mSelectedList.get(i).getUser_id())) {
                        userInfoBean.setIsSelected(1);
                        break;
                    }
                }
            }
            // 如果是添加群成员 那么要把已经有的成员处理为不可点击
            /*if (!mIsDeleteMember && mChatGroupBean != null && mChatGroupBean.getAffiliations().size() > 0) {
                for (int i = 0; i < mChatGroupBean.getAffiliations().size(); i++) {
                    if (userInfoBean.getUser_id().equals(mChatGroupBean.getAffiliations().get(i).getUser_id())) {
                        userInfoBean.setIsSelected(-1);
                        break;
                    }
                }
            }*/
        }
    }

}
