package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;

/**
 * author: huwenyong
 * date: 2018/8/11 17:13
 * description:
 * version:
 */

public class BCWalletBean implements Parcelable,Serializable{

    private static final long serialVersionUID = 1533983180L;

    private String balance;
    private String number;

    protected BCWalletBean(Parcel in) {
        balance = in.readString();
        number = in.readString();
    }

    public static final Creator<BCWalletBean> CREATOR = new Creator<BCWalletBean>() {
        @Override
        public BCWalletBean createFromParcel(Parcel in) {
            return new BCWalletBean(in);
        }

        @Override
        public BCWalletBean[] newArray(int size) {
            return new BCWalletBean[size];
        }
    };

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(balance);
        dest.writeString(number);
    }
}
