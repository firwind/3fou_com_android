package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/7/17 16:09
 * description:
 * version:
 */

public class CurrencyBalanceBean extends BaseListBean implements Parcelable{

    public String currency;
    public double avaliable_num;
    public double frezz_num;

    public CurrencyBalanceBean(String currency,double avaliable_num,double frezz_num){
        this.currency = currency;
        this.avaliable_num = avaliable_num;
        this.frezz_num = frezz_num;
    }

    protected CurrencyBalanceBean(Parcel in) {
        super(in);
        currency = in.readString();
        avaliable_num = in.readDouble();
        frezz_num = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(currency);
        dest.writeDouble(avaliable_num);
        dest.writeDouble(frezz_num);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyBalanceBean> CREATOR = new Creator<CurrencyBalanceBean>() {
        @Override
        public CurrencyBalanceBean createFromParcel(Parcel in) {
            return new CurrencyBalanceBean(in);
        }

        @Override
        public CurrencyBalanceBean[] newArray(int size) {
            return new CurrencyBalanceBean[size];
        }
    };
}
