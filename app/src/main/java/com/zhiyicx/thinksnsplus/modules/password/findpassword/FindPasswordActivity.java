package com.zhiyicx.thinksnsplus.modules.password.findpassword;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword.SettingPayPasswordActivity;

/**
 * @Describe 找回密码
 * @Author zl
 * @Date 2017/1/11
 * @Contact master.jungle68@gmail.com
 */

public class FindPasswordActivity extends TSActivity<FindPasswordPresenter, FindPasswordFragment> {

    @Override
    protected void componentInject() {
        DaggerFindPasswordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .findPasswordPresenterModule(new FindPasswordPresenterModule(mContanierFragment))
                .build()
                .inject(this);

    }

    @Override
    protected FindPasswordFragment getFragment() {
        return FindPasswordFragment.newInstance();
    }

    public static void startSettingPayPasswordActivity(Context context){
        Intent intent = new Intent(context,FindPasswordActivity.class);
        context.startActivity(intent);
    }

}
