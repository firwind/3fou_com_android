package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.modules.settings.SettingsContract;

import javax.inject.Inject;

/**
 * Created by zl on 2018/7/27 0027.
 */

public class SettingPayPasswordPresenter extends BasePresenter<SettingPayPasswordContract.View> implements SettingPayPasswordContract.Presenter{
    @Inject
    public SettingPayPasswordPresenter(SettingPayPasswordContract.View rootView) {
        super(rootView);
    }
}
