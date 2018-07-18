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

    public CurrencyBalanceBean(){

    }

    protected CurrencyBalanceBean(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
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
