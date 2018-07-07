package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/7 15:22
 * description:
 * version:
 */

public class GroupParentBean extends BaseListBean implements Parcelable{

    public GroupParentBean(String title) {
        this.title = title;
    }

    public GroupParentBean(String title,List<ChatGroupBean> list) {
        this.title = title;
        this.childs = list;
    }

    public String title;
    public List<ChatGroupBean> childs;

    protected GroupParentBean(Parcel in) {
        super(in);
        title = in.readString();
        childs = in.createTypedArrayList(ChatGroupBean.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(title);
        dest.writeTypedList(childs);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GroupParentBean> CREATOR = new Creator<GroupParentBean>() {
        @Override
        public GroupParentBean createFromParcel(Parcel in) {
            return new GroupParentBean(in);
        }

        @Override
        public GroupParentBean[] newArray(int size) {
            return new GroupParentBean[size];
        }
    };
}
