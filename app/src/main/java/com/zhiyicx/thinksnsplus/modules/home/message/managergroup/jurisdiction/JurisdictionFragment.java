package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 17:59
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectFriendsAllAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.SelectedFriendsAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_EDIT_DATA;

public class JurisdictionFragment extends TSListFragment<JurisdictionContract.Presenter, UserInfoBean>
        implements JurisdictionContract.View, SelectMuteAdapter.OnUserSelectedListener {
    @Inject
    JurisdictionPresenter mJurisdictionPresenter;
    Unbinder unbinder;
    /**
     * 群信息
     */
    private ChatGroupBean mChatGroupBean;
    @BindView(R.id.iv_search_icon)
    ImageView mIvSearchIcon;
    @BindView(R.id.rv_select_result)
    RecyclerView mRvSelectResult;
    @BindView(R.id.edit_search_friends)
    AppCompatEditText mEditSearchFriends;
    @BindView(R.id.ll_search)
    LinearLayout mLinearLayout;
    private SelectedFriendsAdapter mSelectedFriendsAdapter;
    private List<UserInfoBean> mSelectedList;
    private List<UserInfoBean> mSearchResultList;

    public static JurisdictionFragment instance(Bundle bundle) {
        JurisdictionFragment jurisdictionFragment = new JurisdictionFragment();
        jurisdictionFragment.setArguments(bundle);
        return jurisdictionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new SelectMuteAdapter(getContext(), mListDatas, this);
    }

    @Override
    public void getUserListByKeyResult(List<UserInfoBean> userInfoBeans) {

    }

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }

    @Override
    public String getSearchKeyWord() {
        return mEditSearchFriends.getText().toString().trim();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initListener();
        mSelectedList = new ArrayList<>();
        mSearchResultList = new ArrayList<>();
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    private void initListener() {
        RxTextView.textChanges(mEditSearchFriends)
                .subscribe(charSequence -> {
                    // 搜索
                    mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
                });
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
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        // 当好友没有时隐藏搜索
        initView(data, isLoadMore);
    }

    public void initView(List<UserInfoBean> data, boolean isLoadMore) {
//        mLinearLayout.setVisibility(TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && data.isEmpty() ? View.GONE : View.VISIBLE);
        checkUserIsSelected(data);
        super.onNetResponseSuccess(data, isLoadMore);
        if (!TextUtils.isEmpty(mEditSearchFriends.getText().toString().trim()) && mListDatas.isEmpty()) {
            setEmptyViewVisiable(false);
        }
    }

    /**
     * 获取网络数据后，调用此方法来判断是否已选中
     *
     * @param datas data
     */
    private void checkUserIsSelected(List<UserInfoBean> datas) {

    }

    /**
     * 获取传递过来的数据
     */
    private void getIntentData() {
        if (getArguments() != null) {
            mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_EDIT_DATA);
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_setting_jurisdiction;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_setting_info_jurisdiction);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE);
        onBackPressed();
        super.setLeftClick();
    }

    @Override
    public void onUserSelected(UserInfoBean userInfoBean) {
        // 选中的列表中，如果是选中 那么直接加
//        LogUtils.e(TAG,"是否禁言---"+userInfoBean.getMember_mute());
        if (userInfoBean.getMember_mute() == 1) {
            mSelectedList.add(userInfoBean);
            mPresenter.openBannedPost(mChatGroupBean.getId(), String.valueOf(userInfoBean.getUser_id()),"",userInfoBean.getName());
        } else {
            Iterator<UserInfoBean> userIterator = mSelectedList.iterator();
            while (userIterator.hasNext()) {
                UserInfoBean data = userIterator.next();
                if (data.getUser_id().equals(userInfoBean.getUser_id())) {
                    // 列表中已经有这个用户了->取消选中->直接移除这个人，这里因为有搜索列表，所以不能直接remove
                    userIterator.remove();
                }
            }
            mPresenter.removeBannedPost(mChatGroupBean.getId(), String.valueOf(userInfoBean.getUser_id()),userInfoBean.getName());
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
        checkData();
    }

    /**
     * 禁言按钮是否可以点击
     */
    private void checkData() {
        mIvSearchIcon.setVisibility(mSelectedList.size() > 0 ? View.GONE : View.VISIBLE);
        mSelectedFriendsAdapter.notifyDataSetChanged();
//        if (!mSelectedList.isEmpty()) {
//            mRvSelectResult.smoothScrollToPosition(mSelectedFriendsAdapter.getItemCount() - 1);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_open_mute, R.id.bt_relieve_mute})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_open_mute:

                break;
            case R.id.bt_relieve_mute:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE);
        super.onBackPressed();
    }
}
