package com.zhiyicx.thinksnsplus.modules.rank.main.container;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RankIndexBean;
import com.zhiyicx.thinksnsplus.modules.information.adapter.ScaleTransitionPagerTitleView;
import com.zhiyicx.thinksnsplus.modules.rank.main.list.RankListFragment;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zhiyicx.thinksnsplus.modules.rank.main.list.RankListFragment.BUNDLE_RANK_TYPE;

/**
 * @author Catherine
 * @describe 排行榜首页
 * @date 2017/8/22
 * @contact email:648129313@qq.com
 */

public class RankIndexFragment extends TSViewPagerFragment {

    public RankIndexFragment instance() {
        return new RankIndexFragment();
    }


    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return true;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.rank_user)
                , getString(R.string.rank_qa)
                , getString(R.string.rank_dynamic)
                , getString(R.string.rank_info));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            // 用户
            Bundle bundleUser = new Bundle();
            RankIndexBean rankIndexBean = new RankIndexBean();
            rankIndexBean.setCategory(getString(R.string.rank_user));
            bundleUser.putSerializable(BUNDLE_RANK_TYPE, rankIndexBean);
            mFragmentList.add(new RankListFragment().instance(bundleUser));
            // 问答
            Bundle bundleQa = new Bundle();
            RankIndexBean rankIndexBeanQa = new RankIndexBean();
            rankIndexBeanQa.setCategory(getString(R.string.rank_qa));
            bundleQa.putSerializable(BUNDLE_RANK_TYPE, rankIndexBeanQa);
            mFragmentList.add(new RankListFragment().instance(bundleQa));
            // 动态
            Bundle bundleDynamic = new Bundle();
            RankIndexBean rankIndexBeanDynamic = new RankIndexBean();
            rankIndexBeanDynamic.setCategory(getString(R.string.rank_dynamic));
            bundleDynamic.putSerializable(BUNDLE_RANK_TYPE, rankIndexBeanDynamic);
            mFragmentList.add(new RankListFragment().instance(bundleDynamic));
            // 资讯
            Bundle bundleInfo = new Bundle();
            RankIndexBean rankIndexBeanInfo = new RankIndexBean();
            rankIndexBeanInfo.setCategory(getString(R.string.rank_info));
            bundleInfo.putSerializable(BUNDLE_RANK_TYPE, rankIndexBeanInfo);
            mFragmentList.add(new RankListFragment().instance(bundleInfo));
        }

        return mFragmentList;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);
        mTsvToolbar.setLeftImg(0);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.rank);
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }
}
