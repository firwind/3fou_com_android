package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;

import com.bumptech.glide.load.model.GlideUrl;
import com.google.gson.annotations.SerializedName;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.qa.QATopicBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.BaseConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.PaidNoteConverter;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem.DEFALT_IMAGE_HEIGHT;
import static com.zhiyicx.thinksnsplus.modules.dynamic.list.adapter.DynamicListBaseItem.DEFALT_IMAGE_WITH;

/**
 * @Author Jliuer
 * @Date 2017/06/22/15:55
 * @Email Jliuer@aliyun.com
 * @Description 动态详情 V2
 */
@Entity
public class DynamicDetailBeanV2 extends BaseListBean implements Parcelable, Serializable {
    private static final long serialVersionUID = -7644468875941863599L;

    public static final int DYNAMIC_LIST_CONTENT_MAX_SHOW_SIZE = 140;
    public static final int SEND_ERROR = 0;
    public static final int SEND_ING = 1;
    public static final int SEND_SUCCESS = 2;

    public static final int TOP_SUCCESS = 1;
    public static final int TOP_NONE = 0;
    public static final int TOP_REVIEW = 2;


    /**
     * id : 13
     * created_at : 2017-06-21 01:54:52
     * updated_at : 2017-06-21 01:54:52
     * deleted_at : null
     * user_id : 1
     * feed_content : 动态内容
     * feed_from : 2
     * feed_digg_count : 0
     * feed_view_count : 0
     * feed_comment_count : 0
     * feed_latitude : null
     * feed_longtitude : null
     * feed_geohash : null
     * audit_status : 1
     * feed_mark : 12
     * has_digg : true
     * has_collect : false
     * amount : 20
     * paid : true
     * images : [{"file":4,"size":null,"amount":100,"type":"download","paid":false},{"file":5,
     * "size":"1930x1930"}]
     * diggs : [1]
     */
    @Unique
    private Long id;
    private String created_at;
    private String updated_at;
    private String deleted_at;
    private Long user_id;
    private String feed_content;
    private int feed_from;
    @SerializedName("like_count")
    private int feed_digg_count;
    private int feed_view_count;
    private int feed_comment_count;
    private String feed_latitude;
    private String feed_longtitude;
    private String feed_geohash;
    private int audit_status;
    @Id
    private Long feed_mark;
    @SerializedName("has_like")
    private boolean has_digg;
    private boolean has_collect;
    private long amount;
    @Convert(converter = LikeBeanConvert.class, columnType = String.class)
    private List<DynamicLikeBean> likes;
    private boolean paid;
    @Convert(converter = ImagesBeansVonvert.class, columnType = String.class)
    private List<ImagesBean> images;
    @Convert(converter = IntegerParamsConverter.class, columnType = String.class)
    private List<Integer> diggs;
    @Convert(converter = PaidNoteConverter.class, columnType = String.class)
    private PaidNote paid_node;

    @SerializedName("user")
    @Convert(converter = UserInfoBeanConvert.class, columnType = String.class)
    private UserInfoBean userInfoBean;
    // DynamicBean 的 feed_mark 与 DynamicCommentBean 的 feed_mark 关联
    @ToMany(joinProperties = {@JoinProperty(name = "feed_mark", referencedName = "feed_mark")})
    private List<DynamicCommentBean> comments;

    private Long hot_creat_time;// 标记热门，已及创建时间，用户数据库查询
    private boolean isFollowed;// 是否关注了该条动态（用户）
    private int state = SEND_SUCCESS;// 动态发送状态 0 发送失败 1 正在发送 2 发送成功
    private String sendFailMessage;
    private int top = TOP_NONE;// 置顶状态 0：无置顶状态 1：置顶成功 -1：置顶审核中

    @Convert(converter = DynamicDigListBeanConverter.class, columnType = String.class)
    private List<DynamicDigListBean> digUserInfoList;// 点赞用户的信息列表


    @Convert(converter = RewardCountBeanConverter.class, columnType = String.class)
    private RewardsCountBean reward;// 打赏总额

    @Convert(converter = VideoConverter.class, columnType = String.class)
    private Video video;
    /**
     * 审核操作 PinnedBean
     */
    @Transient
    private int pinned;

    @Transient
    private int startPosition;
    @Transient
    private String friendlyTime;
    @Transient
    private String userCenterFriendlyTimeUp;
    @Transient
    private String userCenterFriendlyTimeDonw;
    @Transient
    private String friendlyContent;

    public int getPinned() {
        return pinned;
    }

    public void setPinned(int pinned) {
        this.pinned = pinned;
    }

    public String getFriendlyTime() {
        return friendlyTime;
    }

    public void setFriendlyTime(String friendlyTime) {
        this.friendlyTime = friendlyTime;
    }

    public String getUserCenterFriendlyTimeUp() {
        return userCenterFriendlyTimeUp;
    }

    public void setUserCenterFriendlyTimeUp(String userCenterFriendlyTimeUp) {
        this.userCenterFriendlyTimeUp = userCenterFriendlyTimeUp;
    }

    public String getUserCenterFriendlyTimeDonw() {
        return userCenterFriendlyTimeDonw;
    }

    public void setUserCenterFriendlyTimeDonw(String userCenterFriendlyTimeDonw) {
        this.userCenterFriendlyTimeDonw = userCenterFriendlyTimeDonw;
    }

    public String getFriendlyContent() {
        return friendlyContent;
    }

    public void setFriendlyContent(String friendlyContent) {
        this.friendlyContent = friendlyContent;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public Long getHot_creat_time() {
        return hot_creat_time;
    }

    public void setHot_creat_time(Long hot_creat_time) {
        this.hot_creat_time = hot_creat_time;
    }

    public String getSendFailMessage() {
        return sendFailMessage;
    }

    public void setSendFailMessage(String sendFailMessage) {
        this.sendFailMessage = sendFailMessage;
    }

    public UserInfoBean getUserInfoBean() {
        return userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    @Keep
    public List<DynamicCommentBean> getComments() {
        return comments;
    }

    @Keep
    public void setComments(List<DynamicCommentBean> comments) {
        this.comments = comments;
    }

    @Keep
    public List<DynamicDigListBean> getDigUserInfoList() {
        return digUserInfoList;
    }

    @Keep
    public void setDigUserInfoList(List<DynamicDigListBean> digUserInfoList) {
        this.digUserInfoList = digUserInfoList;
    }

    public RewardsCountBean getReward() {
        return reward;
    }

    public void setReward(RewardsCountBean reward) {
        this.reward = reward;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFeed_content() {
        return feed_content;
    }

    public void setFeed_content(String feed_content) {
        this.feed_content = feed_content;
    }

    public int getFeed_from() {
        return feed_from;
    }

    public void setFeed_from(int feed_from) {
        this.feed_from = feed_from;
    }

    public int getFeed_digg_count() {
        return feed_digg_count;
    }

    public void setFeed_digg_count(int feed_digg_count) {
        this.feed_digg_count = feed_digg_count;
    }

    public int getFeed_view_count() {
        return feed_view_count;
    }

    public void setFeed_view_count(int feed_view_count) {
        this.feed_view_count = feed_view_count;
    }

    public int getFeed_comment_count() {
        return feed_comment_count;
    }

    public void setFeed_comment_count(int feed_comment_count) {
        this.feed_comment_count = feed_comment_count;
    }

    public int getAudit_status() {
        return audit_status;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
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

    public Long getFeed_mark() {
        return feed_mark;
    }

    public void setFeed_mark(Long feed_mark) {
        this.feed_mark = feed_mark;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public void setAudit_status(int audit_status) {
        this.audit_status = audit_status;
    }

    public boolean isHas_digg() {
        return has_digg;
    }

    public void setHas_digg(boolean has_digg) {
        this.has_digg = has_digg;
    }

    public boolean isHas_collect() {
        return has_collect;
    }

    public void setHas_collect(boolean has_collect) {
        this.has_collect = has_collect;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public List<ImagesBean> getImages() {
        return images;
    }

    public void setImages(List<ImagesBean> images) {
        this.images = images;
    }

    public List<Integer> getDiggs() {
        return diggs;
    }

    public void setDiggs(List<Integer> diggs) {
        this.diggs = diggs;
    }

    public boolean getHas_digg() {
        return this.has_digg;
    }

    public boolean getHas_collect() {
        return this.has_collect;
    }

    public boolean getPaid() {
        return this.paid;
    }

    public boolean getIsFollowed() {
        return this.isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public Long getMaxId() {
        return id;
    }


    /**
     * 用户处理数据，防止在列表中处理
     */
    public void handleData() {
        if (images != null) {
            int imageCount = images.size();
            for (int i = 0; i < imageCount; i++) {
                dealImageBean(images.get(i), i, imageCount);
            }
        }
        dealVideoBean(video);

        if (created_at != null) {
            friendlyTime = TimeUtils.getTimeFriendlyNormal(created_at);
            String timeString = TimeUtils.getTimeFriendlyForUserHome(created_at);
            if (AppApplication.getContext().getString(R.string.today_with_split).equals(timeString) || AppApplication.getContext().getString(R.string
                    .yestorday_with_split).equals
                    (timeString)) {
                userCenterFriendlyTimeUp = timeString.replace(",", "\n");
            } else {
                String[] dayAndMonth = timeString.split(",");
                userCenterFriendlyTimeUp = dayAndMonth[0];
                userCenterFriendlyTimeDonw = dayAndMonth[1];
            }
        }
        if (feed_content != null) {
            friendlyContent = feed_content.replaceAll(MarkdownConfig.NETSITE_FORMAT, Link.DEFAULT_NET_SITE);
            friendlyContent = friendlyContent.replaceAll(MarkdownConfig.IMAGE_FORMAT, "");
            startPosition = friendlyContent.length();
        }
        boolean isMyDynamic = user_id != null && user_id.intValue() == AppApplication.getMyUserIdWithdefault();
        boolean canLookWords = paid_node == null || paid_node.isPaid() || isMyDynamic;
        if (!canLookWords) {
            friendlyContent += AppApplication.getContext().getString(R.string.words_holder);
        }
    }

    /**
     * 预处理 视频的数据
     *
     * @param video
     */
    private void dealVideoBean(Video video) {
        if (video == null) {
            return;
        }
        if (video.getWidth() == 0) {
            video.setWidth(DEFALT_IMAGE_WITH);
        }
        if (video.getHeight() == 0) {
            video.setHeight(DEFALT_IMAGE_HEIGHT);
        }
        int netWidth = video.getWidth();
        int netHeight = video.getHeight();

        int with = ImageUtils.getmImageContainerWith();
        int height = (with * netHeight / netWidth);

        // 视频特殊处理，最大宽高 ImageUtils.getmImageContainerWith()
        int mImageMaxHeight = ImageUtils.getmImageContainerWith();
        height = height > mImageMaxHeight ? mImageMaxHeight : height;
        // 单张图最小高度
        height = height < DEFALT_IMAGE_HEIGHT ? DEFALT_IMAGE_HEIGHT : height;
        video.setWidth(with);
        video.setHeight(height);
        video.setGlideUrl(ImageUtils.imagePathConvertV2(true, video.cover_id, with, height,
                ImageZipConfig.IMAGE_100_ZIP, AppApplication.getTOKEN()));
    }

    /**
     * 计算图片压缩质量
     *
     * @param imageBean
     * @param i
     * @param imageCount
     * @return
     */
    private void dealImageBean(ImagesBean imageBean, int i, int imageCount) {
        if (imageBean.getWidth() == 0) {
            imageBean.setWidth(DEFALT_IMAGE_WITH);
        }
        if (imageBean.getHeight() == 0) {
            imageBean.setHeight(DEFALT_IMAGE_HEIGHT);
        }

        // 计算宽高，从 size 中分离
        int netWidth = imageBean.getWidth();
        int netHeight = imageBean.getHeight();


        int currenCloums;
        int part;
        boolean canLook = !(imageBean.isPaid() != null && !imageBean.isPaid() &&
                imageBean.getType().equals(Toll.LOOK_TOLL_TYPE));
        imageBean.setCanLook(canLook);
        switch (imageCount) {
            case 1:
                currenCloums = part = 1;
                break;
            case 9:
                currenCloums = 3;
                part = 1;
                break;
            case 2:
                part = 1;
                currenCloums = 2;
                break;
            case 3:
                part = 1;
                currenCloums = 3;
                break;
            case 4:
                part = 1;
                currenCloums = 2;
                break;
            case 5:
                currenCloums = 3;
                if (i == 0) {
                    part = 2;
                } else if (i == 1 || i == 2) {
                    part = 1;
                } else {
                    currenCloums = 2;
                    part = 1;
                }
                break;
            case 6:
                part = i == 0 ? 2 : 1;
                currenCloums = 3;
                break;
            case 7:
                if (i == 0 || i == 3 || i == 4) {
                    part = 2;
                } else {
                    part = 1;
                }
                currenCloums = 3;
                break;
            case 8:
                if (i == 3 || i == 4) {
                    part = 2;
                } else {
                    part = 1;
                }
                currenCloums = 3;
                break;
            default:
                currenCloums = 1;
                part = 1;
                break;
        }
        int currentWith = (ImageUtils.getmImageContainerWith() - (currenCloums - 1) * ImageUtils.getmDiverwith()) / currenCloums * part;
        int proportion;
        int with = netWidth > currentWith ? currentWith : netWidth;
        float quality = (float) with / (float) netWidth;
        proportion = (int) (quality * 100);
        proportion = proportion > 100 ? 100 : proportion;
        imageBean.setCurrentWith(currentWith);
        imageBean.setGlideUrl(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), canLook ? currentWith : 0, canLook ? currentWith : 0,
                proportion, AppApplication.getTOKEN()));
        if (imageCount == 1) {
            with = currentWith;
            int height = (with * netHeight / netWidth);
            int mImageMaxHeight = ImageUtils.getmImageMaxHeight();
            height = height > mImageMaxHeight ? mImageMaxHeight : height;
            // 单张图最小高度
            height = height < DEFALT_IMAGE_HEIGHT ? DEFALT_IMAGE_HEIGHT : height;
            // 这个不知道好久才有用哎
            proportion = with * 100 / netWidth;
            imageBean.setGlideUrl(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), canLook ? 0 : with, canLook ? 0 : height
                    , proportion, AppApplication.getTOKEN()));
            imageBean.setImageViewWidth(with);
            imageBean.setImageViewHeight(height);
        }
        imageBean.setPropPart(proportion);
        imageBean.setLongImage(ImageUtils.isLongImage(netHeight, netWidth));
    }

    public static class Video implements Serializable, Parcelable {
        private static final long serialVersionUID = -3037457547196702028L;
        private long id;
        private long user_id;
        private int video_id;
        private int cover_id;
        private long feed_id;
        private int height;
        private int width;
        private String created_at;
        private String updated_at;
        private String url;
        private String cover;
        private transient GlideUrl glideUrl;

        public Video() {
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public GlideUrl getGlideUrl() {
            return glideUrl;
        }

        public void setGlideUrl(GlideUrl glideUrl) {
            this.glideUrl = glideUrl;
        }

        public Video(String url) {
            this.url = url;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public long getUser_id() {
            return user_id;
        }

        public void setUser_id(long user_id) {
            this.user_id = user_id;
        }

        public int getVideo_id() {
            return video_id;
        }

        public void setVideo_id(int video_id) {
            this.video_id = video_id;
        }

        public int getCover_id() {
            return cover_id;
        }

        public void setCover_id(int cover_id) {
            this.cover_id = cover_id;
        }

        public long getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(long feed_id) {
            this.feed_id = feed_id;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.id);
            dest.writeLong(this.user_id);
            dest.writeInt(this.video_id);
            dest.writeInt(this.cover_id);
            dest.writeLong(this.feed_id);
            dest.writeInt(this.height);
            dest.writeInt(this.width);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeString(this.url);
            dest.writeString(this.cover);
        }

        protected Video(Parcel in) {
            this.id = in.readLong();
            this.user_id = in.readLong();
            this.video_id = in.readInt();
            this.cover_id = in.readInt();
            this.feed_id = in.readLong();
            this.height = in.readInt();
            this.width = in.readInt();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.url = in.readString();
            this.cover = in.readString();
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

    public static class ImagesBean implements Parcelable, Serializable {
        private static final long serialVersionUID = 536871009L;
        public static final String FILE_MIME_TYPE_GIF = "image/gif";
        /**
         * file : 4
         * size : null
         * amount : 100
         * type : download
         * paid : false
         */
        private int propPart;
        @SerializedName(value = "file", alternate = {"file_id"})
        private int file;
        private String size;
        private String imgUrl;
        private int paid_node;
        private int width;
        private int height;
        private long amount;
        private String type;
        private Boolean paid;
        /**
         * 图片类型
         */
        @SerializedName(value = "imgMimeType", alternate = {"mime"})
        private String imgMimeType;
        private int currentWith;
        /**
         * imageViewWidth、imageViewHeight 单张图宽高使用
         */
        private int imageViewWidth;
        private int imageViewHeight;
        private boolean canLook;
        private boolean isLongImage;
        private String netUrl;
        private transient GlideUrl glideUrl;

        @Override
        public String toString() {
            return "ImagesBean{" +
                    "propPart=" + propPart +
                    ", file=" + file +
                    ", size='" + size + '\'' +
                    ", imgUrl='" + imgUrl + '\'' +
                    ", paid_node=" + paid_node +
                    ", width=" + width +
                    ", height=" + height +
                    ", amount=" + amount +
                    ", type='" + type + '\'' +
                    ", paid=" + paid +
                    ", imgMimeType='" + imgMimeType + '\'' +
                    ", imageViewWidth=" + imageViewWidth +
                    ", imageViewHeight=" + imageViewHeight +
                    ", netUrl='" + netUrl + '\'' +
                    '}';
        }

        public int getCurrentWith() {
            return currentWith;
        }

        public void setCurrentWith(int currentWith) {
            this.currentWith = currentWith;
        }

        public boolean hasLongImage() {
            return isLongImage;
        }

        public void setLongImage(boolean longImage) {
            isLongImage = longImage;
        }

        public GlideUrl getGlideUrl() {
            return glideUrl;
        }

        public void setGlideUrl(GlideUrl glideUrl) {
            this.glideUrl = glideUrl;
        }

        public boolean isCanLook() {
            return canLook;
        }

        public void setCanLook(boolean canLook) {
            this.canLook = canLook;
        }

        public int getImageViewWidth() {
            return imageViewWidth;
        }

        public void setImageViewWidth(int imageViewWidth) {
            this.imageViewWidth = imageViewWidth;
        }

        public int getImageViewHeight() {
            return imageViewHeight;
        }

        public void setImageViewHeight(int imageViewHeight) {
            this.imageViewHeight = imageViewHeight;
        }

        public String getNetUrl() {
            return netUrl;
        }

        public void setNetUrl(String netUrl) {
            this.netUrl = netUrl;
        }

        public int getPropPart() {
            return propPart;
        }

        public void setPropPart(int propPart) {
            this.propPart = propPart;
        }

        public String getImgMimeType() {
            return imgMimeType;
        }

        public void setImgMimeType(String imgMimeType) {
            this.imgMimeType = imgMimeType;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getSize() {
            return size;
        }

        public int getPaid_node() {
            return paid_node;
        }

        public void setPaid_node(int paid_node) {
            this.paid_node = paid_node;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public int getWidth() {
            if (width > 0) {
                return width;
            }
            if (praseSize()) {
                return width;
            }
            return DEFALT_IMAGE_WITH;
        }

        public int getHeight() {
            if (height > 0) {
                return height;
            }
            if (praseSize()) {
                return height;
            }
            return DEFALT_IMAGE_HEIGHT;
        }

        private boolean praseSize() {
            try {
                if (size != null && size.length() > 0) {
                    String[] sizes = size.split("x");
                    this.width = Integer.parseInt(sizes[0]);
                    this.height = Integer.parseInt(sizes[1]);
                    if (width <= 0) {
                        width = DEFALT_IMAGE_WITH;
                    }
                    if (height <= 0) {
                        height = DEFALT_IMAGE_HEIGHT;
                    }

                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

        public int getFile() {
            return file;
        }

        public void setFile(int file) {
            this.file = file;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Boolean isPaid() {
            return paid;
        }

        public void setPaid(Boolean paid) {
            this.paid = paid;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ImagesBean that = (ImagesBean) o;

            if (file != that.file) {
                return false;
            }
            return paid_node == that.paid_node;

        }

        @Override
        public int hashCode() {
            int result = file;
            result = 31 * result + paid_node;
            return result;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.propPart);
            dest.writeInt(this.file);
            dest.writeString(this.size);
            dest.writeString(this.imgUrl);
            dest.writeInt(this.paid_node);
            dest.writeInt(this.width);
            dest.writeInt(this.height);
            dest.writeLong(this.amount);
            dest.writeString(this.type);
            dest.writeValue(this.paid);
            dest.writeString(this.imgMimeType);
            dest.writeInt(this.imageViewWidth);
            dest.writeInt(this.imageViewHeight);
            dest.writeByte(this.canLook ? (byte) 1 : (byte) 0);
            dest.writeString(this.netUrl);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.propPart = in.readInt();
            this.file = in.readInt();
            this.size = in.readString();
            this.imgUrl = in.readString();
            this.paid_node = in.readInt();
            this.width = in.readInt();
            this.height = in.readInt();
            this.amount = in.readLong();
            this.type = in.readString();
            this.paid = (Boolean) in.readValue(Boolean.class.getClassLoader());
            this.imgMimeType = in.readString();
            this.imageViewWidth = in.readInt();
            this.imageViewHeight = in.readInt();
            this.canLook = in.readByte() != 0;
            this.netUrl = in.readString();
        }

        public static final Creator<ImagesBean> CREATOR = new Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }

    public static class ImagesBeansVonvert implements PropertyConverter<List<ImagesBean>, String> {
        @Override
        public List<ImagesBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<ImagesBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class IntegerParamsConverter implements PropertyConverter<List<Integer>, String> {

        @Override
        public List<Integer> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<Integer> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public static class LikeBeanConvert implements PropertyConverter<List<DynamicLikeBean>, String> {
        @Override
        public List<DynamicLikeBean> convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(List<DynamicLikeBean> entityProperty) {
            if (entityProperty == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(entityProperty);
        }
    }

    public PaidNote getPaid_node() {
        return this.paid_node;
    }

    public void setPaid_node(PaidNote paid_node) {
        this.paid_node = paid_node;
    }


    public DynamicDetailBeanV2() {
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DynamicDetailBeanV2 that = (DynamicDetailBeanV2) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public int getTop() {
        return this.top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public List<DynamicLikeBean> getLikes() {
        return this.likes;
    }

    public void setLikes(List<DynamicLikeBean> likes) {
        this.likes = likes;
    }


    /**
     * list<FollowFansBean> 转 String 形式存入数据库
     */
    public static class DynamicDigListBeanConverter extends BaseConvert<List<DynamicDigListBean>> {
    }

    /**
     * RewardCountBean 转 String 形式存入数据库
     */
    public static class RewardCountBeanConverter extends BaseConvert<RewardsCountBean> {
    }

    public static class VideoConverter extends BaseConvert<Video> {
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 249603048)
    public synchronized void resetComments() {
        comments = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    @Generated(hash = 1372778430)
    public DynamicDetailBeanV2(Long id, String created_at, String updated_at, String deleted_at, Long user_id, String feed_content, int feed_from,
            int feed_digg_count, int feed_view_count, int feed_comment_count, String feed_latitude, String feed_longtitude, String feed_geohash,
            int audit_status, Long feed_mark, boolean has_digg, boolean has_collect, long amount, List<DynamicLikeBean> likes, boolean paid,
            List<ImagesBean> images, List<Integer> diggs, PaidNote paid_node, UserInfoBean userInfoBean, Long hot_creat_time, boolean isFollowed,
            int state, String sendFailMessage, int top, List<DynamicDigListBean> digUserInfoList, RewardsCountBean reward, Video video) {
        this.id = id;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted_at = deleted_at;
        this.user_id = user_id;
        this.feed_content = feed_content;
        this.feed_from = feed_from;
        this.feed_digg_count = feed_digg_count;
        this.feed_view_count = feed_view_count;
        this.feed_comment_count = feed_comment_count;
        this.feed_latitude = feed_latitude;
        this.feed_longtitude = feed_longtitude;
        this.feed_geohash = feed_geohash;
        this.audit_status = audit_status;
        this.feed_mark = feed_mark;
        this.has_digg = has_digg;
        this.has_collect = has_collect;
        this.amount = amount;
        this.likes = likes;
        this.paid = paid;
        this.images = images;
        this.diggs = diggs;
        this.paid_node = paid_node;
        this.userInfoBean = userInfoBean;
        this.hot_creat_time = hot_creat_time;
        this.isFollowed = isFollowed;
        this.state = state;
        this.sendFailMessage = sendFailMessage;
        this.top = top;
        this.digUserInfoList = digUserInfoList;
        this.reward = reward;
        this.video = video;
    }

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 49871375)
    private transient DynamicDetailBeanV2Dao myDao;
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeString(this.deleted_at);
        dest.writeValue(this.user_id);
        dest.writeString(this.feed_content);
        dest.writeInt(this.feed_from);
        dest.writeInt(this.feed_digg_count);
        dest.writeInt(this.feed_view_count);
        dest.writeInt(this.feed_comment_count);
        dest.writeString(this.feed_latitude);
        dest.writeString(this.feed_longtitude);
        dest.writeString(this.feed_geohash);
        dest.writeInt(this.audit_status);
        dest.writeValue(this.feed_mark);
        dest.writeByte(this.has_digg ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_collect ? (byte) 1 : (byte) 0);
        dest.writeLong(this.amount);
        dest.writeTypedList(this.likes);
        dest.writeByte(this.paid ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.images);
        dest.writeList(this.diggs);
        dest.writeParcelable(this.paid_node, flags);
        dest.writeParcelable(this.userInfoBean, flags);
        dest.writeTypedList(this.comments);
        dest.writeValue(this.hot_creat_time);
        dest.writeByte(this.isFollowed ? (byte) 1 : (byte) 0);
        dest.writeInt(this.state);
        dest.writeString(this.sendFailMessage);
        dest.writeInt(this.top);
        dest.writeTypedList(this.digUserInfoList);
        dest.writeParcelable(this.reward, flags);
        dest.writeParcelable(this.video, flags);
        dest.writeInt(this.startPosition);
        dest.writeString(this.friendlyTime);
        dest.writeString(this.userCenterFriendlyTimeUp);
        dest.writeString(this.userCenterFriendlyTimeDonw);
        dest.writeString(this.friendlyContent);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1467065995)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDynamicDetailBeanV2Dao() : null;
    }

    protected DynamicDetailBeanV2(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.deleted_at = in.readString();
        this.user_id = (Long) in.readValue(Long.class.getClassLoader());
        this.feed_content = in.readString();
        this.feed_from = in.readInt();
        this.feed_digg_count = in.readInt();
        this.feed_view_count = in.readInt();
        this.feed_comment_count = in.readInt();
        this.feed_latitude = in.readString();
        this.feed_longtitude = in.readString();
        this.feed_geohash = in.readString();
        this.audit_status = in.readInt();
        this.feed_mark = (Long) in.readValue(Long.class.getClassLoader());
        this.has_digg = in.readByte() != 0;
        this.has_collect = in.readByte() != 0;
        this.amount = in.readLong();
        this.likes = in.createTypedArrayList(DynamicLikeBean.CREATOR);
        this.paid = in.readByte() != 0;
        this.images = in.createTypedArrayList(ImagesBean.CREATOR);
        this.diggs = new ArrayList<Integer>();
        in.readList(this.diggs, Integer.class.getClassLoader());
        this.paid_node = in.readParcelable(PaidNote.class.getClassLoader());
        this.userInfoBean = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.comments = in.createTypedArrayList(DynamicCommentBean.CREATOR);
        this.hot_creat_time = (Long) in.readValue(Long.class.getClassLoader());
        this.isFollowed = in.readByte() != 0;
        this.state = in.readInt();
        this.sendFailMessage = in.readString();
        this.top = in.readInt();
        this.digUserInfoList = in.createTypedArrayList(DynamicDigListBean.CREATOR);
        this.reward = in.readParcelable(RewardsCountBean.class.getClassLoader());
        this.video = in.readParcelable(Video.class.getClassLoader());
        this.startPosition = in.readInt();
        this.friendlyTime = in.readString();
        this.userCenterFriendlyTimeUp = in.readString();
        this.userCenterFriendlyTimeDonw = in.readString();
        this.friendlyContent = in.readString();
    }

    public static final Creator<DynamicDetailBeanV2> CREATOR = new Creator<DynamicDetailBeanV2>() {
        @Override
        public DynamicDetailBeanV2 createFromParcel(Parcel source) {
            return new DynamicDetailBeanV2(source);
        }

        @Override
        public DynamicDetailBeanV2[] newArray(int size) {
            return new DynamicDetailBeanV2[size];
        }
    };
}
