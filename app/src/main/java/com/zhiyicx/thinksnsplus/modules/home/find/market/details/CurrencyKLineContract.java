package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/5 11:24
 * description:
 * version:
 */

public interface CurrencyKLineContract {

    interface View extends IBaseView<Presenter>{
        String getTicker();
        void setKLineData(List<KLineEntity> list);
    }

    interface Presenter extends IBasePresenter{
        void requestKLineData(String period);
    }

}
