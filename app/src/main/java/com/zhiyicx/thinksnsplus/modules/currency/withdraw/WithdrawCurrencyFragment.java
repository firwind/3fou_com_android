package com.zhiyicx.thinksnsplus.modules.currency.withdraw;

import android.os.Bundle;
import android.view.View;

import com.hyphenate.util.DensityUtil;
import com.lwy.righttopmenu.MenuItem;
import com.lwy.righttopmenu.RightTopMenu;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/18 11:48
 * description:
 * version:
 */

public class WithdrawCurrencyFragment extends TSFragment<WithdrawCurrencyContract.Presenter> implements WithdrawCurrencyContract.View{

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
                    .menuItems(list)
                    .onMenuItemClickListener(position -> {

                    })
                    .build();
        }
        mRightTopMenu.showAsDropDown(mToolbarRight,DensityUtil.dip2px(mActivity,15),0);
    }

}
