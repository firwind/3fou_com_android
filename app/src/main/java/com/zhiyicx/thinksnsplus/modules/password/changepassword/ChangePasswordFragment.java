package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.PasswordEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.utils.NotificationUtil;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
public class ChangePasswordFragment extends TSFragment<ChangePasswordContract.Presenter> implements ChangePasswordContract.View {


    @BindView(R.id.et_old_password)
    EditText mEtOldPassword;
    @BindView(R.id.et_new_password)
    EditText mEtNewPassword;
    @BindView(R.id.et_sure_new_password)
    PasswordEditText mEtSureNewPassword;
    @BindView(R.id.tv_error_tip)
    TextView mTvErrorTip;
    @BindView(R.id.ll_old_pwd)
    LinearLayout mLlOldPwd;

    private boolean isOldPasswordEdited;
    private boolean isNewPasswordEdited;
    private boolean isSureNewPasswordEdited;

    public static ChangePasswordFragment newInstance(int passwordType) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.PASSWORD_TYPE,passwordType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_change_password;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.change_password);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.change);
    }

    /**
     * toolbar右边点击
     */
    @Override
    protected void setRightClick() {
        mPresenter.changePassword(mEtOldPassword.getText().toString().trim()
                , mEtNewPassword.getText().toString().trim(), mEtSureNewPassword.getText().toString().trim());
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
    protected void initView(View rootView) {

        mLlOldPwd.setVisibility( (isLoginPwdType() || (!isLoginPwdType() && mPresenter.getPayPasswordIsSetted()))
                ? View.VISIBLE:View.GONE);

        //支付密码设置
        if(!isLoginPwdType()){
            mToolbarCenter.setText(mPresenter.getPayPasswordIsSetted()?"修改支付密码":"设置支付密码");
            InputFilter[] filter = new InputFilter[]{new InputFilter.LengthFilter(6)};
            mEtOldPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEtNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEtSureNewPassword.setInputType(InputType.TYPE_CLASS_NUMBER);
            mEtOldPassword.setFilters(filter);
            mEtNewPassword.setFilters(filter);
            mEtSureNewPassword.setFilters(filter);

            mEtNewPassword.setHint("请输入6位纯数字密码");
            mEtSureNewPassword.setHint("请输入与上面相同的密码");
        }


        // 旧密码观察
        RxTextView.textChanges(mEtOldPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isOldPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 新密码观察
        RxTextView.textChanges(mEtNewPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isNewPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
        // 确认新密码观察
        RxTextView.textChanges(mEtSureNewPassword)
                .compose(this.bindToLifecycle())
                .subscribe(charSequence -> {
                    isSureNewPasswordEdited = !TextUtils.isEmpty(charSequence.toString());
                    setConfirmEnable();
                });
    }

    @Override
    protected void initData() {
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

    /**
     * 更改按钮是否可用
     */
    private void setConfirmEnable() {
        if (isNewPasswordEdited && isSureNewPasswordEdited) {
            if( (isLoginPwdType() && isOldPasswordEdited) ||//登录密码
                    (!isLoginPwdType() && !mPresenter.getPayPasswordIsSetted()) ||//支付密码，并且没有设置支付密码
                    (!isLoginPwdType() && mPresenter.getPayPasswordIsSetted() && isOldPasswordEdited))//支付密码，已经设置过支付密码
                mToolbarRight.setEnabled(true);
        } else {
            mToolbarRight.setEnabled(false);
        }
    }

    /*@Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        if (prompt == Prompt.SUCCESS) {
            try {
                finsh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void changePwdSuccess() {
        if(isLoginPwdType()){
            mPresenter.logOut();
            NotificationUtil.cancelAllNotification(getContext());
            startActivity(new Intent(mActivity, LoginActivity.class));
        }else {
            mRootView.postDelayed(() -> mActivity.finish(),500);
        }
    }

    /**
     * 是否是登录密码的操作（登录密码/支付密码）
     * @return
     */
    @Override
    public boolean isLoginPwdType() {
        return getArguments().getInt(IntentKey.PASSWORD_TYPE,IntentKey.TYPE_PASSWORD_LOGIN)
                == IntentKey.TYPE_PASSWORD_LOGIN;
    }
}
