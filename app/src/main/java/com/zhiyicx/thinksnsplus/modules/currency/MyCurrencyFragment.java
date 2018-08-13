package com.zhiyicx.thinksnsplus.modules.currency;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyExchangeBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.modules.currency.accountbook.AccountBookActivity;
import com.zhiyicx.thinksnsplus.modules.currency.interest.CurrencyInterestActivity;
import com.zhiyicx.thinksnsplus.modules.currency.recharge.RechargeCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.currency.withdraw.WithdrawCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.PayPassWordActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.modules.currency.exchange.ExchangeCurrencyDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public class MyCurrencyFragment extends TSListFragment<MyCurrencyContract.Presenter,CurrencyBalanceBean> implements MyCurrencyContract.View{

    private TextView mTvYearRate;
    private ExchangeCurrencyDialog mExchangeCurrencyDialog;

    public static MyCurrencyFragment newInstance(){
        Bundle bundle = new Bundle();
        MyCurrencyFragment fragment = new MyCurrencyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        StatusBarUtils.setStatusBarColor(mActivity,R.color.themeColor);

        setCenterTextColor(R.color.white);
        rootView.setBackgroundColor(Color.WHITE);

        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_my_currency_header,null);
        headerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        headerView.findViewById(R.id.tv_software).setOnClickListener(v->
                CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_USE_RECOMMEND));
        headerView.findViewById(R.id.bt_join).setOnClickListener(v->
                startActivity(new Intent(mActivity, CurrencyInterestActivity.class)));
        mTvYearRate = (TextView) headerView.findViewById(R.id.tv_year_rate);

        //明细
        headerView.findViewById(R.id.tv_interest_details).setOnClickListener(v -> {
            HashMap<String, String> headers = new HashMap();
            AuthBean authBean = AppApplication.getmCurrentLoginAuth();
            if (authBean != null) {
                headers.put("Authorization", authBean.getToken());
            }
            CustomWEBActivity.startToWEBActivity(mActivity,headers,
                    String.format(ApiConfig.URL_CURRENCY_INTEREST_RECORD+"?from=app&token=%s",
                            authBean.getToken()));
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        mPresenter.requestNetData(0L,false);
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return "数字资产";
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.icon_scan_code;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter mAdapter = new CommonAdapter<CurrencyBalanceBean>(getContext(),
                R.layout.item_currency_balance,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyBalanceBean currencyBalanceBean, int position) {

                if(1 == position){
                    mTvYearRate.setText("+ "+currencyBalanceBean.year_rate+"%");
                }

                ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv_icon),currencyBalanceBean.icon);
                holder.getTextView(R.id.tv_name).setText(currencyBalanceBean.currency);
                holder.getTextView(R.id.tv_balance).setText(currencyBalanceBean.balance);
                holder.getTextView(R.id.tv_frezz).setText(currencyBalanceBean.blocked_balance);

                holder.getView(R.id.tv_detail).setOnClickListener(v->
                        AccountBookActivity.startAccountBookActivity(mActivity,currencyBalanceBean.currency));
                holder.getView(R.id.bt_recharge).setOnClickListener(v->
                        RechargeCurrencyActivity.startRechargeCurrencyActivity(getContext(),currencyBalanceBean.currency));
                holder.getView(R.id.bt_withdraw).setOnClickListener(v->
                        WithdrawCurrencyActivity.startWithdrawCurrencyActivity(getContext(),currencyBalanceBean.currency));
                holder.getView(R.id.bt_exchange).setOnClickListener(getExchangeClickListener(currencyBalanceBean));

                holder.getView(R.id.bt_exchange).setVisibility(
                        null == currencyBalanceBean.exchange || currencyBalanceBean.exchange.size() == 0 ?
                                View.GONE:View.VISIBLE );
            }
        };
        return mAdapter;
    }

    private View.OnClickListener getExchangeClickListener(CurrencyBalanceBean currencyBalanceBean){
        return v -> {
            if(Double.parseDouble(currencyBalanceBean.balance) == 0 ){
                showSnackWarningMessage("您的可用余额不足！");
                return;
            }

            if(mPresenter.getPayPasswordIsSetted()){
                if(currencyBalanceBean.exchange.size() > 1){
                    String[] arr = new String[currencyBalanceBean.exchange.size()];
                    for (int i = 0; i < currencyBalanceBean.exchange.size(); i++) {
                        arr[i] = currencyBalanceBean.exchange.get(i).getCurrency2();
                    }
                    new AlertDialog.Builder(mActivity)
                            .setTitle("选择兑换币种")
                            .setItems(arr, (dialog, which) -> {
                                showExchangeCurrencyDialog(currencyBalanceBean.exchange.get(which));
                            })
                            .create()
                            .show();
                }else {
                    showExchangeCurrencyDialog(currencyBalanceBean.exchange.get(0));
                }

            }else {
                new AlertDialog.Builder(mActivity)
                        .setTitle("提示")
                        .setMessage("请先设置支付密码！")
                        .setPositiveButton("去设置", (dialog, which) -> startActivity(new Intent(mActivity, PayPassWordActivity.class)))
                        .create()
                        .show();
            }
        };
    }

    /**
     * 兑换币弹窗
     */
    private void showExchangeCurrencyDialog(CurrencyExchangeBean rate){

        if(null == mExchangeCurrencyDialog){
            mExchangeCurrencyDialog = new ExchangeCurrencyDialog(mActivity,false);
            mExchangeCurrencyDialog.setOnExchangeCurrencyListener(new ExchangeCurrencyDialog.OnExchangeCurrencyListener() {
                @Override
                public void sendVerifyCode() {
                    mPresenter.requestSendVerifyCode();
                }

                @Override
                public void commitExchangeCurrency(String currency,String currency2,String num,String verifyCode,String password) {
                    mPresenter.requestExchangeCurrency(currency,currency2,num,verifyCode,password);
                }
            });
        }
        mExchangeCurrencyDialog.setExchangeCurrencyRate(rate);
        mExchangeCurrencyDialog.showDialog();

    }

    @Override
    public void setExchangeRate(String currency, String currency2, ExchangeCurrencyRate rate) {

        if(null != rate){
            dismissSnackBar();
            rate.setCurrency(currency);
            rate.setCurrency_exchange(currency2);
            //showExchangeCurrencyDialog(rate);
        }else {
            showSnackErrorMessage("获取兑换比例失败！");
        }
    }

    @Override
    public void sendVerifyCodeSuccess() {
        if(null != mExchangeCurrencyDialog && mExchangeCurrencyDialog.isShowing()){
            mExchangeCurrencyDialog.setSendVerifyCodeSuccess();
        }
    }

    @Override
    public void exchangeCurrencySuccess() {
        startRefreshNoAnimIfEmpty();
    }
}
