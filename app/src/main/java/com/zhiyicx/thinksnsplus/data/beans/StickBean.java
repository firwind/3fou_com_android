package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/24 21:10
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StickBean implements Parcelable{
    @SerializedName("stick_id")
    public String mStickId;

    public String getmStickId() {
        return mStickId;
    }

    public void setmStickId(String mStickId) {
        this.mStickId = mStickId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mStickId);
    }

    public StickBean() {
    }

    protected StickBean(Parcel in) {
        this.mStickId = in.readString();
    }

    public static final Creator<StickBean> CREATOR = new Creator<StickBean>() {
        @Override
        public StickBean createFromParcel(Parcel source) {
            return new StickBean(source);
        }

        @Override
        public StickBean[] newArray(int size) {
            return new StickBean[size];
        }
    };
}
