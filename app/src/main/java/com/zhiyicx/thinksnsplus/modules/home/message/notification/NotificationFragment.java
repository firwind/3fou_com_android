package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.NotificationBean;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/15 10:45
 * description:
 * version:
 */

public class NotificationFragment extends TSListFragment<NotificationContract.Presenter,NotificationBean>
        implements NotificationContract.View{

    @Inject
    NotificationPresenter mPresenter;

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected void initView(View rootView) {

        DaggerNotificationComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .notificationPresenterModule(new NotificationPresenterModule(this))
                .build()
                .inject(this);

        super.initView(rootView);
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }




}
