package com.zhiyicx.thinksnsplus.modules.currency.interest;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyInterest;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/31 15:49
 * description:
 * version:
 */

public class CurrencyInterestPresenter extends AppBasePresenter<CurrencyInterestContract.View>
        implements CurrencyInterestContract.Presenter{

    @Inject
    public CurrencyInterestPresenter(CurrencyInterestContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyInterest> data, boolean isLoadMore) {
        return false;
    }
}
