package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/**
 * author: huwenyong
 * date: 2018/7/30 18:19
 * description:
 * version:
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = PayPasswordModule.class)
public interface PayPasswordComponent extends InjectComponent<PayPassWordFragment>{
}
