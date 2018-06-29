package com.zhiyicx.thinksnsplus.modules.home.find.market;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/6/28 19:32
 * description:
 * version:
 */
@Module
public class MarketPresenterMoudle {

    private MarketContract.View mView;

    public MarketPresenterMoudle(MarketContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MarketContract.View provideMarketContractView(){
        return mView;
    }

}
