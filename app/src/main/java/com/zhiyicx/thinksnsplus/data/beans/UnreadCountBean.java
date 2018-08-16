package com.zhiyicx.thinksnsplus.data.beans;

import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

/**
 * @Describe 未读数内容统计模型  {@see https://slimkit.github.io/plus-docs/v2/core/users/unread}
 * @Author zl
 * @Date 2017/10/23
 * @Contact master.jungle68@gmail.com
 */
public class UnreadCountBean {
    private Long user_id;
    private Long id;
    private String time;
    private UserInfoBean user;
    private ChatGroupBean group;

    public ChatGroupBean getGroup() {
        return group;
    }

    public void setGroup(ChatGroupBean group) {
        this.group = group;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public UserInfoBean getUser() {
        return user;
    }

    public void setUser(UserInfoBean user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UnreadCountBean{" +
                "user_id=" + user_id +
                ", id=" + id +
                ", time='" + time + '\'' +
                ", user=" + user +
                '}';
    }
}
