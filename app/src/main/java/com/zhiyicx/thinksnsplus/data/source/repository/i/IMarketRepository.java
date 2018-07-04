package com.zhiyicx.thinksnsplus.data.source.repository.i;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;

import java.util.List;

import rx.Observable;

/**
 * author: huwenyong
 * date: 2018/7/4 9:29
 * description:
 * version:
 */

public interface IMarketRepository {

    Observable<BaseJsonV2<List<String>>> getMarketCurrencyList();

    Observable<List<CurrencyRankBean>> getMarketCurrencyRankList();
}
