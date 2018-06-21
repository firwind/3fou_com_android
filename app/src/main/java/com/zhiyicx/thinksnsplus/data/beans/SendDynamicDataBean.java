package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.tym.shortvideo.media.VideoInfo;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * @author LiuChao
 * @describe 跳转到发送动态页面，可能需要携带一些数据用来出里相关逻辑，用对象打包传过去
 * @date 2017/4/11
 * @contact email:450127106@qq.com
 */

public class SendDynamicDataBean implements Serializable, Parcelable {
    private static final long serialVersionUID = 4113706643912669235L;
    /**
     * 动态类型
     */
    public static final int PHOTO_TEXT_DYNAMIC = 0;// 图片文字动态
    public static final int TEXT_ONLY_DYNAMIC = 1;// 纯文字动态
    public static final int VIDEO_TEXT_DYNAMIC = 2;// 视频文字动态

    /**
     * 动态归属
     */
    public static final int NORMAL_DYNAMIC = 0;// 普通的动态类型，显示在首页而已
    public static final int GROUP_DYNAMIC = 1;// 属于频道的动态类型，会显示在频道和首页

    private int dynamicType;// 动态类型
    private List<ImageBean> dynamicPrePhotos;// 进入发送页面，已经选好的图片
    private int dynamicBelong;// 动态归属：属于哪儿的动态，频道，非频道。。。
    private long dynamicChannlId;// 动态所属频道id
    private long feedMark;// 动态唯一约束值

    private Video mVideo;// 需要发给服务器的视频信息，包含封面等

    private VideoInfo mVideoInfo;// 视频信息，包含地址等

    public Video getVideo() {
        return mVideo;
    }

    public void setVideo(Video video) {
        mVideo = video;
    }

    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        mVideoInfo = videoInfo;
    }

    public int getDynamicType() {
        return dynamicType;
    }

    public void setDynamicType(int dynamicType) {
        this.dynamicType = dynamicType;
    }

    public List<ImageBean> getDynamicPrePhotos() {
        return dynamicPrePhotos;
    }

    public void setDynamicPrePhotos(List<ImageBean> dynamicPrePhotos) {
        this.dynamicPrePhotos = dynamicPrePhotos;
    }

    public int getDynamicBelong() {
        return dynamicBelong;
    }

    public void setDynamicBelong(int dynamicBelong) {
        this.dynamicBelong = dynamicBelong;
    }

    public long getDynamicChannlId() {
        return dynamicChannlId;
    }

    public void setDynamicChannlId(long dynamicChannlId) {
        this.dynamicChannlId = dynamicChannlId;
    }

    public long getFeedMark() {
        return feedMark;
    }

    public void setFeedMark(long feedMark) {
        this.feedMark = feedMark;
    }

    public SendDynamicDataBean() {
    }

    public static class Video implements Parcelable ,Serializable{
        private static final long serialVersionUID = -4434422478157175851L;
        private long video_id;
        private long cover_id;

        public Video(long video_id, long cover_id) {
            this.video_id = video_id;
            this.cover_id = cover_id;
        }

        public long getVideo_id() {
            return video_id;
        }

        public void setVideo_id(long video_id) {
            this.video_id = video_id;
        }

        public long getCover_id() {
            return cover_id;
        }

        public void setCover_id(long cover_id) {
            this.cover_id = cover_id;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.video_id);
            dest.writeLong(this.cover_id);
        }

        protected Video(Parcel in) {
            this.video_id = in.readLong();
            this.cover_id = in.readLong();
        }

        public static final Creator<Video> CREATOR = new Creator<Video>() {
            @Override
            public Video createFromParcel(Parcel source) {
                return new Video(source);
            }

            @Override
            public Video[] newArray(int size) {
                return new Video[size];
            }
        };
    }

    @Override
    public String toString() {
        return "SendDynamicDataBean{" +
                "dynamicType=" + dynamicType +
                ", dynamicPrePhotos=" + dynamicPrePhotos +
                ", dynamicBelong=" + dynamicBelong +
                ", dynamicChannlId=" + dynamicChannlId +
                ", feedMark=" + feedMark +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.dynamicType);
        dest.writeTypedList(this.dynamicPrePhotos);
        dest.writeInt(this.dynamicBelong);
        dest.writeLong(this.dynamicChannlId);
        dest.writeLong(this.feedMark);
        dest.writeParcelable(this.mVideoInfo, flags);
    }

    protected SendDynamicDataBean(Parcel in) {
        this.dynamicType = in.readInt();
        this.dynamicPrePhotos = in.createTypedArrayList(ImageBean.CREATOR);
        this.dynamicBelong = in.readInt();
        this.dynamicChannlId = in.readLong();
        this.feedMark = in.readLong();
        this.mVideoInfo = in.readParcelable(VideoInfo.class.getClassLoader());
    }

    public static final Creator<SendDynamicDataBean> CREATOR = new Creator<SendDynamicDataBean>() {
        @Override
        public SendDynamicDataBean createFromParcel(Parcel source) {
            return new SendDynamicDataBean(source);
        }

        @Override
        public SendDynamicDataBean[] newArray(int size) {
            return new SendDynamicDataBean[size];
        }
    };
}
