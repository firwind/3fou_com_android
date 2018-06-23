package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 18:03
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */
@Module
public class JurisdictionPresenterModule {
    private JurisdictionContract.View mView;

    public JurisdictionPresenterModule(JurisdictionContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public JurisdictionContract.View provideJurisdictionContractView() {
        return mView;
    }
}
