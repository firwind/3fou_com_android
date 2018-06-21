package com.zhiyicx.thinksnsplus.widget.imageview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;

/**
 * @author zl
 * @describe
 * @date 2018/3/27
 * @contact master.jungle68@gmail.com
 */
public class ChangeGasStationImageView extends android.support.v7.widget.AppCompatImageView {
    private static final int DEFAULT_RADUIS_SIZE = 20;
    private RectF mRectF = new RectF(0, 0, 0, 0);

    /*圆角的半径，依次为左上角 x y 半径，右上角，右下角，左下角*/
    private float[] rids;
    private int mLeftTopRaduis = DEFAULT_RADUIS_SIZE;
    private int mLeftBottomRaduis = DEFAULT_RADUIS_SIZE;
    private int mRightTopRaduis = DEFAULT_RADUIS_SIZE;
    private int mRightBottomRaduis = DEFAULT_RADUIS_SIZE;

    public ChangeGasStationImageView(Context context) {
        super(context, null);

    }


    public ChangeGasStationImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ChangeGasStationImageView);
            mLeftTopRaduis = array.getDimensionPixelOffset(R.styleable.ChangeGasStationImageView_TSLeftTopRaduis, DEFAULT_RADUIS_SIZE);
            mLeftBottomRaduis = array.getDimensionPixelOffset(R.styleable.ChangeGasStationImageView_TSLeftBottomRaduis, DEFAULT_RADUIS_SIZE);
            mRightTopRaduis = array.getDimensionPixelOffset(R.styleable.ChangeGasStationImageView_TSRightTopRaduis, DEFAULT_RADUIS_SIZE);
            mRightBottomRaduis = array.getDimensionPixelOffset(R.styleable.ChangeGasStationImageView_TSRightBottomRaduis, DEFAULT_RADUIS_SIZE);
            array.recycle();
        }
        //创建圆角数组
        rids = new float[]
                {
                        mLeftTopRaduis, mLeftTopRaduis
                        , mRightTopRaduis, mRightTopRaduis
                        , mRightBottomRaduis, mRightBottomRaduis
                        , mLeftBottomRaduis, mLeftBottomRaduis
                };

    }

    public ChangeGasStationImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.right = getWidth();
        mRectF.bottom = getHeight();
        Path mPath = new Path();

        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }
}