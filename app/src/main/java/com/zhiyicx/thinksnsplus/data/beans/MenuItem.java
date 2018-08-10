package com.zhiyicx.thinksnsplus.data.beans;

import android.view.View;

/**
 * author: huwenyong
 * date: 2018/8/9 10:33
 * description:
 * version:
 */

public class MenuItem {

    public int imgResId;
    public String menu;
    public View.OnClickListener mListener;

    public MenuItem(int imgResId, String menu, View.OnClickListener mListener) {
        this.imgResId = imgResId;
        this.menu = menu;
        this.mListener = mListener;
    }
}
