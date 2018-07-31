package com.zhiyicx.thinksnsplus.modules.currency;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.modules.currency.accountbook.AccountBookActivity;
import com.zhiyicx.thinksnsplus.modules.currency.recharge.RechargeCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.currency.withdraw.WithdrawCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.dialog.ExchangeCurrencyDialog;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/7/17 15:58
 * description:
 * version:
 */

public class MyCurrencyFragment extends TSListFragment<MyCurrencyContract.Presenter,CurrencyBalanceBean> implements MyCurrencyContract.View{

    private TextView mTvYearRate;

    public static MyCurrencyFragment newInstance(){
        Bundle bundle = new Bundle();
        MyCurrencyFragment fragment = new MyCurrencyFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);

        StatusBarUtils.setStatusBarColor(mActivity,R.color.themeColor);

        setCenterTextColor(R.color.white);
        rootView.setBackgroundColor(Color.WHITE);

        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.view_my_currency_header,null);
        headerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        headerView.findViewById(R.id.tv_software).setOnClickListener(v->
                CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_USE_RECOMMEND));
        mTvYearRate = (TextView) headerView.findViewById(R.id.tv_year_rate);
    }

    @Override
    protected void initData() {
        super.initData();

        mPresenter.requestNetData(0L,false);
    }

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return "数字资产";
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setLeftImg() {
        return R.mipmap.topbar_back_white;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.icon_scan_code;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter mAdapter = new CommonAdapter<CurrencyBalanceBean>(getContext(),
                R.layout.item_currency_balance,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyBalanceBean currencyBalanceBean, int position) {

                if(1 == position){
                    mTvYearRate.setText("+ "+currencyBalanceBean.year_rate+"%");
                }

                ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv_icon),currencyBalanceBean.icon);
                holder.getTextView(R.id.tv_name).setText(currencyBalanceBean.currency);
                holder.getTextView(R.id.tv_balance).setText(currencyBalanceBean.balance);
                holder.getTextView(R.id.tv_frezz).setText(currencyBalanceBean.blocked_balance);

                holder.getView(R.id.tv_detail).setOnClickListener(v->
                        AccountBookActivity.startActivity(mActivity));
                holder.getView(R.id.bt_recharge).setOnClickListener(v->
                        RechargeCurrencyActivity.startRechargeCurrencyActivity(getContext(),currencyBalanceBean.currency));
                holder.getView(R.id.bt_withdraw).setOnClickListener(v->
                        WithdrawCurrencyActivity.startWithdrawCurrencyActivity(getContext(),currencyBalanceBean.currency));
                holder.getView(R.id.bt_exchange).setOnClickListener(v->
                        new ExchangeCurrencyDialog(mActivity,false).showDialog());

                holder.getView(R.id.bt_exchange).setVisibility( "BCB".equals(currencyBalanceBean.currency)?View.GONE:View.VISIBLE );
            }
        };
        return mAdapter;
    }


}
