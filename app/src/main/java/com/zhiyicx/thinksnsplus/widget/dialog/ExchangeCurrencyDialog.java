package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhiyicx.thinksnsplus.R;

/**
 * author: huwenyong
 * date: 2018/7/27 9:55
 * description:
 * version:
 */

public class ExchangeCurrencyDialog extends HBaseDialog{
    /**
     * 构造方法
     *
     * @param context              上下文
     * @param cancelOnTouchOutside
     */
    public ExchangeCurrencyDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_exchange_currency, cancelOnTouchOutside);
    }

    @Override
    public void showDialog() {
        super.showDialog();
        initView();
    }

    private void initView() {

        ((EditText)getView(R.id.et_num)).setText("");
        getView(R.id.et_num).setFocusable(true);
        getView(R.id.et_num).requestFocusFromTouch();
        getView(R.id.et_num).post(() ->
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        showSoftInput(getView(R.id.et_num), 0));

        getView(R.id.bt_cancel).setOnClickListener(v->dismissDialog());
    }
}
