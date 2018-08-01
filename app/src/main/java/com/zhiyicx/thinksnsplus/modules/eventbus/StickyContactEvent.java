package com.zhiyicx.thinksnsplus.modules.eventbus;

import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/1 15:19
 * description:
 * version:
 */

public class StickyContactEvent {

    private List<ContactsContainerBean> list;

    public StickyContactEvent(List<ContactsContainerBean> list) {
        this.list = list;
    }

    public List<ContactsContainerBean> getList() {
        return list;
    }

    public void setList(List<ContactsContainerBean> list) {
        this.list = list;
    }
}
