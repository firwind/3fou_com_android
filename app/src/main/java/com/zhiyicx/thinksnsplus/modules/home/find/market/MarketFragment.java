package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.home.find.market.list.MarketListFragment;
import com.zhiyicx.thinksnsplus.utils.StringUtils;

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

    private List<String> mTitles = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

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
        mTitles.add(getString(R.string.rank_market));
        //获取缓存中的币种列表
        try {
            String currencyList = SharePreferenceUtils.getString(mActivity,SharePreferenceUtils.MARKET_CURRENCY_LIST);
            if(!TextUtils.isEmpty(currencyList)){
                String[] arr = currencyList.split(ConstantConfig.SPLIT_SMBOL);
                for (int i = 0; i < arr.length; i++) {
                    mTitles.add(arr[i]);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        initViewPager();
    }


    @Override
    protected void initData() {
        mPresenter.getMarketCurrencyList();
    }



    private void initViewPager() {
        mFragments.add(MarketListFragment.newInstance(null));
        mFragments.add(MarketListFragment.newInstance("A"));//先添加一个假的币种，点击tab选择时会更新
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
                int position = tab.getPosition();
                mVp.setCurrentItem(position > 0 ? 1:0,false);
                if(position > 0){
                    ((MarketListFragment)mFragments.get(1)).setCurrencyType(tab.getText().toString());
                    ((MarketListFragment)mFragments.get(1)).startRefrsh();
                }

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
    public void getCurrencyListSuccess(List<String> list) {
        String local = SharePreferenceUtils.getString(mActivity,SharePreferenceUtils.MARKET_CURRENCY_LIST);
        StringBuilder net = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            net.append(list.get(i));
            if(i != list.size()-1)
                net.append(ConstantConfig.SPLIT_SMBOL);
        }
        //如果本地数据和网络数据不同步，则更新本地和当前页面
        if(!net.toString().equals(local)){

            String[] netCurrencyArr = net.toString().split(ConstantConfig.SPLIT_SMBOL);
            mTitles.clear();
            mTitles.add(getString(R.string.rank_market));
            mTabLayout.removeAllTabs();
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitles.get(0)));
            for (int i = 0; i < netCurrencyArr.length; i++) {
                mTitles.add(netCurrencyArr[i]);
                mTabLayout.addTab(mTabLayout.newTab().setText(netCurrencyArr[i]));
            }

            SharePreferenceUtils.saveString(mActivity,SharePreferenceUtils.MARKET_CURRENCY_LIST,net.toString());
        }

    }
}
