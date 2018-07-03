package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.find.market.list.MarketListFragment;

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

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
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
        mTitles.add("A");
        mTitles.add("B");
        mTitles.add("C");
        mTitles.add("D");
        mTitles.add("E");
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

        for (int i = 0; i < mTitles.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(i)));
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mVp.setCurrentItem(tab.getPosition() > 0 ? 1:0,false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void initData() {

    }



}
