package com.zhiyicx.thinksnsplus.modules.information.infomain.flash;

import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class FlashListPresenterModule {

    InfoMainContract.FlashListView mInfoListView;

    public FlashListPresenterModule(InfoMainContract.FlashListView infoListView) {
        mInfoListView = infoListView;
    }

    @Provides
    InfoMainContract.FlashListView provideFlashListView() {
        return mInfoListView;
    }


}
