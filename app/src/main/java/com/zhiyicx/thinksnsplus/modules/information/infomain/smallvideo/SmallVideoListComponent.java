package com.zhiyicx.thinksnsplus.modules.information.infomain.smallvideo;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/8/27 10:02
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SmallVideoListPresenterModule.class)
public interface SmallVideoListComponent extends InjectComponent<SmallVideoListFragment> {
}
