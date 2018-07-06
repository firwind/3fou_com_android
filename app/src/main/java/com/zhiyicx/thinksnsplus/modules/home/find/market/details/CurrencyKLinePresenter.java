package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.MarketRepository;
import com.zhiyicx.thinksnsplus.utils.kline.KLineDataHelper;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * author: huwenyong
 * date: 2018/7/5 11:25
 * description:
 * version:
 */
@FragmentScoped
public class CurrencyKLinePresenter extends AppBasePresenter<CurrencyKLineContract.View> implements CurrencyKLineContract.Presenter {

    @Inject
    public MarketRepository mMarketRepository;

    @Inject
    public CurrencyKLinePresenter(CurrencyKLineContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestKLineData(String period) {

        addSubscrebe(mMarketRepository.getCurrencyKLineData(mRootView.getTicker(), period)
                .flatMap(new Func1<List<KLineEntity>, Observable<List<KLineEntity>>>() {
                    @Override
                    public Observable<List<KLineEntity>> call(List<KLineEntity> kLineEntities) {
                        Collections.sort(kLineEntities);
                        KLineDataHelper.calculate(kLineEntities);
                        return Observable.just(kLineEntities);
                    }
                })
                .subscribe(new BaseSubscribeForV2<List<KLineEntity>>() {
                    @Override
                    protected void onSuccess(List<KLineEntity> data) {
                        mRootView.setKLineData(data);
                    }
                }));

    }
}
