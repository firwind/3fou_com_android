package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/7/20 9:59
 * description:
 * version:
 */

public class CurrencyAddress extends BaseListBean implements Parcelable{

    public String id;
    public String mark;
    public String address;
    public String currency;

    public CurrencyAddress(String id,String mark,String address,String currency){
        this.id = id;
        this.mark = mark;
        this.address = address;
        this.currency = currency;
    }


    protected CurrencyAddress(Parcel in) {
        super(in);
        id = in.readString();
        mark = in.readString();
        address = in.readString();
        currency = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(mark);
        dest.writeString(address);
        dest.writeString(currency);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyAddress> CREATOR = new Creator<CurrencyAddress>() {
        @Override
        public CurrencyAddress createFromParcel(Parcel in) {
            return new CurrencyAddress(in);
        }

        @Override
        public CurrencyAddress[] newArray(int size) {
            return new CurrencyAddress[size];
        }
    };
}
