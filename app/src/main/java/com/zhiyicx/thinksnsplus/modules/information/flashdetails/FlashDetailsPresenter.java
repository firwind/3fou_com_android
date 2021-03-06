package com.zhiyicx.thinksnsplus.modules.information.flashdetails;
/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.view.View;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyWithoutViewImpl;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;


import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class FlashDetailsPresenter extends AppBasePresenter<FlashDetailsContract.View> implements FlashDetailsContract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;
    private ShareContent mShareContent;
    @Inject
    public UserInfoRepository mUserRepository;
    @Inject
    public FlashDetailsPresenter(FlashDetailsContract.View rootView) {
        super(rootView);
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

    @Override
    public void inviteShare(View v, SHARE_MEDIA shareMedia) {
        if(null == mShareContent){
            mShareContent = new ShareContent();
            mShareContent.setBitmap(ConvertUtils.view2Bitmap(v));
        }
        ((UmengSharePolicyWithoutViewImpl) mSharePolicy).setOnShareCallbackListener(this);
        mSharePolicy.setShareContent(mShareContent);
        ((UmengSharePolicyWithoutViewImpl) mSharePolicy).startShare(shareMedia);
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
}
