package com.zhiyicx.thinksnsplus.modules.wallet;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * @Describe
 * @Author zl
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = WalletPresenterModule.class)
public interface WalletComponent extends InjectComponent<WalletActivity> {
}
