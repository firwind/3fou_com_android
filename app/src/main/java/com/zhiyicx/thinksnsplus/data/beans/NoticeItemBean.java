package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 14:39
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import java.util.List;


public class NoticeItemBean extends BaseListBean implements Parcelable{
    private String title;
    private String content;
    private String author;
    private long created_at;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public NoticeItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.author);
        dest.writeLong(this.created_at);
    }

    protected NoticeItemBean(Parcel in) {
        super(in);
        this.title = in.readString();
        this.content = in.readString();
        this.author = in.readString();
        this.created_at = in.readLong();
    }

    public static final Creator<NoticeItemBean> CREATOR = new Creator<NoticeItemBean>() {
        @Override
        public NoticeItemBean createFromParcel(Parcel source) {
            return new NoticeItemBean(source);
        }

        @Override
        public NoticeItemBean[] newArray(int size) {
            return new NoticeItemBean[size];
        }
    };
}

