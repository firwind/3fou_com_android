package com.zhiyicx.thinksnsplus.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
/**
 * Created by mx on 2016/8/4.
 * listview gridview 公共adapter的viewholder
 */
public class ViewHolder {

	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;
	private Context mContext;
	private int mLayoutId;

	public ViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
		mContext = context;
		mLayoutId = layoutId;
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	public View getConvertView() {
		return mConvertView;
	}

	public int getPosition() {
		return mPosition;
	}

	public int getLayoutId() {
		return mLayoutId;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getViewById(int viewId) {
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置View 显示或隐藏
	 *
	 * @param viewId
	 * @param status  //  显示 状态
	 * @return
	 */
	public ViewHolder setVisibility(int viewId, int status) {
		View view = getViewById(viewId);
		view.setVisibility(status);
		return this;
	}
	/**
	 * 设置View 点击事件
	 *
	 * @param viewId
	 * @param listener  //
	 * @return
	 */
	public ViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
		View view = getViewById(viewId);
		view.setOnClickListener(listener);
		return this;
	}

	/**
	 * 为View设置背景图片
	 *
	 * @param viewId
	 * @param id  资源ID
	 * @return
	 */
	public ViewHolder setBackgroundResource(int viewId, int id) {
		View view = getViewById(viewId);
		view.setBackgroundResource(id);
		return this;
	}

	/**
	 * 为TextView设置 字体颜色
	 *
	 * @param viewId
	 * @param id  资源ID
	 * @return
	 */
	public ViewHolder setTextColor(int viewId, int id) {
		TextView view = getViewById(viewId);
		view.setTextColor(id);
		return this;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getViewById(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为TextView设置字符串
	 *
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setTextChar(int viewId, CharSequence text) {
		TextView view = getViewById(viewId);
		view.setText(text);
		return this;
	}


	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getViewById(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param bm
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bm) {
		ImageView view = getViewById(viewId);
		view.setImageBitmap(bm);
		return this;
	}

	/**
	 * 根据指定id的Progressbar设置进度
	 */
	public ViewHolder setProgressBarProcess(int viewId, int process) {
		ProgressBar bar = getViewById(viewId);
		bar.setProgress(process);
		return this;
	}

}