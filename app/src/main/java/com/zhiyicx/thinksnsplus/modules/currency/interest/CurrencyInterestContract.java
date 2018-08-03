package com.zhiyicx.thinksnsplus.modules.currency.interest;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;

/**
 * author: huwenyong
 * date: 2018/7/31 15:41
 * description:
 * version:
 */

public interface CurrencyInterestContract {

    interface View extends ITSListView<CurrencyBalanceBean,Presenter>{

    }

    interface Presenter extends ITSListPresenter<CurrencyBalanceBean>{
        String getCurrentToken();
    }

}
