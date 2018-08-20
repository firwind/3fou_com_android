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
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;

public class SettingPrivacyActivity extends TSActivity<SettingPrivacyPresenter,SettingPrivacyFragment>{
    @Override
    protected SettingPrivacyFragment getFragment() {
        return SettingPrivacyFragment.getInstance(getIntent().getParcelableExtra(IntentKey.GROUP_INFO));
    }

    @Override
    protected void componentInject() {
        DaggerSettingPrivacyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .settingPrivacyPresenterModule(new SettingPrivacyPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startSettingPricacyActivity(Context context,ChatGroupBean chatGroupBean){
        Intent intent = new Intent(context,SettingPrivacyActivity.class);
        intent.putExtra(IntentKey.GROUP_INFO, (Parcelable) chatGroupBean);
        context.startActivity(intent);
    }
}
