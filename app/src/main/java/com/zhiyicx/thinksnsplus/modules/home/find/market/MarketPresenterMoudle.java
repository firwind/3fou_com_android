package com.zhiyicx.thinksnsplus.modules.home.find.market;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/7/2 9:31
 * description:
 * version:
 */
@Module
public class MarketPresenterMoudle {

    private MarketContract.MarketView mView;

    public MarketPresenterMoudle(MarketContract.MarketView mView) {
        this.mView = mView;
    }

    @Provides
    public MarketContract.MarketView provideMarketContractView(){
        return mView;
    }

}
