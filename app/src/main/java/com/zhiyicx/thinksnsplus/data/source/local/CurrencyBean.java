package com.zhiyicx.thinksnsplus.data.source.local;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: huwenyong
 * date: 2018/7/4 19:19
 * description:
 * version:
 */

public class CurrencyBean implements Parcelable{

    /*currency_type	"btc"
currency_name	"BTC"
type	1
sort	1
slug	"bitcoin"*/
    public String currency_type;
    public String currency_name;
    public int type;
    public int sort;
    public String slug;

    public CurrencyBean(){

    }

    protected CurrencyBean(Parcel in) {
        currency_type = in.readString();
        currency_name = in.readString();
        type = in.readInt();
        sort = in.readInt();
        slug = in.readString();
    }

    public static final Creator<CurrencyBean> CREATOR = new Creator<CurrencyBean>() {
        @Override
        public CurrencyBean createFromParcel(Parcel in) {
            return new CurrencyBean(in);
        }

        @Override
        public CurrencyBean[] newArray(int size) {
            return new CurrencyBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(currency_type);
        dest.writeString(currency_name);
        dest.writeInt(type);
        dest.writeInt(sort);
        dest.writeString(slug);
    }
}
