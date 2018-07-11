package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/11
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Bundle;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

public class PaySuccessFragment extends TSFragment{

    public static final PaySuccessFragment newInstance() {
        PaySuccessFragment fragment = new PaySuccessFragment();
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
        return R.layout.fragment_pay_success;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.pay_alert_success);
    }
}
