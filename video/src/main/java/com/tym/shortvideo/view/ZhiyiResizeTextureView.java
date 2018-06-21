package com.tym.shortvideo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import cn.jzvd.JZResizeTextureView;
import cn.jzvd.JZVideoPlayer;

/**
 * @Author Jliuer
 * @Date 2018/04/20/16:59
 * @Email Jliuer@aliyun.com
 * @Description TextureView,重写旋转逻辑
 */
public class ZhiyiResizeTextureView extends JZResizeTextureView {

    protected float rotation;

    public ZhiyiResizeTextureView(Context context) {
        super(context);
    }

    public ZhiyiResizeTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setRotation(float rotation) {
        if (rotation != getRotation()) {
            super.setRotation(rotation);
            this.rotation = rotation;
            requestLayout();
        }
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
