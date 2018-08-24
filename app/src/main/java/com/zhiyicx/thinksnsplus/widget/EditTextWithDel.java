package com.zhiyicx.thinksnsplus.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhiyicx.baseproject.R;

/**
 * author: huwenyong
 * date: 2018/8/24 15:48
 * description:
 * version:
 */

public class EditTextWithDel extends AppCompatEditText {

    private static final int DEFAULT_DELETE_IMG = R.mipmap.login_inputbox_clean;

    private Drawable imgAble;
    private Context mContext;
    private DeleteListener dl;

    public EditTextWithDel(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }

    public EditTextWithDel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        imgAble = mContext.getResources().getDrawable(DEFAULT_DELETE_IMG);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setDrawable();
            }
        });
        setDrawable();
    }

    public void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null && event.getAction() == MotionEvent.ACTION_UP) {
            if (imgAble != null && event.getX() <= (getWidth() - getPaddingRight()+20)
                    && event.getX() >= (getWidth() - getPaddingRight() - imgAble.getBounds().width()-20)) {
                setText("");
                delete();
            }
        }
        return super.onTouchEvent(event);
    }

    public void setDeleteListener(DeleteListener dl) {
        this.dl = dl;
    }

    public interface DeleteListener {
        void onDelete();
    }

    public void delete() {
        if (null != dl) {
            dl.onDelete();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }



}
