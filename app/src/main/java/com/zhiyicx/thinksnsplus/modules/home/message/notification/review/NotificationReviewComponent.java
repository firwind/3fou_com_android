package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/16 13:53
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = NotificationReviewPresenterModule.class)
public interface NotificationReviewComponent extends InjectComponent<NotificationReviewActivity>{
}
