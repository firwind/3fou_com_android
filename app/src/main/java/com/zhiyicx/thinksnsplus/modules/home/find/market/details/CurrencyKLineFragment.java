package com.zhiyicx.thinksnsplus.modules.home.find.market.details;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.tifezh.kchartlib.chart.KChartView;
import com.hyphenate.util.DensityUtil;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.local.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.kline.KLInePeriod;
import com.zhiyicx.thinksnsplus.utils.kline.KLineAdapter;
import com.zhiyicx.thinksnsplus.utils.kline.KLineDataHelper;
import com.zhiyicx.thinksnsplus.utils.kline.KLineEntity;
import com.zhiyicx.thinksnsplus.utils.kline.KLinePeriodOrQuoteView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * author: huwenyong
 * date: 2018/7/5 11:23
 * description:
 * version:
 */

public class CurrencyKLineFragment extends TSFragment<CurrencyKLineContract.Presenter> implements CurrencyKLineContract.View{

    @BindView(R.id.kchart_view)
    KChartView mKchartView;
    @BindView(R.id.tv_high)
    TextView mTvHigh;
    @BindView(R.id.tv_low)
    TextView mTvLow;
    @BindView(R.id.tv_vol)
    TextView mTvVol;
    @BindView(R.id.tv_chg)
    TextView mTvChg;
    @BindView(R.id.tv_cny)
    TextView mTvCny;

    @BindView(R.id.fl_currency_data1)
    FrameLayout mFlCurrencyData1;
    @BindView(R.id.ll_currency_data2)
    LinearLayout mLlCurrencyData2;

    @BindView(R.id.tv_kline_m1)
    TextView mTvKLineM1;
    @BindView(R.id.tv_kline_m15)
    TextView mTvKLineM15;
    @BindView(R.id.tv_kline_h4)
    TextView mTvKLineH4;
    @BindView(R.id.tv_kline_day)
    TextView mTvKLineDay;
    @BindView(R.id.tv_more)
    TextView mTvMore;
    @BindView(R.id.tv_quota)
    TextView mTvQuota;
    @BindView(R.id.iv_reverse)
    ImageView mIvReverse;
    @BindView(R.id.iv_more_corner)
    ImageView mIvMoreCorner;
    @BindView(R.id.iv_quota_corner)
    ImageView mIvQuotaCorner;
    @BindView(R.id.fl_kline_container)
    FrameLayout mFlKLineContainer;

    private KLinePeriodOrQuoteView mKLinePeriodView;
    private KLinePeriodOrQuoteView mKLineQuoteView;

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
        return "--";
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected String setRightTitle() {
        return "CNY";
    }

    @Override
    protected void initView(View rootView) {

        setRightTextColor(R.color.text_color_black);
        mKchartView.setAdapter(new KLineAdapter());

        MarketCurrencyBean data = getArguments().getParcelable(IntentKey.CURRENCY_IN_MARKET);

        MarketCurrencyBean marketCurrencyBean = getArguments().getParcelable(IntentKey.CURRENCY_IN_MARKET);
        SpannableString title = new SpannableString(marketCurrencyBean.exchange_name+"\n"+
                marketCurrencyBean.currency_name+"/"+marketCurrencyBean.unit);
        title.setSpan(new RelativeSizeSpan(0.8f), 0, marketCurrencyBean.exchange_name.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mToolbarCenter.setMaxLines(2);
        mToolbarCenter.setText(title);

        mTvCny.setText("¥"+data.last_cny);
        String high = "最高："+data.high;
        String low = "最低："+data.low;
        String vol = "成交量："+data.vol+"万";
        int color = getResources().getColor(R.color.text_color_black);
        mTvHigh.setText(getColorfulString(high,3,high.length(),color));
        mTvLow.setText(getColorfulString(low,3,low.length(),color));
        mTvVol.setText(getColorfulString(vol,4,vol.length(),color));

        double chg = 0;
        try {
            chg = Double.parseDouble(data.degree);
        }catch (Exception e){

        }
        mTvChg.setSelected(chg>0);
        mTvChg.setText((chg>0?"+":"")+chg+"%");

    }

    @Override
    protected void initData() {
        mTvKLineM15.callOnClick();
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

    /**
     * 横竖屏切换
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        boolean isVertical = (getContext().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT);
        if(isVertical){

            getView().findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            mFlCurrencyData1.setVisibility(View.VISIBLE);
            mLlCurrencyData2.setVisibility(View.VISIBLE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFlKLineContainer.getLayoutParams();
            params.bottomMargin = DensityUtil.dip2px(mActivity,80);
            mFlKLineContainer.setLayoutParams(params);

        }else {

            getView().findViewById(R.id.toolbar).setVisibility(View.GONE);
            mFlCurrencyData1.setVisibility(View.GONE);
            mLlCurrencyData2.setVisibility(View.GONE);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mFlKLineContainer.getLayoutParams();
            params.bottomMargin = DensityUtil.dip2px(mActivity,0);
            mFlKLineContainer.setLayoutParams(params);

        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        boolean isVertical = (getContext().getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_PORTRAIT);
        if(isVertical){
            mActivity.finish();
        }else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        }
    }

    @OnClick({R.id.tv_kline_m1,R.id.tv_kline_m15,R.id.tv_kline_h4,
            R.id.tv_kline_day,R.id.tv_more,R.id.tv_quota,R.id.iv_reverse})
    public void onClick(View v){


        switch (v.getId()){
            case R.id.tv_kline_m1:
                initPeriodState();
                v.setSelected(true);
                mPresenter.requestKLineData(KLineDataHelper.getkLinePeriod(KLInePeriod.M1));
                break;
            case R.id.tv_kline_m15:
                initPeriodState();
                v.setSelected(true);
                mPresenter.requestKLineData(KLineDataHelper.getkLinePeriod(KLInePeriod.M15));
                break;
            case R.id.tv_kline_h4:
                initPeriodState();
                v.setSelected(true);
                mPresenter.requestKLineData(KLineDataHelper.getkLinePeriod(KLInePeriod.H4));
                break;
            case R.id.tv_kline_day:
                initPeriodState();
                v.setSelected(true);
                mPresenter.requestKLineData(KLineDataHelper.getkLinePeriod(KLInePeriod.D1));
                break;
            case R.id.tv_more:
                mIvMoreCorner.setSelected(true);
                mTvMore.setBackgroundResource(R.color.light_gray);
                showKLinePeriodView(v);
                break;
            case R.id.tv_quota:
                mTvQuota.setSelected(true);
                mIvQuotaCorner.setSelected(true);
                showKLineQuoteView(v);
                break;
            case R.id.iv_reverse:
                boolean isVertical = (getContext().getResources().getConfiguration().orientation ==
                        Configuration.ORIENTATION_PORTRAIT);
                mActivity.setRequestedOrientation(isVertical?ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                break;
        }
    }

    /**
     * 初始化周期状态
     */
    private void initPeriodState(){
        mTvKLineM1.setSelected(false);
        mTvKLineM15.setSelected(false);
        mTvKLineH4.setSelected(false);
        mTvKLineDay.setSelected(false);
        mTvMore.setSelected(false);
        mTvMore.setText("更多");
    }

    private SpannableString getColorfulString(String str,int start,int end,int color){
        SpannableString spanString = new SpannableString(str);
        ForegroundColorSpan span = new ForegroundColorSpan(color);
        spanString.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }

    /**
     * 显示更多周期
     */
    private void showKLinePeriodView(View anchor){
        if(null == mKLinePeriodView){
            mKLinePeriodView = new KLinePeriodOrQuoteView(mActivity);
            mKLinePeriodView.getPopupWindow().setOnDismissListener(() -> {
                mIvMoreCorner.setSelected(false);
                mTvMore.setBackgroundResource(R.color.white);
            });
            String[] periodvalue = {KLineDataHelper.getkLinePeriod(KLInePeriod.M5),
                    KLineDataHelper.getkLinePeriod(KLInePeriod.M30),
                    KLineDataHelper.getkLinePeriod(KLInePeriod.H1),
                    KLineDataHelper.getkLinePeriod(KLInePeriod.W1),
                    KLineDataHelper.getkLinePeriod(KLInePeriod.MONTH)};
            mKLinePeriodView.setData(new String[]{"5分", "30分", "1小时", "1周", "1月"}, (view, position) -> {
                initPeriodState();
                //这里选择
                mTvMore.setSelected(true);
                mTvMore.setText(view.getText().toString());
                mPresenter.requestKLineData(periodvalue[position]);
            });
        }
        mKLinePeriodView.showPopupWindow(anchor);
    }

    /**
     * 显示更多指标
     */
    private void showKLineQuoteView(View anchor){
        if(null == mKLineQuoteView){
            mKLineQuoteView = new KLinePeriodOrQuoteView(mActivity);
            mKLineQuoteView.getViewList().get(0).setSelected(true);
            mKLineQuoteView.getPopupWindow().setOnDismissListener(() -> {
                mIvQuotaCorner.setSelected(false);
                mTvQuota.setSelected(false);
            });
            mKLineQuoteView.setData(new String[]{"VOL", "MACD", "KDJ", "RSI", "BOLL"}, (view, position) -> {
                mKchartView.setChildDraw(position);
            });
        }
        mKLineQuoteView.showPopupWindow(anchor);
    }

}
