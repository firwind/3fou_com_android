package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * author: huwenyong
 * date: 2018/6/28 19:29
 * description:
 * version:
 */

public class MarketActivity extends TSActivity<MarketPresenter,MarketFragment>{

    public static void startMarketActivity(Context mContext){
        Intent intent = new Intent(mContext,MarketActivity.class);
        mContext.startActivity(intent);
    }

    @Override
    protected MarketFragment getFragment() {
        return MarketFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerMarketComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .marketPresenterMoudle(new MarketPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }
}
