package com.zhiyicx.thinksnsplus.data.beans;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/30 15:36
 * description:
 * version:
 */

public class SmallVideoDataBean {

    public List<DynamicDetailBeanV2> list;
    public int currentPosition;

    public SmallVideoDataBean(List<DynamicDetailBeanV2> list, int currentPosition) {
        this.list = list;
        this.currentPosition = currentPosition;
    }
}
