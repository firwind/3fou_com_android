package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/7/18 10:15
 * description:
 * version:
 */

public class RechargeCurrencyFragment extends TSFragment<RechargeCurrencyContract.Presenter> implements RechargeCurrencyContract.View{

    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    public static RechargeCurrencyFragment newInstance(){
        RechargeCurrencyFragment fragment = new RechargeCurrencyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

        setCenterTextColor(R.color.white);
        mIvQrcode.setImageBitmap(ImageUtils.createQrcodeImage("xxxxxx",
                getResources().getDimensionPixelSize(R.dimen.dp156),null));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_recharge_currency;
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected String setCenterTitle() {
        return "充币";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }
}
