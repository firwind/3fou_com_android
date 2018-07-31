package com.zhiyicx.thinksnsplus.modules.currency.interest;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/31 15:51
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = CurrencyInterestModule.class)
public interface CurrencyInterestComponent extends InjectComponent<CurrencyInterestActivity>{
}
