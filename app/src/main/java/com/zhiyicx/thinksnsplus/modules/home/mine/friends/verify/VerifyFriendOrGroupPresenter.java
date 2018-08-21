package com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/16 0016
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class VerifyFriendOrGroupPresenter extends BasePresenter<VerifyFriendOrGroupContract.View> implements VerifyFriendOrGroupContract.Presenter {

    @Inject
    ChatInfoRepository mChatInfoRepository;

    @Inject
    public VerifyFriendOrGroupPresenter(VerifyFriendOrGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void addFriendOrGroup(String id, String information) {
        addSubscrebe((mRootView.isGroupVerify() ? mChatInfoRepository.verifyEnterGroup(id, information,true) :
                mChatInfoRepository.verifyAddFriend(id, information))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        mRootView.showSnackSuccessMessage("已提交申请！");
                    }
                }));
    }
}
