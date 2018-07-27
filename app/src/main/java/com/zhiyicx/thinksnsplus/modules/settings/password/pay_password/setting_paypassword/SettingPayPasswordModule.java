package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2018/7/27 0027.
 */
@Module
public class SettingPayPasswordModule {
    private final SettingPayPasswordContract.View mView;

    public SettingPayPasswordModule(SettingPayPasswordContract.View view) {
        this.mView = view;
    }

    @Provides
    SettingPayPasswordContract.View provideSttingPayPasswordContractView() {
        return mView;
    }
}
