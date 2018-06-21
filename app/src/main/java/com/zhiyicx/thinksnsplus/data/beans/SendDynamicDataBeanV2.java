package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tym.shortvideo.media.VideoInfo;
import com.zhiyicx.baseproject.config.PayConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * @Author Jliuer
 * @Date 2017/06/16/14:34
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class SendDynamicDataBeanV2 implements Serializable, Parcelable {
    private static final long serialVersionUID = 4113706643912669235L;

    /**
     * feed_title : 标题
     * feed_content : 内容
     * feed_from : 5
     * feed_mark : xxxxx1
     * storage_task : [{"id":1,"amount":100,"type":"read"},{"id":1,"amount":100,"type":"read"}]
     * feed_latitude : 12.32132123
     * feed_longtitude : 32.33332123
     * feed_geohash : GdUDHyfghjd==
     * amount : 450
     */
    @Id(autoincrement = true)
    private Long id;
    private String feed_title;
    private String feed_content;
    private String feed_from;
    @Unique
    private String feed_mark;
    private String feed_latitude;
    private String feed_longtitude;
    private String feed_geohash;
    private Long amount;
    @Convert(columnType = String.class, converter = StorygeConvert.class)
    private List<StorageTaskBean> images;
    @Convert(converter = ImageBeanConvert.class, columnType = String.class)
    private List<ImageBean> photos;

    @Convert(converter = VideoConvert.class, columnType = String.class)
    @SerializedName(value = "video", alternate = {"mVideo"})
    private Video video;// 需要发给服务器的视频信息，包含封面等

    @Convert(converter = VideoInfoConvert.class, columnType = String.class)
    @Expose(serialize = false)
    private VideoInfo mVideoInfo;// 视频信息，包含地址等

    public Video getVideo() {
        return video;
    }

    @Keep
    public void setVideo(Video video) {
        this.video = video;
    }

    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }

    @Keep
    public void setVideoInfo(VideoInfo videoInfo) {
        mVideoInfo = videoInfo;
    }

    public List<ImageBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<ImageBean> photos) {
        this.photos = photos;
    }

    public String getFeed_title() {
        return feed_title;
    }

    public void setFeed_title(String feed_title) {
        this.feed_title = feed_title;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    public String getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(String feed_from) {
        this.feed_from = feed_from;
    }

    public String getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(String feed_mark) {
        this.feed_mark = feed_mark;
    }

    public String getFeed_latitude() {
        return feed_latitude;
    }

    public void setFeed_latitude(String feed_latitude) {
        this.feed_latitude = feed_latitude;
    }

    public String getFeed_longtitude() {
        return feed_longtitude;
    }

    public void setFeed_longtitude(String feed_longtitude) {
        this.feed_longtitude = feed_longtitude;
    }

    public String getFeed_geohash() {
        return feed_geohash;
    }

    public void setFeed_geohash(String feed_geohash) {
        this.feed_geohash = feed_geohash;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public List<StorageTaskBean> getStorage_task() {
        return images;
    }

    public void setStorage_task(List<StorageTaskBean> storage_task) {
        this.images = storage_task;
    }

    public static class Video implements Parcelable, Serializable {
        private static final long serialVersionUID = -4434422478157175851L;
        private long video_id;
        private long cover_id;

        public Video() {
        }

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

    public static class StorageTaskBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 4113706643912669235L;
        /**
         * id : 1
         * amount : 100
         * type : read
         */

        private int id;
        private Long amount;
        private String type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeLong(this.amount);
            dest.writeString(this.type);
        }

        public StorageTaskBean() {
        }

        protected StorageTaskBean(Parcel in) {
            this.id = in.readInt();
            this.amount = in.readLong();
            this.type = in.readString();
        }

        public static final Creator<StorageTaskBean> CREATOR = new Creator<StorageTaskBean>() {
            @Override
            public StorageTaskBean createFromParcel(Parcel source) {
                return new StorageTaskBean(source);
            }

            @Override
            public StorageTaskBean[] newArray(int size) {
                return new StorageTaskBean[size];
            }
        };
    }


    public static SendDynamicDataBeanV2 DynamicDetailBean2SendDynamicDataBeanV2(DynamicDetailBeanV2 dynamicBean) {
        SendDynamicDataBeanV2 sendDynamicDataBeanV2 = new SendDynamicDataBeanV2();
        sendDynamicDataBeanV2.setFeed_content(dynamicBean.getFeed_content());
        sendDynamicDataBeanV2.setFeed_from(dynamicBean.getFeed_from() + "");
        sendDynamicDataBeanV2.setFeed_mark(dynamicBean.getFeed_mark() + "");
        if (dynamicBean.getVideo() != null) {
            VideoInfo videoInfo = new VideoInfo();
            videoInfo.setWidth(dynamicBean.getVideo().getWidth());
            videoInfo.setHeight(dynamicBean.getVideo().getHeight());
            videoInfo.setPath(dynamicBean.getVideo().getUrl());
            sendDynamicDataBeanV2.setVideoInfo(videoInfo);
        }
        sendDynamicDataBeanV2.setAmount(dynamicBean.getAmount() > 0 ? dynamicBean.getAmount() : null);
        return sendDynamicDataBeanV2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<StorageTaskBean> getImages() {
        return this.images;
    }

    public void setImages(List<StorageTaskBean> images) {
        this.images = images;
    }


    public static class StorygeConvert implements PropertyConverter<List<StorageTaskBean>, String> {
        @Override
        public List<StorageTaskBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<StorageTaskBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class ImageBeanConvert implements PropertyConverter<List<ImageBean>, String> {
        @Override
        public List<ImageBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<ImageBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class VideoConvert extends BaseConvert<Video> {
    }

    public static class VideoInfoConvert extends BaseConvert<VideoInfo> {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.feed_title);
        dest.writeString(this.feed_content);
        dest.writeString(this.feed_from);
        dest.writeString(this.feed_mark);
        dest.writeString(this.feed_latitude);
        dest.writeString(this.feed_longtitude);
        dest.writeString(this.feed_geohash);
        dest.writeValue(this.amount);
        dest.writeTypedList(this.images);
        dest.writeTypedList(this.photos);
        dest.writeParcelable(this.video, flags);
        dest.writeParcelable(this.mVideoInfo, flags);
    }

    public VideoInfo getMVideoInfo() {
        return this.mVideoInfo;
    }

    public void setMVideoInfo(VideoInfo mVideoInfo) {
        this.mVideoInfo = mVideoInfo;
    }

    public SendDynamicDataBeanV2() {
    }

    protected SendDynamicDataBeanV2(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_title = in.readString();
        this.feed_content = in.readString();
        this.feed_from = in.readString();
        this.feed_mark = in.readString();
        this.feed_latitude = in.readString();
        this.feed_longtitude = in.readString();
        this.feed_geohash = in.readString();
        this.amount = (Long) in.readValue(Long.class.getClassLoader());
        this.images = in.createTypedArrayList(StorageTaskBean.CREATOR);
        this.photos = in.createTypedArrayList(ImageBean.CREATOR);
        this.video = in.readParcelable(Video.class.getClassLoader());
        this.mVideoInfo = in.readParcelable(VideoInfo.class.getClassLoader());
    }

    @Generated(hash = 144723578)
    public SendDynamicDataBeanV2(Long id, String feed_title, String feed_content, String feed_from,
                                 String feed_mark, String feed_latitude, String feed_longtitude, String feed_geohash, Long amount,
                                 List<StorageTaskBean> images, List<ImageBean> photos, Video video, VideoInfo mVideoInfo) {
        this.id = id;
        this.feed_title = feed_title;
        this.feed_content = feed_content;
        this.feed_from = feed_from;
        this.feed_mark = feed_mark;
        this.feed_latitude = feed_latitude;
        this.feed_longtitude = feed_longtitude;
        this.feed_geohash = feed_geohash;
        this.amount = amount;
        this.images = images;
        this.photos = photos;
        this.video = video;
        this.mVideoInfo = mVideoInfo;
    }

    public static final Creator<SendDynamicDataBeanV2> CREATOR = new Creator<SendDynamicDataBeanV2>() {
        @Override
        public SendDynamicDataBeanV2 createFromParcel(Parcel source) {
            return new SendDynamicDataBeanV2(source);
        }

        @Override
        public SendDynamicDataBeanV2[] newArray(int size) {
            return new SendDynamicDataBeanV2[size];
        }
    };
}
