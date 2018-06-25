package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/24 21:10
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class StickBean implements Parcelable{


    private StickUserInfoBean userInfoBean;
    private StickChatGroupBean chatGroupBean;

    protected StickBean(Parcel in) {
        userInfoBean = in.readParcelable(StickUserInfoBean.class.getClassLoader());
        chatGroupBean = in.readParcelable(StickChatGroupBean.class.getClassLoader());
    }

    public StickUserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(StickUserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public StickChatGroupBean getChatGroupBean() {
        return chatGroupBean;
    }

    public void setChatGroupBean(StickChatGroupBean chatGroupBean) {
        this.chatGroupBean = chatGroupBean;
    }

    public static final Creator<StickBean> CREATOR = new Creator<StickBean>() {
        @Override
        public StickBean createFromParcel(Parcel in) {
            return new StickBean(in);
        }

        @Override
        public StickBean[] newArray(int size) {
            return new StickBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userInfoBean, flags);
        dest.writeParcelable(chatGroupBean, flags);
    }


    public static class StickUserInfoBean implements Parcelable{

        public String id;
        public String name;
        public String bio;
        public String avatar;

        protected StickUserInfoBean(Parcel in) {
            id = in.readString();
            name = in.readString();
            bio = in.readString();
            avatar = in.readString();
        }

        public static final Creator<StickUserInfoBean> CREATOR = new Creator<StickUserInfoBean>() {
            @Override
            public StickUserInfoBean createFromParcel(Parcel in) {
                return new StickUserInfoBean(in);
            }

            @Override
            public StickUserInfoBean[] newArray(int size) {
                return new StickUserInfoBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(bio);
            dest.writeString(avatar);
        }
    }

    public static class StickChatGroupBean implements Parcelable{

        public String id;
        public String name;
        public String group_face;
        public int affiliations_count;


        protected StickChatGroupBean(Parcel in) {
            id = in.readString();
            name = in.readString();
            group_face = in.readString();
            affiliations_count = in.readInt();
        }

        public static final Creator<StickChatGroupBean> CREATOR = new Creator<StickChatGroupBean>() {
            @Override
            public StickChatGroupBean createFromParcel(Parcel in) {
                return new StickChatGroupBean(in);
            }

            @Override
            public StickChatGroupBean[] newArray(int size) {
                return new StickChatGroupBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(name);
            dest.writeString(group_face);
            dest.writeInt(affiliations_count);
        }
    }



}
