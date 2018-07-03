package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/3
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;

public class NoticeDetailsPresenter extends AppBasePresenter<NoticeDetailsContract.View> implements NoticeDetailsContract.Presenter{
    @Inject
    BaseMessageRepository mBaseMessageRepository;

    @Inject
    public NoticeDetailsPresenter(NoticeDetailsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void deleteNotice(String noticeId) {
        mBaseMessageRepository.delNotice(noticeId)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("正在删除"))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        // 成功后重置页面
                        mRootView.dismissSnackBar();
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.clean_success));
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
    }
}
