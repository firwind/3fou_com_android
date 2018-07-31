package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */

public class ChangePasswordActivity extends TSActivity<ChangePasswordPresenter, ChangePasswordFragment> {
    @Override
    protected void componentInject() {
        DaggerChangePasswordComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .changePasswordPresenterModule(new ChangePasswordPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    protected ChangePasswordFragment getFragment() {
        return ChangePasswordFragment.newInstance(
                getIntent().getIntExtra(IntentKey.PASSWORD_TYPE,IntentKey.TYPE_PASSWORD_LOGIN));
    }

    /**
     * 修改或设置支付密码
     * @param mContext
     */
    public static void startChangePayPasswordActivity(Context mContext){
        Intent intent = new Intent(mContext,ChangePasswordActivity.class);
        intent.putExtra(IntentKey.PASSWORD_TYPE,IntentKey.TYPE_PASSWORD_PAY);
        mContext.startActivity(intent);
    }

}
