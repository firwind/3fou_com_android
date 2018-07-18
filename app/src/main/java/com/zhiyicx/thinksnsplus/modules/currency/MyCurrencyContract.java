package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public interface MyCurrencyContract {

    interface View extends ITSListView<CurrencyBalanceBean,Presenter> {

    }

    interface Presenter extends ITSListPresenter<CurrencyBalanceBean>{

    }

}
