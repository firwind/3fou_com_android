package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;


import dagger.Component;

/**
 * Created by zl on 2018/7/27 0027.
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = SettingPayPasswordModule.class)
public interface SettingPayPasswordComponent extends InjectComponent<SettingPayPasswordActivity>{
}
