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

    public int state;
    public int op_type;
    public CurrencyAddress address;
    public long time;
    public int currency_num;
    public int currency_type;
    public int currency_service_num;

    public AccountBookListBean(){

    }

    protected AccountBookListBean(Parcel in) {
        super(in);
        state = in.readInt();
        op_type = in.readInt();
        address = in.readParcelable(CurrencyAddress.class.getClassLoader());
        time = in.readLong();
        currency_num = in.readInt();
        currency_type = in.readInt();
        currency_service_num = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(state);
        dest.writeInt(op_type);
        dest.writeParcelable(address, flags);
        dest.writeLong(time);
        dest.writeInt(currency_num);
        dest.writeInt(currency_type);
        dest.writeInt(currency_service_num);
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getOp_type() {
        return op_type;
    }

    public void setOp_type(int op_type) {
        this.op_type = op_type;
    }

    public CurrencyAddress getAddress() {
        return address;
    }

    public void setAddress(CurrencyAddress address) {
        this.address = address;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCurrency_num() {
        return currency_num;
    }

    public void setCurrency_num(int currency_num) {
        this.currency_num = currency_num;
    }

    public int getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(int currency_type) {
        this.currency_type = currency_type;
    }

    public int getCurrency_service_num() {
        return currency_service_num;
    }

    public void setCurrency_service_num(int currency_service_num) {
        this.currency_service_num = currency_service_num;
    }
}
