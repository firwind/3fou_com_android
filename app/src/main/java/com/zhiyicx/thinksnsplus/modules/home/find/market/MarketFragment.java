package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.StockCertificateBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/6/28 19:29
 * description:
 * version:
 */

public class MarketFragment extends TSListFragment<MarketContract.Presenter,StockCertificateBean> implements MarketContract.View{

    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_chg)
    TextView mTvChg;

    public static MarketFragment newInstance(){
        MarketFragment marketFragment = new MarketFragment();
        Bundle bundle = new Bundle();
        marketFragment.setArguments(bundle);
        return marketFragment;
    }

    @Override
    protected String setCenterTitle() {
        return getResources().getString(R.string.market);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_market;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<StockCertificateBean> mAapter = new CommonAdapter<StockCertificateBean>(mActivity,
                R.layout.item_stock_certificate,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, StockCertificateBean stockCertificateBean, int position) {

            }
        };
        return mAapter;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }
}
