package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: huwenyong
 * date: 2018/8/2 10:59
 * description:
 * version:
 */

public class CurrencyExchangeBean implements Parcelable{

    private double balance;
    private String currency;
    private String currency2;
    private int number;
    private int number2;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrency2() {
        return currency2;
    }

    public void setCurrency2(String currency2) {
        this.currency2 = currency2;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber2() {
        return number2;
    }

    public void setNumber2(int number2) {
        this.number2 = number2;
    }

    protected CurrencyExchangeBean(Parcel in) {
        balance = in.readDouble();
        currency = in.readString();
        currency2 = in.readString();
        number = in.readInt();
        number2 = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(balance);
        dest.writeString(currency);
        dest.writeString(currency2);
        dest.writeInt(number);
        dest.writeInt(number2);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CurrencyExchangeBean> CREATOR = new Creator<CurrencyExchangeBean>() {
        @Override
        public CurrencyExchangeBean createFromParcel(Parcel in) {
            return new CurrencyExchangeBean(in);
        }

        @Override
        public CurrencyExchangeBean[] newArray(int size) {
            return new CurrencyExchangeBean[size];
        }
    };
}
