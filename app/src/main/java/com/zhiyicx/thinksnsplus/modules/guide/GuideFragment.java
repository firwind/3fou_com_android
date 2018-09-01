package com.zhiyicx.thinksnsplus.modules.guide;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.login.LoginActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.BannerImageLoaderUtil;
import com.zhiyicx.thinksnsplus.widget.TCountTimer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

public class GuideFragment extends TSFragment<GuideContract.Presenter> implements
        GuideContract.View, OnBannerListener {

    @BindView(R.id.guide_banner)
    Banner mGuideBanner;
    @BindView(R.id.guide_text)
    TextView mGuideText;

    public static final String ADVERT = "advert";

    private List<RealAdvertListBean> mBootAdverts;

    private Handler mHandler = new Handler();

    private Runnable runnable = () -> {
        //直接进入
        startActivity(new Intent(mActivity,mPresenter.checkLogin()? HomeActivity.class: LoginActivity.class));
        mActivity.finish();
    };

    @Override
    protected boolean setStatusbarGrey() {
        return false;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }


    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_guide_v2;
    }

    @Override
    protected void initView(View rootView) {

        RxView.clicks(mGuideText).throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid ->
                        //直接进入
                        mHandler.post(runnable));

    }

    @Override
    protected void initData() {

        if (com.zhiyicx.common.BuildConfig.USE_ADVERT && null != mPresenter.getBootAdvert()) {
            mBootAdverts = mPresenter.getBootAdvert();
            List<String> urls = new ArrayList<>();
            for (RealAdvertListBean realAdvertListBean : mBootAdverts) {
                urls.add(realAdvertListBean.getAdvertFormat().getImage().getImage());
            }
            int time = mBootAdverts.get(0).getAdvertFormat().getImage().getDuration() * 1000;
            time = time > 0 ? time : 3000;
            mGuideBanner.setBannerStyle(BannerConfig.NOT_INDICATOR);
            mGuideBanner.setImageLoader(new BannerImageLoaderUtil());
            mGuideBanner.setImages(urls);
            mGuideBanner.isDownStopAutoPlay(false);
            mGuideBanner.setViewPagerIsScroll(false);
            mGuideBanner.setDelayTime(time);
            mGuideBanner.setOnBannerListener(this);
            mGuideBanner.start();

            mGuideText.setVisibility(View.VISIBLE);
            mHandler.postDelayed(runnable,2000);

        }else {
            //直接进入
//            startActivity(new Intent(mActivity,mPresenter.checkLogin()? HomeActivity.class: LoginActivity.class));
            mHandler.post(runnable);
        }
        //初始化配置
        mPresenter.initConfig();
    }

    public void onNewIntent(Intent intent){
        mHandler.post(runnable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mHandler.post(runnable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler.removeCallbacks(runnable);
        if(null != mGuideBanner)
            mGuideBanner.releaseBanner();
    }

    @Override
    public void OnBannerClick(int position) {

        mHandler.removeCallbacks(runnable);

        CustomWEBActivity.startToWEBActivity(mActivity, mBootAdverts.get(position)
                        .getAdvertFormat().getImage().getLink(), mBootAdverts.get(position).getTitle(), ADVERT);
    }



}
