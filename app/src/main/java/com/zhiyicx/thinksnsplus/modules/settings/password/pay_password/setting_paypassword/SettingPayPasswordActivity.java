package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class SettingPayPasswordActivity extends TSActivity<SettingPayPasswordPresenter,SettingPayPasswordFragment> {
    @Override
    protected SettingPayPasswordFragment getFragment() {
        return SettingPayPasswordFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }

    public static void startSettingPayPasswordActivity(Context context){
        Intent intent = new Intent(context,SettingPayPasswordActivity.class);
        context.startActivity(intent);
    }
}
