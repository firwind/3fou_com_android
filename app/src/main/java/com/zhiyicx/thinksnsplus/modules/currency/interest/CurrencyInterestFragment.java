package com.zhiyicx.thinksnsplus.modules.currency.interest;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyBalanceBean;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/31 15:41
 * description:
 * version:
 */

public class CurrencyInterestFragment extends TSListFragment<CurrencyInterestContract.Presenter,CurrencyBalanceBean>
        implements CurrencyInterestContract.View{

    public static CurrencyInterestFragment newInstance(){
        CurrencyInterestFragment fragment = new CurrencyInterestFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterTextColor(R.color.white);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_currency_interest_list;
    }

    @Override
    protected boolean isLayzLoad() {
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
        return "货币";
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.themeColor;
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<CurrencyBalanceBean> mAdapter = new CommonAdapter<CurrencyBalanceBean>(mActivity,
                R.layout.item_currency_interest,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyBalanceBean currencyInterest, int position) {
                holder.setText(R.id.tv_rank,String.valueOf(position+1));
                ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv_currency_icon),currencyInterest.icon);
                holder.setText(R.id.tv_currency_name,currencyInterest.currency);
                holder.setText(R.id.tv_chg,currencyInterest.year_rate+"%");
                holder.getView(R.id.tv_chg).setSelected(true);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                /*CurrencyInterestWebActivity.startCurrencyWebActivity(mActivity, ApiConfig.URL_CURRENCY_INTEREST,
                        mListDatas.get(position).currency);*/
                CustomWEBActivity.startToWEBActivity(mActivity,
                        String.format(ApiConfig.URL_CURRENCY_INTEREST+"?form=app&token=%s&currency=%s",
                                mPresenter.getCurrentToken(),mListDatas.get(position).currency));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return mAdapter;
    }
}
