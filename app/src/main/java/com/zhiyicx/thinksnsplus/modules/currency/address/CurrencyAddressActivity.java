package com.zhiyicx.thinksnsplus.modules.currency.address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/7/20 9:55
 * description:
 * version:
 */

public class CurrencyAddressActivity extends TSActivity<CurrencyAddressPresenter,CurrencyAddressFragment>{
    @Override
    protected CurrencyAddressFragment getFragment() {
        return CurrencyAddressFragment.newInstance(getIntent().getBooleanExtra(IntentKey.IS_SELECT,false));
    }

    @Override
    protected void componentInject() {
        DaggerCurrencyAddressComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .currencyAddressPresenterMoudle(new CurrencyAddressPresenterMoudle(mContanierFragment))
                .build()
                .inject(this);
    }


    public static void startCurrencyAddressActivityForResult(Activity mActivity, boolean isSelect,int requestCode){

        Intent intent = new Intent(mActivity,CurrencyAddressActivity.class);
        intent.putExtra(IntentKey.IS_SELECT,isSelect);
        mActivity.startActivityForResult(intent,requestCode);

    }

}
