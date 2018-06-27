package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/25 17:13
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;

public class AddGroupFragment extends TSListFragment<AddGroupContract.Presenter, ChatGroupServerBean> implements AddGroupContract.View {

    @BindView(R.id.tv_no_search_result)
    TextView mNoSearchResult;
    @BindView(R.id.rv_search_group)
    RecyclerView mSearchRecyclerView;
    Unbinder unbinder;
    @BindView(R.id.et_search_group)
    DeleteEditText mSearchGroup;
    private boolean mIsSearch = false;

    public static AddGroupFragment instance(Bundle bundle) {
        AddGroupFragment fragment = new AddGroupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.tv_add_group_chat);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_add_group;
    }

    @Override
    public String getsearchKeyWord() {

        return mSearchGroup.getText().toString().trim();
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<ChatGroupServerBean> data, boolean isLoadMore) {
        CommonAdapter adapter = null;
        if (mIsSearch) {

            adapter = new CommonAdapter<ChatGroupServerBean>(getActivity(), R.layout.item_group_list, data) {
                @Override
                protected void convert(ViewHolder holder, ChatGroupServerBean chatGroupBean, int position) {
                    holder.setText(R.id.tv_group_name, chatGroupBean.getName());
                    Glide.with(mContext)
                            .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                                    .getGroup_face())
                            .error(R.mipmap.ico_ts_assistant)
                            .placeholder(R.mipmap.ico_ts_assistant)
                            .transform(new GlideCircleTransform(mContext))
                            .into(holder.getImageViwe(R.id.uv_group_head));

                }
            };
            adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                    ChatGroupServerBean groupBean = data.get(position);
                    mPresenter.checkGroupExist(groupBean.getId());
                }

                @Override
                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                    return false;
                }
            });
            mSearchRecyclerView.setAdapter(adapter);
            if (data.size() == 0) {
                mSearchRecyclerView.setVisibility(View.GONE);
                mNoSearchResult.setVisibility(View.VISIBLE);
            } else {
                mSearchRecyclerView.setVisibility(View.VISIBLE);
                mNoSearchResult.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
            }
        } else {
            super.onNetResponseSuccess(data, isLoadMore);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        getGroupListData();
    }

    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //设置Item的间隔
        mSearchRecyclerView.addItemDecoration(getItemDecoration());
        mSearchRecyclerView.setHasFixedSize(false);
        mSearchRecyclerView.setLayoutManager(mLayoutManager);
        mSearchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mPresenter.setSearchGroup(mSearchGroup.getText().toString().trim());
                if (mSearchGroup.getText().toString().trim().length() > 0)
                    mIsSearch = true;
                else
                    mIsSearch = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    public void checkGroupExist(String id, EMGroup data) {
        mPresenter.checkIsAddGroup(id, data);
    }

    @Override
    public void checkIsAddGroup(String id, EMGroup data, boolean isExist) {
        if (data != null && isExist) {//已经在群里
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(id, EMConversation.EMConversationType.GroupChat, true);
            ChatActivity.startChatActivity(mActivity, conversation.conversationId(), CHATTYPE_GROUP);
//            mActivity.finish();
        } else {//
            ChatInfoActivity.startChatInfoActivity(getContext(), isExist, id, CHATTYPE_GROUP);

        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<ChatGroupServerBean>(getActivity(), R.layout.item_group_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, ChatGroupServerBean chatGroupBean, int position) {
                holder.setText(R.id.tv_group_name, chatGroupBean.getName());
                Glide.with(mContext)
                        .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                                .getGroup_face())
                        .error(R.mipmap.ico_ts_assistant)
                        .placeholder(R.mipmap.ico_ts_assistant)
                        .transform(new GlideCircleTransform(mContext))
                        .into(holder.getImageViwe(R.id.uv_group_head));

            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ChatGroupServerBean groupBean = mListDatas.get(position);
                mPresenter.checkGroupExist(groupBean.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return adapter;
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

    /**
     * 获取群列表
     */
    private void getGroupListData() {
        if (mPresenter != null) {
//            mRefreshlayout.autoRefresh(0);
            mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID,false);
        }
    }
}
