package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/16 13:51
 * description:
 * version:
 */
@Module
public class NotificationReviewPresenterModule {

    private NotificationReviewContract.View mView;

    public NotificationReviewPresenterModule(NotificationReviewContract.View mView) {
        this.mView = mView;
    }


    @Provides
    public NotificationReviewContract.View provideNotificationReviewContractView(){
        return mView;
    }

}
