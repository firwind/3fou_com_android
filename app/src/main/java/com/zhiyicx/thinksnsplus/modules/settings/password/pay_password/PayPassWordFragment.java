package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.settings.password.pay_password.setting_paypassword.SettingPayPasswordActivity.startSettingPayPasswordActivity;

/**
 * Created by zl on 2018/7/27 0027.
 */

public class PayPassWordFragment extends TSFragment {

    Unbinder unbinder;

    public static PayPassWordFragment newInstance() {
        PayPassWordFragment fragment = new PayPassWordFragment();
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
        return getString(R.string.password_pay);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_pay_password;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_change_pay_password, R.id.bt_forget_pay_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_change_pay_password:
                startSettingPayPasswordActivity(getContext());
                break;
            case R.id.bt_forget_pay_password:
                FindPasswordActivity.startSettingPayPasswordActivity(getContext());
                break;
        }
    }
}
