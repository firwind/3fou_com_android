package com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration;

import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.IntegrationRuleBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.integration.IntegrationConfigBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.MineIntegrationContract;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * author: huwenyong
 * date: 2018/8/3 17:13
 * description:
 * version:
 */

public class NewMineIntegrationPresenter extends AppBasePresenter<NewMineIntegrationContract.View> implements NewMineIntegrationContract.Presenter{

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    BillRepository mBillRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAdvertListBeanGreenDao;

    public static int DEFAULT_LOADING_SHOW_TIME=1;
    /**
     * action tag
     */
    public static final int TAG_DETAIL=0; // detail
    public static final int TAG_RECHARGE=1; // recharge
    public static final int TAG_WITHDRAW=2; // withdraw
    public static final int TAG_SHOWRULE_POP=3; // show rulepop
    public static final int TAG_SHOWRULE_JUMP=4; // jump rule

    /**
     * 用户信息是否拿到了
     */
    private boolean mIsUsreInfoRequseted=false;
    /**
     * 钱包配置信息，必须的数据
     */
    private IntegrationConfigBean mIntegrationConfigBean = null;

    @Inject
    public NewMineIntegrationPresenter(NewMineIntegrationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void checkIntegrationConfig(int tag, boolean isNeedTip) {
        if(mIntegrationConfigBean!=null){
            mRootView.integrationConfigCallBack(mIntegrationConfigBean,tag);
            return;
        }
        getWalletConfigFromServer(tag,isNeedTip);
    }

    @NotNull
    @Override
    public String getTipPopRule() {
        return mIntegrationConfigBean==null?
                mContext.getString(R.string.integration_rule):mIntegrationConfigBean.getRule();
    }

    @NotNull
    @Override
    public List<RealAdvertListBean> getIntegrationAdvert() {
        AllAdverListBean adverBean=mAdvertListBeanGreenDao.getIntegrationAdvert();
        return adverBean == null ? new ArrayList<>():adverBean.getMRealAdvertListBeen();
    }

    @Override
    public void updateUserInfo() {
        Subscription timerSub= Observable.timer(DEFAULT_LOADING_SHOW_TIME, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if(!mIsUsreInfoRequseted){
                        mRootView.handleLoading(true);
                    }
                });

        Subscription userInfoSub=mUserInfoRepository.getLocalUserInfoBeforeNet(AppApplication.getMyUserIdWithdefault())
                .doAfterTerminate(() -> {
                    mRootView.handleLoading(false);
                    mIsUsreInfoRequseted=true;
                })
                .subscribe(new BaseSubscribeForV2<UserInfoBean>() {
                    @Override
                    protected void onSuccess(UserInfoBean data) {
                        mUserInfoBeanGreenDao.insertOrReplace(data);
                        if(data.getWallet()!=null){
                            mWalletBeanGreenDao.insertOrReplace(data.getWallet());
                        }
                        mRootView.updateBalance(data.getCurrency());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackWarningMessage(message);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                    }


                });
        addSubscrebe(timerSub);
        addSubscrebe(userInfoSub);
    }

    @Override
    public boolean checkIsNeedTipPop() {
        boolean isNotFrist = SharePreferenceUtils.getBoolean(mContext,
                SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET);
        if(!isNotFrist){
            SharePreferenceUtils.saveBoolean(mContext,
                    SharePreferenceTagConfig.SHAREPREFERENCE_TAG_IS_NOT_FIRST_LOOK_WALLET,true);
        }
        return!isNotFrist;
    }


    /**
     * get wallet config info from server
     *
     * @param tag       action tag, 1 recharge 2 withdraw
     * @param isNeedTip true show tip
     */
    private void getWalletConfigFromServer(Integer tag,boolean isNeedTip){

        Subscription walletConfigSub=mBillRepository.getIntegrationConfig()
                .doOnSubscribe(() -> {
                    if(isNeedTip){
                        mRootView.showSnackLoadingMessage(mContext.getString(R.string.integration_config_info_get_loading_tip));
                    }
                })
                .subscribe(new BaseSubscribeForV2<IntegrationConfigBean>() {
                    @Override
                    protected void onSuccess(IntegrationConfigBean data) {
                        mIntegrationConfigBean=data;
                        if(isNeedTip){
                            mRootView.dismissSnackBar();
                        }
                        mRootView.integrationConfigCallBack(data,tag);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        if(isNeedTip){
                            mRootView.showSnackErrorMessage(message);
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if(isNeedTip){
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.err_net_not_work));
                        }
                    }
                });
        addSubscrebe(walletConfigSub);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        mBillRepository.getIntegrationRules()
                .subscribe(new BaseSubscribeForV2<List<IntegrationRuleBean>>() {
                    @Override
                    protected void onSuccess(List<IntegrationRuleBean> data) {
                        mRootView.onNetResponseSuccess(data,isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        //mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable,isLoadMore);
                    }
                });
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<IntegrationRuleBean> data, boolean isLoadMore) {
        return false;
    }
}


