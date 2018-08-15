package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/15 11:35
 * description:
 * version:
 */
@Module
public class NotificationPresenterModule {

    private NotificationContract.View mView;

    public NotificationPresenterModule(NotificationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NotificationContract.View provideNotificationContractView(){
        return mView;
    }

}
