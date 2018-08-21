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
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.AuthRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import javax.inject.Inject;

public class SettingPrivacyPresenter extends AppBasePresenter<SettingPrivacyContract.View> implements SettingPrivacyContract.Presenter{

    @Inject
    ChatInfoRepository mChatInfoRepository;

    @Inject
    public SettingPrivacyPresenter(SettingPrivacyContract.View rootView) {
        super(rootView);
    }

    public UserInfoBean getCurrentUser(){
        return mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault()));
    }

    @Override
    public void settingAddFriendOrGroupWay(int setState) {
        addSubscrebe( (null == mRootView.getChatGroupBean()?mChatInfoRepository.settingAddFriends(setState):
        mChatInfoRepository.setGroupPrivacy(mRootView.getChatGroupBean().getId(),setState))
        .subscribe(new BaseSubscriberV3<String>(mRootView){
            @Override
            protected void onSuccess(String data) {
                super.onSuccess(data);
                if(null == mRootView.getChatGroupBean()){//单人隐私设置
                    /*UserInfoBean userInfoBean = AppApplication.getmCurrentLoginAuth().getUser();
                    userInfoBean.setFriends_set(setState);
                    mAuthRepository.saveAuthBean(AppApplication.getmCurrentLoginAuth());*/
                    UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault()));
                    userInfoBean.setFriends_set(setState);
                    mUserInfoBeanGreenDao.insertOrReplace(userInfoBean);
                }else {

                }
                mRootView.showSnackSuccessMessage("设置成功");
                mRootView.settingSuccess(setState);
            }
        }));
    }
}
