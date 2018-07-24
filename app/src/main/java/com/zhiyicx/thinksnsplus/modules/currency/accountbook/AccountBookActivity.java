package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * author: huwenyong
 * date: 2018/7/23 9:24
 * description:
 * version:
 */

public class AccountBookActivity extends TSActivity{
    @Override
    protected Fragment getFragment() {
        return AccountBookFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }

    public static void startActivity(Context mContext){
        mContext.startActivity(new Intent(mContext,AccountBookActivity.class));
    }

}
