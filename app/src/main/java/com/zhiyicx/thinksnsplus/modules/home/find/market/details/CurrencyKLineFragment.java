package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import android.os.Bundle;
import android.view.View;

import com.github.tifezh.kchartlib.chart.KChartView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.kline.KLineAdapter;
import com.zhiyicx.thinksnsplus.utils.kline.KLineDataHelper;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/7/5 11:23
 * description:
 * version:
 */

public class CurrencyKLineFragment extends TSFragment<CurrencyKLineContract.Presenter> implements CurrencyKLineContract.View{

    @BindView(R.id.kchart_view)
    KChartView mKchartView;

    public static CurrencyKLineFragment newInstance(MarketCurrencyBean marketCurrencyBean){
        CurrencyKLineFragment fragment = new CurrencyKLineFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKey.CURRENCY_IN_MARKET,marketCurrencyBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        MarketCurrencyBean marketCurrencyBean = getArguments().getParcelable(IntentKey.CURRENCY_IN_MARKET);
        return marketCurrencyBean.currency_name+"/"+marketCurrencyBean.exchange_name;
    }

    @Override
    protected String setRightTitle() {
        return "CNY";
    }

    @Override
    protected void initView(View rootView) {
        mKchartView.setAdapter(new KLineAdapter());
    }

    @Override
    protected void initData() {
        mPresenter.requestKLineData(KLineDataHelper.getkLinePeriod().get("1分钟"));
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_currency_kline;
    }

    @Override
    public String getTicker() {
        return ((MarketCurrencyBean)getArguments().getParcelable(IntentKey.CURRENCY_IN_MARKET)).ticker;
    }

    @Override
    public void setKLineData(List<KLineEntity> list) {
        ((KLineAdapter)mKchartView.getAdapter()).setData(list);
    }

}
