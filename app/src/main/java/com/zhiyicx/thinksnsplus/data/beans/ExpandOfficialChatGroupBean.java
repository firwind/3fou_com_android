package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/22 16:08
 * description:
 * version:
 */

public class ExpandOfficialChatGroupBean {

    private String id;
    private String name;
    private List<ChatGroupBean> group;


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

    public List<ChatGroupBean> getGroup() {
        return group;
    }

    public void setGroup(List<ChatGroupBean> group) {
        this.group = group;
    }
}
