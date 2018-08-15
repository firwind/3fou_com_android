package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：隐私设置
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：设置隐私，加好友设置
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

public class SettingPricacyActivity extends TSActivity<SettingPricacyPresenter,SettingPricacyFragment>{
    @Override
    protected SettingPricacyFragment getFragment() {
        return SettingPricacyFragment.getInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSettingPricacyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .settingPricacyPresenterModule(new SettingPricacyPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startSettingPricacyActivity(Context context){
        Intent intent = new Intent(context,SettingPricacyActivity.class);
        context.startActivity(intent);
    }
}
