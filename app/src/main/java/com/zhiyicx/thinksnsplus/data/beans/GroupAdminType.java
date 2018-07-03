package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author: huwenyong
 * date: 2018/7/3 15:26
 * description:
 * version:
 */

public class GroupAdminType implements Parcelable{

    public String user_id;
    public String admin_type;

    protected GroupAdminType(Parcel in) {
        user_id = in.readString();
        admin_type = in.readString();
    }

    public static final Creator<GroupAdminType> CREATOR = new Creator<GroupAdminType>() {
        @Override
        public GroupAdminType createFromParcel(Parcel in) {
            return new GroupAdminType(in);
        }

        @Override
        public GroupAdminType[] newArray(int size) {
            return new GroupAdminType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(admin_type);
    }
}
