package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TSPNotificationBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Catherine
 * @describe 通知列表页
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
public class NotificationListFragment extends TSListFragment<NotificationListContract.Presenter, TSPNotificationBean>
        implements NotificationListContract.View {

    @Inject
    NotificationListPresenter mNotificationListPresenter;

    public static NotificationListFragment instance() {
        NotificationListFragment fragment = new NotificationListFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.system_notification);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new NotificationListAdapter(getContext(), mListDatas);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }
    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mPresenter != null && isVisibleToUser) {
            mRefreshlayout.autoRefresh();
        }
    }

    @Override
    protected Long getMaxId(@NotNull List<TSPNotificationBean> data) {
        return (long) mListDatas.size();
    }
}
