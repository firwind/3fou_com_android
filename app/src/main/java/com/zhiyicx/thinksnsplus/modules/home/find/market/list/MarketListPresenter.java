package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.StockCertificateBean;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;

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
@FragmentScoped
public class MarketListPresenter extends AppBasePresenter<MarketContract.MarketListView> implements MarketContract.MarektListPresenter{

    @Inject
    public MarketListPresenter(MarketContract.MarketListView rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        List<BaseListBean> list = new ArrayList<>();
        list.add(new StockCertificateBean());
        list.add(new StockCertificateBean());
        list.add(new StockCertificateBean());
        mRootView.onNetResponseSuccess(list,isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }
}
