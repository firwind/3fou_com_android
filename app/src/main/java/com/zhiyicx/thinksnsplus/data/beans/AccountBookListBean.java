package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/7/23 9:31
 * description:
 * version:
 */

public class AccountBookListBean extends BaseListBean implements Parcelable{

    public String id;
    public int type;//1：入账、-1：支出、0：兑换',
    public int target_type;//1、充值，2、提现，3、兑换，4、转入冻结',
    public String currency;
    public String number;//货币数量
    public String number2;//要兑换的货币数量
    public String rate;//币种费率,%
    public String exchange_rate;//兑换比率
    public int state;//'订单状态，0: 等待，1：审核，2：成功，-1: 失败',
    public String currency2;
    public String mark;
    public String toaddress;
    public String remark;
    public String service_charge;

    public long created_time;
    public long updated_time;

    protected AccountBookListBean(Parcel in) {
        super(in);
        id = in.readString();
        type = in.readInt();
        target_type = in.readInt();
        currency = in.readString();
        number = in.readString();
        number2 = in.readString();
        rate = in.readString();
        exchange_rate = in.readString();
        state = in.readInt();
        currency2 = in.readString();
        mark = in.readString();
        toaddress = in.readString();
        remark = in.readString();
        service_charge = in.readString();
        created_time = in.readLong();
        updated_time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeInt(type);
        dest.writeInt(target_type);
        dest.writeString(currency);
        dest.writeString(number);
        dest.writeString(number2);
        dest.writeString(rate);
        dest.writeString(exchange_rate);
        dest.writeInt(state);
        dest.writeString(currency2);
        dest.writeString(mark);
        dest.writeString(toaddress);
        dest.writeString(remark);
        dest.writeString(service_charge);
        dest.writeLong(created_time);
        dest.writeLong(updated_time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AccountBookListBean> CREATOR = new Creator<AccountBookListBean>() {
        @Override
        public AccountBookListBean createFromParcel(Parcel in) {
            return new AccountBookListBean(in);
        }

        @Override
        public AccountBookListBean[] newArray(int size) {
            return new AccountBookListBean[size];
        }
    };
}
