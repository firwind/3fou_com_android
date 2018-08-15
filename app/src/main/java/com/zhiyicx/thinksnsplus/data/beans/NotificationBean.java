package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * author: huwenyong
 * date: 2018/8/15 11:22
 * description:
 * version:
 */

public class NotificationBean extends BaseListBean implements Parcelable{

    private String title;
    private String notification;
    private int unreadCount;
    private long time;

    public NotificationBean(String title, String notification, int unreadCount, long time) {
        this.title = title;
        this.notification = notification;
        this.unreadCount = unreadCount;
        this.time = time;
    }

    public NotificationBean(){

    }

    protected NotificationBean(Parcel in) {
        super(in);
        title = in.readString();
        notification = in.readString();
        unreadCount = in.readInt();
        time = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeString(notification);
        dest.writeInt(unreadCount);
        dest.writeLong(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationBean> CREATOR = new Creator<NotificationBean>() {
        @Override
        public NotificationBean createFromParcel(Parcel in) {
            return new NotificationBean(in);
        }

        @Override
        public NotificationBean[] newArray(int size) {
            return new NotificationBean[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
