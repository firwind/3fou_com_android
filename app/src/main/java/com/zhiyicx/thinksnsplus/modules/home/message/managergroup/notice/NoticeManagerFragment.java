package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 14:23
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice.ReleaseNoticeActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupContract;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_NOTICE;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.IS_GROUP_OWNER;

public class NoticeManagerFragment extends TSListFragment<NoticeManagerContract.Presenter, NoticeItemBean> implements NoticeManagerContract.View {

    private String mGroupId;
    private boolean isGroupOwner;
    @Inject
    NoticeManagerPresenter mNoticeManagerPresenter;

    public static NoticeManagerFragment newInstance(String groupId, boolean isGroupOwner) {
        NoticeManagerFragment noticeManagerFragment = new NoticeManagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GROUP_ORIGINAL_ID, groupId);
        bundle.putBoolean(IS_GROUP_OWNER, isGroupOwner);
        noticeManagerFragment.setArguments(bundle);
        return noticeManagerFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        NoticeManagerAdapter adapter = new NoticeManagerAdapter(getContext(), mListDatas);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                NoticeItemBean itemBean = mListDatas.get(position);
                startActivity(NoticeDetailsActivity.newNoticeDetailsIntent(getContext(), itemBean));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return adapter;


    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        if (getArguments() != null) {
            mGroupId = getArguments().getString(GROUP_ORIGINAL_ID);
            isGroupOwner = getArguments().getBoolean(IS_GROUP_OWNER, false);
        }
    }

    @Override
    protected void initData() {
        super.initData();

        if (isGroupOwner) {
            mToolbarRight.setText(setRightTitle());
        } else {
            mToolbarRight.setText("");
        }

        getNoticeListData();
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_btn);
    }


    @Override
    protected void setRightClick() {
        super.setRightClick();
        startActivity(ReleaseNoticeActivity.newIntent(getContext(), mGroupId));
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_edit_group_announcement);
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
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
        return true;
    }

    @Override
    public void getNoticeItemBeanList(List<NoticeItemBean> noticeItemBeans) {
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    @Override
    public void refreshNoticeListData() {
//        getNoticeListData();
    }

    /**
     * 获取公告列表
     */
    private void getNoticeListData() {
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_IM_GROUP_UPDATE_GROUP_NOTICE)
    public void onPublishNoticeSuccess(String isRefresh) {
        getNoticeListData();
    }
}
