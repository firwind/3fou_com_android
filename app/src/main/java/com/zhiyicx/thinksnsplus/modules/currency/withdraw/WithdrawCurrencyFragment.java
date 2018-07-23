package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.BackgroundRequestTaskBean;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyAddress;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.currency.accountbook.AccountBookActivity;
import com.zhiyicx.thinksnsplus.modules.currency.address.CurrencyAddressActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.scan.ScanCodeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: huwenyong
 * date: 2018/7/18 11:48
 * description:
 * version:
 */

public class WithdrawCurrencyFragment extends TSFragment<WithdrawCurrencyContract.Presenter> implements WithdrawCurrencyContract.View{

    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.et_tag)
    EditText mEtTag;

    private RightTopMenu mRightTopMenu;

    public static WithdrawCurrencyFragment newInstance(){
        WithdrawCurrencyFragment fragment = new WithdrawCurrencyFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected void initView(View rootView) {
        setCenterTextColor(R.color.white);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_withdraw_currency;
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
    protected int setRightImg() {
        return R.mipmap.icon_add_white;
    }

    @Override
    protected void setRightClick() {
        //super.setRightClick();
        showRightTopMenu();

    }

    @Override
    protected String setCenterTitle() {
        return "提币";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @OnClick({R.id.iv_address})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_address:
                CurrencyAddressActivity.startActivityForResult(this,true, IntentKey.REQ_CODE_SELECT_CURRENCY_ADDRESS);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //选择钱包地址返回
        if(resultCode == mActivity.RESULT_OK && null != data){
            switch (requestCode){
                case IntentKey.REQ_CODE_SELECT_CURRENCY_ADDRESS:

                    mEtAddress.setText( ((CurrencyAddress)data.getParcelableExtra(IntentKey.RESULT_CURRENCY_ADDRESS)).address );
                    mEtTag.setText( ((CurrencyAddress)data.getParcelableExtra(IntentKey.RESULT_CURRENCY_ADDRESS)).tag );

                    break;
                case IntentKey.REQ_CODE_GET_SCAN_RESULT:

                    mEtAddress.setText( data.getStringExtra(IntentKey.RESULT_SCAN) );

                    break;
            }
        }

    }

    /**
     * 右上角弹窗
     */
    private void showRightTopMenu(){
        if(null == mRightTopMenu){
            List<MenuItem> list = new ArrayList<>();
            list.add(new MenuItem(R.mipmap.icon_scan_gray,"扫一扫"));
            list.add(new MenuItem(R.mipmap.icon_withdraw_gray,"转出记录"));
            mRightTopMenu = new RightTopMenu.Builder(mActivity)
                    .dimBackground(true) //背景变暗，默认为true
                    .needAnimationStyle(true) //显示动画，默认为true
                    .animationStyle(R.style.RTM_ANIM_STYLE)  //默认为R.style.RTM_ANIM_STYLE
                    .windowWidth(DensityUtil.dip2px(mActivity,160))
                    .windowHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .menuItems(list)
                    .onMenuItemClickListener(position -> {
                        if(position == 0){
                            ScanCodeActivity.startActivityForResult(this,IntentKey.REQ_CODE_GET_SCAN_RESULT);
                        }else {
                            AccountBookActivity.startActivity(mActivity);
                        }
                    })
                    .build();
        }
        mRightTopMenu.showAsDropDown(mToolbarRight,DensityUtil.dip2px(mActivity,15),0);
    }

}
