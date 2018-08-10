package com.zhiyicx.thinksnsplus.modules.information.flashdetails;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@Module
public class FlashDetailsPresenterModule {
    private FlashDetailsContract.View mView;

    public FlashDetailsPresenterModule(FlashDetailsContract.View mView) {
        this.mView = mView;
    }
    @Provides
    FlashDetailsContract.View provideFlashDetailsContractView() {
        return mView;
    }
}
