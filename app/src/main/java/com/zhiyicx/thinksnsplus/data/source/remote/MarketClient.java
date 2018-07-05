package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * author: huwenyong
 * date: 2018/7/4 9:32
 * description:
 * version: 行情网络请求
 */

public interface MarketClient {

    @GET
    Observable<List<CurrencyBean>> getMarketCurrencyList(@Url String url);

    @GET(ApiConfig.APP_PATH_MARKET_CURRENCY_RANK_LIST)
    Observable<List<CurrencyRankBean>> getMarketCurrencyRankList();

    @GET
    Observable<List<MarketCurrencyBean>> getMarketCurrencyDetails(@Url String url);

}
