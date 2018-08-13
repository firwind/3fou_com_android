package com.zhiyicx.thinksnsplus.modules.currency.address;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/7/20 10:01
 * description:
 * version:
 */
@FragmentScoped
public class CurrencyAddressPresenter extends AppBasePresenter<CurrencyAddressContract.View> implements CurrencyAddressContract.Presenter {

    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public CurrencyAddressPresenter(CurrencyAddressContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        addSubscrebe(mCurrencyRepository.getCurrencyAddressList(mRootView.getCurrency()).
                subscribe(new BaseSubscribeForV2<List<CurrencyAddress>>() {
                    @Override
                    protected void onSuccess(List<CurrencyAddress> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                }));
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyAddress> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void addCurrencyAddress(String address, String mark) {
        addSubscrebe(mCurrencyRepository.addCurrencyAddress(mRootView.getCurrency(), address, mark)
                .doOnSubscribe(() -> {
                    mRootView.showSnackLoadingMessage("请稍后...");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.dismissSnackBar();
                        mRootView.startRefrsh();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                }));
    }

    @Override
    public void editCurrencyAddress(String address_id, String address, String mark) {
        addSubscrebe(mCurrencyRepository.editCurrencyAddress(address_id, address, mark)
                .doOnSubscribe(() -> {
                    mRootView.showSnackLoadingMessage("请稍后...");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.dismissSnackBar();
                        mRootView.startRefrsh();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                }));
    }

    @Override
    public void deleteCurrencyAddress(String address_id) {
        addSubscrebe(mCurrencyRepository.deleteCurrencyAddress(address_id)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> {
                    mRootView.showSnackLoadingMessage("请稍后...");
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.dismissSnackBar();
                        mRootView.startRefrsh();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                }));
    }
}
