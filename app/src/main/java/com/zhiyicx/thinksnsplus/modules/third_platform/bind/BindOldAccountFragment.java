package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.button.LoadingButton;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.third_platform.choose_bind.ChooseBindActivity;
import com.zhiyicx.thinksnsplus.modules.usertag.EditUserTagFragment;
import com.zhiyicx.thinksnsplus.modules.usertag.TagFrom;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.config.ConstantConfig.MOBILE_PHONE_NUMBER_LENGHT;

/**
 * @author Catherine
 * @describe 绑定已有账号失败的情况下，直接返回到完善资料页面
 * 修改者：张炼
 * 修改内容：将原来绑定已有账号改为绑定手机，该手机不能为已有账号，必须是新账号
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */

public class BindOldAccountFragment extends TSFragment<BindOldAccountContract.Presenter>
        implements BindOldAccountContract.View {

    @BindView(R.id.et_login_phone)
    EditText mEtLoginName;
    @BindView(R.id.et_login_password)
    EditText mEtLoginPassword;
    @BindView(R.id.bt_login_login)
    LoadingButton mBtLoginLogin;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.et_complete_input)
    AppCompatAutoCompleteTextView mEtCompleteInput;
    @BindView(R.id.et_phone)
    DeleteEditText mEtPhone;
    @BindView(R.id.bt_send_verify_code)
    TextView mBtSendVerifyCode;
    @BindView(R.id.et_verify_code)
    DeleteEditText mEtVerifyCode;
    @BindView(R.id.et_sure_password)
    PasswordEditText mEtSurePassword;
    @BindView(R.id.et_invitation_code)
    DeleteEditText mEtInvitationCode;
    @BindView(R.id.iv_verify_loading)
    ImageView mIvVerifyLoading;
    Unbinder unbinder;
    private Animatable mVerifyAnimationDrawable;
    private boolean mNameEdited;
    private boolean mIsPhoneEdited;
    private boolean mIsVerifyEdited;
    private boolean mIsPasswordEdited;
    private boolean mIsAffirmPasswordEdited;
    private boolean mIsInvitationCodeEdited;
    private boolean mIsVerifyCodeEnable = true;
    private ThridInfoBean mThridInfoBean;

    public static BindOldAccountFragment instance(Bundle bundle) {
        BindOldAccountFragment fragment = new BindOldAccountFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mThridInfoBean = getArguments().getParcelable(ChooseBindActivity.BUNDLE_THIRD_INFO);
        }
    }

    @Override
    protected void initView(View rootView) {
        mEtCompleteInput.setVisibility(View.GONE);
        mEtLoginName.setVisibility(View.VISIBLE);
        mBtLoginLogin.setText(getString(R.string.third_platform_bind_confirm));
        mVerifyAnimationDrawable = (Animatable) mIvVerifyLoading.getDrawable();
        mEtLoginName.setText(mThridInfoBean.getName());
    }

    @Override
    protected void initData() {
        initListener();
    }

    private void initListener() {
        // 用户名输入框观察
        RxTextView.textChanges(mEtLoginName)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mNameEdited = !TextUtils.isEmpty(charSequence.toString());
                    mThridInfoBean.setName(mEtLoginName.getText().toString());
                    setConfirmEnable();
                });
        // 手机号码输入框观察
        RxTextView.textChanges(mEtPhone)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    if (mIsVerifyCodeEnable) {
                        mBtSendVerifyCode.setEnabled(charSequence.length() == MOBILE_PHONE_NUMBER_LENGHT);
                    }
                    mIsPhoneEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 验证码
        RxTextView.textChanges(mEtVerifyCode)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsVerifyEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });

//        // 密码
//        RxTextView.textChanges(mEtSurePassword)
//                .compose(this.bindToLifecycle())
//                .subscribe(charSequence -> {
//                    mIsAffirmPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
//                    setConfirmEnable();
//                });
        // 密码输入框观察
        RxTextView.textChanges(mEtLoginPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    mIsPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
//        // 邀请码验证观察
//        RxTextView.textChanges(mEtInvitationCode)
//                .compose(this.bindToLifecycle())
//                .subscribe(charSequence -> {
//                    mIsInvitationCodeEdited = !TextUtils.isEmpty(charSequence.toString());
//                    setConfirmEnable();
//                });
        // 点击登录按钮
        RxView.clicks(mBtLoginLogin)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .compose(mRxPermissions.ensure(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE))
                .subscribe(aBoolean -> {
                    if (aBoolean) {// 获取到了权限
//                        if (!mEtLoginPassword.getText().toString().equals(mEtSurePassword.getText().toString())) {
//                            showErrorTips(getString(R.string.password_diffrent));
//                            return;
//                        }
                        if (mThridInfoBean != null) {
                            mPresenter.bindAccount(mThridInfoBean, mEtLoginName.getText().toString(),
                                    mEtLoginPassword.getText().toString(),
                                    mEtPhone.getText().toString(),
                                    mEtVerifyCode.getText().toString(),
                                    mEtInvitationCode.getText().toString());
                        }

                    } else {// 拒绝权限，但是可以再次请求
                        showErrorTips(getString(R.string.permisson_refused));
                    }
                });
        // 点击发送验证码
        RxView.clicks(mBtSendVerifyCode)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mPresenter.getVertifyCode(mEtPhone.getText().toString().trim());
                });

    }


    @Override
    public void setLogining() {
        mBtLoginLogin.handleAnimation(true);
        mBtLoginLogin.setEnabled(false);
    }

    @Override
    public void setVerifyCodeBtEnabled(boolean isEnable) {
        mIsVerifyCodeEnable = isEnable;
        mBtSendVerifyCode.setEnabled(isEnable);
    }

    @Override
    public void setVerifyCodeLoading(boolean isEnable) {
        if (isEnable) {
            mIvVerifyLoading.setVisibility(View.VISIBLE);
            mBtSendVerifyCode.setVisibility(View.INVISIBLE);
            mVerifyAnimationDrawable.start();
        } else {
            mVerifyAnimationDrawable.stop();
            mIvVerifyLoading.setVisibility(View.INVISIBLE);
            mBtSendVerifyCode.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setVerifyCodeBtText(String text) {
        mBtSendVerifyCode.setText(text);
    }

    @Override
    public void setLoginState(boolean loginState) {
        mBtLoginLogin.handleAnimation(false);
        mBtLoginLogin.setEnabled(true);
        if (loginState) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
            mTvErrorTip.setText("");
            mEtLoginPassword.setText("");
            mEtCompleteInput.setText("");
            mEtInvitationCode.setText("");
            mEtVerifyCode.setText("");
            mEtPhone.setText("");
//            mEtSurePassword.setText("");
            mEtCompleteInput.requestFocus();
            DeviceUtils.hideSoftKeyboard(getContext(), mEtLoginPassword);
            goHome();
        }
    }
    @Override
    public void goHome() {
//        startActivity(new Intent(getActivity(), HomeActivity.class));
//        getActivity().finish();
        ActivityHandler.getInstance().finishAllActivityEcepteCurrent();// 清除 homeAcitivity 重新加载
//        boolean needCompleteUserInfo = mSystemConfigBean.getRegisterSettings() == null
//                || mSystemConfigBean.getRegisterSettings().isCompleteData() || "need".equals(mSystemConfigBean.getRegisterSettings().getFixed());
//        if (needCompleteUserInfo) {
            EditUserTagFragment.startToEditTagActivity(getActivity(), TagFrom.REGISTER, null);
//        } else {
//            startActivity(new Intent(getActivity(), HomeActivity.class));
//        }
        getActivity().finish();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_bind_old_account;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.third_platform_bind_phone);
    }

    @Override
    public void showErrorTips(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    /**
     * 设置登录按钮是否可点击
     */
    private void setConfirmEnable() {
        if (mNameEdited && mIsPasswordEdited && mIsPhoneEdited && mIsVerifyEdited /*&& mIsAffirmPasswordEdited*/) {
            mBtLoginLogin.setEnabled(true);
        } else {
            mBtLoginLogin.setEnabled(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void showMessage(String message) {
        if (TextUtils.isEmpty(message)) {
            mTvErrorTip.setVisibility(View.INVISIBLE);
        } else {
            mTvErrorTip.setVisibility(View.VISIBLE);
            mTvErrorTip.setText(message);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
