package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword;

import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

/**
 * Created by Administrator on 2018/7/27 0027.
 */

public class SettingPayPasswordFragment extends TSFragment<SettingPayPasswordContract.Presenter> implements SettingPayPasswordContract.View{

    public static SettingPayPasswordFragment newInstance(){
        SettingPayPasswordFragment fragment = new SettingPayPasswordFragment();
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_setting_pay_password;
    }
}
