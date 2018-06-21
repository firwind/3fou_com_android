package com.zhiyicx.thinksnsplus.data.beans.report;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.report.ReportType;

import java.io.Serializable;

/**
 * @Describe 要举报的内容说明
 * @Author zl
 * @Date 2017/12/11
 * @Contact master.jungle68@gmail.com
 */
public class ReportResourceBean implements Serializable {
    private static final long serialVersionUID = -8484328463541856394L;
    /**
     * id 要举报资源的 id
     * title 要举报资源的标题
     * img 要举报资源的图片资源
     * des 要举报资源的内容
     */
    private UserInfoBean user;
    private String id;
    private String title;
    private String img;
    private String des;
    private ReportType type;
    private boolean desCanlook = true;

    public ReportResourceBean(UserInfoBean user, String id, String title, String img, String des, ReportType type) {
        this.user = user;
        this.id = id;
        this.title = title;
        this.img = img;
        this.des = des;
        this.type = type;
    }

    public boolean isDesCanlook() {
        return desCanlook;
    }

    public void setDesCanlook(boolean desCanlook) {
        this.desCanlook = desCanlook;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }
}
