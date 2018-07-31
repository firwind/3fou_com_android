package com.zhiyicx.thinksnsplus.modules.currency.interest;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/31 15:50
 * description:
 * version:
 */
@Module
public class CurrencyInterestModule {

    private CurrencyInterestContract.View mView;

    public CurrencyInterestModule(CurrencyInterestContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CurrencyInterestContract.View provideCurrencyInterestContractView(){
        return mView;
    }

}
