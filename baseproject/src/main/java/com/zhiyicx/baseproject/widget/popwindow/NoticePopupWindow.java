package com.zhiyicx.baseproject.widget.popwindow;
/*
 * 文件名: 公告弹出框
 * 创建者：zhangl
 * 时  间：2018/6/29
 * 描  述：
 * 版  权: 九曲互动
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;

public class NoticePopupWindow extends CustomPopupWindow {
    private NoticePopWindowItem1ClickListener mNoticePopWindowItem1ClickListener;
    private String titleStr;
    private String desStr;
    private String item1Str;
    private String nameStr;
    private String timeStr;
    private int titleColor;
    private int desColor;
    private int item1Color;

    public static RBuilder builder() {
        return new RBuilder();
    }

    protected NoticePopupWindow(RBuilder builder) {
        super(builder);
        this.titleStr = builder.titleStr;
        this.desStr = builder.desStr;
        this.item1Str = builder.item1Str;
        this.titleColor = builder.titleColor;
        this.desColor = builder.desColor;
        this.item1Color = builder.item1Color;
        this.nameStr = builder.nameStr;
        this.timeStr = builder.timeStr;
        this.mNoticePopWindowItem1ClickListener = builder.mNoticePopWindowItem1ClickListener;
        initView();
    }

    protected void initView() {
        initTextView(titleStr, titleColor, R.id.ppw_center_title, null);
        initTextView(desStr, desColor, R.id.ppw_center_description, null);
        initTextView(nameStr, desColor, R.id.ppw_center_name, null);
        initTextView(timeStr, desColor, R.id.ppw_center_time, null);
        initTextView(item1Str, item1Color, R.id.ppw_center_item, mNoticePopWindowItem1ClickListener);

    }

    protected void initTextView(String text, int colorId, int resId, final NoticePopupWindow.NoticePopWindowItem1ClickListener clickListener) {
        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) mContentView.findViewById(resId);
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
            LinearLayout linearLayout = (LinearLayout) mContentView.findViewById(R.id.ll_more);
            if (colorId != 0) {
                textView.setTextColor(ContextCompat.getColor(mActivity, colorId));
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onClicked();
                    }
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onMore();
                }
            });
        }
    }

    public static final class RBuilder extends Builder {

        private NoticePopupWindow.NoticePopWindowItem1ClickListener mNoticePopWindowItem1ClickListener;
        private String titleStr;
        private String nameStr;
        private String timeStr;
        private String desStr;
        private String item1Str;

        private int titleColor;
        private int desColor;
        private int item1Color;

        public NoticePopupWindow.RBuilder buildNoticePopWindowItem1ClickListener(NoticePopupWindow.NoticePopWindowItem1ClickListener mNoticePopWindowItem1ClickListener) {
            this.mNoticePopWindowItem1ClickListener = mNoticePopWindowItem1ClickListener;
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder width(int width) {
            super.width(width);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder height(int height) {
            super.height(height);
            return this;
        }

        public NoticePopupWindow.RBuilder desColor(int descrColor) {
            this.desColor = descrColor;
            return this;
        }

        public NoticePopupWindow.RBuilder item1Color(int item1Color) {
            this.item1Color = item1Color;
            return this;
        }

        public NoticePopupWindow.RBuilder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public NoticePopupWindow.RBuilder titleStr(String titleStr) {
            this.titleStr = titleStr;
            return this;
        }

        public NoticePopupWindow.RBuilder desStr(String desStr) {
            this.desStr = desStr;
            return this;
        }

        public NoticePopupWindow.RBuilder timeStr(String timeStr) {
            this.timeStr = nameStr;
            return this;
        }

        public NoticePopupWindow.RBuilder nameStr(String nameStr) {
            this.nameStr = nameStr;
            return this;
        }
        public NoticePopupWindow.RBuilder item1Str(String item1Str) {
            this.item1Str = item1Str;
            return this;
        }


        @Override
        public NoticePopupWindow.RBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder isFocus(boolean isFocus) {

            super.isFocus(isFocus);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder backgroundDrawable(Drawable backgroundDrawable) {
            super.backgroundDrawable(backgroundDrawable);
            return this;
        }

        @Override
        public NoticePopupWindow.RBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        public NoticePopupWindow.RBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public NoticePopupWindow build() {
            contentViewId = R.layout.ppw_for_notice_info;
            isWrap = true;
            return new NoticePopupWindow(this);
        }
    }

    public interface NoticePopWindowItem1ClickListener {
        void onClicked();
        void onMore();
    }
}
