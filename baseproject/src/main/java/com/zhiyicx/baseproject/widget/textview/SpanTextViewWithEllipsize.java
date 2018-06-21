package com.zhiyicx.baseproject.widget.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @Describe
 * @Author zl
 * @Date 2017/12/21
 * @Contact master.jungle68@gmail.com
 */
public class SpanTextViewWithEllipsize extends android.support.v7.widget.AppCompatTextView {
    private int mLastCharDown = 0;
    private SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
    private CharSequence mCharSequence;

    public SpanTextViewWithEllipsize(Context context) {
        super(context);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpanTextViewWithEllipsize(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 注意：spannableString 设置Spannable 的对象到spannableString中时，要用Spannable.SPAN_EXCLUSIVE_EXCLUSIVE的flag值，不然可能会会出现后面的衔接字符串不会显示
     */
    @Override
    protected void onDraw(Canvas canvas) {
        mCharSequence = getText();
        try {
            // 最多显示 3 行
            mLastCharDown = getLayout().getLineVisibleEnd(2);
        } catch (Exception ignored) {
            mLastCharDown = 0;
        }
        if (mLastCharDown > 0 && mCharSequence.length() > mLastCharDown) {
            spannableStringBuilder.clear();
            spannableStringBuilder.append(mCharSequence.subSequence(0, mLastCharDown - 1)).append("...");
            setText(spannableStringBuilder);
        }
        super.onDraw(canvas);
    }
}
