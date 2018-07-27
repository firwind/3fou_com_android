package com.zhiyicx.thinksnsplus.modules.settings.password;
import android.content.Context;
import android.content.Intent;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.common.mvp.BasePresenter;

/**
 * Created by zl on 2018/7/27 0027.
 * 密码管理
 */

public class PassWordManagerActivity extends TSActivity<BasePresenter,PassWordManagerFragment>{

    public static void startPassWordManagerActivity(Context context){
        Intent intent = new Intent(context,PassWordManagerActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected PassWordManagerFragment getFragment() {
            return PassWordManagerFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }
}
