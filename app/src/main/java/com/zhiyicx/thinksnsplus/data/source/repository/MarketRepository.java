package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.CurrencyBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.beans.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.local.HomeMessageIndexBean;
import com.zhiyicx.thinksnsplus.data.source.remote.MarketClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IMarketRepository;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;

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
    public Observable<List<CurrencyBean>> getMarketCurrencyList() {
        return mMarketClient.getMarketCurrencyList("http://api.jinse.com/v4/market/currencyList?version=3.2.0&source=android")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<CurrencyRankBean>> getMarketCurrencyRankList() {
        return mMarketClient.getMarketCurrencyRankList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<MarketCurrencyBean>> getMarketList(String currency,String type) {
        return mMarketClient.getMarketCurrencyDetails(
                String.format("http://api.jinse.com/v3/market/list?currency_type=%s&type=%s&currency=CNY&version=3.2.0&source=android"
                        ,currency,type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<KLineEntity>> getCurrencyKLineData(String ticker, String period) {
        return mMarketClient.getCurrencyKLineData(ticker,period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<HomeMessageIndexBean> getHomeMessageIndexData() {
        return mMarketClient.getHomeMessageIndex()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
