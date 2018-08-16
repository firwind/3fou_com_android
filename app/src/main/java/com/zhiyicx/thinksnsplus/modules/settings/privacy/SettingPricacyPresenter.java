package com.zhiyicx.thinksnsplus.modules.settings.privacy;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/14 0014
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;

import javax.inject.Inject;

public class SettingPricacyPresenter extends BasePresenter<SettingPricacyContract.View> implements SettingPricacyContract.Presenter{
    @Inject
    BaseFriendsRepository mBaseFriendsRepository;
    @Inject
    protected AuthRepository mAuthRepository;
    @Inject
    public SettingPricacyPresenter(SettingPricacyContract.View rootView) {
        super(rootView);
    }

    @Override
    public void settingAddFriendWay(int setState) {
        mBaseFriendsRepository.settingAddFriends(setState)
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage("设置成功");
                        UserInfoBean userInfoBean = AppApplication.getmCurrentLoginAuth().getUser();
                        userInfoBean.setFriends_set(setState);
                        mAuthRepository.saveAuthBean(AppApplication.getmCurrentLoginAuth());
                        mRootView.settingSuccess(setState);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showMessage(e.getMessage());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showMessage(message);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
    }
}
