package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/17 16:15
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MyCurrencyPresenterMoudle.class)
public interface MyCurrencyComponent extends InjectComponent<MyCurrencyActivity>{
}
