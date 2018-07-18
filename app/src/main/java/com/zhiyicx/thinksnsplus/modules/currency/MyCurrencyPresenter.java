package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public class MyCurrencyPresenter extends AppBasePresenter<MyCurrencyContract.View> implements MyCurrencyContract.Presenter{

    @Inject
    public MyCurrencyPresenter(MyCurrencyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        List<CurrencyBalanceBean> list = new ArrayList<>();
        list.add(new CurrencyBalanceBean());
        list.add(new CurrencyBalanceBean());
        list.add(new CurrencyBalanceBean());
        list.add(new CurrencyBalanceBean());
        mRootView.closeLoadingView();
        mRootView.onNetResponseSuccess(list,isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyBalanceBean> data, boolean isLoadMore) {
        return false;
    }
}
