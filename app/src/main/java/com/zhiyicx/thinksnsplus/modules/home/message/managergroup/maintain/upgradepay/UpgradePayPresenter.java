package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/9
 * 描  述：
 * 版  权: 九曲互动
 */

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhiyicx.baseproject.config.UmengConfig;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.WXPayInfo;
import com.zhiyicx.thinksnsplus.data.beans.WXPayResult;
import com.zhiyicx.thinksnsplus.data.source.repository.BillRepository;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup.UpgradeGroupActivity;


import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_WX_PAY_RESULT;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_ALIPAY_SUCCESS;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_ALIPAY_V2;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_BALANCE;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_BALANCE_V2;
import static com.zhiyicx.tspay.TSPayClient.CHANNEL_WXPAY_V2;

public class UpgradePayPresenter extends AppBasePresenter<UpgradePayContract.View> implements UpgradePayContract.Presenter {
    @Inject
    BillRepository mBillRepository;

    @Inject
    public UpgradePayPresenter(UpgradePayContract.View rootView) {
        super(rootView);
    }

    @Override
    public void getPayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths) {
        if (channel.equals(CHANNEL_BALANCE_V2)) {//余额支付
            getBalancePayStr(groupId, upGradeType, channel, amount, fewmouths);
        } else if (channel.equals(CHANNEL_ALIPAY_V2)) {
            getAliPayStr(groupId, upGradeType, channel, amount, fewmouths);//支付宝
        } else if (channel.equals(CHANNEL_WXPAY_V2)) {
            getWeChatStr(groupId, upGradeType, channel, amount, fewmouths);//微信支付
        }
    }

    private void getBalancePayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths ) {
        mBillRepository.getUpgradeGroupBalancePayStr(groupId, upGradeType, channel, amount, fewmouths)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing)))
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.pay_alert_success));
                        gotoSuccessActivity();
                    }
                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }
                });
    }

    private void getWeChatStr(String groupId, String upGradeType, String channel, double amount, int fewmouths) {
        mBillRepository.getUpgradeGroupWXPayStr(groupId, upGradeType, channel, amount, fewmouths)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing)))
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
                        mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }
                });
    }

    private void getAliPayStr(String groupId, String upGradeType, String channel, double amount, int fewmouths) {
        mBillRepository.getUpgradeGroupAliPayStr(groupId, upGradeType, channel, amount, fewmouths)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.recharge_credentials_ing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(stringBaseJsonV2 -> {
                    PayTask payTask = new PayTask(mRootView.getActivity());
                    String orderInfo = stringBaseJsonV2.getData();
                    return Observable.just(payTask.payV2(orderInfo, true));
                })
                .flatMap(stringStringMap -> {
                    String status = stringStringMap.get("resultStatus");
                    if (CHANNEL_ALIPAY_SUCCESS.equals(status)) {
                        return mBillRepository.aliPayIntegrationVerify(stringStringMap.get("memo"), stringStringMap.get("result"), stringStringMap.get("resultStatus"));
                    } else {
                        return Observable.error(new IllegalArgumentException(stringStringMap.get("memo")));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.pay_alert_success));
                        gotoSuccessActivity();
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
    protected boolean useEventBus() {
        return true;
    }
    @org.simple.eventbus.Subscriber(tag = EVENT_WX_PAY_RESULT)
    public void onWeChatResp(WXPayResult wxPayResult){
        switch (wxPayResult.getCode()){
            case 0://支付成功
                mRootView.showSnackSuccessMessage(mContext.getString(R.string.pay_alert_success));
                gotoSuccessActivity();
                break;
            case -1://可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                mRootView.showSnackErrorMessage(mContext.getString(R.string.pay_alert_failed));
                break;
            case -2://取消支付
                mRootView.showSnackErrorMessage(mContext.getString(R.string.pay_cancel));
                break;
        }
    }

    public void gotoSuccessActivity(){
        PaySuccessActivity.startPaySuccessActivity(mContext);
        mRootView.getActivity().finish();
        UpgradeGroupActivity.mFlag.finish();
    }
}
