package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyphenate.chat.EMMessage;
import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.Comparator;

/**
 * author: huwenyong
 * date: 2018/6/28 11:07
 * description:
 * version:
 */

public class ChatRecord extends BaseListBean implements Parcelable{

    private UserInfoBean userInfo;
    private EMMessage emMessage;

    public ChatRecord(){

    }

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public EMMessage getEmMessage() {
        return emMessage;
    }

    public void setEmMessage(EMMessage emMessage) {
        this.emMessage = emMessage;
    }

    protected ChatRecord(Parcel in) {
        super(in);
        userInfo = in.readParcelable(UserInfoBean.class.getClassLoader());
        emMessage = in.readParcelable(EMMessage.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(userInfo, flags);
        dest.writeParcelable(emMessage, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ChatRecord> CREATOR = new Creator<ChatRecord>() {
        @Override
        public ChatRecord createFromParcel(Parcel in) {
            return new ChatRecord(in);
        }

        @Override
        public ChatRecord[] newArray(int size) {
            return new ChatRecord[size];
        }
    };

}
