package com.zhiyicx.thinksnsplus.data.source.local;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/4 14:15
 * description:
 * version:
 */

public class MarketCurrencyBean {

    public String exchange_name;
    public String exchange_code;
    public String currency_code;
    public String currency_name;
    public String logo;
    public int sort;
    public double last;
    public double high;
    public double low;
    public double degree;
    public double vol;
    public String domain;
    public String ticker;
    public List<Double> line;
    public long datetimes;
    public String coin_url;
    public String klines_url;

    /*{
            "exchange_name": "Huobi",
            "exchange_code": "huobipro",
            "currency_code": "btc",
            "currency_name": "BTC",
            "logo": "https://resource.jinse.com/www/img/cslogo/huobipro.png?v=844",
            "sort": 1,
            "last": "43040.48",
            "high": "44121.67",
            "low": "42544.35",
            "degree": "-1.50",
            "vol": "0.96",
            "domain": "https://www.huobipro.com/zh-cn/",
            "ticker": "HUOBIPRO:BTCUSDT",
            "line": [
                "44044.88",
                "44038.97"
            ],
            "datetimes": null,
            "coin_url": "https://www.jinse.com/coin/bitcoin",
            "klines_url": "https://www.jinse.com/klines/bitcoin?symbol=HUOBIPRO:BTCUSDT"
        }*/


}
