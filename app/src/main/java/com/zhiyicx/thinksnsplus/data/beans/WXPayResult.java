package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2018/05/21/19:29
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WXPayResult implements Parcelable,Serializable {
    private static final long serialVersionUID = 2500485757998080223L;
    private String prepayId;
    private String returnKey;
    private String extData;
    private int type;
    private int code;

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getReturnKey() {
        return returnKey;
    }

    public void setReturnKey(String returnKey) {
        this.returnKey = returnKey;
    }

    public String getExtData() {
        return extData;
    }

    public void setExtData(String extData) {
        this.extData = extData;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.prepayId);
        dest.writeString(this.returnKey);
        dest.writeString(this.extData);
        dest.writeInt(this.type);
        dest.writeInt(this.code);
    }

    public WXPayResult() {
    }

    protected WXPayResult(Parcel in) {
        this.prepayId = in.readString();
        this.returnKey = in.readString();
        this.extData = in.readString();
        this.type = in.readInt();
        this.code = in.readInt();
    }

    public static final Creator<WXPayResult> CREATOR = new Creator<WXPayResult>() {
        @Override
        public WXPayResult createFromParcel(Parcel source) {
            return new WXPayResult(source);
        }

        @Override
        public WXPayResult[] newArray(int size) {
            return new WXPayResult[size];
        }
    };
}
