package com.zhiyicx.thinksnsplus.data.beans;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.BaseListBean;

public class OrganizationBean extends BaseListBean implements Parcelable {


    /**
     * id : 4
     * name : 其他
     * status : 1
     * created_at : 2018-08-18 15:33:21
     * updated_at : null
     */

    private int id;

    public int getOrganize_id() {
        return organize_id;
    }

    public void setOrganize_id(int organize_id) {
        this.organize_id = organize_id;
    }

    private int organize_id;
    private String name;
    private int status;
    private String created_at;
    private String updated_at;
    private boolean isSelector = false;

    public boolean isSelector() {
        return isSelector;
    }

    public void setSelector(boolean selector) {
        isSelector = selector;
    }

    public OrganizationBean() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.id);
        dest.writeInt(this.organize_id);
        dest.writeString(this.name);
        dest.writeInt(this.status);
        dest.writeString(this.created_at);
        dest.writeString(this.updated_at);
        dest.writeByte(this.isSelector ? (byte) 1 : (byte) 0);
    }

    protected OrganizationBean(Parcel in) {
        super(in);
        this.id = in.readInt();
        this.organize_id = in.readInt();
        this.name = in.readString();
        this.status = in.readInt();
        this.created_at = in.readString();
        this.updated_at = in.readString();
        this.isSelector = in.readByte() != 0;
    }

    public static final Creator<OrganizationBean> CREATOR = new Creator<OrganizationBean>() {
        @Override
        public OrganizationBean createFromParcel(Parcel source) {
            return new OrganizationBean(source);
        }

        @Override
        public OrganizationBean[] newArray(int size) {
            return new OrganizationBean[size];
        }
    };
}
