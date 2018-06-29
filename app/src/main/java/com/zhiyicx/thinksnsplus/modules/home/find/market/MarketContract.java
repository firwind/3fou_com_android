package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.data.beans.StockCertificateBean;

/**
 * author: huwenyong
 * date: 2018/6/28 19:29
 * description:
 * version:
 */

public interface MarketContract {

    interface View extends ITSListView<StockCertificateBean,Presenter> {

    }

    interface Presenter extends ITSListPresenter<StockCertificateBean>{



    }

}
