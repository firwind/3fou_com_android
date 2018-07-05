package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：自己服务器里面的群聊
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/25 19:21
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyicx.baseproject.base.BaseListBean;

public class ChatGroupServerBean extends BaseListBean implements Parcelable {

    /**
     * cluster_id : 96
     * id : 52584069005313
     * name : 黎明之景、链圈小助手、root、买大买大大呢
     * description : 暂无
     * public : 0
     * membersonly : 1
     * allowinvites : 0
     * maxusers : 200
     * affiliations_count : 4
     * affiliations : [{"owner":"3"},{"member":"2"},{"member":"10"},{"member":"1"}]
     * owner : 3
     * member : 0
     * invite_need_confirm : 0
     * adminlist : null
     * mutelist : null
     * is_mute : 0
     * admin_type : null
     * created_at : 1529546476
     * updated_at : null
     * deleted_at : null
     * grouplevel : 0
     */

    private int cluster_id;
    private String id;
    private String name;
    private String description;
    @SerializedName("public")
    private int publicX;
    private int membersonly;
    private int allowinvites;
    private int maxusers;
    private int affiliations_count;
    private String affiliations;
    private String owner;
    private String member;

    public String getGroup_face() {
        return group_face;
    }

    public void setGroup_face(String group_face) {
        this.group_face = group_face;
    }

    private String group_face;
    private int invite_need_confirm;
    private Object adminlist;
    private Object mutelist;
    private int is_mute;
    private Object admin_type;
    private String created_at;
    private Object updated_at;
    private Object deleted_at;
    private int group_level;

    public int getCluster_id() {
        return cluster_id;
    }

    public void setCluster_id(int cluster_id) {
        this.cluster_id = cluster_id;
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
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPublicX() {
        return publicX;
    }

    public void setPublicX(int publicX) {
        this.publicX = publicX;
    }

    public int getMembersonly() {
        return membersonly;
    }

    public void setMembersonly(int membersonly) {
        this.membersonly = membersonly;
    }

    public int getAllowinvites() {
        return allowinvites;
    }

    public void setAllowinvites(int allowinvites) {
        this.allowinvites = allowinvites;
    }

    public int getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(int maxusers) {
        this.maxusers = maxusers;
    }

    public int getAffiliations_count() {
        return affiliations_count;
    }

    public void setAffiliations_count(int affiliations_count) {
        this.affiliations_count = affiliations_count;
    }

    public String getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(String affiliations) {
        this.affiliations = affiliations;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int getInvite_need_confirm() {
        return invite_need_confirm;
    }

    public void setInvite_need_confirm(int invite_need_confirm) {
        this.invite_need_confirm = invite_need_confirm;
    }

    public Object getAdminlist() {
        return adminlist;
    }

    public void setAdminlist(Object adminlist) {
        this.adminlist = adminlist;
    }

    public Object getMutelist() {
        return mutelist;
    }

    public void setMutelist(Object mutelist) {
        this.mutelist = mutelist;
    }

    public int getIs_mute() {
        return is_mute;
    }

    public void setIs_mute(int is_mute) {
        this.is_mute = is_mute;
    }

    public Object getAdmin_type() {
        return admin_type;
    }

    public void setAdmin_type(Object admin_type) {
        this.admin_type = admin_type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Object getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Object updated_at) {
        this.updated_at = updated_at;
    }

    public Object getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(Object deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getGrouplevel() {
        return group_level;
    }

    public void setGrouplevel(int grouplevel) {
        this.group_level = grouplevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.cluster_id);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.publicX);
        dest.writeInt(this.membersonly);
        dest.writeInt(this.allowinvites);
        dest.writeInt(this.maxusers);
        dest.writeInt(this.affiliations_count);
        dest.writeString(this.affiliations);
        dest.writeString(this.owner);
        dest.writeString(this.member);
        dest.writeInt(this.invite_need_confirm);

        dest.writeInt(this.is_mute);
        dest.writeString(this.created_at);

        dest.writeInt(this.group_level);
    }

    public ChatGroupServerBean() {
    }

    protected ChatGroupServerBean(Parcel in) {
        super(in);
        this.cluster_id = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.publicX = in.readInt();
        this.membersonly = in.readInt();
        this.allowinvites = in.readInt();
        this.maxusers = in.readInt();
        this.affiliations_count = in.readInt();
        this.affiliations = in.readString();
        this.owner = in.readString();
        this.member = in.readString();
        this.invite_need_confirm = in.readInt();
        this.adminlist = in.readParcelable(Object.class.getClassLoader());
        this.mutelist = in.readParcelable(Object.class.getClassLoader());
        this.is_mute = in.readInt();
        this.admin_type = in.readParcelable(Object.class.getClassLoader());
        this.created_at = in.readString();
        this.updated_at = in.readParcelable(Object.class.getClassLoader());
        this.deleted_at = in.readParcelable(Object.class.getClassLoader());
        this.group_level = in.readInt();
    }

    public static final Creator<ChatGroupServerBean> CREATOR = new Creator<ChatGroupServerBean>() {
        @Override
        public ChatGroupServerBean createFromParcel(Parcel source) {
            return new ChatGroupServerBean(source);
        }

        @Override
        public ChatGroupServerBean[] newArray(int size) {
            return new ChatGroupServerBean[size];
        }
    };
}
