package com.zhiyicx.thinksnsplus.data.source.local;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/4 14:15
 * description:
 * version:
 */

public class MarketCurrencyBean extends BaseListBean implements Parcelable {

    /*exchange_id	44
exchange_name	"Huobi"
exchange_type	"huobipro"
currency_type	"btc"
currency_name	"BTC"
en_name	"Bitcoin"
unit	"USDT"
slug	"bitcoin"
icon_url	""
sort	1
exchange_rate	0
last_cny	"43126.84"
last_usd	"6519.06"
high	"43942.82"
low	"42440.21"
open	"43588.88"
close	"43126.84"
degree	"-1.06"
vol	"0.98"
buy	"0"
sell	"0"
cslogo	"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=844"
ticker	"HUOBIPRO:BTCUSDT"
last	"43126.84"
currency_symbol	"￥"*/

    public String exchange_id;
    public String exchange_name;
    public String exchange_type;
    public String currency_type;
    public String currency_name;
    public String en_name;
    public String unit;
    public String slug;
    public String icon_url;
    public int sort;
    public double exchange_rate;
    public String last_cny;
    public String last_usd;
    public String high;
    public String low;
    public String open;
    public String close;
    public String degree;
    public String vol;
    public String buy;
    public String sell;
    public String cslogo;
    public String ticker;
    public String last;
    public String currency_symbol;


    protected MarketCurrencyBean(Parcel in) {
        super(in);
        exchange_id = in.readString();
        exchange_name = in.readString();
        exchange_type = in.readString();
        currency_type = in.readString();
        currency_name = in.readString();
        en_name = in.readString();
        unit = in.readString();
        slug = in.readString();
        icon_url = in.readString();
        sort = in.readInt();
        exchange_rate = in.readDouble();
        last_cny = in.readString();
        last_usd = in.readString();
        high = in.readString();
        low = in.readString();
        open = in.readString();
        close = in.readString();
        degree = in.readString();
        vol = in.readString();
        buy = in.readString();
        sell = in.readString();
        cslogo = in.readString();
        ticker = in.readString();
        last = in.readString();
        currency_symbol = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(exchange_id);
        dest.writeString(exchange_name);
        dest.writeString(exchange_type);
        dest.writeString(currency_type);
        dest.writeString(currency_name);
        dest.writeString(en_name);
        dest.writeString(unit);
        dest.writeString(slug);
        dest.writeString(icon_url);
        dest.writeInt(sort);
        dest.writeDouble(exchange_rate);
        dest.writeString(last_cny);
        dest.writeString(last_usd);
        dest.writeString(high);
        dest.writeString(low);
        dest.writeString(open);
        dest.writeString(close);
        dest.writeString(degree);
        dest.writeString(vol);
        dest.writeString(buy);
        dest.writeString(sell);
        dest.writeString(cslogo);
        dest.writeString(ticker);
        dest.writeString(last);
        dest.writeString(currency_symbol);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MarketCurrencyBean> CREATOR = new Creator<MarketCurrencyBean>() {
        @Override
        public MarketCurrencyBean createFromParcel(Parcel in) {
            return new MarketCurrencyBean(in);
        }

        @Override
        public MarketCurrencyBean[] newArray(int size) {
            return new MarketCurrencyBean[size];
        }
    };
}
