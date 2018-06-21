package com.zhiyicx.thinksnsplus.modules.wallet.recharge;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.UmengConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.BuildConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BackgroundTaskRequestMethodConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.PayStrV2Bean;
import com.zhiyicx.thinksnsplus.data.beans.RechargeSuccessBean;
import com.zhiyicx.thinksnsplus.data.beans.WXPayInfo;
import com.zhiyicx.thinksnsplus.data.beans.WXPayResult;
import com.zhiyicx.thinksnsplus.data.source.local.BackgroundRequestTaskBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.tspay.TSPayClient;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * @Describe
 * @Author zl
 * @Date 2017/05/22
 * @Contact master.jungle68@gmail.com
 */

public class RechargePresenter extends AppBasePresenter<RechargeContract.View> implements RechargeContract.Presenter {


    @Inject
    BackgroundRequestTaskBeanGreenDaoImpl mBackgroundRequestTaskBeanGreenDao;

    @Inject
    BillRepository mBillRepository;

    @Inject
    public RechargePresenter(RechargeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getPayStr(String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mBillRepository.getPayStr(channel, amount)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
                }).subscribe(new BaseSubscribeForV2<PayStrV2Bean>() {
            @Override
            protected void onSuccess(PayStrV2Bean data) {
                mRootView.payCredentialsResult(data);
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.showSnackErrorMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                showErrorTip(throwable);
            }

        });
    }


    @Override
    public void getAliPayStr(@NotNull String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mBillRepository.getAliPayStr(channel, amount)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
                })
                .flatMap((Func1<BaseJsonV2<String>, Observable<Map<String, String>>>) stringBaseJsonV2 -> {
                    String orderInfo = stringBaseJsonV2.getData();
                    PayTask alipay = new PayTask(mRootView.getCurrentActivity());
                    return Observable.just(alipay.payV2(orderInfo, true));
                })
                .flatMap((Func1<Map<String, String>, Observable<BaseJsonV2<String>>>) stringStringMap -> {
                    if (TSPayClient.CHANNEL_ALIPAY_SUCCESS.equals(stringStringMap.get("resultStatus"))){
                        return mBillRepository.aliPayVerify(stringStringMap.get("memo"),
                                stringStringMap.get("result"), stringStringMap.get("resultStatus"));
                    }
                    return Observable.error(new IllegalArgumentException(stringStringMap.get("memo")));
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
    }
    @Override
    public void rechargeSuccess(String charge) {
        Subscription subscribe = mBillRepository.rechargeSuccess(charge).subscribe(new BaseSubscribeForV2<RechargeSuccessBean>() {
            @Override
            protected void onSuccess(RechargeSuccessBean data) {
                mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_success));

            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
            }
        });
        addSubscrebe(subscribe);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_WX_PAY_RESULT)
    public void wxPayResult(WXPayResult wxPayResult) {
        if (wxPayResult.getCode() == 0) {
            // 0 ,微信交易成功
            mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_success));
        } else if (wxPayResult.getCode() == -2) {
            // -2 ,取消交易
            mRootView.showSnackSuccessMessage(mContext.getString(R.string.recharge_cancle));
        } else {
            mRootView.dismissSnackBar();
        }
    }


    @Override
    public void rechargeSuccessCallBack(String charge) {

    }

    @Override
    public void getWXPayStr(@NotNull String channel, double amount) {
        if (mRootView.getMoney() != (int) mRootView.getMoney() && mRootView.useInputMonye()) {
            mRootView.initmRechargeInstructionsPop();
            return;
        }
        mBillRepository.getWXPayStr(channel, amount)
                .doOnSubscribe(() -> {
                    mRootView.configSureBtn(false);
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing));
                })
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<WXPayInfo>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<WXPayInfo> data) {
                        WXPayInfo wxPayInfo = data.getData();
                        IWXAPI api = WXAPIFactory.createWXAPI(mContext, UmengConfig.WEIXIN_APPID, false);
                        api.registerApp(UmengConfig.WEIXIN_APPID);
                        PayReq request = new PayReq();
                        request.appId = UmengConfig.WEIXIN_APPID;
                        request.partnerId = wxPayInfo.getPartnerid();
                        request.prepayId = wxPayInfo.getPrepayid();
                        request.packageValue = wxPayInfo.getPackagestr();
                        request.nonceStr = wxPayInfo.getNoncestr();
                        request.timeStamp = wxPayInfo.getTimestamp();
                        request.sign = wxPayInfo.getSign();
                        api.sendReq(request);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
    }
}
