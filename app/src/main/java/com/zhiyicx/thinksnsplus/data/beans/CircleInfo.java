package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.CircleTypeBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.JoinedBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserTagListBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

/**
 * @author Jliuer
 * @Date 2017/11/28/10:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Entity
public class CircleInfo extends BaseListBean implements Serializable {

    private static final long serialVersionUID = 4393338023102640914L;
    /**
     * 名称	类型	说明
     id	integer	社区唯一id
     name	string	社区名称
     user_id	integer	所属用户id
     category_id	integer	社区所属分类id
     location	string	社区位置
     longitude	string	经度
     latitude	string	纬度
     geo_hash	string	geoHash
     allow_feed	integer	是否允许同步动态，0 不允许 1允许
     mode	string	社区类型:public: 公开，private：私有，paid：付费的
     permissions	string	发帖权限:member,administrator,founder 所有，administrator,founder 管理员和圈主，administrator圈主
     money	string	如果 mode 为 paid 用于标示收费金额
     summary	string	简介
     notice	string	公告
     users_count	integer	成员统计
     posts_count	integer	帖子统计
     audit	integer	审核状态:0 未审核 1 通过 2 拒绝
     created_at	string	创建时间
     update_at	string	更新时间
     join_income_count	integer	加圈收益统计
     pinned_income_count	integer	置顶收益统计
     joined	object	是否加入：null未加入
     avatar	地址	头像地址
     blacklist_count 黑名单数量
     */
    /**
     * id : 3
     * name : 白岩说
     * user_id : 18
     * category_id : 2
     * location : chengdu
     * longitude : 100.23
     * latitude : 180.22
     * geo_hash : 1122tym
     * allow_feed : 0
     * mode : public
     * money : 0
     * summary : 第二个社区
     * notice :
     * users_count : 0
     * posts_count : 0
     * audit : 1
     * join_income_count : 1 加圈收益统计
     * pinned_income_count : 1 置顶收益统计
     * created_at : 2017-11-28 02:46:28
     * updated_at : 2017-11-28 02:46:28
     * joined : {"id":2,"group_id":3,"user_id":18,"audit":0,"role":"founder","disabled":0,"created_at":"2017-11-29 17:08:16",
     * "updated_at":"2017-11-29 17:08:17"}
     */
    public enum CirclePayMode {
        PUBLIC("public"),
        PRIVATE("private"),
        PAID("paid");
        public String value;

        CirclePayMode(String value) {
            this.value = value;
        }
    }

    /**
     * 审核状态:0 未审核 1 通过 2 拒绝
     */
    public enum CircleAuditStatus {
        NOT_AUDIT(0),
        PASS(1),
        REFUSE(2);

        public int value;

        CircleAuditStatus(int value) {
            this.value = value;
        }
    }

    /**
     * 角色，member - administrator - 管理者、founder - 创建者
     */
    public enum CircleRole {
        MEMBER("member"),
        ADMINISTRATOR("administrator"),
        FOUNDER("founder");
        public String value;

        CircleRole(String value) {
            this.value = value;
        }
    }


    @Id
    private Long id;
    private String name;
    private String avatar;
    private int user_id;
    private int join_income_count;
    private int pinned_income_count;
    private int category_id;
    private String location;
    private String longitude;
    private String latitude;
    private String geo_hash;
    private String permissions;
    private int allow_feed;
    private String mode;
    private int money;
    private String summary;
    private String notice;
    private int users_count;
    private int posts_count;
    private int blacklist_count;
    private int audit;
    private String created_at;
    private String updated_at;
    @Convert(columnType = String.class, converter = JoinedBeanConvert.class)
    private CircleJoinedBean joined;
    @Convert(columnType = String.class, converter = UserInfoBeanConvert.class)
    private UserInfoBean user;
    @Convert(columnType = String.class, converter = JoinedBeanConvert.class)
    private CircleJoinedBean founder;
    @Convert(columnType = String.class, converter = UserTagListBeanConvert.class)
    private List<UserTagBean> tags;
    @Convert(columnType = String.class, converter = CircleTypeBeanConvert.class)
    private CircleTypeBean category;

    public boolean isRelevance() {
        return isRelevance;
    }

    public void setRelevance(boolean relevance) {
        isRelevance = relevance;
    }

    private boolean isRelevance;
    @Override
    public Long getMaxId() {
        return id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getLocation() {
        return location;
    }

    public int getJoin_income_count() {
        return join_income_count;
    }

    public void setJoin_income_count(int join_income_count) {
        this.join_income_count = join_income_count;
    }

    public int getPinned_income_count() {
        return pinned_income_count;
    }

    public void setPinned_income_count(int pinned_income_count) {
        this.pinned_income_count = pinned_income_count;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getGeo_hash() {
        return geo_hash;
    }

    public void setGeo_hash(String geo_hash) {
        this.geo_hash = geo_hash;
    }

    public int getAllow_feed() {
        return allow_feed;
    }

    public void setAllow_feed(int allow_feed) {
        this.allow_feed = allow_feed;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public int getUsers_count() {
        return users_count;
    }

    public void setUsers_count(int users_count) {
        this.users_count = users_count;
    }

    public int getPosts_count() {
        return posts_count;
    }

    public void setPosts_count(int posts_count) {
        this.posts_count = posts_count;
    }

    public int getAudit() {
        return audit;
    }

    public void setAudit(int audit) {
        this.audit = audit;
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

    public CircleJoinedBean getJoined() {
        return joined;
    }

    public void setJoined(CircleJoinedBean joined) {
        this.joined = joined;
    }

    public int getBlacklist_count() {
        return blacklist_count;
    }

    public void setBlacklist_count(int blacklist_count) {
        this.blacklist_count = blacklist_count;
    }

    public CircleInfo() {
    }

    public CircleInfo(long id) {
        this.id = id;
    }

    public CircleInfo(long id, int audit, CircleJoinedBean joined) {
        this.id = id;
        this.audit = audit;
        this.joined = joined;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CircleInfo that = (CircleInfo) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public UserInfoBean getUser() {
        return this.user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public CircleJoinedBean getFounder() {
        return this.founder;
    }

    public void setFounder(CircleJoinedBean founder) {
        this.founder = founder;
    }

    public List<UserTagBean> getTags() {
        return this.tags;
    }

    public void setTags(List<UserTagBean> tags) {
        this.tags = tags;
    }

    public CircleTypeBean getCategory() {
        return this.category;
    }

    public void setCategory(CircleTypeBean category) {
        this.category = category;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.id);
        dest.writeString(this.name);
        dest.writeString(this.avatar);
        dest.writeInt(this.user_id);
        dest.writeInt(this.join_income_count);
        dest.writeInt(this.pinned_income_count);
        dest.writeInt(this.category_id);
        dest.writeInt(this.blacklist_count);
        dest.writeString(this.location);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.geo_hash);
        dest.writeString(this.permissions);
        dest.writeInt(this.allow_feed);
        dest.writeString(this.mode);
        dest.writeInt(this.money);
        dest.writeString(this.summary);
        dest.writeString(this.notice);
        dest.writeInt(this.users_count);
        dest.writeInt(this.posts_count);
        dest.writeInt(this.audit);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeParcelable(this.joined, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.founder, flags);
        dest.writeTypedList(this.tags);
        dest.writeParcelable(this.category, flags);
    }

    public String getPermissions() {
        return this.permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public boolean getIsRelevance() {
        return this.isRelevance;
    }

    public void setIsRelevance(boolean isRelevance) {
        this.isRelevance = isRelevance;
    }

    protected CircleInfo(Parcel in) {
        super(in);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.name = in.readString();
        this.avatar = in.readString();
        this.user_id = in.readInt();
        this.join_income_count = in.readInt();
        this.pinned_income_count = in.readInt();
        this.category_id = in.readInt();
        this.blacklist_count = in.readInt();
        this.location = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.geo_hash = in.readString();
        this.permissions = in.readString();
        this.allow_feed = in.readInt();
        this.mode = in.readString();
        this.money = in.readInt();
        this.summary = in.readString();
        this.notice = in.readString();
        this.users_count = in.readInt();
        this.posts_count = in.readInt();
        this.audit = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.joined = in.readParcelable(CircleJoinedBean.class.getClassLoader());
        this.user = in.readParcelable(UserInfoBean.class.getClassLoader());
        this.founder = in.readParcelable(CircleJoinedBean.class.getClassLoader());
        this.tags = in.createTypedArrayList(UserTagBean.CREATOR);
        this.category = in.readParcelable(CircleTypeBean.class.getClassLoader());
    }

    @Generated(hash = 209821711)
    public CircleInfo(Long id, String name, String avatar, int user_id, int join_income_count, int pinned_income_count,
            int category_id, String location, String longitude, String latitude, String geo_hash, String permissions, int allow_feed,
            String mode, int money, String summary, String notice, int users_count, int posts_count, int blacklist_count, int audit,
            String created_at, String updated_at, CircleJoinedBean joined, UserInfoBean user, CircleJoinedBean founder,
            List<UserTagBean> tags, CircleTypeBean category, boolean isRelevance) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.user_id = user_id;
        this.join_income_count = join_income_count;
        this.pinned_income_count = pinned_income_count;
        this.category_id = category_id;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.geo_hash = geo_hash;
        this.permissions = permissions;
        this.allow_feed = allow_feed;
        this.mode = mode;
        this.money = money;
        this.summary = summary;
        this.notice = notice;
        this.users_count = users_count;
        this.posts_count = posts_count;
        this.blacklist_count = blacklist_count;
        this.audit = audit;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.joined = joined;
        this.user = user;
        this.founder = founder;
        this.tags = tags;
        this.category = category;
        this.isRelevance = isRelevance;
    }

    public static final Creator<CircleInfo> CREATOR = new Creator<CircleInfo>() {
        @Override
        public CircleInfo createFromParcel(Parcel source) {
            return new CircleInfo(source);
        }

        @Override
        public CircleInfo[] newArray(int size) {
            return new CircleInfo[size];
        }
    };
}
