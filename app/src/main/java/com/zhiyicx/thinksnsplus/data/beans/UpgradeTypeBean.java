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
     * clause_id : 1
     * name : 升级官方群
     * preferentia_policy : 1,2,3,4,5
     * price : 100
     * obligate : 1,2,3
     * created_at : null
     * updated_at : null
     * deleted_at : null
     * taocan_data : [{"icon":"http://www.faceke.com/api/v2/files/12","dec":"群成员人数不限制"},{"icon":"http://www.faceke.com/api/v2/files/54","dec":"你的群将被加上VIP标志"},{"icon":"http://www.faceke.com/api/v2/files/15","dec":"官方群位置优先展示"},{"icon":"http://www.faceke.com/api/v2/files/13","dec":"搜索热门群推荐"},{"icon":"http://www.faceke.com/api/v2/files/465","dec":"群相册超大容量"}]
     * zhekou_data : [{"fewmouths":3,"discount":1},{"fewmouths":6,"discount":0.88},{"fewmouths":12,"discount":0.68}]
     */

    private int clause_id;
    private String name;
    private String preferentia_policy;
    private int price;
    private String obligate;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private List<TaocanDataBean> taocan_data;
    private List<ZhekouDataBean> zhekou_data;

    public int getClause_id() {
        return clause_id;
    }

    public void setClause_id(int clause_id) {
        this.clause_id = clause_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreferentia_policy() {
        return preferentia_policy;
    }

    public void setPreferentia_policy(String preferentia_policy) {
        this.preferentia_policy = preferentia_policy;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getObligate() {
        return obligate;
    }

    public void setObligate(String obligate) {
        this.obligate = obligate;
    }

    public Object getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Object getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public List<TaocanDataBean> getTaocan_data() {
        return taocan_data;
    }

    public void setTaocan_data(List<TaocanDataBean> taocan_data) {
        this.taocan_data = taocan_data;
    }

    public List<ZhekouDataBean> getZhekou_data() {
        return zhekou_data;
    }

    public void setZhekou_data(List<ZhekouDataBean> zhekou_data) {
        this.zhekou_data = zhekou_data;
    }

    public static class TaocanDataBean implements Parcelable{
        /**
         * icon : http://www.faceke.com/api/v2/files/12
         * dec : 群成员人数不限制
         */

        private String icon;
        private String dec;

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.icon);
            dest.writeString(this.dec);
        }

        public TaocanDataBean() {
        }

        protected TaocanDataBean(Parcel in) {
            this.icon = in.readString();
            this.dec = in.readString();
        }

        public static final Creator<TaocanDataBean> CREATOR = new Creator<TaocanDataBean>() {
            @Override
            public TaocanDataBean createFromParcel(Parcel source) {
                return new TaocanDataBean(source);
            }

            @Override
            public TaocanDataBean[] newArray(int size) {
                return new TaocanDataBean[size];
            }
        };
    }

    public static class ZhekouDataBean implements Parcelable {
        /**
         * fewmouths : 3
         * discount : 1
         */
        private boolean isSelector;

        public boolean isSelector() {
            return isSelector;
        }

        public void setSelector(boolean selector) {
            isSelector = selector;
        }

        private int fewmouths;
        private double discount;

        public int getFewmouths() {
            return fewmouths;
        }

        public void setFewmouths(int fewmouths) {
            this.fewmouths = fewmouths;
        }

        public double getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.isSelector ? (byte) 1 : (byte) 0);
            dest.writeInt(this.fewmouths);
            dest.writeDouble(this.discount);
        }

        public ZhekouDataBean() {
        }

        protected ZhekouDataBean(Parcel in) {
            this.isSelector = in.readByte() != 0;
            this.fewmouths = in.readInt();
            this.discount = in.readDouble();
        }

        public static final Creator<ZhekouDataBean> CREATOR = new Creator<ZhekouDataBean>() {
            @Override
            public ZhekouDataBean createFromParcel(Parcel source) {
                return new ZhekouDataBean(source);
            }

            @Override
            public ZhekouDataBean[] newArray(int size) {
                return new ZhekouDataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clause_id);
        dest.writeString(this.name);
        dest.writeString(this.preferentia_policy);
        dest.writeInt(this.price);
        dest.writeString(this.obligate);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeList(this.taocan_data);
        dest.writeList(this.zhekou_data);
    }

    public UpgradeTypeBean() {
    }

    protected UpgradeTypeBean(Parcel in) {
        this.clause_id = in.readInt();
        this.name = in.readString();
        this.preferentia_policy = in.readString();
        this.price = in.readInt();
        this.obligate = in.readString();
        this.created_at = in.readParcelable(Object.class.getClassLoader());
        this.updated_at = in.readParcelable(Object.class.getClassLoader());
        this.deleted_at = in.readParcelable(Object.class.getClassLoader());
        this.taocan_data = new ArrayList<TaocanDataBean>();
        in.readList(this.taocan_data, TaocanDataBean.class.getClassLoader());
        this.zhekou_data = new ArrayList<ZhekouDataBean>();
        in.readList(this.zhekou_data, ZhekouDataBean.class.getClassLoader());
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
