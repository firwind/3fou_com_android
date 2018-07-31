package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Author:huwenyong
 * Time:2018/4/16 19:12
 * Description:This is HBaseDialog.For...
 */
public class HBaseDialog {

    private Dialog dialog;

    protected Activity mContext;

    private SparseArray<View> mViews;

    protected int layoutResId;

    protected int gravity = Gravity.CENTER;

    private HBaseDialog.DialogShowListener listener;

    /**
     * 构造方法
     * @param context 上下文
     * @param layoutResId 布局id
     */
    public HBaseDialog(Activity context, int layoutResId, boolean cancelOnTouchOutside){
        this.mContext = context;
        mViews = new SparseArray<>();
        this.layoutResId = layoutResId;
        initView(cancelOnTouchOutside);

    }

    /**
     * 设置位置
     * @param gravity
     */
    public void setGravity(int gravity){
        this.gravity = gravity;
        dialog.getWindow().setGravity(gravity);
    }

    /**
     * 初始化view
     */
    private void initView(boolean cancelOnTouchOutside) {
        dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        dialog.setContentView(layoutResId);
        dialog.getWindow().setGravity(gravity);
        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        cancelDialogTitleLineColor();
    }
    /**
     * 取消蓝色线 4.4系统出现蓝线
     */
    private void cancelDialogTitleLineColor(){
        int divierId = mContext.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = dialog.findViewById(divierId);
        if(divider != null){
            divider.setBackgroundColor(Color.TRANSPARENT);
        }
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
            view = dialog.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public TextView getTextView(int viewId){
        View view = mViews.get(viewId);
        if (view == null) {
            view = dialog.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (TextView) view;
    }

    public EditText getEditText(int viewId){
        View view = mViews.get(viewId);
        if (view == null) {
            view = dialog.findViewById(viewId);
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
    public HBaseDialog setText(int viewId, String textContent){
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
    public HBaseDialog setText(int viewId, int resId){
        TextView textView = getView(viewId);
        textView.setText(resId);
        return this;
    }

    public void showDialog(){
        if(dialog!=null){
            dialog.show();
        }
    }

    public void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
        }
    }

    public Dialog getDialog(){
        return dialog;
    }

    /**
     * 切换dialog
     */
    public void toggleDialog(){
        if(dialog != null){
            if(dialog.isShowing()){
                dialog.dismiss();
                if(listener != null){
                    listener.onDismiss();
                }
            }else{
                dialog.show();
            }
        }
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

    /**
     * 监听对话框的消失
     * @param listener
     */
    public void setDialogShowListener(HBaseDialog.DialogShowListener listener){
        this.listener = listener;
    }

    public interface DialogShowListener{
        void onDismiss();
    }

    public void setOnKeyDown(DialogInterface.OnKeyListener listener){
        dialog.setOnKeyListener(listener);
    }

}
