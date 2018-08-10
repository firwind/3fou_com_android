package com.zhiyicx.thinksnsplus.data.beans;

/**
 * author: huwenyong
 * date: 2018/8/10 11:19
 * description: 二维码需要存储的数据
 * version:
 */

public class QrCodeData {

    private int type;  //1--加入群,data为群id
    private String data;

    public QrCodeData(int type, String data) {
        this.type = type;
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
