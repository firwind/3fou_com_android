package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.StockCertificateBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/6/28 19:31
 * description:
 * version:
 */

public class MarketPresenter extends AppBasePresenter<MarketContract.View> implements MarketContract.Presenter{
    @Inject
    public MarketPresenter(MarketContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        List<StockCertificateBean> list = new ArrayList<>();
        list.add(new StockCertificateBean());
        list.add(new StockCertificateBean());
        list.add(new StockCertificateBean());
        mRootView.onNetResponseSuccess(list,isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<StockCertificateBean> data, boolean isLoadMore) {
        return false;
    }
}
