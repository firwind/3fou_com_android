package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/15 15:05
 * description: 垂直滚动的textview
 * version:
 */

public class VerticalTextSwitcher extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private Handler mHandler;
    private List<String> texts = new ArrayList<>();//内容
    private int curPosition = 0;//当前位置
    private int MESSAGE_SWITCH_TEXT = 22;
    private int SWITCH_DURATION = 3000;
    private final String ERROR_STRING = "获取数据失败！";

    public VerticalTextSwitcher(Context context) {
        this(context,null);
    }

    public VerticalTextSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHandler();
        setFactory(this);
        setInAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.switcher_translate_y_in));
        setOutAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.switcher_translate_y_out));
    }

    /**
     * 设置数据源
     * @param list
     */
    public void setupData(List<String> list){
        mHandler.removeMessages(MESSAGE_SWITCH_TEXT);
        this.texts = null == list ? new ArrayList<>() : list;
        if(null != list && list.size() > 1){
            setCurrentText(list.get(0));
            mHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_TEXT,SWITCH_DURATION);
        }

    }

    /**
     * 获取当前位置
     * @return
     */
    public int getCurPosition(){
        return curPosition;
    }

    @Override
    public View makeView() {
        TextView textView = new TextView(getContext());
        setTextViewStyle(textView);
        textView.setText(curPosition < texts.size() ? texts.get(curPosition) : ERROR_STRING);
        return textView;
    }

    /**
     * 设置textview的style
     * @param textView
     */
    private void setTextViewStyle(TextView textView){
        textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    /**
     * 停止自动滚动
     */
    public void stopAutoPlay(){
        if(null != mHandler)
            mHandler.removeMessages(MESSAGE_SWITCH_TEXT);
    }

    /**
     * 开始自动滚动
     */
    public void startAutoPlay(){
        if(null != mHandler){
            mHandler.removeMessages(MESSAGE_SWITCH_TEXT);
            mHandler.sendEmptyMessageDelayed(MESSAGE_SWITCH_TEXT,SWITCH_DURATION);
        }
    }

    /**
     * 初始化handler
     */
    private void initHandler(){

        mHandler = new Handler(getContext().getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                curPosition++;
                if(curPosition==texts.size())
                    curPosition = 0;
                setText(curPosition < texts.size() ? texts.get(curPosition) : ERROR_STRING);

                sendEmptyMessageDelayed(MESSAGE_SWITCH_TEXT,SWITCH_DURATION);
            }
        };

    }

}
