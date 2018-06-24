package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 14:00
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

public class GroupHankBean extends BaseListBean implements Parcelable{
    public String mHankName;
    public int mHankNum;

    public int getmType() {
        return mType;
    }

    public void setmType(int mType) {
        this.mType = mType;
    }

    public int mType;

    public int getmIsEdit() {
        return mIsEdit;
    }

    public void setmIsEdit(int mIsEdit) {
        this.mIsEdit = mIsEdit;
    }

    public int mIsEdit;
    public List<UserInfoBean> getUserInfoBeans() {
        return userInfoBeans;
    }
    public void setUserInfoBeans(List<UserInfoBean> userInfoBeans) {
        this.userInfoBeans = userInfoBeans;
    }
    public List<UserInfoBean> userInfoBeans;
    public String getmHankName() {
        return mHankName;
    }

    public void setmHankName(String mHankName) {
        this.mHankName = mHankName;
    }

    public int getmHankNum() {
        return mHankNum;
    }

    public void setmHankNum(int mHankNum) {
        this.mHankNum = mHankNum;
    }

    public int isOwner() {
        return isOwner;
    }

    public void setOwner(int owner) {
        isOwner = owner;
    }

    public int isOwner;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.mHankName);
        dest.writeInt(this.mHankNum);
        dest.writeInt(this.mType);
        dest.writeInt(this.mIsEdit);
        dest.writeTypedList(this.userInfoBeans);
        dest.writeInt(this.isOwner);
    }

    public GroupHankBean() {
    }

    protected GroupHankBean(Parcel in) {
        super(in);
        this.mHankName = in.readString();
        this.mHankNum = in.readInt();
        this.mIsEdit = in.readInt();
        this.mType = in.readInt();
        this.userInfoBeans = in.createTypedArrayList(UserInfoBean.CREATOR);
        this.isOwner = in.readInt();
    }

    public static final Creator<GroupHankBean> CREATOR = new Creator<GroupHankBean>() {
        @Override
        public GroupHankBean createFromParcel(Parcel source) {
            return new GroupHankBean(source);
        }

        @Override
        public GroupHankBean[] newArray(int size) {
            return new GroupHankBean[size];
        }
    };
}
