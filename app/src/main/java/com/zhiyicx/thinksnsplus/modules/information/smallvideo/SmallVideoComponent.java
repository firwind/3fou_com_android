package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/27 18:59
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SmallVideoPresenterModule.class)
public interface SmallVideoComponent extends InjectComponent<SmallVideoActivity>{
}
