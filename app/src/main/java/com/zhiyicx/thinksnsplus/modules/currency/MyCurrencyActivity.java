package com.zhiyicx.thinksnsplus.modules.currency;

import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.base.BaseActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public class MyCurrencyActivity extends TSActivity<MyCurrencyPresenter,MyCurrencyFragment> {

    @Override
    protected MyCurrencyFragment getFragment() {
        return MyCurrencyFragment.newInstance();
    }

    @Override
    protected void componentInject() {
        DaggerMyCurrencyComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myCurrencyPresenterMoudle(new MyCurrencyPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }


    public static void startMyCurrencyActivity(Context mContext){
        Intent intent = new Intent(mContext,MyCurrencyActivity.class);
        mContext.startActivity(intent);
    }

}
