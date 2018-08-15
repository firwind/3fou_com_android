package com.zhiyicx.thinksnsplus.modules.settings.privacy;

import com.zhiyicx.thinksnsplus.modules.settings.SettingsContract;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@Module
public class SettingPricacyPresenterModule {
    private final SettingPricacyContract.View mView;

    public SettingPricacyPresenterModule(SettingPricacyContract.View view) {
        this.mView = view;
    }

    @Provides
    SettingPricacyContract.View provideSettingPricacyContractView() {
        return mView;
    }
}
