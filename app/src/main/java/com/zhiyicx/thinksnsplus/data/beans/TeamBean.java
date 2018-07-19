package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;

public class TeamBean extends BaseListBean{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    public TeamBean() {
    }

    protected TeamBean(Parcel in) {
        super(in);
    }

    public static final Creator<TeamBean> CREATOR = new Creator<TeamBean>() {
        @Override
        public TeamBean createFromParcel(Parcel source) {
            return new TeamBean(source);
        }

        @Override
        public TeamBean[] newArray(int size) {
            return new TeamBean[size];
        }
    };
}
