package com.zhiyicx.thinksnsplus.modules.currency.address;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;

/**
 * author: huwenyong
 * date: 2018/7/20 9:59
 * description:
 * version:
 */

public interface CurrencyAddressContract {

    interface View extends ITSListView<CurrencyAddress,Presenter>{

    }

    interface Presenter extends ITSListPresenter<CurrencyAddress>{

    }

}
