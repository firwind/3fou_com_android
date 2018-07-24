package com.zhiyicx.thinksnsplus.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

/**
 * author: huwenyong
 * date: 2018/7/21 9:56
 * description:
 * version:
 */

public class EditCurrencyAddressDialog extends HBaseDialog implements TextWatcher {

    private OnAddressConfirmedListener mListener;
    private CurrencyAddress mEditAddress = null;

    public void setEditAddress(CurrencyAddress mEditAddress) {
        this.mEditAddress = mEditAddress;
    }

    public CurrencyAddress getEditAddress() {
        return mEditAddress;
    }

    /**
     * 构造函数
     * @param context
     * @param cancelOnTouchOutside
     */
    public EditCurrencyAddressDialog(Activity context, boolean cancelOnTouchOutside) {
        super(context, R.layout.dialog_add_currency_address, cancelOnTouchOutside);
        setGravity(Gravity.CENTER);
        //initView();
    }

    public void setOnAddressConfirmedListener(OnAddressConfirmedListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void showDialog() {
        super.showDialog();
        initView();
    }

    private void initView() {

        ((EditText)getView(R.id.et_address)).addTextChangedListener(this);
        ((EditText)getView(R.id.et_tag)).addTextChangedListener(this);

        if(null != mEditAddress){
            ((EditText)getView(R.id.et_address)).setText(mEditAddress.address);
            ((EditText)getView(R.id.et_tag)).setText(mEditAddress.tag);
        }else {
            ((EditText)getView(R.id.et_address)).setText("");
            ((EditText)getView(R.id.et_tag)).setText("");
        }

        ((EditText)getView(R.id.et_address)).setSelection(((EditText)getView(R.id.et_address)).getText().toString().length());
        getView(R.id.et_address).setFocusable(true);
        getView(R.id.et_address).requestFocusFromTouch();
        getView(R.id.et_address).post(() ->
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        showSoftInput(getView(R.id.et_address), 0));
        ((TextView)getView(R.id.tv_cancel)).setText(null == mEditAddress ? "取消":"删除");

        getView(R.id.tv_confirm).setOnClickListener(v->{
            dismissDialog();
            if(null != mListener){
                String address = ((EditText)getView(R.id.et_address)).getText().toString();
                String tag = ((EditText)getView(R.id.et_tag)).getText().toString();
                if(null != mEditAddress){
                    mListener.onAddressEdit(mEditAddress.id, address, tag);
                }else {
                    mListener.onAddressAdd( address,tag );
                }
            }
        });

        //取消
        getView(R.id.tv_cancel).setOnClickListener(v -> {
            dismissDialog();
            if(null != mListener && null != mEditAddress){
                mListener.onAddressDeleted(mEditAddress.id);
            }
        });

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
        void onAddressAdd(String address,String tag);
        void onAddressEdit(String id,String address,String tag);
        void onAddressDeleted(String id);
    }

}
