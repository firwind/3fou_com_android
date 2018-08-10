package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.currency.accountbook.AccountBookActivity;
import com.zhiyicx.thinksnsplus.modules.currency.address.CurrencyAddressActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: huwenyong
 * date: 2018/7/18 11:48
 * description:
 * version:
 */

public class WithdrawCurrencyFragment extends TSFragment<WithdrawCurrencyContract.Presenter>
        implements WithdrawCurrencyContract.View, TextWatcher {

    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_tag)
    EditText mEtTag;
    @BindView(R.id.et_num)
    EditText mEtNum;
    @BindView(R.id.cb_save)
    CheckBox mCbSave;
    @BindView(R.id.et_remark)
    EditText mEtRemark;
    @BindView(R.id.tv_avaliable_balance)
    TextView mTvAvaliableBalance;
    @BindView(R.id.tv_transfer)
    TextView mTvTransfer;
    @BindView(R.id.tv_rate)
    TextView mTvRate;

    private RightTopMenu mRightTopMenu;
    private double mTransferRate = 0;//转账手续费
    private double mAvaliableBalance = 0;//可用余额

    public static WithdrawCurrencyFragment newInstance(String currency){
        WithdrawCurrencyFragment fragment = new WithdrawCurrencyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.CURRENCY_IN_MARKET,currency);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
        mEtNum.addTextChangedListener(this);
    }

    @Override
    protected void initData() {
        mPresenter.requestCostFeeRate();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdraw_currency;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        initData();
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.icon_add_white;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void setRightClick() {
        //super.setRightClick();
        showRightTopMenu();

    }

    @Override
    protected String setCenterTitle() {
        return "提币";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @OnClick({R.id.iv_address,R.id.bt_confirm})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_address:
                CurrencyAddressActivity.startCurrencyAddressActivityForResult(this,
                        getArguments().getString(IntentKey.CURRENCY_IN_MARKET),true,
                        IntentKey.REQ_CODE_SELECT_CURRENCY_ADDRESS);
                break;
            case R.id.bt_confirm:
                if(checkData()){
                    mPresenter.requestWithdrawCurrency(mEtAddress.getText().toString(),
                            mEtTag.getText().toString(),
                            mCbSave.isChecked(),
                            mEtNum.getText().toString(),
                            mEtRemark.getText().toString());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择钱包地址返回
        if(resultCode == mActivity.RESULT_OK && null != data){
            switch (requestCode){
                case IntentKey.REQ_CODE_SELECT_CURRENCY_ADDRESS:

                    mEtAddress.setText( ((CurrencyAddress)data.getParcelableExtra(IntentKey.RESULT_CURRENCY_ADDRESS)).address );
                    mEtTag.setText( ((CurrencyAddress)data.getParcelableExtra(IntentKey.RESULT_CURRENCY_ADDRESS)).mark );

                    mEtNum.setFocusable(true);
                    mEtNum.requestFocusFromTouch();

                    break;
                case IntentKey.REQ_CODE_GET_SCAN_RESULT:

                    mEtAddress.setText( data.getStringExtra(IntentKey.RESULT_SCAN) );

                    mEtNum.setFocusable(true);
                    mEtNum.requestFocusFromTouch();

                    break;
            }
        }

    }

    /**
     * 右上角弹窗
     */
    private void showRightTopMenu(){
        if(null == mRightTopMenu){
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem(R.mipmap.icon_scan_gray,"扫一扫"));
            list.add(new MenuItem(R.mipmap.icon_withdraw_gray,"转出记录"));
            mRightTopMenu = new RightTopMenu.Builder(mActivity)
                    .dimBackground(true) //背景变暗，默认为true
                    .needAnimationStyle(true) //显示动画，默认为true
                    .animationStyle(R.style.RTM_ANIM_STYLE)  //默认为R.style.RTM_ANIM_STYLE
                    .windowWidth(DensityUtil.dip2px(mActivity,160))
                    /*.windowHeight(ViewGroup.LayoutParams.WRAP_CONTENT)*/
                    .menuItems(list)
                    .onMenuItemClickListener(position -> {
                        if(position == 0){
                            ScanCodeActivity.startActivityForResult(this,IntentKey.REQ_CODE_GET_SCAN_RESULT);
                        }else {
                            AccountBookActivity.startAccountBookActivity(mActivity,getCurrency());
                        }
                    })
                    .build();
        }
        mRightTopMenu.showAsDropDown(mToolbarRight,DensityUtil.dip2px(mActivity,15),0);
    }

    /**
     * 本地校验提交数据
     */
    private boolean checkData(){

        if(TextUtils.isEmpty(mEtAddress.getText().toString())){
            showSnackErrorMessage("请输入接收地址！");
            return false;
        }

        if(TextUtils.isEmpty(mEtTag.getText().toString()) && mCbSave.isChecked()){
            showSnackErrorMessage("请填写地址标签！");
            return false;
        }

        if(TextUtils.isEmpty(mEtNum.getText().toString())){
            showSnackErrorMessage("请输入发送数量！");
            return false;
        }

        try {
            Double num = Double.parseDouble(mEtNum.getText().toString());
            if(num <= mTransferRate){
                showSnackErrorMessage("输入的金额必须大于手续费！");
                return false;
            }
        }catch (Exception e){
            showSnackErrorMessage("请填写正确的信息！");
            return false;
        }

        return true;

    }

    @Override
    public String getCurrency() {
        return getArguments().getString(IntentKey.CURRENCY_IN_MARKET);
    }

    @Override
    public void setBalanceAndRate(boolean isSuccess, double balance, double rate) {
        if(isSuccess){
            closeLoadingView();
            mTvAvaliableBalance.setText("可用余额："+balance+" "+getCurrency());
            mTvRate.setText("转账手续费（"+rate+getCurrency()+"）");

            this.mTransferRate = rate;
            this.mAvaliableBalance = balance;
        }else {
            setLoadViewHolderImag(R.mipmap.img_default_internet);
            showLoadViewLoadError();
        }
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if(Prompt.SUCCESS == prompt){
            mActivity.finish();
        }
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
            edit = Double.parseDouble(mEtNum.getText().toString());
        }catch (Exception e){
            //
        }

        if(edit == 0){
            mTvTransfer.setText("--");
            return;
        }

        if(edit > mAvaliableBalance){
            mEtNum.setText(String.valueOf(mAvaliableBalance));
            mEtNum.setSelection(mEtNum.getText().toString().length());
        }else {
            mTvTransfer.setText(String.format("%.10d",edit-mTransferRate) );
        }


    }
}
