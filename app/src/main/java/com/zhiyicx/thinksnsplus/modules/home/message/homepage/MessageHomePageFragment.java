package com.zhiyicx.thinksnsplus.modules.home.message.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.data.beans.MenuItem;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.HomeMessageIndexBean;
import com.zhiyicx.thinksnsplus.modules.circle.mine.container.MyCircleContainerActivity;
import com.zhiyicx.thinksnsplus.modules.currency.MyCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.currency.interest.CurrencyInterestActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeFragment;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.market.details.CurrencyKLineActivity;
import com.zhiyicx.thinksnsplus.modules.home.main.MainActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerHeader;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.QA_Activity;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankIndexActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

/**
 * author: huwenyong
 * date: 2018/8/9 9:35
 * description:
 * version:
 */

public class MessageHomePageFragment extends TSListFragment<MessageHomePageContract.Presenter,InfoListDataBean>
        implements MessageHomePageContract.View, InfoBannerHeader.InfoBannerHeadlerClickEvent {

    @Inject
    MessageHomePagePresenter mPresenter;

    private RecyclerView mRvMenus;
    private RecyclerView mRvMarket;
    private Banner mBanner;
    private TextView mTvYearRate;
    private CombinationButton mBtNews;

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    /*@Override
    protected boolean setUseCenterLoading() {
        return true;
    }*/

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }


    @Override
    protected void initView(View rootView) {
        DaggerMessageHomePageComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .messageHomePagePresenterModule(new MessageHomePagePresenterModule(this))
                .build()
                .inject(this);
        super.initView(rootView);
        initHeaderView();
    }

    @Override
    protected void initData() {
        super.initData();
        //mPresenter.requestNetData(0L,false);
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<InfoListDataBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        //closeLoadingView();
    }


    @Override
    public void setHeaderData(HomeMessageIndexBean data) {
        //轮播图
        initAdvert(data.getBanners());
        //行情
        ((CommonAdapter)mRvMarket.getAdapter()).refreshData(data.getJinsecaijing());
        //持币生息
        mTvYearRate.setText("+ "+data.getYear_rate()+"%");
        //资讯上边刷新
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        MultiItemTypeAdapter adapter = new MultiItemTypeAdapter(getActivity(), mListDatas);
        adapter.addItemViewDelegate(new InfoListItem(false) {
            @Override
            public void itemClick(int position, ImageView imageView, TextView title, InfoListDataBean realData) {
                if (TouristConfig.INFO_DETAIL_CAN_LOOK || !mPresenter.handleTouristControl()) {
                    if (!AppApplication.sOverRead.contains(realData.getId())) {
                        AppApplication.sOverRead.add(realData.getId().intValue());
                    }
                    FileUtils.saveBitmapToFile(getActivity(), ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                            , imageView.getDrawable(), R.mipmap.icon), "info_share.jpg");
                    title.setTextColor(getResources()
                            .getColor(R.color.normal_for_assist_text));
                    Intent intent = new Intent(getActivity(), InfoDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_INFO, realData);
                    bundle.putString(BUNDLE_INFO_TYPE, InfoContainerFragment.RECOMMEND_INFO);
                    intent.putExtra(BUNDLE_INFO, bundle);
                    startActivity(intent);
                }
            }
        });
        return adapter;
    }


    /**
     * 初始化头部view
     */
    private void initHeaderView(){

        View head = LayoutInflater.from(mActivity).inflate(R.layout.view_message_home_page_header,null);
        head.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mHeaderAndFooterWrapper.addHeaderView(head);

        mBanner = (Banner) head.findViewById(R.id.banner);
        mTvYearRate = (TextView) head.findViewById(R.id.tv_year_rate);
        //持币生息和应用推荐
        head.findViewById(R.id.fl_currency).setOnClickListener(v -> startActivity(new Intent(mActivity,CurrencyInterestActivity.class)));
        head.findViewById(R.id.tv_software).setOnClickListener(v -> CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_USE_RECOMMEND));

        //首页菜单
        mRvMenus = (RecyclerView) head.findViewById(R.id.rv_menus);
        mRvMenus.setLayoutManager(new GridLayoutManager(mActivity,5));
        mRvMenus.setAdapter(getMenuAdapter());

        //行情
        mRvMarket = (RecyclerView) head.findViewById(R.id.rv_market);
        mRvMarket.setLayoutManager(new LinearLayoutManager(mActivity,LinearLayoutManager.HORIZONTAL,false));
        mRvMarket.setAdapter(getMarketAdapter());

        //资讯，查看更多
        mBtNews = (CombinationButton) head.findViewById(R.id.bt_check_news);
        mBtNews.setRightText("查看更多");
        mBtNews.setOnClickListener(v -> {
            ((HomeFragment)((HomeActivity)mActivity).getContanierFragment()).setPagerSelection(2);
        });

    }

    /**
     * 菜单adapter
     * @return
     */
    private CommonAdapter getMenuAdapter(){
        CommonAdapter mAdapter = new CommonAdapter<MenuItem>(mActivity,R.layout.item_home_menu,getMenuItems()) {
            @Override
            protected void convert(ViewHolder holder, MenuItem menuItem, int position) {
                holder.getImageViwe(R.id.iv_icon).setImageDrawable(getResources().getDrawable(menuItem.imgResId));
                holder.getTextView(R.id.tv_menu).setText(menuItem.menu);
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ((MenuItem)mAdapter.getDatas().get(position)).mListener.onClick(view);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return mAdapter;
    }

    /**
     * 行情adapter
     * @return
     */
    private CommonAdapter getMarketAdapter(){
        CommonAdapter mAdapter = new CommonAdapter<MarketCurrencyBean>(mActivity,R.layout.item_home_market,new ArrayList<>()) {

            @Override
            protected void convert(ViewHolder holder, MarketCurrencyBean marketCurrencyBean, int position) {
                holder.getTextView(R.id.tv_currency).setText(marketCurrencyBean.currency_name+"/"+marketCurrencyBean.exchange_name);
                double degree = 0;
                try {
                    degree = Double.parseDouble(marketCurrencyBean.degree);
                } catch (Exception e) {

                }
                holder.setText(R.id.tv_degree, (degree > 0 ? "+" : "") + marketCurrencyBean.degree + "%");
                holder.getTextView(R.id.tv_degree).setTextColor(getResources()
                        .getColor(degree > 0 ? R.color.market_red : R.color.market_green));
                holder.setText(R.id.tv_price,"¥ "+marketCurrencyBean.last_cny);
                holder.getTextView(R.id.tv_price).setTextColor(getResources()
                        .getColor(degree > 0 ? R.color.market_red : R.color.market_green));
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if(!TextUtils.isEmpty(((MarketCurrencyBean)mAdapter.getDatas().get(position)).ticker)){
                    //跳转k线图
                    CurrencyKLineActivity.startActivity(mActivity, (MarketCurrencyBean) mAdapter.getDatas().get(position));
                }else {
                    ToastUtils.showToast("行情信息正在赶来的路上~");
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return mAdapter;
    }

    /**
     * 初始化广告图
     * @param advertList
     */
    private void initAdvert(List<RealAdvertListBean> advertList) {

        if (advertList == null || advertList.isEmpty()) {
            return;
        }

        List<String> advertTitle = new ArrayList<>();
        List<String> advertUrls = new ArrayList<>();
        List<String> advertLinks = new ArrayList<>();

        for (RealAdvertListBean advert : advertList) {
            advertTitle.add(advert.getTitle());
            advertUrls.add(advert.getAdvertFormat().getImage().getImage());
            advertLinks.add(advert.getAdvertFormat().getImage().getLink());
            if (advert.getType().equals("html")) {
                showStickyHtmlMessage((String) advert.getData());
            }
        }

        mBanner.setDelayTime(2000);
        mBanner.setImageLoader(new BannerImageLoaderUtil());
        mBanner.setImages(advertUrls);
        mBanner.setOnBannerListener(position ->
                CustomWEBActivity.startToWEBActivity(getActivity(), advertLinks.get(position), advertTitle.get(position)));
        mBanner.start();
    }

    /**
     * 获取菜单列表
     * @return
     */
    private List<MenuItem> getMenuItems(){
        List<MenuItem> list = new ArrayList<>();
        list.add(new MenuItem(R.mipmap.icon_home_menu_offical_group, "企业群", v -> {
            ((MessageContainerFragment)getParentFragment()).setCurrentItem(1);
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_community, "社区", v -> {
            startActivity(new Intent(mActivity, MyCircleContainerActivity.class));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_friend_circle, "朋友圈", v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_shop, "商城", v -> {
            /*CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_JIPU_SHOP);*/
            ToastUtils.showToast(mActivity,"暂未开放~");
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_question, "问答", v -> {
            Intent intent = new Intent(getActivity(), QA_Activity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_flash, "7x24快讯", v -> {
            // TODO: 2018/8/9  这里需要跳转到7*24快讯
            ToastUtils.showToast(mActivity,"暂未开放~");
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_news, "资讯", v -> {
            mBtNews.callOnClick();
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_market, "行情", v -> {
            MarketActivity.startMarketActivity(mActivity);
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_wallet, "资产", v -> {
            MyCurrencyActivity.startMyCurrencyActivity(getContext());
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu_rank, "排行榜", v -> {
            startActivity(new Intent(getActivity(), RankIndexActivity.class));
        }));
        return list;
    }

    @Override
    public void headClick(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
    }
}
