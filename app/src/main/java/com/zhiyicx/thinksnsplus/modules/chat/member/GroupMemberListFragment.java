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
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

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

public class GroupMemberListFragment extends TSListFragment<GroupMemberListContract.Presenter,UserInfoBean> implements GroupMemberListContract.View {

    public static final String BUNDLE_GROUP_MEMBER = "bundle_group_member";
    public static final int DEFAULT_COLUMS = 5;
/*
    @BindView(R.id.rv_member_list)
    RecyclerView mRvMemberList;*/

    private ChatGroupBean mChatGroupBean;

    public GroupMemberListFragment instance(Bundle bundle) {
        GroupMemberListFragment fragment = new GroupMemberListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mChatGroupBean = getArguments().getParcelable(BUNDLE_GROUP_MEMBER);
        super.initView(rootView);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(getContext(), DEFAULT_COLUMS);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new GridDecoration(10, getResources().getDimensionPixelOffset(R.dimen.spacing_large));
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
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        //initAddOrDeleteBtn();
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter mAdapter = new ChatMemberAdapter(getContext(), mListDatas, mChatGroupBean.getOwner(), true);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mListDatas.get(position).getUser_id() == -1L) {
                    // 添加
                    Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                    bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (mListDatas.get(position).getUser_id() == -2L) {
                    // 移除
                    Intent intent = new Intent(getContext(), SelectFriendsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(BUNDLE_GROUP_EDIT_DATA, mChatGroupBean);
                    bundle.putBoolean(BUNDLE_GROUP_IS_DELETE, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    PersonalCenterFragment.startToPersonalCenter(getContext(), mListDatas.get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return mAdapter;
    }

   /* @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_group_member_list;
    }*/

    @Override
    public ChatGroupBean getGroupData() {
        return mChatGroupBean;
    }


    private void initAddOrDeleteBtn() {
        UserInfoBean chatUserInfoBean = new UserInfoBean();
        chatUserInfoBean.setUser_id(-1L);
        mListDatas.add(chatUserInfoBean);
        if (getGroupData().getOwner() == AppApplication.getMyUserIdWithdefault()) {
            // 删除按钮，仅群主
            UserInfoBean chatUserInfoBean1 = new UserInfoBean();
            chatUserInfoBean1.setUser_id(-2L);
            mListDatas.add(chatUserInfoBean1);
        }
    }

}
