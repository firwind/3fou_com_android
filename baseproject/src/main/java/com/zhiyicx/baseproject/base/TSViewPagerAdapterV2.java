package com.zhiyicx.baseproject.base;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/5
 * @Contact master.jungle68@gmail.com
 */
public class TSViewPagerAdapterV2 extends FragmentPagerAdapter {
    private List<Fragment> list;
    private String[] mLitles;

    public TSViewPagerAdapterV2(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    public void bindData(List<Fragment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void bindData(List<Fragment> list, String[] titles) {
        this.list = list;
        this.mLitles = titles;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (mLitles != null) {
            return mLitles[position];
        }
        return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return null == list ? 0 : list.size();
    }



}

