package com.zhiyicx.thinksnsplus.modules.home.message.notification;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/15 11:37
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = NotificationPresenterModule.class)
public interface NotificationComponent extends InjectComponent<NotificationFragment>{
}
