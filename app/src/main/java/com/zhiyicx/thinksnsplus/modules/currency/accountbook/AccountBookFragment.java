package com.zhiyicx.thinksnsplus.modules.currency.accountbook;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragmentV2;
import com.zhiyicx.thinksnsplus.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/23 9:24
 * description:
 * version:
 */

public class AccountBookFragment extends TSViewPagerFragmentV2{
    public static int ACCOUNT_BOOK_ALL = 0;
    public static int ACCOUNT_BOOK_RECHARGE = 1;
    public static int ACCOUNT_BOOK_DEPOSIT = 2;
    public static int ACCOUNT_BOOK_EXCHANGE = 3;
    public static int ACCOUNT_BOOK_CHECK = 4;

    public static AccountBookFragment newInstance(){
        AccountBookFragment fragment = new AccountBookFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterTextColor(R.color.white);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    public void setPresenter(Object presenter) {

    }

    @Override
    protected boolean isAdjustMode() {
        return true;
    }

    @Override
    protected int tabSpacing() {
        return getContext().getResources().getDimensionPixelSize(R.dimen.dp10);
    }

    @Override
    protected boolean showToolbar() {
        return true;
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
        return "明细";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList("全部","充币","提币","审核中","兑换");
    }

    @Override
    protected List<Fragment> initFragments() {
        return Arrays.asList(AccountBookChildFragment.newInstance(ACCOUNT_BOOK_ALL),
                AccountBookChildFragment.newInstance(ACCOUNT_BOOK_RECHARGE),
                AccountBookChildFragment.newInstance(ACCOUNT_BOOK_DEPOSIT),
                AccountBookChildFragment.newInstance(ACCOUNT_BOOK_CHECK),
                AccountBookChildFragment.newInstance(ACCOUNT_BOOK_EXCHANGE));
    }

    @Override
    protected void initData() {

    }
}
