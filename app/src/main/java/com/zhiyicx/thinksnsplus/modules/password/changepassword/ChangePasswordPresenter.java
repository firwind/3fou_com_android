package com.zhiyicx.thinksnsplus.modules.password.changepassword;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/10
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class ChangePasswordPresenter extends AppBasePresenter<ChangePasswordContract.View> implements ChangePasswordContract.Presenter {

    @Inject
    PasswordRepository mPasswordRepository;
    @Inject
    AuthRepository mIAuthRepository;

    @Inject
    public ChangePasswordPresenter(ChangePasswordContract.View rootView) {
        super(rootView);
    }


    @Override
    public void onStart() {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword, String sureNessword) {

        if (!newPassword.equals(sureNessword)) {
            mRootView.showMessage(mContext.getString(R.string.password_diffrent));
            return;
        }

        Subscription changePasswordSub = null;
        BaseSubscribeForV2<Object> subscribeForV2 = new BaseSubscribeForV2<Object>() {
            @Override
            protected void onSuccess(Object data) {

                //如果是设置支付密码，则更新缓存
                if(!mRootView.isLoginPwdType() && !getPayPasswordIsSetted()){
                    UserInfoBean user = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
                    user.setPay_password(true);
                    mUserInfoBeanGreenDao.insertOrReplace(user);
                }

                mRootView.showSnackSuccessMessage(mContext.getString(R.string.change_success));
                mRootView.changePwdSuccess();
            }
            @Override
            protected void onFailure(String message, int code) {
                mRootView.dismissSnackBar();
                mRootView.showMessage(message);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRootView.dismissSnackBar();
                mRootView.showMessage("网络异常！");
            }
        };

        if(mRootView.isLoginPwdType()){
            if (checkPasswordLength(oldPassword, mContext.getString(R.string.old_password_toast_hint)) ||
                    checkPasswordLength(newPassword, mContext.getString(R.string.new_password_toast_hint)) ||
                    checkPasswordLength(sureNessword, mContext.getString(R.string.sure_new_password_toast_hint))) {
                return;
            }
            mRootView.showSnackLoadingMessage("正在处理......");
            changePasswordSub = mPasswordRepository.changePasswordV2(oldPassword, newPassword)
                    .subscribe(subscribeForV2);
        }else {//支付密码

            if(newPassword.length() != 6){
                mRootView.showMessage("请输入6位数字密码");
                return;
            }
            changePasswordSub = getPayPasswordIsSetted()?
                    mPasswordRepository.updatePayPassword(oldPassword,newPassword).subscribe(subscribeForV2):
                    mPasswordRepository.setPayPassword(newPassword).subscribe(subscribeForV2);

        }

        addSubscrebe(changePasswordSub);
    }

    @Override
    public void logOut() {
        mIAuthRepository.clearAuthBean();
        mIAuthRepository.clearThridAuth();
    }

    @Override
    public boolean getPayPasswordIsSetted() {
        return mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()).isPay_password();
    }

    /**
     * 检查密码是否是最小长度
     *
     * @param password
     * @return
     */
    private boolean checkPasswordLength(String password, String hint) {
        if (password.length() < mContext.getResources().getInteger(R.integer.password_min_length)) {
            mRootView.showMessage(hint);
            return true;
        }
        return false;
    }
}
