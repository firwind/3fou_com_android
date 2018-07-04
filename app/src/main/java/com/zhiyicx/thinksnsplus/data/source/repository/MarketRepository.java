package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MarketClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IMarketRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/7/4 9:31
 * description:
 * version:
 */

public class MarketRepository implements IMarketRepository{

    private MarketClient mMarketClient;

    @Inject
    public MarketRepository(ServiceManager serviceManager){
        this.mMarketClient = serviceManager.getMarketClient();
    }

    @Override
    public Observable<BaseJsonV2<List<String>>> getMarketCurrencyList() {
        return mMarketClient.getMarketCurrencyList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CurrencyRankBean>> getMarketCurrencyRankList() {
        return mMarketClient.getMarketCurrencyRankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
