package com.zhiyicx.thinksnsplus.data.source.remote;

import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * author: huwenyong
 * date: 2018/7/4 9:32
 * description:
 * version: 行情网络请求
 */

public interface MarketClient {

    @GET(ApiConfig.APP_PATH_MARKET_CURRENCY_LIST)
    Observable<BaseJsonV2<List<String>>> getMarketCurrencyList();

    @GET(ApiConfig.APP_PATH_MARKET_CURRENCY_RANK_LIST)
    Observable<List<CurrencyRankBean>> getMarketCurrencyRankList();

    @GET(ApiConfig.APP_PATH_MARKET_CURRENCY_DETAILS)
    Observable<BaseJsonV2<List<MarketCurrencyBean>>> getMarketCurrencyDetails(@Query("currency") String currency);

}
