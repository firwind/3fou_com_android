package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

/**
 * author: huwenyong
 * date: 2018/7/21 9:56
 * description:
 * version:
 */

public class AddCurrencyAddressDialog extends HBaseDialog implements TextWatcher {

    private OnAddressConfirmedListener mListener;

    /**
     * 构造函数
     * @param context
     * @param cancelOnTouchOutside
     */
    public AddCurrencyAddressDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_add_currency_address, cancelOnTouchOutside);
        setGravity(Gravity.CENTER);
        initView();
    }

    public void setOnAddressConfirmedListener(OnAddressConfirmedListener mListener) {
        this.mListener = mListener;
    }

    private void initView() {

        ((EditText)getView(R.id.et_address)).addTextChangedListener(this);
        ((EditText)getView(R.id.et_tag)).addTextChangedListener(this);

        getView(R.id.tv_confirm).setOnClickListener(v->{
            dismissDialog();
            if(null != mListener){
                mListener.onAddressConfirmed( ((EditText)getView(R.id.et_address)).getText().toString(),
                        ((EditText)getView(R.id.et_tag)).getText().toString() );
            }
        });

        //扫描二维码
        getView(R.id.iv_scan).setOnClickListener(v-> ScanCodeActivity.startActivityForResult(mContext, IntentKey.REQ_CODE_GET_SCAN_RESULT));
        //取消
        getView(R.id.tv_cancel).setOnClickListener(v -> dismissDialog());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
    @Override
    public void afterTextChanged(Editable s) {

        String address = ((EditText)getView(R.id.et_address)).getText().toString();
        String tag = ((EditText)getView(R.id.et_tag)).getText().toString();
        getView(R.id.tv_confirm).setEnabled(address.length() != 0 && tag.length() != 0);
    }


    public interface OnAddressConfirmedListener{
        void onAddressConfirmed(String address,String tag);
    }

}
