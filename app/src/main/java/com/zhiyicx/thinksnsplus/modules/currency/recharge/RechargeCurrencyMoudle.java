package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/18 10:17
 * description:
 * version:
 */
@Module
public class RechargeCurrencyMoudle {

    private RechargeCurrencyContract.View mView;

    public RechargeCurrencyMoudle(RechargeCurrencyContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public RechargeCurrencyContract.View provideRechargeCurrencyContractView(){
        return mView;
    }

}
