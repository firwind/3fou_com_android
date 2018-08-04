package com.zhiyicx.thinksnsplus.modules.home.mine.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/23
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.base.TSViewPagerAdapterV2;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.base.TSViewPagerFragmentV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.team.MyTeamListFragment;
import com.zhiyicx.thinksnsplus.widget.popwindow.TypeChoosePopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

public class MyTeamFragment extends TSViewPagerFragmentV2<MyTeamContract.Presenter> implements MyTeamContract.View {
    private static final int DEFAULT_OFFSET_PAGE = 3;
    @BindView(R.id.sp_team_spinner)
    TextView mTeamSpinner;
    @BindView(R.id.tv_my_total)
    TextView mMyTotal;
    @BindView(R.id.mg_indicator)
    MagicIndicator mMgIndicator;
    @BindView(R.id.vp_fragment)
    ViewPager mVpFragment;
    private TypeChoosePopupWindow mTypeChoosePopupWindow;
    Unbinder unbinder;
    private CommonAdapter adapter;
    private String mCurrnecyType;
    public static MyTeamFragment newInstance(Bundle bundle) {
        MyTeamFragment fragment = new MyTeamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void getCurrencyType(List<CurrencyTypeBean> bean) {
        EventBus.getDefault().post(bean.get(0), EventBusTagConfig.EVENT_SELECT_CURRENCY);
        adapter = new CommonAdapter<CurrencyTypeBean>(getContext(), R.layout.item_currency, bean) {
            @Override
            protected void convert(ViewHolder holder, CurrencyTypeBean bean, int position) {

                holder.setText(R.id.tv_currency_name, bean.getCurrency());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CurrencyTypeBean typeBean = (CurrencyTypeBean) adapter.getDatas().get(position);
                mTeamSpinner.setText(typeBean.getCurrency());
                mCurrnecyType = typeBean.getCurrency();
                EventBus.getDefault().post(typeBean, EventBusTagConfig.EVENT_SELECT_CURRENCY);
                mTypeChoosePopupWindow.dismiss();
                mMyTotal.setText("我的总资产"+typeBean.getMoney()+typeBean.getCurrency());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initPop();
    }

    @Override
    public void getInitData(String currency, String money) {
        mTeamSpinner.setText(currency);
        mCurrnecyType  = currency;
        mMyTotal.setText("我的总资产"+money+currency);
    }

    public void initPop() {
        mTypeChoosePopupWindow = TypeChoosePopupWindow.Builder()
                .with(mActivity)
                .adapter(adapter)
                .asVertical()
                .width(mTeamSpinner.getLayoutParams().width + DensityUtil.dip2px(mActivity, 10))
                .alpha(1.0f)
                .parentView(mTeamSpinner)
                .build();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_team;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.team);
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
    protected void initView(View rootView) {
        super.initView(rootView);

    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getString(R.string.stair),
                getString(R.string.second_level), getString(R.string.three_level)

        );
    }

    @Override
    protected void initViewPager(View rootView) {
        initMagicIndicator();
        mVpFragment = (ViewPager) rootView.findViewById(R.id.vp_fragment);
        mVpFragment.setOffscreenPageLimit(DEFAULT_OFFSET_PAGE);
        tsViewPagerAdapter = new TSViewPagerAdapterV2(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);

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
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mStringList.get(index));
                clipPagerTitleView.setTextSize(getResources().getDimensionPixelSize(R.dimen.size_number_team));
                clipPagerTitleView.setTextColor(ContextCompat.getColor(getContext(), R.color.black_deep));
                clipPagerTitleView.setClipColor(getColor(R.color.themeColor));
                clipPagerTitleView.setOnClickListener(v -> mVpFragment.setCurrentItem(index));
                clipPagerTitleView.setPadding(UIUtil.dip2px(context, 18.0D), 0, UIUtil.dip2px(context, 18.0D), 0);
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return super.getTitleWeight(context, index);
            }
        };
    }
    private void initMagicIndicator() {
        mMgIndicator.setBackgroundResource(R.color.white);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(getCommonNavigatorAdapter(initTitles()));
        mMgIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerDrawable(getResources().getDrawable(R.color.transparent));
        ViewPagerHelper.bind(mMgIndicator, mVpFragment);
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(MyTeamListFragment.instance(1,mCurrnecyType));
            mFragmentList.add(MyTeamListFragment.instance(2,mCurrnecyType));
            mFragmentList.add(MyTeamListFragment.instance(3,mCurrnecyType));
        }
        return mFragmentList;
    }

    @Override
    protected void initData() {
        mPresenter.requestCurrencyType();//获取币种
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.sp_team_spinner)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_team_spinner:
                if (mTypeChoosePopupWindow != null) {
                    mTypeChoosePopupWindow.show();
                }
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
