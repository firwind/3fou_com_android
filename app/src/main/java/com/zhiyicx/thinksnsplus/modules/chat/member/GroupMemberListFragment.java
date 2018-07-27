package com.zhiyicx.thinksnsplus.modules.chat.member;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_EDIT_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsFragment.BUNDLE_GROUP_IS_DELETE;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/23
 * @contact email:648129313@qq.com
 */

public class GroupMemberListFragment extends TSFragment<GroupMemberListContract.Presenter> implements GroupMemberListContract.View {

    public static final String BUNDLE_GROUP_MEMBER = "bundle_group_member";
    public static final int DEFAULT_COLUMS = 5;

    @BindView(R.id.rv_member_list)
    RecyclerView mRvMemberList;

    private ChatGroupBean mChatGroupBean;
    private List<UserInfoBean> mMemberList;
    private ChatMemberAdapter mAdapter;

    public GroupMemberListFragment instance(Bundle bundle) {
        GroupMemberListFragment fragment = new GroupMemberListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        GridLayoutManager manager = new GridLayoutManager(getContext(), DEFAULT_COLUMS);
        mRvMemberList.setLayoutManager(manager);
        mRvMemberList.addItemDecoration(new GridDecoration(10, getResources().getDimensionPixelOffset(R.dimen.spacing_large)));
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_group_all_member);
    }

    @Override
    protected void initData() {
        mMemberList = new ArrayList<>();
        mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_MEMBER);
//        initMemberList();

    }

    @Override
    public void onResume() {
        super.onResume();
        mMemberList.clear();
        mPresenter.getAllUserBean(mChatGroupBean.getId());
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_group_member_list;
    }

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean) {
        mMemberList.clear();
        mMemberList.addAll(chatGroupBean.getAffiliations());
        initMemberList();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getUserInfos(List<UserInfoBean> data) {
        mChatGroupBean.setAffiliations(data);
        initMemberList();
        mMemberList.addAll(data);

        mAdapter = new ChatMemberAdapter(getContext(), mMemberList, mChatGroupBean.getOwner(), true);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mMemberList.get(position).getUser_id() == -1L) {
                    // 添加
                    Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                    bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mMemberList.get(position).getUser_id() == -2L) {
                    // 移除
                    Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                    bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    PersonalCenterFragment.startToPersonalCenter(getContext(), mMemberList.get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
//        View view=new View(getContext());
//        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getResources().getDimensionPixelOffset(com
//                .hyphenate.easeui.R.dimen.chat_bottom_footer_height)));
        mRvMemberList.setAdapter(mAdapter);
    }

    private void initMemberList() {
//        mMemberList.clear();
        // 添加按钮，都可以拉人
        UserInfoBean chatUserInfoBean = new UserInfoBean();
        chatUserInfoBean.setUser_id(-1L);
        mMemberList.add(chatUserInfoBean);
        if (mPresenter.isOwner()) {
            // 删除按钮，仅群主
            UserInfoBean chatUserInfoBean1 = new UserInfoBean();
            chatUserInfoBean1.setUser_id(-2L);
            mMemberList.add(chatUserInfoBean1);
        }
//        mMemberList.addAll(mChatGroupBean.getAffiliations());
    }
}
