package com.zhiyicx.thinksnsplus.modules.currency.address;

import com.zhiyicx.common.dagger.module.AppModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/20 10:03
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = CurrencyAddressPresenterMoudle.class)
public interface CurrencyAddressComponent extends InjectComponent<CurrencyAddressActivity> {
}
