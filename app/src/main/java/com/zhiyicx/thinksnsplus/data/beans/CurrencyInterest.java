package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/7/31 15:42
 * description:
 * version:
 */

public class CurrencyInterest extends BaseListBean implements Parcelable{

    public String icon;
    public String currency;
    public String rate;
    public boolean isIn;


    protected CurrencyInterest(Parcel in) {
        super(in);
        icon = in.readString();
        currency = in.readString();
        rate = in.readString();
        isIn = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(icon);
        dest.writeString(currency);
        dest.writeString(rate);
        dest.writeByte((byte) (isIn ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyInterest> CREATOR = new Creator<CurrencyInterest>() {
        @Override
        public CurrencyInterest createFromParcel(Parcel in) {
            return new CurrencyInterest(in);
        }

        @Override
        public CurrencyInterest[] newArray(int size) {
            return new CurrencyInterest[size];
        }
    };
}
