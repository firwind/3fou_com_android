package com.zhiyicx.thinksnsplus.data.beans;

/**
 * author: huwenyong
 * date: 2018/8/25 10:09
 * description:
 * version:
 */

public class GroupAndFriendNotificaiton {

    public NotificationBean group;
    public NotificationBean friend;

    public GroupAndFriendNotificaiton(NotificationBean group, NotificationBean friend) {
        this.group = group;
        this.friend = friend;
    }
}
