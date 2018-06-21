package com.hudong.wemedia.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhiyicx.baseproject.config.UmengConfig;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.WXPayResult;

import org.simple.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, UmengConfig.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
        LogUtils.d(req);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp instanceof PayResp) {
            PayResp payResp = (PayResp) resp;
            WXPayResult wxPayResult = new WXPayResult();
            wxPayResult.setExtData(payResp.extData);
            wxPayResult.setPrepayId(payResp.prepayId);
            wxPayResult.setReturnKey(payResp.returnKey);
            wxPayResult.setType(payResp.getType());
            wxPayResult.setCode(payResp.errCode);
            EventBus.getDefault().post(wxPayResult, EventBusTagConfig.EVENT_WX_PAY_RESULT);
            finish();
        }
    }

}



