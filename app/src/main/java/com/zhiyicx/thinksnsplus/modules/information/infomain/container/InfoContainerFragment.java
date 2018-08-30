package com.zhiyicx.thinksnsplus.modules.information.infomain.container;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.base.TSViewPagerAdapter;
import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.baseproject.config.TouristConfig;
import com.zhiyicx.baseproject.widget.TabSelectView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoTypeCatesBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity;
import com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.information.infochannel.ChannelActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.modules.information.infomain.flash.FlashListFragment;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListFragment;
import com.zhiyicx.thinksnsplus.modules.information.infosearch.SearchActivity;
import com.zhiyicx.thinksnsplus.modules.information.publish.detail.EditeInfoDetailActivity;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_DATA;
import static com.zhiyicx.thinksnsplus.modules.certification.detail.CertificationDetailActivity.BUNDLE_DETAIL_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_CERTIFICATION_TYPE;
import static com.zhiyicx.thinksnsplus.modules.certification.input.CertificationInputActivity.BUNDLE_TYPE;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO_TYPE;

/**
 * @Author Jliuer
 * @Date 2017/03/03
 * @Email Jliuer@aliyun.com
 * @Description 资讯的分类
 */
public class InfoContainerFragment extends TSViewPagerFragment<InfoMainContract.InfoContainerPresenter>
        implements InfoMainContract.InfoContainerView {

    private List<String> mTitle;

    public static final String SUBSCRIBE_EXTRA = "mycates";
    public static final String RECOMMEND_INFO = "-1";
    public static final String FLASH_INFO_ID = "7";
    public static final String VIDEO_INFO_ID = "8";
    public static final int REQUEST_CODE = 0;

    /**
     * 仅用于构造
     */
    @Inject
    InfoContainerPresenter mDynamicPresenter;

    private UserCertificationInfo mUserCertificationInfo;

    // 提示需要认证的
    private ActionPopupWindow mCertificationAlertPopWindow;

    // 提示需要付钱的
    private ActionPopupWindow mPayAlertPopWindow;

    private InfoTypeBean mInfoTypeBean;

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    //    @Override
//    protected int setTitleDrawableLeft() {
//        return R.mipmap.btn_open;
//    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.ico_news_contribute;
    }

    @Override
    protected void setRightClick() {
        // 发布提示 1、首先需要认证 2、需要付费
        if (mPresenter.handleTouristControl()) {
            return;
        }
        mPresenter.checkCertification();
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View rootView) {
        DaggerInfoContainerFComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .infoContainerPresenterModule(
                        new InfoContainerPresenterModule(this))
                .build()
                .inject(this);
        super.initView(rootView);

        mPresenter.getInfoType();
    }

    public static InfoContainerFragment newInstance() {
        InfoContainerFragment fragment = new InfoContainerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserCertificationInfo(UserCertificationInfo userCertificationInfo) {
        mUserCertificationInfo = userCertificationInfo;
        mSystemConfigBean = mPresenter.getSystemConfigBean();
        SystemConfigBean.NewsConfig publishInfoConfig = mSystemConfigBean.getNewsContribute();
        if (userCertificationInfo.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value || !publishInfoConfig.hasVerified()) {
            if (mPresenter.isNeedPayTip() && (publishInfoConfig != null
                    && publishInfoConfig.hasPay())) {
                showPayWindow();
            } else {

                startActivity(new Intent(getActivity(), EditeInfoDetailActivity.class));
            }
        } else {
            showCertificationWindow();
        }
    }

    @Override
    protected void initViewPager(View rootView) {

        mTsvToolbar = (TabSelectView) rootView.findViewById(com.zhiyicx.baseproject.R.id.tsv_toolbar);
        mTsvToolbar.setRightImg(R.mipmap.sec_nav_arrow, R.color.white);
        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.setDefaultTabLinehegiht(R.integer.no_line_height);
        mTsvToolbar.setDefaultTabLeftMargin(com.zhiyicx.baseproject.R.integer.tab_margin_10);
        mTsvToolbar.setDefaultTabRightMargin(com.zhiyicx.baseproject.R.integer.tab_margin_10);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setIndicatorMatchWidth(true);
        mTsvToolbar.setIndicatorMode(LinePagerIndicator.MODE_MATCH_EDGE);
        mTsvToolbar.setTabSpacing(getResources().getDimensionPixelOffset(R.dimen.info_container_tab_spacing));
        mVpFragment = (ViewPager) rootView.findViewById(R.id.vp_fragment);
        tsViewPagerAdapter = new TSViewPagerAdapter(getChildFragmentManager());
        tsViewPagerAdapter.bindData(initFragments());
        mVpFragment.setAdapter(tsViewPagerAdapter);
        mTsvToolbar.setAdjustMode(false);
        mTsvToolbar.initTabView(mVpFragment, initTitles());
        mTsvToolbar.setLeftClickListener(this, this::setLeftClick);
        mTsvToolbar.setRightClickListener(this, () -> {
            if (mInfoTypeBean == null || mPresenter.handleTouristControl()) {
                return;
            }
            Intent intent = new Intent(getActivity(), ChannelActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(BUNDLE_INFO_TYPE, mInfoTypeBean);
            intent.putExtra(BUNDLE_INFO_TYPE, bundle);
            startActivityForResult(intent, REQUEST_CODE);
            getActivity().overridePendingTransition(R.anim.slide_from_top_enter, R.anim
                    .slide_from_top_quit);
        });
        mVpFragment.setOffscreenPageLimit(getOffsetPage());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            for (int i = 0;i<mTitle.size();i++){
                if (i!=0&&i!=1){
                    mTitle.remove(mTitle.get(i));
                    i--;
                }
            }
            for (int i = 0;i<mFragmentList.size();i++){
                if (i!=0&&i!=1){
                    mFragmentList.remove(mFragmentList.get(i));
                    i--;
                }
            }
            mInfoTypeBean = data.getBundleExtra(SUBSCRIBE_EXTRA).getParcelable(SUBSCRIBE_EXTRA);

            Observable.from(mInfoTypeBean.getMy_cates())
                    .subscribe(myCatesBean -> {
                        mTitle.add(myCatesBean.getName());
                        mFragmentList.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
                    });
            mTsvToolbar.notifyDataSetChanged(mTitle);
            tsViewPagerAdapter.bindData(mFragmentList, mTitle.toArray(new String[]{}));
            mVpFragment.setOffscreenPageLimit(mTitle.size());
        }

    }

    @Override
    protected int setRightLeftImg() {
        return R.mipmap.ico_search;
    }

    @Override
    protected void setRightLeftClick() {
        super.setRightLeftClick();
        if (!TouristConfig.INFO_CAN_SEARCH && mPresenter.handleTouristControl()) {
            return;
        }
        startActivity(new Intent(getActivity(), SearchActivity.class));
    }

    @Override
    public void setInfoType(InfoTypeBean infoType) {
        mInfoTypeBean = infoType;
        //mFragmentList.clear();
//        mInfoTypeBean.getMy_cates().add(0, new InfoTypeCatesBean(-1L, getString(R.string
//                .flash), true));
        for (InfoTypeCatesBean myCatesBean : infoType.getMy_cates()) {
            if (mInfoTypeBean.getMy_cates().indexOf(myCatesBean) != -1
                    && !mTitle.contains(myCatesBean.getName())) {
                LogUtils.d(myCatesBean.getName());
                mTitle.add(myCatesBean.getName());
//                if (mInfoTypeBean.getMy_cates().indexOf(myCatesBean) == 0){
//                    mFragmentList.add(FlashListFragment.newInstance(myCatesBean.getId() + ""));
//                }else {
                    mFragmentList.add(InfoListFragment.newInstance(myCatesBean.getId() + ""));
//                }
            }
        }
        mTsvToolbar.notifyDataSetChanged(mTitle);
        tsViewPagerAdapter.bindData(mFragmentList, mTitle.toArray(new String[]{}));
        mVpFragment.setOffscreenPageLimit(mTitle.size());
    }

    @Override
    public void setPresenter(InfoMainContract.InfoContainerPresenter infoContainerPresenter) {
        mPresenter = infoContainerPresenter;
    }


    @Override
    protected List<String> initTitles() {
        if (mTitle == null) {
            mTitle = new ArrayList<>();
            mTitle.add(0,getString(R.string.flash));
            mTitle.add(1,getString(R.string.videos));
        }
        return mTitle;
    }

    @Override
    protected List<Fragment> initFragments() {

        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
            mFragmentList.add(0,FlashListFragment.newInstance(FLASH_INFO_ID));
            mFragmentList.add(1,InfoListFragment.newInstance(VIDEO_INFO_ID));
        }
        return mFragmentList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getActivity(), HomeActivity.class));
    }

    /**
     * 切换方法，暴露给外部调用
     * @param index
     */
    public void setCurrentItem(int index){
        if(!isAdded())
            return;
        if(index >= mFragmentList.size())
            index = 0;
        if(mFragmentList.size() != 0)
            mVpFragment.setCurrentItem(index,false);

    }

    /**
     * 支付弹窗
     */
    private void showPayWindow(){
        if (mPayAlertPopWindow == null) {
            mPayAlertPopWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .item6Str(getString(R.string.info_publish_go_to_next))
                    .desStr(getString(R.string.info_publish_hint_pay, mPresenter.getSystemConfigBean().getNewsPayContribute(), mPresenter.getGoldName()))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .bottomClickListener(() -> mPayAlertPopWindow.hide())
                    .item6ClickListener(() -> {
                        mPayAlertPopWindow.hide();
                        mPresenter.savePayTip(false);
                        startActivity(new Intent(getActivity(), EditeInfoDetailActivity.class));
                    })
                    .build();
        }

        mPayAlertPopWindow.show();
    }

    /**
     * 认证弹窗
     */
    private void showCertificationWindow(){
        if (mCertificationAlertPopWindow == null) {
            mCertificationAlertPopWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .item2Str(getString(R.string.certification_personage))
                    .item3Str(getString(R.string.certification_company))
                    .desStr(getString(R.string.info_publish_hint_certification))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .bottomClickListener(() -> mCertificationAlertPopWindow.hide())
                    .item2ClickListener(() -> {
                        // 个人认证
                        mCertificationAlertPopWindow.hide();
                        // 待审核
                        if (mUserCertificationInfo != null
                                && mUserCertificationInfo.getId() != 0
                                && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {
                            Intent intentToDetail = new Intent(getActivity(), CertificationDetailActivity.class);
                            Bundle bundleData = new Bundle();
                            bundleData.putInt(BUNDLE_DETAIL_TYPE, 0);
                            bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                            intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                            startActivity(intentToDetail);
                        } else {
                            Intent intent = new Intent(getActivity(), CertificationInputActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BUNDLE_TYPE, 0);
                            intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
                            startActivity(intent);
                        }
                    })
                    .item3ClickListener(() -> {
                        // 企业认证
                        mCertificationAlertPopWindow.hide();
                        // 待审核
                        if (mUserCertificationInfo != null
                                && mUserCertificationInfo.getId() != 0
                                && mUserCertificationInfo.getStatus() != UserCertificationInfo.CertifyStatusEnum.REJECTED.value) {

                            Intent intentToDetail = new Intent(getActivity(), CertificationDetailActivity.class);
                            Bundle bundleData = new Bundle();
                            bundleData.putInt(BUNDLE_DETAIL_TYPE, 1);
                            bundleData.putParcelable(BUNDLE_DETAIL_DATA, mUserCertificationInfo);
                            intentToDetail.putExtra(BUNDLE_DETAIL_TYPE, bundleData);
                            startActivity(intentToDetail);
                        } else {
                            Intent intent = new Intent(getActivity(), CertificationInputActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BUNDLE_TYPE, 1);
                            intent.putExtra(BUNDLE_CERTIFICATION_TYPE, bundle);
                            startActivity(intent);
                        }
                    })
                    .build();
        }
        mCertificationAlertPopWindow.show();
    }

}
