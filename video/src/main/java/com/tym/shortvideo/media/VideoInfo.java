package com.tym.shortvideo.media;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @author Jliuer
 * @Date 18/04/08 14:03
 * @Email Jliuer@aliyun.com
 * @Description 视频信息类
 */
public class VideoInfo implements Parcelable, Serializable {
    private static final long serialVersionUID = 1333866610918025407L;
    public String path;//路径
    public String cover;//封面路径
    public String name;//名字
    public String createTime;//时间
    public int duration;//时长
    public int rotation;//旋转角度
    public int width;//宽
    public int height;//高
    public int bitRate;//比特率
    public int frameRate;//帧率
    public int frameInterval;//关键帧间隔
    public long size;//视频大小


    public int expWidth;//期望宽度
    public int expHeight;//期望高度
    public int cutPoint;//剪切的开始点
    public int cutDuration;//剪切的时长

    public int storeId;

    // 动态文本内容
    public String dynamicContent;

    // 视频需要压缩
    public boolean needCompressVideo;
    // 视频需要获取封面
    public boolean needGetCoverFromVideo;
    // 视频需要转码
    public boolean needTranscodingVideo;

    public boolean needTranscodingVideo() {
        return needTranscodingVideo;
    }

    public void setNeedTranscodingVideo(boolean needTranscodingVideo) {
        this.needTranscodingVideo = needTranscodingVideo;
    }

    public boolean needGetCoverFromVideo() {
        return needGetCoverFromVideo;
    }

    public void setNeedGetCoverFromVideo(boolean needGetCoverFromVideo) {
        this.needGetCoverFromVideo = needGetCoverFromVideo;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean needCompressVideo() {
        return needCompressVideo;
    }

    public void setNeedCompressVideo(boolean needCompressVideo) {
        this.needCompressVideo = needCompressVideo;
    }

    public String getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public VideoInfo() {
    }

    public VideoInfo(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getWidth() {
        if (width > 0) {
            return width;
        }
        return 500;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        if (height > 0) {
            return height;
        } else {
            return 500;
        }
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(int frameRate) {
        this.frameRate = frameRate;
    }

    public int getFrameInterval() {
        return frameInterval;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getExpWidth() {
        return expWidth;
    }

    public void setExpWidth(int expWidth) {
        this.expWidth = expWidth;
    }

    public int getExpHeight() {
        return expHeight;
    }

    public void setExpHeight(int expHeight) {
        this.expHeight = expHeight;
    }

    public int getCutPoint() {
        return cutPoint;
    }

    public void setCutPoint(int cutPoint) {
        this.cutPoint = cutPoint;
    }

    public int getCutDuration() {
        return cutDuration;
    }

    public void setCutDuration(int cutDuration) {
        this.cutDuration = cutDuration;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.cover);
        dest.writeString(this.name);
        dest.writeString(this.createTime);
        dest.writeInt(this.duration);
        dest.writeInt(this.rotation);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeInt(this.bitRate);
        dest.writeInt(this.frameRate);
        dest.writeInt(this.frameInterval);
        dest.writeLong(this.size);
        dest.writeInt(this.expWidth);
        dest.writeInt(this.expHeight);
        dest.writeInt(this.cutPoint);
        dest.writeInt(this.cutDuration);
        dest.writeInt(this.storeId);
        dest.writeString(this.dynamicContent);
        dest.writeByte(this.needCompressVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needGetCoverFromVideo ? (byte) 1 : (byte) 0);
        dest.writeByte(this.needTranscodingVideo ? (byte) 1 : (byte) 0);
    }

    protected VideoInfo(Parcel in) {
        this.path = in.readString();
        this.cover = in.readString();
        this.name = in.readString();
        this.createTime = in.readString();
        this.duration = in.readInt();
        this.rotation = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
        this.bitRate = in.readInt();
        this.frameRate = in.readInt();
        this.frameInterval = in.readInt();
        this.size = in.readLong();
        this.expWidth = in.readInt();
        this.expHeight = in.readInt();
        this.cutPoint = in.readInt();
        this.cutDuration = in.readInt();
        this.storeId = in.readInt();
        this.dynamicContent = in.readString();
        this.needCompressVideo = in.readByte() != 0;
        this.needGetCoverFromVideo = in.readByte() != 0;
        this.needTranscodingVideo = in.readByte() != 0;
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
