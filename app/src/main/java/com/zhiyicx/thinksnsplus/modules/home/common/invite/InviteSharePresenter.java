package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyWithoutViewImpl;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:36
 *     desc :
 *     version : 1.0
 * <pre>
 */
@FragmentScoped
public class InviteSharePresenter extends AppBasePresenter<InviteShareContract.View> implements InviteShareContract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;
    @Inject
    public UserInfoRepository mUserRepository;

    private ShareContent mShareContent;

    @Inject
    public InviteSharePresenter(InviteShareContract.View rootView) {
        super(rootView);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onDestroy() {

    }

    /**
     * 获取用户头像
     * @return
     */
    @Override
    public String getUserAvatar(){
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao
                .getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        return TextUtils.isEmpty(userInfoBean.getLocalAvatar()) ? null : userInfoBean.getLocalAvatar();
    }

    @Override
    public void getInviteCode() {
        addSubscrebe(mUserRepository.getInviteCode().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BaseSubscribeForV2<InviteAndQrcode>() {
            @Override
            protected void onSuccess(InviteAndQrcode data) {
                mRootView.setInviteAndQrCode(data);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRootView.showSnackErrorMessage(e.getMessage());
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mRootView.closeLoadingView();
            }
        }));
    }

    @Override
    public void inviteShare(View v,SHARE_MEDIA shareMedia) {

        if(null == mShareContent){
            mShareContent = new ShareContent();
            mShareContent.setBitmap(ConvertUtils.view2Bitmap(v));
        }
        ((UmengSharePolicyWithoutViewImpl) mSharePolicy).setOnShareCallbackListener(this);
        mSharePolicy.setShareContent(mShareContent);
        ((UmengSharePolicyWithoutViewImpl) mSharePolicy).startShare(shareMedia);
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage("分享成功");
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(throwable.getMessage());
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage("分享取消");
    }

}
