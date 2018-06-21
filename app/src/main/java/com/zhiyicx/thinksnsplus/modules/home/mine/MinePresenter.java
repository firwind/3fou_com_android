package com.zhiyicx.thinksnsplus.modules.home.mine;

import android.os.Bundle;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.AuthBean;
import com.zhiyicx.thinksnsplus.data.beans.FlushMessages;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.WalletBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.WalletBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SystemRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/9
 * @contact email:450127106@qq.com
 */
@FragmentScoped
public class MinePresenter extends AppBasePresenter<MineContract.View> implements MineContract.Presenter {

    FlushMessageBeanGreenDaoImpl mFlushMessageBeanGreenDao;

    UserInfoRepository mUserInfoRepository;

    UserCertificationInfoGreenDaoImpl mUserCertificationInfoGreenDao;
    private Subscription mCertificationSub;
    private Subscription mUserinfoSub;
    private Subscription mNewMessageSub;

    @Inject
    public MinePresenter(MineContract.View rootView,FlushMessageBeanGreenDaoImpl flushMessageBeanGreenDao,
    UserInfoRepository userInfoRepository,UserCertificationInfoGreenDaoImpl userCertificationInfoGreenDao) {
        super(rootView);
        this.mFlushMessageBeanGreenDao = flushMessageBeanGreenDao;
        this.mUserInfoRepository = userInfoRepository;
        this.mUserCertificationInfoGreenDao = userCertificationInfoGreenDao;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void getUserInfoFromDB() {
        if (mUserInfoBeanGreenDao == null) {
            return;
        }
        // 尝试从数据库获取当前用户的信息
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            WalletBean walletBean = mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault());
            if (walletBean != null) {
                int ratio = mSystemRepository.getBootstrappersInfoFromLocal().getWallet_ratio();
///                    walletBean.setBalance(walletBean.getBalance() * (ratio / MONEY_UNIT));
                userInfoBean.setWallet(walletBean);
            }
            mRootView.setUserInfo(userInfoBean);
        }

    }

    /**
     * 获取最新粉丝数量
     */
    @Override
    public void updateUserNewMessage() {
        if (mNewMessageSub != null && !mNewMessageSub.isUnsubscribed()) {
            mNewMessageSub.unsubscribe();
        }
        mNewMessageSub = mUserInfoRepository.getUserAppendFollowerCount()
                .subscribe(new BaseSubscribeForV2<UserFollowerCountBean>() {
                    @Override
                    protected void onSuccess(UserFollowerCountBean data) {
                        setFollowFansCount(data);

                    }
                });
        addSubscrebe(mNewMessageSub);
    }

    /**
     * 用户信息在后台更新后，在该处进行刷新，这儿获取的是自己的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void upDataUserInfo(List<UserInfoBean> data) {
        Subscription subscribe = rx.Observable.just(data)
                .observeOn(Schedulers.io())
                .map(userInfoBeans -> {
                    if (userInfoBeans != null) {
                        for (UserInfoBean userInfoBean : userInfoBeans) {
                            if (userInfoBean.getUser_id() == AppApplication.getMyUserIdWithdefault()) {
                                userInfoBean.setWallet(mWalletBeanGreenDao.getSingleDataFromCacheByUserId(AppApplication.getMyUserIdWithdefault()));
                                return userInfoBean;
                            }
                        }
                    }
                    return null;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userInfoBean -> {
                    if (userInfoBean != null) {
                        mRootView.setUserInfo(userInfoBean);
                    }
                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);

    }

    /**
     * 用户信息在后台更新后，在该处进行刷新，这儿获取的是自己的用户信息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USERINFO_UPDATE)
    public void updateUserHeadPic(boolean isShow) {
        getUserInfoFromDB();
    }

    /**
     * 更新粉丝数量、系統消息
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_SET_MINE_FANS_TIP_VISABLE)
    public void setFollowFansCount(UserFollowerCountBean data) {
        int followingCount = data == null || data.getUser() == null ? 0 : data.getUser().getFollowing();
        int firendsCount = data == null || data.getUser() == null ? 0 : data.getUser().getMutual();
        mRootView.setNewFollowTip(followingCount);
        mRootView.setNewFriendsTip(firendsCount);
        // 更新底部红点
        boolean isShowMessagTip = followingCount > 0 || firendsCount > 0;
        EventBus.getDefault().post(isShowMessagTip, EventBusTagConfig.EVENT_IM_SET_MINE_TIP_VISABLE);
    }

    /**
     * 更新用户信息
     */
    @Override
    public void updateUserInfo() {
        if (mUserInfoBeanGreenDao == null) {
            return;
        }
        if (mUserinfoSub != null && !mUserinfoSub.isUnsubscribed()) {
            mUserinfoSub.unsubscribe();
        }
        mUserinfoSub = mUserInfoRepository.getCurrentLoginUserInfo()
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(data.getUser_id()));
                        if (!TextUtils.isEmpty(userInfoBean.getLocalAvatar())) {
                            data.setAvatar(userInfoBean.getLocalAvatar());
                        }
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if (data.getWallet() != null) {
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.setUserInfo(data);
                    }
                });
        addSubscrebe(mUserinfoSub);
    }

    @Override
    public void getCertificationInfo() {
        if (mUserInfoBeanGreenDao == null) {
            return;
        }
        if (mCertificationSub != null && !mCertificationSub.isUnsubscribed()) {
            mCertificationSub.unsubscribe();
        }
        mCertificationSub = mUserInfoRepository.getCertificationInfo()
                .compose(mSchedulersTransformer)
                .subscribe(new BaseSubscribeForV2<UserCertificationInfo>() {

                    @Override
                    protected void onSuccess(UserCertificationInfo data) {
                        mUserCertificationInfoGreenDao.insertOrReplace(data);
                        mRootView.updateCertification(data);
                    }
                });
        addSubscrebe(mCertificationSub);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS)
    public void updateCertification(Bundle bundle) {
        getCertificationInfo();
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_CERTIFICATION_SUCCESS)
    public void sendSuccess(Bundle bundle) {
        getCertificationInfo();
    }


}
