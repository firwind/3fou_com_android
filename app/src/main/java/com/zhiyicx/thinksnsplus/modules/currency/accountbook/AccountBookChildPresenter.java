package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AccountBookListBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/23 9:32
 * description:
 * version:
 */
@FragmentScoped
public class AccountBookChildPresenter extends AppBasePresenter<AccountBookChildContract.View>
        implements AccountBookChildContract.Presenter{
    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public AccountBookChildPresenter(AccountBookChildContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mCurrencyRepository.getAccountBookList(maxId,mRootView.getBookTag())
                .subscribe(new BaseSubscribeForV2<List<AccountBookListBean>>() {
                    @Override
                    protected void onSuccess(List<AccountBookListBean> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
//
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<AccountBookListBean> data, boolean isLoadMore) {
        return false;
    }

}
