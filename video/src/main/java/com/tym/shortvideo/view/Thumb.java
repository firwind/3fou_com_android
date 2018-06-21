package com.tym.shortvideo.view;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.tym.video.R;

import java.util.List;
import java.util.Vector;

/**
 * @author Jliuer
 * @Date 18/04/28 9:50
 * @Email Jliuer@aliyun.com
 * @Description 视频截取信息
 */
public class Thumb {

    public static final int LEFT = 0;
    public static final int RIGHT = 1;

    private int mIndex;
    private float mVal;
    private float mPos;
    private Bitmap mBitmap;
    private int mWidthBitmap;
    private int mHeightBitmap;

    private float mLastTouchX;
    private float mLastTouchY;

    private Thumb() {
        mVal = 0;
        mPos = 0;
    }

    public int getIndex() {
        return mIndex;
    }

    private void setIndex(int index) {
        mIndex = index;
    }

    public float getVal() {
        return mVal;
    }

    public void setVal(float val) {
        mVal = val;
    }

    public float getPos() {
        return mPos;
    }

    public void setPos(float pos) {
        mPos = pos;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap( Bitmap bitmap) {
        mBitmap = bitmap;
        mWidthBitmap = bitmap.getWidth();
        mHeightBitmap = bitmap.getHeight();
    }


    public static List<Thumb> initThumbs(Resources resources) {

        List<Thumb> thumbs = new Vector<>();

        for (int i = 0; i < 2; i++) {
            Thumb th = new Thumb();
            th.setIndex(i);
            if (i == 0) {
                int resImageLeft = R.mipmap.pic_left;
                th.setBitmap(BitmapFactory.decodeResource(resources, resImageLeft));
            } else {
                int resImageRight = R.mipmap.pic_right;
                th.setBitmap(BitmapFactory.decodeResource(resources, resImageRight));
            }

            thumbs.add(th);
        }

        return thumbs;
    }

    public static int getWidthBitmap(List<Thumb> thumbs) {
        return thumbs.get(0).getWidthBitmap();
    }

    public static int getHeightBitmap(List<Thumb> thumbs) {
        return thumbs.get(0).getHeightBitmap();
    }

    public float getLastTouchX() {
        return mLastTouchX;
    }

    public void setLastTouchX(float lastTouchX) {
        mLastTouchX = lastTouchX;
    }

    public float getLastTouchY() {
        return mLastTouchY;
    }

    public void setLastTouchY(float mLastTouchY) {
        this.mLastTouchY = mLastTouchY;
    }

    public int getWidthBitmap() {
        return mWidthBitmap;
    }

    private int getHeightBitmap() {
        return mHeightBitmap;
    }
}
