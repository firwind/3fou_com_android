package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.IBaseTouristPresenter;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBean;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/6/28 19:29
 * description:
 * version:
 */

public interface MarketContract {

    interface MarketView extends IBaseView<MarketPresenter>{
        void getCurrencyListSuccess(List<CurrencyBean> list);
    }

    interface MarketPresenter extends IBaseTouristPresenter{
        void getMarketCurrencyList();
    }


    interface MarketListView extends ITSListView<BaseListBean,MarektListPresenter> {
        boolean isRankMarket();
        CurrencyBean getCurrency();
    }

    interface MarektListPresenter extends ITSListPresenter<BaseListBean>{

    }

}
