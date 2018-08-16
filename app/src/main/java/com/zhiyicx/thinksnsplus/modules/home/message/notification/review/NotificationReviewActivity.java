package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/8/16 11:59
 * description:
 * version:
 */

public class NotificationReviewActivity extends TSActivity<NotificationReviewPresenter,NotificationReviewFragment>{


    @Override
    protected NotificationReviewFragment getFragment() {
        return NotificationReviewFragment.newInstance(getIntent().getIntExtra(IntentKey.NOTIFICATION_REVIEW_TYPE,
                IntentKey.NOTIFICATION_REVIEW_FRIEND));
    }

    @Override
    protected void componentInject() {
        DaggerNotificationReviewComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .notificationReviewPresenterModule(new NotificationReviewPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


    public static void startNotificationReviewActivity(Context mContext,int type){

        Intent intent = new Intent(mContext,NotificationReviewActivity.class);
        intent.putExtra(IntentKey.NOTIFICATION_REVIEW_TYPE,type);
        mContext.startActivity(intent);

    }

}
