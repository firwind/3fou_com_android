package com.zhiyicx.thinksnsplus.data.source.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/7/4 11:04
 * description:
 * version:
 */

public class CurrencyRankBean extends BaseListBean implements Parcelable{

    public String currency;
    public String name;
    public String describe;
    public String iconUrl;
    public String price;
    public String vol;
    public double change;
    public String maxSupply;
    public String supply;
    public String marketCap;
    public long updateTime;

    /*  "currency": "BTC",  币种名称
    "name": "比特币",
    "describe": "比特币（BitCoin）的概念最初由中本聪在2009年提出，根据中本聪的思路设计发布的开源软件以及建构其上的P2P网络。是一种P2P形式的数字货币。点对点的传输意味着一个去中心化的支付系统。比特币与其他虚拟货币最大的不同，是其总数量非常有限，具有极强的稀缺性。该货币系统曾在4年内只有不超过1050万个，之后的总数量将被永久限制在2100万个。",  // 币种描述
    "iconUrl": "http://www.jinse.com/static/phenix/img/coin/BTC.png",  // 币种图标
    "price": 41778.35,  // 价格
    "vol": 90094794019.62, // 交易额
    "change": -22.63,  // 涨跌幅
    "maxSupply": 21000000,  // 发行量
    "supply": 16848937,   // 流通量
    "marketCap": 703920758469.63,  // 总市值
    "updateTime": 1517901551000  // 更新时间*/

    protected CurrencyRankBean(Parcel in) {
        super(in);
        currency = in.readString();
        name = in.readString();
        describe = in.readString();
        iconUrl = in.readString();
        price = in.readString();
        vol = in.readString();
        change = in.readDouble();
        maxSupply = in.readString();
        supply = in.readString();
        marketCap = in.readString();
        updateTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(currency);
        dest.writeString(name);
        dest.writeString(describe);
        dest.writeString(iconUrl);
        dest.writeString(price);
        dest.writeString(vol);
        dest.writeDouble(change);
        dest.writeString(maxSupply);
        dest.writeString(supply);
        dest.writeString(marketCap);
        dest.writeLong(updateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyRankBean> CREATOR = new Creator<CurrencyRankBean>() {
        @Override
        public CurrencyRankBean createFromParcel(Parcel in) {
            return new CurrencyRankBean(in);
        }

        @Override
        public CurrencyRankBean[] newArray(int size) {
            return new CurrencyRankBean[size];
        }
    };
}
