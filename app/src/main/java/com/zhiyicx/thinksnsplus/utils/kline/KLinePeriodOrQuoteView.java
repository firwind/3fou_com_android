package com.zhiyicx.thinksnsplus.utils.kline;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/6 9:40
 * description:
 * version:
 */

public class KLinePeriodOrQuoteView implements View.OnClickListener {

    private Context mContext;
    private PopupWindow mPopupWindow;//弹窗
    private String[] mData;//数据源
    private List<TextView> mViewList;
    private OnViewSelectedListener mListener;

    public KLinePeriodOrQuoteView(Context context) {
        this.mContext = context;
        initPopupWindow();
    }

    /**
     * 显示popupWindow
     */
    public void showPopupWindow(View anchor){
        if(null == mPopupWindow ||
                null == mData || mData.length<5)
            return;
        for (int i = 0; i < mData.length; i++) {
            mViewList.get(i).setText(mData[i]);
        }
        if(!mPopupWindow.isShowing())
            mPopupWindow.showAsDropDown(anchor);
    }

    /**
     * 设置数据
     * @param data
     * @param listener
     */
    public void setData(String[] data,OnViewSelectedListener listener){
        this.mData = data;
        this.mListener = listener;
    }

    public void setData(String[] data){
        this.mData = data;
    }

    public void setData(OnViewSelectedListener listener){
        this.mListener = listener;
    }


    @Override
    public void onClick(View v) {
        for (int i = 0; i < mViewList.size(); i++) {
            mViewList.get(i).setSelected(false);
        }
        v.setSelected(true);
        mPopupWindow.dismiss();

        if(null != mListener)
            mListener.onViewSelected((TextView) v,mViewList.indexOf(v));

    }

    public interface OnViewSelectedListener{
        void onViewSelected(TextView view,int position);
    }

    public PopupWindow getPopupWindow(){
        return mPopupWindow;
    }

    public List<TextView> getViewList(){
        return mViewList;
    }

    /**
     * 初始化popupwindow
     */
    private void initPopupWindow(){
        mPopupWindow = new PopupWindow(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_kline_period_or_quote,null);
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(DensityUtil.dip2px(mContext,40));
        mPopupWindow.setAnimationStyle(R.style.popupwindow_translateY_anim);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);

        mViewList = new ArrayList<>();
        mViewList.add((TextView) view.findViewById(R.id.tv1));
        mViewList.add((TextView) view.findViewById(R.id.tv2));
        mViewList.add((TextView) view.findViewById(R.id.tv3));
        mViewList.add((TextView) view.findViewById(R.id.tv4));
        mViewList.add((TextView) view.findViewById(R.id.tv5));

        for (int i = 0; i < mViewList.size(); i++) {
            mViewList.get(i).setOnClickListener(this);
        }
    }

}
