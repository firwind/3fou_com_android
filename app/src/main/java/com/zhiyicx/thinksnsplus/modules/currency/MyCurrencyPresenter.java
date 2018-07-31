package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;
import com.zhiyicx.thinksnsplus.data.source.remote.CurrencyClient;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.VertifyCodeRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public class MyCurrencyPresenter extends AppBasePresenter<MyCurrencyContract.View> implements MyCurrencyContract.Presenter {

    @Inject
    CurrencyRepository mCurrencyRepository;
    @Inject
    VertifyCodeRepository mVerifyCodeRepository;

    @Inject
    public MyCurrencyPresenter(MyCurrencyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        addSubscrebe(mCurrencyRepository.getMyCurrencyList().subscribe(new BaseSubscribeForV2<List<CurrencyBalanceBean>>() {
            @Override
            protected void onSuccess(List<CurrencyBalanceBean> list) {
                mRootView.onNetResponseSuccess(list, isLoadMore);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRootView.onResponseError(e, isLoadMore);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mRootView.closeLoadingView();
            }
        }));
    }

    @Override
    public void requestExchangeRate(String currency, String currency2) {
        addSubscrebe(mCurrencyRepository.getExchangeRate(currency, currency2)
                .subscribe(new BaseSubscribeForV2<ExchangeCurrencyRate>() {
                    @Override
                    protected void onSuccess(ExchangeCurrencyRate data) {
                        mRootView.setExchangeRate(currency, currency2, data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.setExchangeRate(currency,currency2,null);
                    }
                }));
    }

    @Override
    public void requestSendVerifyCode() {
        String phone = mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault())).getPhone();
        addSubscrebe(mVerifyCodeRepository
                .getMemberVertifyCode(phone).subscribe(new BaseSubscribeForV2<Object>() {

                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.sendVerifyCodeSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                    }
                }));
    }

    @Override
    public void requestExchangeCurrency(String currency, String currency2, String num, String verifyCode, String password) {
        mRootView.showSnackLoadingMessage("请稍后...");
        String phone = mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault())).getPhone();
        addSubscrebe(mCurrencyRepository.exchangeCurrency(currency,currency2,num,phone,verifyCode,password)
        .subscribe(new BaseSubscribeForV2<String>() {
            @Override
            protected void onSuccess(String data) {
                mRootView.showSnackSuccessMessage("兑换成功！");
                mRootView.exchangeCurrencySuccess();
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.showSnackErrorMessage(message);
            }
        }));
    }

    @Override
    public boolean getPayPasswordIsSetted() {
        return mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()).isPay_password();
    }


    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyBalanceBean> data, boolean isLoadMore) {
        return false;
    }

}
