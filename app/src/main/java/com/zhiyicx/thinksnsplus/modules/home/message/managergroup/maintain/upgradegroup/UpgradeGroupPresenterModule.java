package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */
@Module
public class UpgradeGroupPresenterModule {
    UpgradeGroupContract.View mView;

    public UpgradeGroupPresenterModule(UpgradeGroupContract.View view) {
        mView = view;
    }

    @Provides
    UpgradeGroupContract.View provideUpgradeGroupContractView() {
        return mView;
    }
}
