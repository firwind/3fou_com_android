package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Author:huwenyong
 * Time:2018/4/16 19:12
 * Description:This is HBaseDialog.For...
 */
public class HBasePopupWindow {

    private PopupWindow popupWindow;

    protected Activity mContext;

    private SparseArray<View> mViews;

    protected int layoutResId;

    protected int gravity = Gravity.CENTER;

    /**
     * 构造方法
     * @param context 上下文
     * @param layoutResId 布局id
     */
    public HBasePopupWindow(Activity context, int layoutResId, boolean cancelOnTouchOutside){
        this.mContext = context;
        mViews = new SparseArray<>();
        this.layoutResId = layoutResId;
        initView(cancelOnTouchOutside);

    }

    /**
     * 初始化view
     */
    private void initView(boolean cancelOnTouchOutside) {
        popupWindow = new PopupWindow(mContext);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.setContentView(LayoutInflater.from(mContext).inflate(layoutResId,null));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

    }

    /**
     * 通过id寻找到控件
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = popupWindow.getContentView().findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId){
        View view = mViews.get(viewId);
        if (view == null) {
            view = popupWindow.getContentView().findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (TextView) view;
    }

    public EditText getEditText(int viewId){
        View view = mViews.get(viewId);
        if (view == null) {
            view = popupWindow.getContentView().findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (EditText) view;
    }

    /**
     * 设置文本数据
     * @param viewId TextView 的 Id
     * @param textContent 文本内容
     * @return
     */
    public HBasePopupWindow setText(int viewId, String textContent){
        TextView textView = getView(viewId);
        textView.setText(textContent);
        return this;
    }

    /**
     * 设置文本数据
     * @param viewId TextView 的 Id
     * @param resId 文本内容资源id
     * @return
     */
    public HBasePopupWindow setText(int viewId, int resId){
        TextView textView = getView(viewId);
        textView.setText(resId);
        return this;
    }

    public void showDialog(ViewGroup viewGroup){
        if(popupWindow!=null){
            popupWindow.showAtLocation(viewGroup,Gravity.BOTTOM,0,0);
        }
    }

    public void dismissDialog(){
        if(popupWindow != null){
            popupWindow.dismiss();
        }
    }

}
