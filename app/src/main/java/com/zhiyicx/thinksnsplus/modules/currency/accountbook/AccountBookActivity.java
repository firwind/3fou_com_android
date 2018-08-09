package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.i.IntentKey;

/**
 * author: huwenyong
 * date: 2018/7/23 9:24
 * description:
 * version:
 */

public class AccountBookActivity extends TSActivity{
    @Override
    protected Fragment getFragment() {
        return AccountBookFragment.newInstance(getIntent().getStringExtra(IntentKey.CURRENCY_IN_MARKET));
    }

    @Override
    protected void componentInject() {

    }

    public static void startAccountBookActivity(Context mContext,String currency){
        Intent intent = new Intent(mContext,AccountBookActivity.class);
        intent.putExtra(IntentKey.CURRENCY_IN_MARKET,currency);
        mContext.startActivity(intent);
    }

}
