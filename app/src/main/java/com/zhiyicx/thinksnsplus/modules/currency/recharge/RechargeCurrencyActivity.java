package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/7/18 10:15
 * description:
 * version:
 */

public class RechargeCurrencyActivity extends TSActivity<RechargeCurrencyPresenter,RechargeCurrencyFragment>{
    @Override
    protected RechargeCurrencyFragment getFragment() {
        return RechargeCurrencyFragment.newInstance(getIntent().getStringExtra(IntentKey.CURRENCY_IN_MARKET));
    }

    @Override
    protected void componentInject() {
        DaggerRechargeCurrencyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .rechargeCurrencyMoudle(new RechargeCurrencyMoudle(mContanierFragment))
                .build()
                .inject(this);
    }



    public static void startRechargeCurrencyActivity(Context mContext,String currency){
        Intent intent = new Intent(mContext,RechargeCurrencyActivity.class);
        intent.putExtra(IntentKey.CURRENCY_IN_MARKET,currency);
        mContext.startActivity(intent);
    }

}
