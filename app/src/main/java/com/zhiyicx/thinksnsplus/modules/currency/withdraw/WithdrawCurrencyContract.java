package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * author: huwenyong
 * date: 2018/7/18 13:50
 * description:
 * version:
 */

public interface WithdrawCurrencyContract {

    interface View extends IBaseView<Presenter>{
        String getCurrency();
        void setBalanceAndRate(boolean isSuccess,String balance,String rate);
    }

    interface Presenter extends IBasePresenter{

        void requestCostFeeRate();

        void requestWithdrawCurrency(String address, String mark,
                              boolean isSave, String money, String remark,String pay_password);
    }

}
