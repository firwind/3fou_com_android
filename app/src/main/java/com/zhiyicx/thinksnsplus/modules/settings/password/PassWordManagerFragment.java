package com.zhiyicx.thinksnsplus.modules.settings.password;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.PayPassWordActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by zl on 2018/7/27 0027.
 */

public class PassWordManagerFragment extends TSFragment {

    public static PassWordManagerFragment newInstance() {
        PassWordManagerFragment fragment = new PassWordManagerFragment();
        return fragment;
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.password_manager);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_password_manager;
    }

    @OnClick({R.id.bt_change_password, R.id.bt_pay_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_change_password://修改密码
                startActivity(new Intent(getContext(), ChangePasswordActivity.class));
                break;
            case R.id.bt_pay_password://支付密码
                startActivity(new Intent(getContext(), PayPassWordActivity.class));
                break;
        }
    }
}
