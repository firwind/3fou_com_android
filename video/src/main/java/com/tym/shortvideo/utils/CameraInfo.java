package com.tym.shortvideo.utils;

/**
 * @author Jliuer
 * @Date 18/04/28 9:44
 * @Email Jliuer@aliyun.com
 * @Description 相机信息
 */
public class CameraInfo {
    int facing;
    int orientation;

    public CameraInfo(int facing, int orientation) {
        this.facing = facing;
        this.orientation = orientation;
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
}
