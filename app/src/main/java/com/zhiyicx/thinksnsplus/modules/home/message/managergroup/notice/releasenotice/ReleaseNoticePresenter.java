package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/20 11:58
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Bundle;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.EmptySubscribe;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReleaseNoticePresenter extends AppBasePresenter<ReleaseNoticeContract.View> implements ReleaseNoticeContract.Presenter {

    @Inject
    BaseMessageRepository mBaseMessageRepository;

    @Inject
    public ReleaseNoticePresenter(ReleaseNoticeContract.View rootView) {
        super(rootView);
    }


    @Override
    public void releaseNotice(String group_id, String title, String content, String author) {
        Subscription subscription = mBaseMessageRepository.releaseNotice(group_id, title, content, author)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("发布中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        // 成功后重置页面
                        mRootView.dismissSnackBar();
                        Observable.empty()
                                .observeOn(Schedulers.io())
                                .subscribe(new EmptySubscribe<Object>() {
                                    @Override
                                    public void onCompleted() {
                                        try {
                                            EMClient.getInstance().groupManager().updateGroupAnnouncement(group_id, content);//更新群公告
                                        } catch (HyphenateException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        mRootView.relaseSuccess();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
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
            mRootView.setUserInfo(userInfoBean);
        }
    }


}
