package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/7/5 11:23
 * description:
 * version:
 */

public class CurrencyKLineActivity extends TSActivity<CurrencyKLinePresenter, CurrencyKLineFragment> {

    public static void startActivity(Context mContext,MarketCurrencyBean marketCurrencyBean){
        Intent intent = new Intent(mContext,CurrencyKLineActivity.class);
        intent.putExtra(IntentKey.CURRENCY_IN_MARKET,marketCurrencyBean);
        mContext.startActivity(intent);
    }

    @Override
    protected CurrencyKLineFragment getFragment() {
        return CurrencyKLineFragment.newInstance(getIntent().getParcelableExtra(IntentKey.CURRENCY_IN_MARKET));
    }

    @Override
    protected void componentInject() {
        DaggerCurrencyKLineComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .currencyKLinePresenterMoudle(new CurrencyKLinePresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        mContanierFragment.onBackPressed();
    }
}
