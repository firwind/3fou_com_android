package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
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


    public static MarketListFragment newInstance(CurrencyBean marketType){
        MarketListFragment marketListFragment = new MarketListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKey.CURRENCY_TYPE,marketType);
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
        CommonAdapter mAdapter = null;
        if(isRankMarket()){

            mAdapter = new CommonAdapter<BaseListBean>(mActivity,
                    R.layout.item_market_rank, mListDatas) {
                @Override
                protected void convert(ViewHolder holder, BaseListBean baseListBean, int position) {
                    CurrencyRankBean data = (CurrencyRankBean) baseListBean;
                    holder.setText(R.id.tv_rank,String.valueOf(position+1));
                    ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv_currency_icon),data.iconUrl);
                    holder.setText(R.id.tv_currency_name,data.currency);
                    holder.setText(R.id.tv_price,"¥"+data.price);
                    holder.setText(R.id.tv_chg,(data.change>0?"+":"")+data.change+"%");
                    holder.getView(R.id.tv_chg).setSelected(data.change>0);

                }
            };
        }else {
            mAdapter = new CommonAdapter<BaseListBean>(mActivity,
                    R.layout.item_market_currency, mListDatas) {
                @Override
                protected void convert(ViewHolder holder, BaseListBean baseListBean, int position) {
                    MarketCurrencyBean data = (MarketCurrencyBean) baseListBean;
                    holder.setText(R.id.tv_market,data.exchange_name);
                    holder.setText(R.id.tv_unit,data.unit);
                    holder.setText(R.id.tv_vol,"量："+data.vol+"万");
                    holder.setText(R.id.tv_last_cny,"¥"+data.last_cny);
                    holder.setText(R.id.tv_last_usd,"$"+data.last_usd);
                    double degree = 0;
                    try {
                        degree = Double.parseDouble(data.degree);
                    }catch (Exception e){

                    }
                    holder.setText(R.id.tv_degree,(degree>0?"+":"")+data.degree+"%" );

                    holder.getTextView(R.id.tv_last_cny).setTextColor(getResources()
                            .getColor(degree>0?R.color.market_red:R.color.market_green));
                    holder.getView(R.id.tv_degree).setSelected(degree>0);

                }
            };
        }
        return mAdapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
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
        return null == getArguments().getParcelable(IntentKey.CURRENCY_TYPE);
    }

    @Override
    public CurrencyBean getCurrency() {
        return getArguments().getParcelable(IntentKey.CURRENCY_TYPE);
    }

    /**
     * 设置币种类型
     * @param currency
     */
    public void setCurrencyType(CurrencyBean currency){
        getArguments().putParcelable(IntentKey.CURRENCY_TYPE,currency);
    }

}
