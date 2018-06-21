package com.zhiyicx.thinksnsplus.modules.home.mine.scan;

import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@Module
public class ScanCodePresenterModule {

    private ScanCodeContract.View mView;

    public ScanCodePresenterModule(ScanCodeContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public ScanCodeContract.View provideScanCodeContractView(){
        return mView;
    }
}
