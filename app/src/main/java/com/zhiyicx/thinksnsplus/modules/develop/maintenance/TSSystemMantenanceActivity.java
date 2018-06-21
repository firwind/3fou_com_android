package com.zhiyicx.thinksnsplus.modules.develop.maintenance;

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;

/**
 * @Describe 系统 503 维护提示页
 * @Author zl
 * @Date 2017/3/24
 * @Contact master.jungle68@gmail.com
 */

public class TSSystemMantenanceActivity extends TSActivity {


    @Override
    protected Fragment getFragment() {
        return TSSystemMaintenanceFragment.newInstance();
    }

    @Override
    protected void componentInject() {

    }

    @Override
    public void onBackPressed() {
        ((TSSystemMaintenanceFragment) mContanierFragment).onBackPressed();
    }
}
