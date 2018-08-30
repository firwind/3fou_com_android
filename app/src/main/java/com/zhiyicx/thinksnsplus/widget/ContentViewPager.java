package com.zhiyicx.thinksnsplus.widget;
/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/28 0028
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ContentViewPager extends ViewPager {
    //是否可以进行滑动
    private boolean canScroll = true;//默认可以滑动

    public ContentViewPager(Context context) {
        super(context);
    }

    public ContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return canScroll;
    }

}
