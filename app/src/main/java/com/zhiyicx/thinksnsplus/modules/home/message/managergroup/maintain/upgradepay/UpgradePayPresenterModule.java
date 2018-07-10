package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */
@Module
public class UpgradePayPresenterModule {
    UpgradePayContract.View mView;
    public UpgradePayPresenterModule(UpgradePayContract.View view){
         this.mView = view;
    }

    @Provides
    UpgradePayContract.View provideUpgradePayContractView() {
        return mView;
    }
}
