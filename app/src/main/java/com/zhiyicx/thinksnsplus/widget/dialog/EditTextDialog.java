package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.StringUtils;

/**
 * Author:huwenyong
 * Time:2018/5/3 9:23
 * Description:This is EditTextDialog.For...
 */
public class EditTextDialog extends HBaseDialog implements View.OnClickListener, TextWatcher {

    private OnInputOkListener mOnInputOkListener;
    private String hintText = "";

    public void setOnInputOkListener(OnInputOkListener onInputOkListener) {
        mOnInputOkListener = onInputOkListener;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

    /**
     * 构造方法
     *
     * @param context              上下文
     * @param cancelOnTouchOutside
     */
    public EditTextDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_edittext, cancelOnTouchOutside);

        setGravity(Gravity.BOTTOM);
        //处理键盘
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().setOnDismissListener(dialog -> DeviceUtils.hideSoftKeyboard(mContext,((EditText)getView(R.id.et_input))));
        getView(R.id.bt_confirm).setOnClickListener(this);
        ((EditText)getView(R.id.et_input)).addTextChangedListener(this);
    }

    @Override
    public void showDialog() {
        super.showDialog();


        ((EditText)getView(R.id.et_input)).setText("");
        ((EditText)getView(R.id.et_input)).setHint(hintText);
        ((EditText)getView(R.id.et_input)).requestFocus();
        ((EditText)getView(R.id.et_input)).post(() ->
                DeviceUtils.showSoftKeyboardV2(mContext,((EditText)getView(R.id.et_input))));

    }

    @Override
    public void dismissDialog() {

        DeviceUtils.hideSoftKeyboard(mContext,((EditText)getView(R.id.et_input)));
        super.dismissDialog();
    }

    @Override
    public void onClick(View v) {
        if(null == mOnInputOkListener)
            return;
        dismissDialog();
        mOnInputOkListener.onInputOk(((EditText)getView(R.id.et_input)).getText().toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        getView(R.id.bt_confirm).setEnabled(!TextUtils.isEmpty(((EditText)getView(R.id.et_input)).getText().toString()));
    }


    public interface OnInputOkListener{
        void onInputOk(String str);
    }

}
