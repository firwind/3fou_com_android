package com.zhiyicx.thinksnsplus.modules.currency.recharge;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.currency.accountbook.AccountBookActivity;
import com.zhiyicx.thinksnsplus.modules.currency.withdraw.WithdrawCurrencyActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: huwenyong
 * date: 2018/7/18 10:15
 * description:
 * version:
 */

public class RechargeCurrencyFragment extends TSFragment<RechargeCurrencyContract.Presenter> implements RechargeCurrencyContract.View{

    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.tv_address)
    TextView mTvAddress;

    public static RechargeCurrencyFragment newInstance(String currency){
        RechargeCurrencyFragment fragment = new RechargeCurrencyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.CURRENCY_IN_MARKET,currency);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

        setCenterTextColor(R.color.white);
    }

    @Override
    protected void initData() {
        mPresenter.requestCurrencyAddress();
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


    @OnClick({R.id.bt_copy_address,R.id.ll_account_book,R.id.ll_withdraw})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bt_copy_address:
                DeviceUtils.copyTextToBoard(getContext(),"12345678");
                showSnackSuccessMessage("复制成功");
                break;
            case R.id.ll_account_book:
                AccountBookActivity.startActivity(mActivity);
                break;
            case R.id.ll_withdraw:
                WithdrawCurrencyActivity.startWithdrawCurrencyActivity(mActivity,getArguments().getString(IntentKey.CURRENCY_IN_MARKET));
                break;
        }
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected void setLoadingViewHolderClick() {
        super.setLoadingViewHolderClick();
        initData();
    }

    @Override
    public String getCurrency() {
        return getArguments().getString(IntentKey.CURRENCY_IN_MARKET);
    }


    @Override
    public void setCurrencyAddress(String address) {

        if(TextUtils.isEmpty(address)){
            showLoadViewLoadError();
        }else {
            closeLoadingView();
            mTvAddress.setText(address);
            mIvQrcode.setImageBitmap(ImageUtils.createQrcodeImage(address,
                    getResources().getDimensionPixelSize(R.dimen.dp156),null));
        }
    }
}
