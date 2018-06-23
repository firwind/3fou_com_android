package com.zhiyicx.thinksnsplus.data.beans;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 16:12
 *     desc :
 *     version : 1.0
 * <pre>
 */

public class MessageGroupAlbumBean extends BaseListBean implements Parcelable{

    public String id;
    public String group_images_id;
    public int file_id;
    public String created_at;
    public String user_id;
    public String user_name;

    public MessageGroupAlbumBean(){

    }

    protected MessageGroupAlbumBean(Parcel in) {
        super(in);
        id = in.readString();
        group_images_id = in.readString();
        file_id = in.readInt();
        created_at = in.readString();
        user_id = in.readString();
        user_name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(id);
        dest.writeString(group_images_id);
        dest.writeInt(file_id);
        dest.writeString(created_at);
        dest.writeString(user_id);
        dest.writeString(user_name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageGroupAlbumBean> CREATOR = new Creator<MessageGroupAlbumBean>() {
        @Override
        public MessageGroupAlbumBean createFromParcel(Parcel in) {
            return new MessageGroupAlbumBean(in);
        }

        @Override
        public MessageGroupAlbumBean[] newArray(int size) {
            return new MessageGroupAlbumBean[size];
        }
    };
}
