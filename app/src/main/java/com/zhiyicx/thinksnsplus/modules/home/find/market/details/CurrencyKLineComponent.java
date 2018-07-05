package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import javax.inject.Scope;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/5 11:43
 * description:
 * version:
 */

@FragmentScoped
@Component(dependencies = AppComponent.class,modules = {CurrencyKLinePresenterMoudle.class})
public interface CurrencyKLineComponent extends InjectComponent<CurrencyKLineActivity>{
}
