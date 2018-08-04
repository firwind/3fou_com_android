package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/8/3 16:01
 * description:
 * version:
 */

public class IntegrationRuleBean extends BaseListBean implements Parcelable{

    /*"id": 5,
 "title": "注册会员赠送",
   "str": "register",
 "value": "1000",
    "group_id": "0",
    "status": 0,
 "start_time": null,
"end_time": null,
  "created_at": "2018-08-02 09:38:41",
  "updated_at": "2018-08-02 09:38:41",
 "icon": "",
 "dec": null,
  "deleted_at": null*/
    private String title;
    private String str;
    private String value;
    private String icon;
    private String dec;
    private boolean isExpand;

    protected IntegrationRuleBean(Parcel in) {
        super(in);
        title = in.readString();
        str = in.readString();
        value = in.readString();
        icon = in.readString();
        dec = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(str);
        dest.writeString(value);
        dest.writeString(icon);
        dest.writeString(dec);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IntegrationRuleBean> CREATOR = new Creator<IntegrationRuleBean>() {
        @Override
        public IntegrationRuleBean createFromParcel(Parcel in) {
            return new IntegrationRuleBean(in);
        }

        @Override
        public IntegrationRuleBean[] newArray(int size) {
            return new IntegrationRuleBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDec() {
        return dec;
    }

    public void setDec(String dec) {
        this.dec = dec;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
