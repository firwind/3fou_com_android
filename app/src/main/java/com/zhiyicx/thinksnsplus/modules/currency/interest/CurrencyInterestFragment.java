package com.zhiyicx.thinksnsplus.modules.currency.interest;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyInterest;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

/**
 * author: huwenyong
 * date: 2018/7/31 15:41
 * description:
 * version:
 */

public class CurrencyInterestFragment extends TSListFragment<CurrencyInterestContract.Presenter,CurrencyInterest>
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
        CommonAdapter<CurrencyInterest> mAdapter = new CommonAdapter<CurrencyInterest>(mActivity,
                R.layout.item_currency_interest,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CurrencyInterest currencyInterest, int position) {

            }
        };
        return mAdapter;
    }
}
