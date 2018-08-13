package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WithdrawCurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * author: huwenyong
 * date: 2018/7/18 13:51
 * description:
 * version:
 */

public class WithdrawCurrencyPresenter extends AppBasePresenter<WithdrawCurrencyContract.View> implements WithdrawCurrencyContract.Presenter{

    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public WithdrawCurrencyPresenter(WithdrawCurrencyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestCostFeeRate() {
        mCurrencyRepository.getWithdrawRate(mRootView.getCurrency())
                .subscribe(new BaseSubscribeForV2<WithdrawCurrencyBean>() {
                    @Override
                    protected void onSuccess(WithdrawCurrencyBean data) {
                        mRootView.setBalanceAndRate(true,data.balance,data.rate);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.setBalanceAndRate(false,0,0);
                    }
                });
    }

    @Override
    public void requestWithdrawCurrency(String address, String mark, boolean isSave, String money, String remark) {
        mCurrencyRepository.withdrawCurrency(mRootView.getCurrency(),address,mark,isSave,money,remark)
                .doOnSubscribe(() -> {
                    mRootView.showSnackLoadingMessage("请稍后...");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage("已提交审核！");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
    }
}
