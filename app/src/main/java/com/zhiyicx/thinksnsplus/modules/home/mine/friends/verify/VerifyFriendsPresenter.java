package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.support.v4.app.Fragment;

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class VerifyFriendsPresenter extends BasePresenter<VerifyFriendsContract.View> implements VerifyFriendsContract.Presenter{

    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    public VerifyFriendsPresenter(VerifyFriendsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addFriend(String user_id, String information) {
        mBaseFriendsRepository.addFriend(user_id,information)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage("已提交申请！");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }
                });
    }
}
