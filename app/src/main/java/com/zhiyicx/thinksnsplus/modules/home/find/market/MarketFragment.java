package com.zhiyicx.thinksnsplus.modules.home.find.market;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyBean;
import com.zhiyicx.thinksnsplus.data.source.local.CurrencyRankBean;
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

    private List<CurrencyBean> mCurrencyList = new ArrayList<>();
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
        //获取缓存中的币种列表
        mCurrencyList = SharePreferenceUtils.getList(mActivity,SharePreferenceUtils.MARKET_CURRENCY_LIST, CurrencyBean.class);
        initViewPager();
        initTabs();
    }


    @Override
    protected void initData() {
        mPresenter.getMarketCurrencyList();
    }


    @Override
    public void getCurrencyListSuccess(List<CurrencyBean> list) {
        //如果本地数据和网络数据不同步，则更新本地和当前页面
        Gson gson = new Gson();
        String net = gson.toJson(list);
        String local = gson.toJson(mCurrencyList);
        if(!net.equals(local)){
            mCurrencyList = list;
            initTabs();
            SharePreferenceUtils.saveList(mActivity,SharePreferenceUtils.MARKET_CURRENCY_LIST,mCurrencyList);
        }

    }


    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(MarketListFragment.newInstance(null));
        mFragments.add(MarketListFragment.newInstance(new CurrencyBean()));//先添加一个假的币种，点击tab选择时会更新
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


    }

    /**
     * 初始化tab
     */
    private void initTabs(){
        mTabLayout.removeAllTabs();
        mTabLayout.addTab(mTabLayout.newTab().setText("排行"));
        for (int i = 0; i < mCurrencyList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mCurrencyList.get(i).currency_name));
        }
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                mVp.setCurrentItem(position > 0 ? 1:0,false);
                if(position > 0){
                    //position-1----前面有一个排行
                    ((MarketListFragment)mFragments.get(1)).setCurrencyType(mCurrencyList.get(position-1));
                    ((MarketListFragment)mFragments.get(1)).onNewReqRefresh();
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


}
