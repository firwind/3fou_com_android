package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */
@Module
public class SmallVideoPresenterModule {

    private SmallVideoContract.View mView;

    public SmallVideoPresenterModule(SmallVideoContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public SmallVideoContract.View provideSmallVideoContractView(){
        return mView;
    }

}
