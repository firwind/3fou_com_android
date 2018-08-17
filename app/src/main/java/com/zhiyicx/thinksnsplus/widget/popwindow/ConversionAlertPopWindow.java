package com.zhiyicx.thinksnsplus.widget.popwindow;
/*
 * 文件名：兑换糖果弹出框
 * 创建者：zl
 * 时  间：2018/8/17 0017
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;

public class ConversionAlertPopWindow extends CustomPopupWindow {
    private ConversionPopWindowItemClickListener mConversionPopWindowItemClickListener;
    private long candiesNum;
    private String ratios;
    private String rule;

    public static CBuilder builder() {
        return new CBuilder();
    }

    private ConversionAlertPopWindow(CBuilder builder) {
        super(builder);
        this.candiesNum = builder.candiesNum;
        this.ratios = builder.ratios;
        this.rule = builder.rule;

        this.mConversionPopWindowItemClickListener = builder.mConversionPopWindowItemClickListener;
        initView(rule, candiesNum, ratios, mConversionPopWindowItemClickListener);

    }

    /**
     * @param des           规则
     * @param candiesNum    可用数量
     * @param ratio         比例
     * @param clickListener
     */
    private void initView(String des, long candiesNum, String ratio, final ConversionPopWindowItemClickListener clickListener) {
        DeleteEditText mEditNum = (DeleteEditText) mContentView.findViewById(R.id.et_conversion_num);
        TextView mFCCNum = (TextView) mContentView.findViewById(R.id.tv_fcc_num);
        TextView mAllNum = (TextView) mContentView.findViewById(R.id.tv_all_num);
        TextView mUsableNum = (TextView) mContentView.findViewById(R.id.tv_usable_num);
        TextView mConversionRatio = (TextView) mContentView.findViewById(R.id.tv_conversion_ratio);
        TextView mConversionContent = (TextView) mContentView.findViewById(R.id.tv_conversion_content);
        TextView mError = (TextView) mContentView.findViewById(R.id.tv_error_tip);
        ImageView mClose = (ImageView) mContentView.findViewById(R.id.iv_close_pop);

        Button mAffirm = (Button) mContentView.findViewById(R.id.bt_affirm_conversion);
        mUsableNum.setText("(今日可用" + candiesNum + "个)");
        mConversionRatio.setText("兑换为(" + ratio + ")");
        mConversionContent.setText(des);
        mAllNum.setOnClickListener(v -> {
            mEditNum.setText(candiesNum + "");
            initFCC(mFCCNum, Integer.valueOf(mEditNum.getText().toString()));
        });
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hide();
            }
        });
        mAffirm.setOnClickListener(v -> {
            String num = mEditNum.getText().toString();
            if (TextUtils.isEmpty(num) || num.equals("0")) {
                mError.setVisibility(View.VISIBLE);
                mError.setText("请输入糖果数量");
                return;
            }
            if (candiesNum < Long.parseLong(num)) {
                mError.setVisibility(View.VISIBLE);
                mError.setText("可用糖果不足");
                return;
            }
            clickListener.affirmConversion(num);
        });
        mEditNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (mEditNum.getText().toString().length() > 0) {
                    mError.setVisibility(View.INVISIBLE);
                }
                initFCC(mFCCNum, Integer.valueOf(TextUtils.isEmpty(mEditNum.getText().toString()) ? "0" : mEditNum.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initFCC(TextView textView, int candiesNum) {
        textView.setText((candiesNum * 0.001) + "");

    }

    public static final class CBuilder extends Builder {
        private ConversionPopWindowItemClickListener mConversionPopWindowItemClickListener;
        private long candiesNum;
        private String ratios;
        private String rule;

        public CBuilder buildConversionPopWindowItemClickListener(ConversionPopWindowItemClickListener mConversionPopWindowItemClickListener) {
            this.mConversionPopWindowItemClickListener = mConversionPopWindowItemClickListener;
            return this;
        }

        @Override
        public CBuilder backgroundAlpha(float alpha) {
            super.backgroundAlpha(alpha);
            return this;
        }

        @Override
        public CBuilder width(int width) {
            super.width(width);
            return this;
        }

        @Override
        public CBuilder height(int height) {
            super.height(height);
            return this;
        }

        public CBuilder desStr(long candiesNum) {
            this.candiesNum = candiesNum;
            return this;
        }

        public CBuilder ratiosStr(String ratios) {
            this.ratios = ratios;
            return this;
        }

        public CBuilder ruleStr(String rule) {
            this.rule = rule;
            return this;
        }

        @Override
        public CBuilder with(Activity activity) {
            super.with(activity);
            return this;
        }

        @Override
        public CBuilder isOutsideTouch(boolean isOutsideTouch) {
            super.isOutsideTouch(isOutsideTouch);
            return this;
        }

        @Override
        public CBuilder isFocus(boolean isFocus) {
            super.isFocus(isFocus);
            return this;
        }

        @Override
        public CBuilder backgroundDrawable(Drawable backgroundDrawable) {
            super.backgroundDrawable(backgroundDrawable);
            return this;
        }

        @Override
        public CBuilder animationStyle(int animationStyle) {
            super.animationStyle(animationStyle);
            return this;
        }

        public CBuilder parentView(View parentView) {
            super.parentView(parentView);
            return this;
        }

        @Override
        public ConversionAlertPopWindow build() {
            contentViewId = R.layout.pop_conversion_candies;
            isWrap = true;
            return new ConversionAlertPopWindow(this);
        }
    }

    public interface ConversionPopWindowItemClickListener {
        void affirmConversion(String candiesNum);
    }
}
