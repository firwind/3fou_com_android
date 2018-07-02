package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.find.market.list.MarketListFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.badge.BadgePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/7/2 9:35
 * description:
 * version:
 */

public class MarketFragment extends TSFragment<MarketContract.MarketPresenter> implements MarketContract.MarketView{

    private static final int DEFAULT_TAB_UNSELECTED_TEXTCOLOR = com.zhiyicx.baseproject.R.color.normal_for_assist_text;// 缺省的tab未选择文字
    private static final int DEFAULT_TAB_SELECTED_TEXTCOLOR = R.color.text_color_black;// 缺省的tab被选择文字
    private static final int DEFAULT_TAB_TEXTSIZE = com.zhiyicx.baseproject.R.integer.tab_text_size;// 缺省的tab文字大小
    private static final int DEFAULT_TAB_MARGIN = com.zhiyicx.baseproject.R.integer.tab_margin;// 缺省的tab左右padding
    private static final int DEFAULT_TAB_PADDING = com.zhiyicx.baseproject.R.integer.tab_padding;// 缺省的tab的线和文字的边缘距离
    private static final int DEFAULT_TAB_LINE_COLOR = com.zhiyicx.baseproject.R.color.themeColor;// 缺省的tab的线的颜色
    private static final int DEFAULT_TAB_LINE_HEGIHT = com.zhiyicx.baseproject.R.integer.line_height;// 缺省的tab的线的高度

    @BindView(R.id.indicator)
    MagicIndicator mIndicator;
    @BindView(R.id.vp)
    ViewPager mVp;

    private List<String> mTitles;
    private List<Fragment> mFragments;

    public static MarketFragment newInstance(){
        MarketFragment marketFragment = new MarketFragment();
        Bundle bundle = new Bundle();
        marketFragment.setArguments(bundle);
        return marketFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_no_scroll_viewpager;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getResources().getString(R.string.market);
    }


    @Override
    protected void initView(View rootView) {

        mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.rank_market));
        mTitles.add("A");
        mTitles.add("B");
        mTitles.add("C");
        mTitles.add("D");
        mTitles.add("E");

        initViewPager();

    }


    private void initViewPager() {
        mFragments = new ArrayList<>();
        mFragments.add(MarketListFragment.newInstance(null));
        mFragments.add(MarketListFragment.newInstance("A"));
        mVp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });

        CommonNavigator mCommonNavigator = new CommonNavigator(mActivity);
        mCommonNavigator.setAdapter(getCommonNavigatorAdapter(mTitles));
        mCommonNavigator.setAdjustMode(true);
        mIndicator.setNavigator(mCommonNavigator);

    }

    @Override
    protected void initData() {

    }


    @NonNull
    private CommonNavigatorAdapter getCommonNavigatorAdapter(final List<String> mStringList) {
        return new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return mStringList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setNormalColor(ContextCompat.getColor(context, DEFAULT_TAB_UNSELECTED_TEXTCOLOR));
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(context, DEFAULT_TAB_SELECTED_TEXTCOLOR));
                simplePagerTitleView.setText(mStringList.get(index));
                simplePagerTitleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, context.getResources().getInteger(DEFAULT_TAB_TEXTSIZE));
                simplePagerTitleView.setOnClickListener(v -> {
                    mIndicator.onPageSelected(index);
                    mVp.setCurrentItem(index > 0 ? 1 : 0);
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);// 占满
                linePagerIndicator.setXOffset(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_PADDING)));// 每个item边缘到指示器的边缘距离
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, context.getResources()
                        .getInteger(DEFAULT_TAB_LINE_HEGIHT)));
                linePagerIndicator.setColors(ContextCompat.getColor(context,
                        DEFAULT_TAB_LINE_COLOR));
                return linePagerIndicator;
            }
        };
    }



}
