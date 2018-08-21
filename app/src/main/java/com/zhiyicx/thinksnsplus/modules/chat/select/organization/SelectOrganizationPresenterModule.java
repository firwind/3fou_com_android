package com.zhiyicx.thinksnsplus.modules.chat.select.organization;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@Module
public class SelectOrganizationPresenterModule {
    private SelectOrganizationContract.View mView;

    public SelectOrganizationPresenterModule(SelectOrganizationContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SelectOrganizationContract.View provideSelectOrganizationContractView() {
        return mView;
    }

}
