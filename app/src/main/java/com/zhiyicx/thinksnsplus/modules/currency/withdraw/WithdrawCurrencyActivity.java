package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/7/18 11:47
 * description:
 * version:
 */

public class WithdrawCurrencyActivity extends TSActivity<WithdrawCurrencyPresenter,WithdrawCurrencyFragment>{
    @Override
    protected WithdrawCurrencyFragment getFragment() {
        return WithdrawCurrencyFragment.newInstance(getIntent().getStringExtra(IntentKey.CURRENCY_IN_MARKET));
    }

    @Override
    protected void componentInject() {
        DaggerWithdrawCurrencyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .withdrawCurrencyPresenterMoudle(new WithdrawCurrencyPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }

    public static void startWithdrawCurrencyActivity(Context mContext,String currency){
        Intent intent = new Intent(mContext,WithdrawCurrencyActivity.class);
        intent.putExtra(IntentKey.CURRENCY_IN_MARKET,currency);
        mContext.startActivity(intent);
    }

}
