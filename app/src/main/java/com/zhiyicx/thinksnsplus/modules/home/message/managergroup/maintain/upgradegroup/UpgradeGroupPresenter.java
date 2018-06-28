package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class UpgradeGroupPresenter extends AppBasePresenter<UpgradeGroupContract.View> implements UpgradeGroupContract.Presenter{
    @Inject
    ChatInfoRepository repository;
    @Inject
    public UpgradeGroupPresenter(UpgradeGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getUpgradeType() {
        repository.getUpgradeGroups().subscribe(new BaseSubscribeForV2<List<UpgradeTypeBean>>() {
            @Override
            protected void onSuccess(List<UpgradeTypeBean> data) {
                mRootView.getUpgradeTypes(data);
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
            }
        });
    }

    @Override
    public void upgradegroup(String groupId, int type) {
        Subscription subscription = repository.upgradeGroup(groupId,type)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("升级中..."))
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.chat_info_upgrade_success));
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
}
