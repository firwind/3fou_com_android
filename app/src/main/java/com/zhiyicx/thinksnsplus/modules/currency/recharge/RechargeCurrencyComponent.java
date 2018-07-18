package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/18 10:18
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = RechargeCurrencyMoudle.class)
public interface RechargeCurrencyComponent extends InjectComponent<RechargeCurrencyActivity>{
}
