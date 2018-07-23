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
    public String tag;
    public String address;

    public CurrencyAddress(String id,String tag,String address){
        this.id = id;
        this.tag = tag;
        this.address = address;
    }


    protected CurrencyAddress(Parcel in) {
        super(in);
        id = in.readString();
        tag = in.readString();
        address = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(tag);
        dest.writeString(address);
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
