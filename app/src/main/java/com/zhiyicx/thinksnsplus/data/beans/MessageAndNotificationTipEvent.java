package com.zhiyicx.thinksnsplus.data.beans;

/**
 * author: huwenyong
 * date: 2018/9/1 15:24
 * description:
 * version:
 */

public class MessageAndNotificationTipEvent {

    public static final int MESSAGE_TIP = 0;
    public static final int NOTIFICATION_TIP = 1;

    public boolean isTipShow;
    public int type;

    public MessageAndNotificationTipEvent(boolean isTipShow, int type) {
        this.isTipShow = isTipShow;
        this.type = type;
    }
}
