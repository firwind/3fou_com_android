package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.ArrayList;
import java.util.List;

public class TeamBean extends BaseListBean{

    /**
     * total : 100
     * unit : BCB
     * teamList : [{"userName":"张小哇","earnings":"100","time":"2018/7/20","id":1},{"userName":"文能安邦","earnings":"100","time":"2018/7/20","id":2},{"userName":"huwenyong","earnings":"100","time":"2018/7/20","id":3}]
     */

    private String total;
    private String unit;
    private List<TeamListBean> teamList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<TeamListBean> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamListBean> teamList) {
        this.teamList = teamList;
    }

    public static class TeamListBean extends BaseListBean implements Parcelable{
        /**
         * userName : 张小哇
         * earnings : 100
         * time : 2018/7/20
         * id : 1
         */

        private String userName;
        private String earnings;
        private String time;
        private String endTime;
        private int id;

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getEarnings() {
            return earnings;
        }

        public void setEarnings(String earnings) {
            this.earnings = earnings;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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
            dest.writeString(this.userName);
            dest.writeString(this.earnings);
            dest.writeString(this.time);
            dest.writeInt(this.id);
        }

        public TeamListBean() {
        }

        protected TeamListBean(Parcel in) {
            this.userName = in.readString();
            this.earnings = in.readString();
            this.time = in.readString();
            this.id = in.readInt();
        }

        public static final Creator<TeamListBean> CREATOR = new Creator<TeamListBean>() {
            @Override
            public TeamListBean createFromParcel(Parcel source) {
                return new TeamListBean(source);
            }

            @Override
            public TeamListBean[] newArray(int size) {
                return new TeamListBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.total);
        dest.writeString(this.unit);
        dest.writeList(this.teamList);
    }

    public TeamBean() {
    }

    protected TeamBean(Parcel in) {
        super(in);
        this.total = in.readString();
        this.unit = in.readString();
        this.teamList = new ArrayList<TeamListBean>();
        in.readList(this.teamList, TeamListBean.class.getClassLoader());
    }

    public static final Creator<TeamBean> CREATOR = new Creator<TeamBean>() {
        @Override
        public TeamBean createFromParcel(Parcel source) {
            return new TeamBean(source);
        }

        @Override
        public TeamBean[] newArray(int size) {
            return new TeamBean[size];
        }
    };
}
