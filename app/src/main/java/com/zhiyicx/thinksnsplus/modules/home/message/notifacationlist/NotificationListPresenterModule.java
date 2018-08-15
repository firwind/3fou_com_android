package com.zhiyicx.thinksnsplus.modules.home.message.notifacationlist;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/31
 * @contact email:648129313@qq.com
 */
@Module
public class NotificationListPresenterModule {

    private NotificationListContract.View mView;

    public NotificationListPresenterModule(NotificationListContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public NotificationListContract.View provideNotificationContractView(){
        return mView;
    }

}
