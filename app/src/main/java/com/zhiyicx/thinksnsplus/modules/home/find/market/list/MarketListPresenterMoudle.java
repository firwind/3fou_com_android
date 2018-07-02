package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;

import dagger.Module;
import dagger.Provides;

/**
 * author: huwenyong
 * date: 2018/6/28 19:32
 * description:
 * version:
 */
@Module
public class MarketListPresenterMoudle {

    private MarketContract.MarketListView mView;

    public MarketListPresenterMoudle(MarketContract.MarketListView mView) {
        this.mView = mView;
    }

    @Provides
    public MarketContract.MarketListView provideMarketContractListView(){
        return mView;
    }

}
