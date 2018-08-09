package com.zhiyicx.thinksnsplus.modules.home.message.homepage;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/9 10:01
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MessageHomePagePresenterModule.class)
public interface MessageHomePageComponent extends InjectComponent<MessageHomePageFragment>{
}
