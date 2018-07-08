package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/7 15:40
 * description:
 * version:
 */

public class ExpandChatGroupBean extends BaseListBean implements Parcelable{
    public List<ChatGroupBean> official;
    public List<ChatGroupBean> hot;
    public List<ChatGroupBean> common;

    protected ExpandChatGroupBean(Parcel in) {
        super(in);
        official = in.createTypedArrayList(ChatGroupBean.CREATOR);
        hot = in.createTypedArrayList(ChatGroupBean.CREATOR);
        common = in.createTypedArrayList(ChatGroupBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeTypedList(official);
        dest.writeTypedList(hot);
        dest.writeTypedList(common);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ExpandChatGroupBean> CREATOR = new Creator<ExpandChatGroupBean>() {
        @Override
        public ExpandChatGroupBean createFromParcel(Parcel in) {
            return new ExpandChatGroupBean(in);
        }

        @Override
        public ExpandChatGroupBean[] newArray(int size) {
            return new ExpandChatGroupBean[size];
        }
    };
}
