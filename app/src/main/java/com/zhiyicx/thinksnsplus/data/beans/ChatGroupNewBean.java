package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/22 9:25
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

public class ChatGroupNewBean extends ChatGroupBean implements Parcelable{
    @SerializedName("group_notice")
    @Convert(converter = NoticeItemBeanConverter.class, columnType = String.class)
    @Transient
    private GroupNoticeBean noticeItemBean = null;
    @Transient
    public List<MessageGroupAlbumBean> group_images_data = null;
    @SerializedName("is_mute")
    private int mIsMute;

    public GroupNoticeBean getNoticeItemBean() {
        return noticeItemBean;
    }

    public void setNoticeItemBean(GroupNoticeBean noticeItemBean) {
        this.noticeItemBean = noticeItemBean;
    }

    public int getmIsMute() {
        return mIsMute;
    }

    public void setmIsMute(int mIsMute) {
        this.mIsMute = mIsMute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.noticeItemBean, flags);
        dest.writeInt(this.mIsMute);
        dest.writeList(this.group_images_data);
    }

    public ChatGroupNewBean() {
    }

    protected ChatGroupNewBean(Parcel in) {
        super(in);
        this.noticeItemBean = in.readParcelable(GroupNoticeBean.class.getClassLoader());
        this.mIsMute = in.readInt();
        this.group_images_data = in.readArrayList(MessageGroupAlbumBean.class.getClassLoader());
    }

    public static final Creator<ChatGroupNewBean> CREATOR = new Creator<ChatGroupNewBean>() {
        @Override
        public ChatGroupNewBean createFromParcel(Parcel source) {
            return new ChatGroupNewBean(source);
        }

        @Override
        public ChatGroupNewBean[] newArray(int size) {
            return new ChatGroupNewBean[size];
        }
    };
}
