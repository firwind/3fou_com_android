package com.zhiyicx.thinksnsplus.modules.third_platform.bind;

import android.os.CountDownTimer;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.ThridInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.RegisterClient;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.VertifyCodeRepository;
import com.zhiyicx.thinksnsplus.service.backgroundtask.BackgroundTaskManager;

import javax.inject.Inject;

import rx.Subscription;

import static com.zhiyicx.thinksnsplus.modules.register.RegisterPresenter.S_TO_MS_SPACING;
import static com.zhiyicx.thinksnsplus.modules.settings.bind.AccountBindPresenter.SNS_TIME;

/**
 * @author Catherine
 * @describe
 * @date 2017/7/31
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class BindOldAccountPresenter extends BasePresenter<BindOldAccountContract.View>
        implements BindOldAccountContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    AuthRepository mAuthRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    WalletBeanGreenDaoImpl mWalletBeanGreenDao;
    @Inject
    BillRepository mWalletRepository;
    @Inject
    VertifyCodeRepository mVertifyCodeRepository;

    private int mTimeOut = SNS_TIME;

    @Inject
    public BindOldAccountPresenter(
            BindOldAccountContract.View rootView) {
        super(rootView);
    }

    @Override
    public void checkName(ThridInfoBean thridInfoBean, String name) {
        if (checkUsername(name)) {
            return;
        }
    }

    @Override
    public void bindAccount(ThridInfoBean thridInfoBean, String login, String password, String phone, String verifiable_code, String user_code) {
        mRootView.setLogining();
        Subscription subscribe = mUserInfoRepository.bindWithInput(thridInfoBean.getProvider(), thridInfoBean.getAccess_token(), login, password, phone, verifiable_code, RegisterClient.REGITER_TYPE_SMS, user_code,thridInfoBean.getName())
                .subscribe(new BaseSubscribeForV2<AuthBean>() {
                    @Override
                    protected void onSuccess(AuthBean data) {
                        loginSuccess(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        // 登录失败
                        mRootView.setLoginState(false);
                        mRootView.showErrorTips(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.showErrorTips(mContext.getString(R.string.err_net_not_work));
                        mRootView.setLoginState(false);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void getVertifyCode(String phone) {
        if (checkPhone(phone)) {
            return;
        }
        mRootView.setVerifyCodeBtEnabled(false);
        mRootView.setVerifyCodeLoading(true);
        Subscription subscription = mVertifyCodeRepository.getNonMemberVertifyCode(phone)
                .subscribe(new BaseSubscribeForV2<Object>() {

                    @Override
                    protected void onSuccess(Object data) {
                        mRootView.hideLoading();//隐藏loading
                        timer.start();//开始倒计时
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        throwable.printStackTrace();
                        mRootView.showMessage(mContext.getString(R.string.err_net_not_work));
                        mRootView.setVerifyCodeBtEnabled(true);
                        mRootView.setVerifyCodeLoading(false);
                    }
                });
        // 代表检测成功
        mRootView.showMessage("");
        addSubscrebe(subscription);

    }

    private void loginSuccess(AuthBean data) {
        mAuthRepository.clearAuthBean();
        mAuthRepository.clearThridAuth();
        // 登录成功跳转
        mAuthRepository.saveAuthBean(data);// 保存auth信息
        // IM 登录 需要 token ,所以需要先保存登录信息
        handleIMLogin();
        // 钱包信息我也不知道在哪儿获取
        mWalletRepository.getWalletConfigWhenStart(Long.parseLong(data.getUser_id() + ""));
        mUserInfoBeanGreenDao.insertOrReplace(data.getUser());
        if (data.getUser().getWallet() != null) {
            mWalletBeanGreenDao.insertOrReplace(data.getUser().getWallet());
        }
        mRootView.setLoginState(true);
    }

    private void handleIMLogin() {
        BackgroundTaskManager.getInstance(mContext).addBackgroundRequestTask(new BackgroundRequestTaskBean(BackgroundTaskRequestMethodConfig
                .GET_IM_INFO));
    }

    /**
     * 检测手机号码是否正确
     */
    private boolean checkPhone(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showMessage(mContext.getString(R.string.phone_number_toast_hint));
            return true;
        }
        return false;
    }

    CountDownTimer timer = new CountDownTimer(mTimeOut, S_TO_MS_SPACING) {

        @Override
        public void onTick(long millisUntilFinished) {
            mRootView.setVerifyCodeBtText(millisUntilFinished / S_TO_MS_SPACING + mContext.getString(R.string.seconds));//显示倒数的秒速
        }

        @Override
        public void onFinish() {
            mRootView.setVerifyCodeBtEnabled(true);//恢复初始化状态
            mRootView.setVerifyCodeBtText(mContext.getString(R.string.send_vertify_code));
        }
    };

    /**
     * 检查用户名是否小于最小长度,不能以数字开头
     *
     * @param name
     * @return
     */
    private boolean checkUsername(String name) {
        if (!RegexUtils.isUsernameLength(name, mContext.getResources().getInteger(R.integer.username_min_byte_length), mContext.getResources()
                .getInteger(R.integer.username_max_byte_length))) {
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_hint));
            return true;
        }
        if (RegexUtils.isUsernameNoNumberStart(name)) {// 数字开头
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_not_number_start_hint));
            return true;
        }
        if (!RegexUtils.isUsername(name)) {// 用户名只能包含数字、字母和下划线
            mRootView.showErrorTips(mContext.getString(R.string.username_toast_not_symbol_hint));
            return true;
        }
        return false;
    }

}
