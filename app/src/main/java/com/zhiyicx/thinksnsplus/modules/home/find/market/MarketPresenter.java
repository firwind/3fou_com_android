package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/2 9:46
 * description:
 * version:
 */
@FragmentScoped
public class MarketPresenter extends AppBasePresenter<MarketContract.MarketView> implements MarketContract.MarketPresenter{
    @Inject
    public MarketPresenter(MarketContract.MarketView rootView) {
        super(rootView);
    }
}
