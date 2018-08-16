package com.zhiyicx.thinksnsplus.base;

import android.content.Context;

import com.zhiyicx.common.mvp.i.IBaseView;

/**
 * author: huwenyong
 * date: 2018/8/16 17:25
 * description:
 * version:
 */

public class BaseSubscriberV3<T> extends BaseSubscribeForV2<T>{

    private IBaseView mView;

    public BaseSubscriberV3(IBaseView mView) {
        this.mView = mView;
    }

    @Override
    protected void onSuccess(T data) {
        mView.dismissSnackBar();
    }

    @Override
    protected void onFailure(String message, int code) {
        super.onFailure(message, code);
        mView.showSnackErrorMessage(message);
    }

    @Override
    protected void onException(Throwable throwable) {
        super.onException(throwable);
        mView.showSnackErrorMessage("网络异常，请检查网络！");
    }
}
