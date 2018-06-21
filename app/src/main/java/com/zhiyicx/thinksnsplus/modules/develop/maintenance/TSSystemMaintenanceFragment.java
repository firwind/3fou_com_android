package com.zhiyicx.thinksnsplus.modules.develop.maintenance;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.thinksnsplus.R;

/**
 * @Describe 开发提示页面
 * @Author zl
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class TSSystemMaintenanceFragment extends TSFragment {


    public static TSSystemMaintenanceFragment newInstance() {
        return new TSSystemMaintenanceFragment();
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_ts_system_maintenance;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        ActivityHandler.getInstance().AppExit();
    }


}
