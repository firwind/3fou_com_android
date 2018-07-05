package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/5 11:42
 * description:
 * version:
 */
@Module
public class CurrencyKLinePresenterMoudle {

    private CurrencyKLineContract.View mView;

    public CurrencyKLinePresenterMoudle(CurrencyKLineContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public CurrencyKLineContract.View provieCurrencyKLineContractView(){
        return mView;
    }

}
