package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/18 14:23
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = WithdrawCurrencyPresenterMoudle.class)
public interface WithdrawCurrencyComponent extends InjectComponent<WithdrawCurrencyActivity>{
}
