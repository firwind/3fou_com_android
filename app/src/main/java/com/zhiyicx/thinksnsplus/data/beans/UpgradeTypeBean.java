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

import java.util.ArrayList;
import java.util.List;

public class UpgradeTypeBean implements Parcelable{


    /**
     * price : 68
     * upgrade_title : 升级热门群
     * id : 2
     * combo : [{"combo_price":100,"duration":3},{"combo_price":88,"duration":6},{"combo_price":68,"duration":12}]
     */

    private String price;
    private String upgrade_title;
    private int id;
    private List<ComboBean> combo;

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

    public List<ComboBean> getCombo() {
        return combo;
    }

    public void setCombo(List<ComboBean> combo) {
        this.combo = combo;
    }

    public static class ComboBean implements Parcelable{
        /**
         * combo_price : 100
         * duration : 3
         */

        private int combo_price;
        private int duration;

        public boolean isSelector() {
            return isSelector;
        }

        public void setSelector(boolean selector) {
            isSelector = selector;
        }

        private boolean isSelector;
        public int getCombo_price() {
            return combo_price;
        }

        public void setCombo_price(int combo_price) {
            this.combo_price = combo_price;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public ComboBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.combo_price);
            dest.writeInt(this.duration);
            dest.writeByte(this.isSelector ? (byte) 1 : (byte) 0);
        }

        protected ComboBean(Parcel in) {
            this.combo_price = in.readInt();
            this.duration = in.readInt();
            this.isSelector = in.readByte() != 0;
        }

        public static final Creator<ComboBean> CREATOR = new Creator<ComboBean>() {
            @Override
            public ComboBean createFromParcel(Parcel source) {
                return new ComboBean(source);
            }

            @Override
            public ComboBean[] newArray(int size) {
                return new ComboBean[size];
            }
        };
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
        dest.writeList(this.combo);
    }

    public UpgradeTypeBean() {
    }

    protected UpgradeTypeBean(Parcel in) {
        this.price = in.readString();
        this.upgrade_title = in.readString();
        this.id = in.readInt();
        this.combo = new ArrayList<ComboBean>();
        in.readList(this.combo, ComboBean.class.getClassLoader());
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
