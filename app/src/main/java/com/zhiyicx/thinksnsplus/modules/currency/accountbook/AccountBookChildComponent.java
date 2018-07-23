package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/23 9:38
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = AccountBookChildMoudle.class)
public interface AccountBookChildComponent extends InjectComponent<AccountBookChildFragment>{
}
