package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/8/16 13:48
 * description:
 * version:
 */

public class GroupOrFriendReviewBean extends BaseListBean implements Parcelable{

    private String id;
    private String user_id;//主动发起验证信息的user_id
    private String friend_user_id;//被加好友的user_id
    private int status; //0待验证好友，1好友，2拒绝好友
    private long shehe_time;
    private String information;
    private String remark;
    private String created_at;
    private UserInfoBean friend_data;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFriend_user_id() {
        return friend_user_id;
    }

    public void setFriend_user_id(String friend_user_id) {
        this.friend_user_id = friend_user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getShehe_time() {
        return shehe_time;
    }

    public void setShehe_time(long shehe_time) {
        this.shehe_time = shehe_time;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public UserInfoBean getFriend_data() {
        return friend_data;
    }

    public void setFriend_data(UserInfoBean friend_data) {
        this.friend_data = friend_data;
    }

    protected GroupOrFriendReviewBean(Parcel in) {
        super(in);
        id = in.readString();
        user_id = in.readString();
        friend_user_id = in.readString();
        status = in.readInt();
        shehe_time = in.readLong();
        information = in.readString();
        remark = in.readString();
        created_at = in.readString();
        friend_data = in.readParcelable(UserInfoBean.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(user_id);
        dest.writeString(friend_user_id);
        dest.writeInt(status);
        dest.writeLong(shehe_time);
        dest.writeString(information);
        dest.writeString(remark);
        dest.writeString(created_at);
        dest.writeParcelable(friend_data, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupOrFriendReviewBean> CREATOR = new Creator<GroupOrFriendReviewBean>() {
        @Override
        public GroupOrFriendReviewBean createFromParcel(Parcel in) {
            return new GroupOrFriendReviewBean(in);
        }

        @Override
        public GroupOrFriendReviewBean[] newArray(int size) {
            return new GroupOrFriendReviewBean[size];
        }
    };
}
