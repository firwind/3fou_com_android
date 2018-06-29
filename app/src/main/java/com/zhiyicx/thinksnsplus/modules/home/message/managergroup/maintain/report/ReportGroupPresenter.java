package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import javax.inject.Inject;

import rx.Subscription;

public class ReportGroupPresenter extends AppBasePresenter<ReportGroupContract.View> implements ReportGroupContract.Presenter {
    @Inject
    ChatInfoRepository repository;

    @Inject
    public ReportGroupPresenter(ReportGroupContract.View rootView) {
        super(rootView);
    }


    @Override
    public void sumitReport(String groupId, String reason, String tel) {
        if (reason.length()==0){
            mRootView.showSnackErrorMessage(mContext.getString(R.string.hint_report));
        }
        if (checkPhone(tel)){
            return;
        }
        Subscription subscription  = repository.reportGroup(String.valueOf(AppApplication.getMyUserIdWithdefault()),
                groupId, reason, tel)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("举报中..."))
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.report_success_tip));
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

    /**
     * 检测手机号码是否正确
     */
    private boolean checkPhone(String phone) {
        if (!RegexUtils.isMobileExact(phone)) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.phone_number_toast_hint));
            return true;
        }
        return false;
    }
}
