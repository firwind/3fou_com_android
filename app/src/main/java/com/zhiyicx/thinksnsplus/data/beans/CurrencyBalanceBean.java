package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/17 16:09
 * description:
 * version:
 */

public class CurrencyBalanceBean extends BaseListBean implements Parcelable{

    /*"id": 1,
  "currency": "BCB",
  "user_id": 70,
  "balance": "0.0000000000",
   "created_at": "2018-07-25 14:46:45",
    "updated_at": "2018-07-25 14:46:45",
   "chain": 1,
    "address_id": 0*/
    public String id;
    public String currency;
    public String balance;
    public String blocked_balance;//冻结余额
    public String icon;//icon
    public String year_rate;//年利率
    public List<CurrencyExchangeBean> exchange;

    protected CurrencyBalanceBean(Parcel in) {
        super(in);
        id = in.readString();
        currency = in.readString();
        balance = in.readString();
        blocked_balance = in.readString();
        icon = in.readString();
        year_rate = in.readString();
        exchange = in.createTypedArrayList(CurrencyExchangeBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(currency);
        dest.writeString(balance);
        dest.writeString(blocked_balance);
        dest.writeString(icon);
        dest.writeString(year_rate);
        dest.writeTypedList(exchange);
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
