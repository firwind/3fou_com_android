package com.zhiyicx.thinksnsplus.modules.currency;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.data.beans.ExchangeCurrencyRate;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public interface MyCurrencyContract {

    interface View extends ITSListView<CurrencyBalanceBean,Presenter> {
        void setExchangeRate(String currency, String currency2, ExchangeCurrencyRate rate);
        void sendVerifyCodeSuccess();
        void exchangeCurrencySuccess();
    }

    interface Presenter extends ITSListPresenter<CurrencyBalanceBean>{
        void requestExchangeRate(String currency,String currency2);
        void requestSendVerifyCode();
        void requestExchangeCurrency(String currency,String currency2,String num,String verifyCode,String password);
        boolean getPayPasswordIsSetted();
    }

}
