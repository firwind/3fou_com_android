package com.zhiyicx.thinksnsplus.modules.currency;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/17 16:13
 * description:
 * version:
 */
@Module
public class MyCurrencyPresenterMoudle {

    private MyCurrencyContract.View mView;

    public MyCurrencyPresenterMoudle(MyCurrencyContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyCurrencyContract.View provideMyCurrencyContractView(){
        return mView;
    }

}
