package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名:升级群类型
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Parcel;
import android.os.Parcelable;

public class UpgradeTypeBean implements Parcelable{

    /**
     * price : 88
     * upgrade_title : 升级官方群
     * id : 1
     */

    private String price;
    private String upgrade_title;
    private int id;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpgrade_title() {
        return upgrade_title;
    }

    public void setUpgrade_title(String upgrade_title) {
        this.upgrade_title = upgrade_title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.price);
        dest.writeString(this.upgrade_title);
        dest.writeInt(this.id);
    }

    public UpgradeTypeBean() {
    }

    protected UpgradeTypeBean(Parcel in) {
        this.price = in.readString();
        this.upgrade_title = in.readString();
        this.id = in.readInt();
    }

    public static final Creator<UpgradeTypeBean> CREATOR = new Creator<UpgradeTypeBean>() {
        @Override
        public UpgradeTypeBean createFromParcel(Parcel source) {
            return new UpgradeTypeBean(source);
        }

        @Override
        public UpgradeTypeBean[] newArray(int size) {
            return new UpgradeTypeBean[size];
        }
    };
}
