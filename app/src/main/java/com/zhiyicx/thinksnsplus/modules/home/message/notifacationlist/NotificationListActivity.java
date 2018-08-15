package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */

public class NotificationListActivity extends TSActivity<NotificationListPresenter, NotificationListFragment>{

    @Override
    protected NotificationListFragment getFragment() {
        return NotificationListFragment.instance();
    }

    @Override
    protected void componentInject() {
        DaggerNotificationListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .notificationListPresenterModule(new NotificationListPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
}
