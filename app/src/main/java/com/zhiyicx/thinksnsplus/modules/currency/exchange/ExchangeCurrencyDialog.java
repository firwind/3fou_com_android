package com.zhiyicx.thinksnsplus.modules.currency.exchange;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyExchangeBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.widget.dialog.HBaseDialog;

import java.math.BigDecimal;

/**
 * author: huwenyong
 * date: 2018/7/27 9:55
 * description:
 * version:
 */

public class ExchangeCurrencyDialog extends HBaseDialog implements TextWatcher {

    private BigDecimal maxExchangeNum;
    private BigDecimal mRate1;
    private BigDecimal mRate2;
    private CurrencyExchangeBean mExchangeCurrencyRate;

    private OnExchangeCurrencyListener mListener;

    public void setOnExchangeCurrencyListener(OnExchangeCurrencyListener mListener) {
        this.mListener = mListener;
    }

    public void setExchangeCurrencyRate(CurrencyExchangeBean mExchangeCurrencyRate) {
        this.mExchangeCurrencyRate = mExchangeCurrencyRate;
    }

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

    /**
     * 发送验证码成功
     */
    public void setSendVerifyCodeSuccess(){
        getView(R.id.tv_send_verify_code).setVisibility(View.GONE);
        getView(R.id.et_verify_code).setVisibility(View.VISIBLE);
    }

    private void initView() {

        if(null == mExchangeCurrencyRate)
            return;
        //最多可兑换量
        mRate1 = new BigDecimal(mExchangeCurrencyRate.getNumber());
        mRate2 = new BigDecimal(mExchangeCurrencyRate.getNumber2());
        maxExchangeNum = new BigDecimal(mExchangeCurrencyRate.getBalance())
                .divide(mRate1,10,BigDecimal.ROUND_UP)
                .multiply(mRate2).setScale(10,BigDecimal.ROUND_UP);//保留10位小数，直接进位

        getTextView(R.id.tv_avaliable_balance_desc).setText(mExchangeCurrencyRate.getCurrency()+"可用量");
        getTextView(R.id.tv_avaliable_balance).setText(String.valueOf(mExchangeCurrencyRate.getBalance()));
        getTextView(R.id.tv_exchange_rate).setText(mExchangeCurrencyRate.getNumber()+":"+mExchangeCurrencyRate.getNumber2());
        getTextView(R.id.tv_num_desc).setText("本次兑换"+mExchangeCurrencyRate.getCurrency2()+"量");
        getTextView(R.id.tv_cost_num_desc).setText(String.format("本次兑换%s消耗%s量",mExchangeCurrencyRate.getCurrency2(),
                mExchangeCurrencyRate.getCurrency()));

        getEditText(R.id.et_num).addTextChangedListener(this);

        ((EditText)getView(R.id.et_num)).setText("");
        getTextView(R.id.tv_cost_num).setText("0.00");
        getView(R.id.et_num).requestFocusFromTouch();
        getView(R.id.et_num).post(() ->
                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).
                        showSoftInput(getView(R.id.et_num), 0));

        getView(R.id.bt_cancel).setOnClickListener(v->dismissDialog());
        getView(R.id.tv_send_verify_code).setOnClickListener(v -> {
            if(null != mListener)
                mListener.sendVerifyCode();
        });
        getView(R.id.tv_forget_password).setOnClickListener(v-> FindPasswordActivity.startSettingPayPasswordActivity(mContext));
        getView(R.id.bt_reset).setOnClickListener(v -> {
            getEditText(R.id.et_num).setText("");
            getEditText(R.id.et_verify_code).setText("");
            getEditText(R.id.et_password).setText("");
        });
        getView(R.id.bt_confirm).setOnClickListener(v -> {
            String num = getEditText(R.id.et_num).getText().toString();
            String verifyCode = getEditText(R.id.et_verify_code).getText().toString();
            String password = getEditText(R.id.et_password).getText().toString();
            if(TextUtils.isEmpty(num) || TextUtils.isEmpty(verifyCode) || TextUtils.isEmpty(password)){
                ToastUtils.showToast(mContext,"请输入完整的信息！");
                return;
            }
            if(null != mListener){
                dismissDialog();
                mListener.commitExchangeCurrency(mExchangeCurrencyRate.getCurrency(),mExchangeCurrencyRate.getCurrency2(),
                        num,verifyCode,password);
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
        double edit = 0;
        try {
            edit = Double.parseDouble(getEditText(R.id.et_num).getText().toString());
        }catch (Exception e){
            //
        }
        if(edit>maxExchangeNum.doubleValue()){
            getEditText(R.id.et_num).setText(maxExchangeNum.toString());
            getEditText(R.id.et_num).setSelection(getEditText(R.id.et_num).getText().toString().length());
        }else {
            getTextView(R.id.tv_cost_num).setText(edit == 0?"0.00":calExchangeCost(edit).toString());
        }
    }

    private BigDecimal calExchangeCost(double exchange_currency){
        return new BigDecimal(exchange_currency).multiply(mRate1).divide(mRate2,10,BigDecimal.ROUND_UP)
                .setScale(10,BigDecimal.ROUND_UP);//保留10位小数，直接进位
    }

    public interface OnExchangeCurrencyListener{
        void sendVerifyCode();
        void commitExchangeCurrency(String currency,String currency2,String num,String verifyCode,String password);
    }

}
