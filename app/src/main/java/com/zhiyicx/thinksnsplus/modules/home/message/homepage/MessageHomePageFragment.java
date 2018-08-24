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
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

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
import com.zhiyicx.thinksnsplus.data.beans.CurrencyRankBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.MarketCurrencyBean;
import com.zhiyicx.thinksnsplus.data.beans.MenuItem;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.HomeMessageIndexBean;
import com.zhiyicx.thinksnsplus.modules.chat.select.addgroup.AddGroupActivity;
import com.zhiyicx.thinksnsplus.modules.circle.main.CircleMainActivity;
import com.zhiyicx.thinksnsplus.modules.circle.mine.container.MyCircleContainerActivity;
import com.zhiyicx.thinksnsplus.modules.currency.MyCurrencyActivity;
import com.zhiyicx.thinksnsplus.modules.currency.interest.CurrencyInterestActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeFragment;
import com.zhiyicx.thinksnsplus.modules.home.common.invite.InviteShareActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.market.details.CurrencyKLineActivity;
import com.zhiyicx.thinksnsplus.modules.home.main.MainActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoBannerHeader;
import com.zhiyicx.thinksnsplus.modules.information.adapter.InfoListItem;
import com.zhiyicx.thinksnsplus.modules.information.flashdetails.FlashDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;
import com.zhiyicx.thinksnsplus.modules.q_a.QA_Activity;
import com.zhiyicx.thinksnsplus.modules.rank.main.container.RankIndexActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.integration.mine.newIntegration.NewMineIntegrationActivity;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhiyicx.thinksnsplus.widget.VerticalTextSwitcher;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
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
    private VerticalTextSwitcher mTextSwitcher;
    private TextView mTvFlashNewsMore;

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

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
        mPresenter.requestNetData(0L,false);
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }


    @Override
    public void setHeaderData(HomeMessageIndexBean data) {
        //轮播图
        initAdvert(data.getBanners());
        //行情
        ((CommonAdapter)mRvMarket.getAdapter()).refreshData(null == data.getJinsecaijing()?new ArrayList():data.getJinsecaijing());
        //持币生息
        mTvYearRate.setText("+ "+data.getYear_rate()+"%");
        //资讯上边刷新

        if(null != data && null != data.getFlash() && data.getFlash().size()>0){
            //快讯
            List<String> list = new ArrayList<>();
            for (InfoListDataBean info:data.getFlash()) {
                list.add(info.getTitle());
            }
            mTextSwitcher.setupData(list);
            mTextSwitcher.setOnClickListener(v ->
                    FlashDetailsActivity.startActivity(mActivity,data.getFlash().get(mTextSwitcher.getCurPosition())));
        }
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
            ((HomeFragment)((HomeActivity)mActivity).getContanierFragment()).goToNewsPage(false);
        });

        //快讯
        mTextSwitcher = (VerticalTextSwitcher) head.findViewById(R.id.text_switcher);
        mTvFlashNewsMore = (TextView) head.findViewById(R.id.tv_flash_news_more);
        mTvFlashNewsMore.setOnClickListener(v -> {
            ((HomeFragment)((HomeActivity)mActivity).getContanierFragment()).goToNewsPage(true);
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
        CommonAdapter mAdapter = new CommonAdapter<CurrencyRankBean>(mActivity,R.layout.item_home_market,new ArrayList<>()) {

            @Override
            protected void convert(ViewHolder holder, CurrencyRankBean marketCurrencyBean, int position) {

                holder.getView(R.id.ll_parent).setBackgroundResource(position%2==0?R.mipmap.bg_home_market_1:R.mipmap.bg_home_market_2);

                holder.getTextView(R.id.tv_currency).setText(marketCurrencyBean.currency);
                holder.setText(R.id.tv_degree, (marketCurrencyBean.change > 0 ? "+" : "") + marketCurrencyBean.change + "%");
                holder.getTextView(R.id.tv_degree).setTextColor(getResources()
                        .getColor(marketCurrencyBean.change > 0 ? R.color.market_red : R.color.market_green));
                holder.setText(R.id.tv_price,"¥ "+marketCurrencyBean.price);
                holder.getTextView(R.id.tv_price).setTextColor(getResources()
                        .getColor(marketCurrencyBean.change > 0 ? R.color.market_red : R.color.market_green));
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MarketActivity.startMarketActivity(mActivity);
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
        list.add(new MenuItem(R.mipmap.icon_home_menu1, "群组", v -> {
            AddGroupActivity.startAddGroupActivity(getContext());
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu2, "社区", v -> {
            startActivity(new Intent(getActivity(), CircleMainActivity.class));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu3, "朋友圈", v -> {
            startActivity(new Intent(getActivity(), MainActivity.class));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu4, "商城", v -> {
            /*CustomWEBActivity.startToWEBActivity(getContext(), ApiConfig.URL_JIPU_SHOP);*/
            ToastUtils.showToast(mActivity,"暂未开放~");
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu5, "问答", v -> {
            Intent intent = new Intent(getActivity(), QA_Activity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu6, "邀请好友", v -> {
            startActivity(InviteShareActivity.newIntent(mActivity));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu7, "糖果", v -> {
            startActivity(new Intent(mActivity,NewMineIntegrationActivity.class));
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu8, "行情", v -> {
            MarketActivity.startMarketActivity(mActivity);
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu9, "资产", v -> {
            MyCurrencyActivity.startMyCurrencyActivity(getContext());
        }));
        list.add(new MenuItem(R.mipmap.icon_home_menu10, "签到", v -> {
            /*startActivity(new Intent(getActivity(), RankIndexActivity.class));*/
            ((HomeFragment)((HomeActivity)mActivity).getContanierFragment()).getCheckInInfo();
        }));
        return list;
    }

    @Override
    public void headClick(String link, String title) {
        CustomWEBActivity.startToWEBActivity(getActivity(), link, title);
    }
}
