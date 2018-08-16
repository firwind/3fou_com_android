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
    private String user_id;
    private String friend_user_id;
    private int status;
    private long shehe_time;
    private String information;
    private String remark;
    private String create_at;
    private UserInfoBean friend_data;

    protected GroupOrFriendReviewBean(Parcel in) {
        super(in);
        id = in.readString();
        user_id = in.readString();
        friend_user_id = in.readString();
        status = in.readInt();
        shehe_time = in.readLong();
        information = in.readString();
        remark = in.readString();
        create_at = in.readString();
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
        dest.writeString(create_at);
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
