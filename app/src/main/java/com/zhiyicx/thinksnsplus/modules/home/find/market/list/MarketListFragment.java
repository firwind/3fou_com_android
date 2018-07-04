package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/6/28 19:29
 * description:
 * version:
 */

public class MarketListFragment extends TSListFragment<MarketContract.MarektListPresenter,BaseListBean> implements
        MarketContract.MarketListView{

    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;

    @Inject
    public MarketListPresenter mPresenter;


    public static MarketListFragment newInstance(String marketType){
        MarketListFragment marketListFragment = new MarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.CURRENCY_TYPE,marketType);
        marketListFragment.setArguments(bundle);
        return marketListFragment;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        DaggerMarketListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .marketListPresenterMoudle(new MarketListPresenterMoudle(MarketListFragment.this))
                .build()
                .inject(MarketListFragment.this);

        super.initView(rootView);

        mTv1.setText(isRankMarket()?"名称":"交易所");
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_market_list;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter<BaseListBean> mAapter = new CommonAdapter<BaseListBean>(mActivity,
                R.layout.item_market_currency, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, BaseListBean baseListBean, int position) {

            }
        };
        return mAapter;
    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    public boolean isRankMarket() {
        return null == getArguments().getString(IntentKey.CURRENCY_TYPE);
    }

    @Override
    public String getCurrency() {
        return getArguments().getString(IntentKey.CURRENCY_TYPE);
    }

    /**
     * 设置币种类型
     * @param currency
     */
    public void setCurrencyType(String currency){
        getArguments().putString(IntentKey.CURRENCY_TYPE,currency);
    }

}
