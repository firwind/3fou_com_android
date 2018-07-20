package com.zhiyicx.thinksnsplus.modules.currency.address;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/20 10:02
 * description:
 * version:
 */
@Module
public class CurrencyAddressPresenterMoudle {

    private CurrencyAddressContract.View mView;

    public CurrencyAddressPresenterMoudle(CurrencyAddressContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CurrencyAddressContract.View provideCurrencyAddressContractView(){
        return mView;
    }

}
