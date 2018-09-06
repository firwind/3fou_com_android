package com.zhiyicx.thinksnsplus.data.beans.event;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/9/6 11:22
 * description:
 * version:
 */

public class TSNewFriendsEvent {

    private List<Long> friends;

    public TSNewFriendsEvent(List<Long> friends) {
        this.friends = friends;
    }


    public List<Long> getFriends() {
        return friends;
    }

    public void setFriends(List<Long> friends) {
        this.friends = friends;
    }
}
