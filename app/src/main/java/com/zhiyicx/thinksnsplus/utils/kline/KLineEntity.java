package com.zhiyicx.thinksnsplus.utils.kline;

import android.support.annotation.NonNull;

import com.github.tifezh.kchartlib.chart.entity.IKLine;

import java.util.Comparator;
import java.util.Date;

/**
 * author: huwenyong
 * date: 2018/7/5 15:17
 * description:
 * version:
 */

public class KLineEntity implements IKLine,Comparable<KLineEntity>{


    public String getDatetime() {
        return new Date(dateTime).toString();
    }

    @Override
    public float getOpenPrice() {
        return open;
    }

    @Override
    public float getHighPrice() {
        return high;
    }

    @Override
    public float getLowPrice() {
        return low;
    }

    @Override
    public float getClosePrice() {
        return close;
    }

    @Override
    public float getMA5Price() {
        return MA5Price;
    }

    @Override
    public float getMA10Price() {
        return MA10Price;
    }

    @Override
    public float getMA20Price() {
        return MA20Price;
    }

    @Override
    public float getDea() {
        return dea;
    }

    @Override
    public float getDif() {
        return dif;
    }

    @Override
    public float getMacd() {
        return macd;
    }

    @Override
    public float getK() {
        return k;
    }

    @Override
    public float getD() {
        return d;
    }

    @Override
    public float getJ() {
        return j;
    }

    @Override
    public float getRsi1() {
        return rsi1;
    }

    @Override
    public float getRsi2() {
        return rsi2;
    }

    @Override
    public float getRsi3() {
        return rsi3;
    }

    @Override
    public float getUp() {
        return up;
    }

    @Override
    public float getMb() {
        return mb;
    }

    @Override
    public float getDn() {
        return dn;
    }

    @Override
    public float getVolume() {
        return vol;
    }

    @Override
    public float getMA5Volume() {
        return MA5Volume;
    }

    @Override
    public float getMA10Volume() {
        return MA10Volume;
    }

    /*   "symbol": "BTCUSD",
        "dateTime": 1530770580000,
        "high": 43876.50768,
        "open": 43876.50768,
        "low": 43868.54172,
        "close": 43874.51619,
        "vol": 5.19574363*/

    public long dateTime;
    public float open;
    public float high;
    public float low;
    public float close;
    public float vol;

    public float MA5Price;

    public float MA10Price;

    public float MA20Price;

    public float dea;

    public float dif;

    public float macd;

    public float k;

    public float d;

    public float j;

    public float rsi1;

    public float rsi2;

    public float rsi3;

    public float up;

    public float mb;

    public float dn;

    public float MA5Volume;

    public float MA10Volume;

    @Override
    public int compareTo(@NonNull KLineEntity o) {
        return (int)(this.dateTime-o.dateTime);
    }
}
