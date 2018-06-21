package com.zhiyicx.thinksnsplus.modules.settings.account;

import android.content.Intent;

import com.umeng.socialize.UMShareAPI;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * @author Catherine
 * @describe 账户管理页面
 * @date 2017/7/28
 * @contact email:648129313@qq.com
 */

public class AccountManagementActivity extends TSActivity<AccountManagementPresenter, AccountManagementFragment>{
    @Override
    protected AccountManagementFragment getFragment() {
        return new AccountManagementFragment();
    }

    @Override
    protected void componentInject() {
        DaggerAccountManagementComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .accountManagementPresenterModule(new AccountManagementPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode,resultCode,data,this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }

}
