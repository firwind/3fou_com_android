package com.zhiyicx.thinksnsplus.modules.settings.password.pay_password;

import android.text.TextUtils;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.PasswordRepository;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/7/30 18:12
 * description:
 * version:
 */
@FragmentScoped
public class PayPasswordPresenter extends AppBasePresenter<PayPasswordContract.View> implements PayPasswordContract.Presenter{

    @Inject
    public PayPasswordPresenter(PayPasswordContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getPayPasswordIsSetted() {
        mRootView.setPayPasswordIsSetted(
                mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault()).isPay_password());
    }

    @Override
    public UserInfoBean getCurrentUser() {
        return mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
    }


}
