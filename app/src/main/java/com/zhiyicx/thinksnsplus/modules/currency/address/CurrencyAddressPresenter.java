package com.zhiyicx.thinksnsplus.modules.currency.address;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/20 10:01
 * description:
 * version:
 */
@FragmentScoped
public class CurrencyAddressPresenter extends AppBasePresenter<CurrencyAddressContract.View> implements CurrencyAddressContract.Presenter{
    @Inject
    public CurrencyAddressPresenter(CurrencyAddressContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        List<CurrencyAddress> list = new ArrayList<>();
        list.add(new CurrencyAddress());
        list.add(new CurrencyAddress());
        list.add(new CurrencyAddress());
        list.add(new CurrencyAddress());
        mRootView.onNetResponseSuccess(list,isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyAddress> data, boolean isLoadMore) {
        return false;
    }
}
