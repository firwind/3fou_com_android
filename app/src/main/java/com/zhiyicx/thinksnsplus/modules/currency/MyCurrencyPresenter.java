package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CurrencyClient;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

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

public class MyCurrencyPresenter extends AppBasePresenter<MyCurrencyContract.View> implements MyCurrencyContract.Presenter{

    @Inject
    CurrencyRepository mCurrencyRepository;

    @Inject
    public MyCurrencyPresenter(MyCurrencyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        addSubscrebe(mCurrencyRepository.getMyCurrencyList().subscribe(new BaseSubscribeForV2<String>() {
            @Override
            protected void onSuccess(String data) {
                List<CurrencyBalanceBean> list = new ArrayList<>();
                list.add(new CurrencyBalanceBean("bcb",0,0));
                mRootView.onNetResponseSuccess(list,isLoadMore);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mRootView.closeLoadingView();
            }
        }));
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CurrencyBalanceBean> data, boolean isLoadMore) {
        return false;
    }
}
