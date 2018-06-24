package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/21 19:55
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

public class GroupNoticeBean extends BaseListBean implements Parcelable {

    private NoticeItemBean original;

    public NoticeItemBean getOriginal() {
        return original;
    }

    public void setOriginal(NoticeItemBean original) {
        this.original = original;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    private String exception;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.original, flags);
        dest.writeString(this.exception);
    }

    public GroupNoticeBean() {
    }

    protected GroupNoticeBean(Parcel in) {
        super(in);

        this.original = in.readParcelable(NoticeItemBean.class.getClassLoader());
        this.exception = in.readString();
    }

    public static final Creator<GroupNoticeBean> CREATOR = new Creator<GroupNoticeBean>() {
        @Override
        public GroupNoticeBean createFromParcel(Parcel source) {
            return new GroupNoticeBean(source);
        }

        @Override
        public GroupNoticeBean[] newArray(int size) {
            return new GroupNoticeBean[size];
        }
    };
}
