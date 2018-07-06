package com.zhiyicx.thinksnsplus.data.beans;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.AnswerInfoBeanConvert;
import com.zhiyicx.thinksnsplus.data.source.local.data_convert.UserInfoListBeanConvert;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * @author Catherine
 * @describe 群组会话的bean
 * @date 2018/1/15
 * @contact email:648129313@qq.com
 */
@Entity
public class ChatGroupBean extends BaseListBean implements Parcelable, Serializable {

    private static final long serialVersionUID = -8073135988935750687L;
    @Id(autoincrement = true)
    private Long key;
    @SerializedName(value = "id", alternate = {"im_group_id"})
    @Unique
    private String id;
    @SerializedName(value = "name", alternate = {"groupname"})
    private String name;
    @SerializedName(value = "description", alternate = {"desc"})
    private String description;
    /**
     * 群信息返回的权限
     */
    @SerializedName(value = "membersonly", alternate = {"members_only"})
    private boolean membersonly;
    private boolean allowinvites;
    private int maxusers;
    private long owner;

    public int getIs_stick() {
        return is_stick;
    }

    public void setIs_stick(int is_stick) {
        this.is_stick = is_stick;
    }

    @Transient
    private int is_stick;
    private String created;
    private String group_face;
    private int affiliations_count;
    @Convert(columnType = String.class, converter = UserInfoListBeanConvert.class)
    private List<UserInfoBean> affiliations;
    @SerializedName("public")
    private boolean isPublic;
    private int group_level;//群等级，1-官方群;2-热门群
    @Transient
    public static final int ONELIST = 0;//一级列表
    @Transient
    public static final int TWOLIST = 1;//二级列表
    @Transient
    public int type = ONELIST;//默认一级
    @Transient
    private List<ChatGroupBean> official;
    @Transient
    private List<ChatGroupBean> hot;
    @Transient
    private List<ChatGroupBean> common;
    @Transient
    public String leaf1;//支叶内容
    @Transient
    private boolean expand = false;//是否展开子项
    @Transient
    private List<ChatGroupBean> treeBeanList;
    @Transient
    private String mParentName;
    @Transient
    private int mParentNum;

    public int getIs_in() {
        return mIsIn;
    }

    public void setIs_in(int is_in) {
        this.mIsIn = is_in;
    }
    @SerializedName(value = "mIsIn", alternate = {"is_in"})
    @Transient
    private int mIsIn;//是否加入该群

    public String getLeaf1() {
        return leaf1;
    }

    public void setLeaf1(String leaf1) {
        this.leaf1 = leaf1;
    }

    public List<ChatGroupBean> getTreeBeanList() {
        return treeBeanList;
    }
    public void setTreeBeanList(List<ChatGroupBean> treeBeanList) {
        this.treeBeanList = treeBeanList;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }
    public static int getONELIST() {
        return ONELIST;
    }

    public static int getTWOLIST() {
        return TWOLIST;
    }
    public String getmParentName() {
        return mParentName;
    }

    public void setmParentName(String mParentName) {
        this.mParentName = mParentName;
    }

    public int getmParentNum() {
        return mParentNum;
    }

    public void setmParentNum(int mParentNum) {
        this.mParentNum = mParentNum;
    }

    public List<ChatGroupBean> getOfficial() {
        return official;
    }

    public void setOfficial(List<ChatGroupBean> official) {
        this.official = official;
    }

    public List<ChatGroupBean> getHot() {
        return hot;
    }

    public void setHot(List<ChatGroupBean> hot) {
        this.hot = hot;
    }

    public List<ChatGroupBean> getCommon() {
        return common;
    }

    public void setCommon(List<ChatGroupBean> common) {
        this.common = common;
    }

    public int getGroup_level() {
        return group_level;
    }

    public void setGroup_level(int group_level) {
        this.group_level = group_level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (TextUtils.isEmpty(name)) {
            return;
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isMembersonly() {
        return membersonly;
    }

    public void setMembersonly(boolean membersonly) {
        this.membersonly = membersonly;
    }

    public boolean isAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(boolean allowinvites) {
        this.allowinvites = allowinvites;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public long getOwner() {
        return owner;
    }

    public void setOwner(long owner) {
        if (owner <= 0) {
            return;
        }
        this.owner = owner;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public int getAffiliations_count() {
        return affiliations_count;
    }

    public void setAffiliations_count(int affiliations_count) {
        this.affiliations_count = affiliations_count;
    }

    public List<UserInfoBean> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<UserInfoBean> affiliations) {
        this.affiliations = affiliations;
    }

    public String getGroup_face() {
        return group_face;
    }

    public void setGroup_face(String group_face) {
        if (TextUtils.isEmpty(group_face)) {
            return;
        }
        this.group_face = group_face;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public ChatGroupBean() {
    }

    @Override
    public String toString() {
        return "ChatGroupBean{" +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", membersonly=" + membersonly +
                ", allowinvites=" + allowinvites +
                ", maxusers=" + maxusers +
                ", owner=" + owner +
                ", created='" + created + '\'' +
                ", group_face='" + group_face + '\'' +
                ", affiliations_count=" + affiliations_count +
                ", affiliations=" + affiliations +
                ", isPublic=" + isPublic +
                ", group_level" + group_level +
                '}';
    }


    public Long getKey() {
        return this.key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public boolean getMembersonly() {
        return this.membersonly;
    }

    public boolean getAllowinvites() {
        return this.allowinvites;
    }

    public boolean getIsPublic() {
        return this.isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    @Generated(hash = 363480501)
    public ChatGroupBean(Long key, String id, String name, String description, boolean membersonly,
            boolean allowinvites, int maxusers, long owner, String created, String group_face,
            int affiliations_count, List<UserInfoBean> affiliations, boolean isPublic,
            int group_level) {
        this.key = key;
        this.id = id;
        this.name = name;
        this.description = description;
        this.membersonly = membersonly;
        this.allowinvites = allowinvites;
        this.maxusers = maxusers;
        this.owner = owner;
        this.created = created;
        this.group_face = group_face;
        this.affiliations_count = affiliations_count;
        this.affiliations = affiliations;
        this.isPublic = isPublic;
        this.group_level = group_level;
    }

    public static class NoticeItemBeanConverter implements PropertyConverter<NoticeItemBean, String> {

        @Override
        public NoticeItemBean convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return ConvertUtils.base64Str2Object(databaseValue);
        }

        @Override
        public String convertToDatabaseValue(NoticeItemBean noticeItemBean) {
            if (noticeItemBean == null) {
                return null;
            }
            return ConvertUtils.object2Base64Str(noticeItemBean);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(this.key);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeByte(this.membersonly ? (byte) 1 : (byte) 0);
        dest.writeByte(this.allowinvites ? (byte) 1 : (byte) 0);
        dest.writeInt(this.maxusers);
        dest.writeLong(this.owner);
        dest.writeInt(this.is_stick);
        dest.writeString(this.created);
        dest.writeString(this.group_face);
        dest.writeInt(this.affiliations_count);
        dest.writeTypedList(this.affiliations);
        dest.writeByte(this.isPublic ? (byte) 1 : (byte) 0);
        dest.writeInt(this.group_level);
        dest.writeInt(this.type);
        dest.writeTypedList(this.official);
        dest.writeTypedList(this.hot);
        dest.writeTypedList(this.common);
        dest.writeString(this.leaf1);
        dest.writeByte(this.expand ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.treeBeanList);
        dest.writeString(this.mParentName);
        dest.writeInt(this.mParentNum);
    }

    protected ChatGroupBean(Parcel in) {
        super(in);
        this.key = (Long) in.readValue(Long.class.getClassLoader());
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.membersonly = in.readByte() != 0;
        this.allowinvites = in.readByte() != 0;
        this.maxusers = in.readInt();
        this.owner = in.readLong();
        this.is_stick = in.readInt();
        this.created = in.readString();
        this.group_face = in.readString();
        this.affiliations_count = in.readInt();
        this.affiliations = in.createTypedArrayList(UserInfoBean.CREATOR);
        this.isPublic = in.readByte() != 0;
        this.group_level = in.readInt();
        this.type = in.readInt();
        this.official = in.createTypedArrayList(ChatGroupBean.CREATOR);
        this.hot = in.createTypedArrayList(ChatGroupBean.CREATOR);
        this.common = in.createTypedArrayList(ChatGroupBean.CREATOR);
        this.leaf1 = in.readString();
        this.expand = in.readByte() != 0;
        this.treeBeanList = in.createTypedArrayList(ChatGroupBean.CREATOR);
        this.mParentName = in.readString();
        this.mParentNum = in.readInt();
    }

    public static final Creator<ChatGroupBean> CREATOR = new Creator<ChatGroupBean>() {
        @Override
        public ChatGroupBean createFromParcel(Parcel source) {
            return new ChatGroupBean(source);
        }

        @Override
        public ChatGroupBean[] newArray(int size) {
            return new ChatGroupBean[size];
        }
    };
}
