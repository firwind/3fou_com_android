package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @Author Jliuer
 * @Date 2018/05/21/18:39
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class WXPayInfo implements Serializable,Parcelable {

    private static final long serialVersionUID = 336871009;

    private String appid;
    private String partnerid;
    private String prepayid;
    @SerializedName("package")
    private String packagestr;
    private String noncestr;
    private String timestamp;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getPackagestr() {
        return packagestr;
    }

    public void setPackagestr(String packagestr) {
        this.packagestr = packagestr;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.appid);
        dest.writeString(this.partnerid);
        dest.writeString(this.prepayid);
        dest.writeString(this.packagestr);
        dest.writeString(this.noncestr);
        dest.writeString(this.timestamp);
        dest.writeString(this.sign);
    }

    public WXPayInfo() {
    }

    protected WXPayInfo(Parcel in) {
        this.appid = in.readString();
        this.partnerid = in.readString();
        this.prepayid = in.readString();
        this.packagestr = in.readString();
        this.noncestr = in.readString();
        this.timestamp = in.readString();
        this.sign = in.readString();
    }

    public static final Creator<WXPayInfo> CREATOR = new Creator<WXPayInfo>() {
        @Override
        public WXPayInfo createFromParcel(Parcel source) {
            return new WXPayInfo(source);
        }

        @Override
        public WXPayInfo[] newArray(int size) {
            return new WXPayInfo[size];
        }
    };
}
