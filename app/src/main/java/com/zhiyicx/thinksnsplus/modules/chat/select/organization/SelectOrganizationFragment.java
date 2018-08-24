package com.zhiyicx.thinksnsplus.modules.chat.select.organization;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.OrganizationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.thinksnsplus.modules.chat.select.organization.SelectOrganizationActivity.GROUP_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.select.organization.SelectOrganizationActivity.GROUP_INFO;
import static com.zhiyicx.thinksnsplus.modules.chat.select.organization.SelectOrganizationActivity.GROUP_INFO_BEAN;
import static com.zhiyicx.thinksnsplus.modules.chat.select.organization.SelectOrganizationActivity.GROUP_ORGANIZATION_ID;


public class SelectOrganizationFragment extends TSListFragment<SelectOrganizationContract.Presenter, OrganizationBean> implements SelectOrganizationContract.View {
    @BindView(R.id.et_search_organization)
    DeleteEditText mSearchOrganization;
    public int mOrganizationId = -1;
    public String mGroupId;
    public boolean isCreate;
    public boolean isSelect;
    List<UserInfoBean> list;


    public static SelectOrganizationFragment instance(Bundle bundle) {
        SelectOrganizationFragment fragment = new SelectOrganizationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            list = (List<UserInfoBean>) getArguments().getSerializable(GROUP_INFO_BEAN);
            Bundle bundle = getArguments().getBundle(GROUP_INFO);
            if (bundle != null) {
                mOrganizationId = bundle.getInt(GROUP_ORGANIZATION_ID, -1);
                isCreate = mOrganizationId == -1 ? true : false;
                mGroupId = bundle.getString(GROUP_ID);
            } else {
                isCreate = true;
            }
        }
        getGroupListData();
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSearchOrganization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getGroupListData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        if (isCreate) {
            if (getOrganizationId() != -1) {
                mPresenter.createConversation(list);
            } else {
                showSnackErrorMessage("请选择组织");
            }
        } else {
            mPresenter.changOrganization();
        }

    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.album_finish);

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.select_organization_center_title);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_select_organization;
    }

    @Override
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
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
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<OrganizationBean>(getActivity(), R.layout.item_organization, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, OrganizationBean organizationBean, int position) {
                holder.setText(R.id.tv_organization_name, organizationBean.getName());
                ImageView imageView = holder.getView(R.id.iv_organization);
                imageView.setBackgroundResource(organizationBean.isSelector() ? R.mipmap.msg_box_succeed : R.mipmap.msg_box);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                for (int i = 0; i < mListDatas.size(); i++) {
                    OrganizationBean organizationBean = mListDatas.get(i);
                    if (position == i) {
                        organizationBean.setSelector(true);
                        setmOrganizationId(organizationBean.getId());
                    } else {
                        organizationBean.setSelector(false);
                    }
                }
                refreshData();

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS) {
            getActivity().finish();

        }
    }

    /**
     * 获取群列表
     */
    private void getGroupListData() {
        if (mPresenter != null) {
//            mRefreshlayout.autoRefresh(0);
            mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
        }
    }

    @Override
    public String getSearchKeyWord() {
        return mSearchOrganization.getText().toString();
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
        SelectFriendsActivity.mSelectFriendsActivity.finish();
    }

    @Override
    public int getOrganizationId() {
        return mOrganizationId;
    }
    public void setmOrganizationId(int mOrganizationId) {
        this.mOrganizationId = mOrganizationId;
    }
    @Override
    public String getGroupId() {
        return mGroupId;
    }

}
