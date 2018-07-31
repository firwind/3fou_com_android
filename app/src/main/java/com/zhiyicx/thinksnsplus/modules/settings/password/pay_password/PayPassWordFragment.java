package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.modules.password.changepassword.ChangePasswordActivity;
import com.zhiyicx.thinksnsplus.modules.password.findpassword.FindPasswordActivity;
import com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_DATA;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_STATE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindActivity.BUNDLE_BIND_TYPE;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindFragment.DEAL_TYPE_PHONE;

/**
 * Created by zl on 2018/7/27 0027.
 */

public class PayPassWordFragment extends TSFragment<PayPasswordContract.Presenter> implements PayPasswordContract.View{

    @BindView(R.id.bt_change_pay_password)
    CombinationButton mBtChangePayPwd;
    @BindView(R.id.bt_forget_pay_password)
    CombinationButton mBtForgetPayPwd;

    @Inject
    public PayPasswordPresenter mPresenter;

    public static PayPassWordFragment newInstance() {
        PayPassWordFragment fragment = new PayPassWordFragment();
        return fragment;
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
        DaggerPayPasswordComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .payPasswordModule(new PayPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getPayPasswordIsSetted();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_pay_password;
    }


    @OnClick({R.id.bt_change_pay_password, R.id.bt_forget_pay_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_change_pay_password:
                if(!TextUtils.isEmpty(mPresenter.getCurrentUser().getPhone()) ){
                    ChangePasswordActivity.startChangePayPasswordActivity(mActivity);
                }else {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("提示")
                            .setMessage("请先绑定手机号！")
                            .setPositiveButton("去绑定", (dialog, which) -> {
                                Intent intent = new Intent(getActivity(), AccountBindActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(BUNDLE_BIND_TYPE, DEAL_TYPE_PHONE);
                                bundle.putBoolean(BUNDLE_BIND_STATE, !TextUtils.isEmpty(mPresenter.getCurrentUser().getPhone()));
                                bundle.putParcelable(BUNDLE_BIND_DATA, mPresenter.getCurrentUser());
                                intent.putExtras(bundle);
                                startActivity(intent);
                            })
                            .create()
                            .show();
                }
                break;
            case R.id.bt_forget_pay_password:
                FindPasswordActivity.startSettingPayPasswordActivity(getContext());
                break;
        }
    }

    @Override
    public void setPayPasswordIsSetted(boolean isSetted) {
        mBtChangePayPwd.setLeftText(isSetted?"修改支付密码":"设置支付密码");
    }
}
