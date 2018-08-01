package com.zhiyicx.thinksnsplus.modules.currency.interest;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * author: huwenyong
 * date: 2018/7/31 15:41
 * description:
 * version:
 */

public class CurrencyInterestActivity extends TSActivity<CurrencyInterestPresenter,CurrencyInterestFragment> {

    @Override
    protected CurrencyInterestFragment getFragment() {
        return CurrencyInterestFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerCurrencyInterestComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .currencyInterestModule(new CurrencyInterestModule(mContanierFragment))
                .build()
                .inject(this);
    }

}
