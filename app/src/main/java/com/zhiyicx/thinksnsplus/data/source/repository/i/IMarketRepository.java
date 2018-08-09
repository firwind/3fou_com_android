package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.thinksnsplus.data.beans.CurrencyBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.beans.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.local.HomeMessageIndexBean;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;

import java.util.List;

import rx.Observable;

/**
 * author: huwenyong
 * date: 2018/7/4 9:29
 * description:
 * version:
 */

public interface IMarketRepository {

    Observable<List<CurrencyBean>> getMarketCurrencyList();

    Observable<List<CurrencyRankBean>> getMarketCurrencyRankList();

    Observable<List<MarketCurrencyBean>> getMarketList(String currency,String type);

    Observable<List<KLineEntity>> getCurrencyKLineData(String ticker,String period);

    Observable<HomeMessageIndexBean> getHomeMessageIndexData();
}
