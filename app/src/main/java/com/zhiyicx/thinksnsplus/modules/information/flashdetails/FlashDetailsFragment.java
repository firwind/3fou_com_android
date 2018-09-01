package com.zhiyicx.thinksnsplus.modules.information.flashdetails;
/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.ShareDecoration;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.NoDefaultPadingTextView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

public class FlashDetailsFragment extends TSFragment<FlashDetailsContract.Presenter> implements FlashDetailsContract.View {
    @BindView(R.id.rv_share)
    RecyclerView mRvShare;
    @BindView(R.id.flash_details_sv)
    ScrollView mScrollView;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.view_bull)
    View mBull;
    @BindView(R.id.view_fall)
    View mFall;
    InfoListDataBean infoListDataBean;
    @BindView(R.id.tv_bull)
    TextView tvBull;
    @BindView(R.id.tv_bear_news)
    TextView tvBearNews;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_info_title)
    NoDefaultPadingTextView tvInfoTitle;
    @BindView(R.id.tv_info_content)
    NoDefaultPadingTextView tvInfoContent;
    @BindView(R.id.iv_back)
    ImageView mIvBack;

    public static FlashDetailsFragment newInstance(Bundle bundle) {
        FlashDetailsFragment fragment = new FlashDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        StatusBarUtils.statusBarDarkMode(mActivity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    | WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            rootView.setFitsSystemWindows(false);
        }
        initShare();

        int statusBarHeight = com.zhiyicx.common.utils.DeviceUtils.getStatuBarHeight(mActivity);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIvBack.getLayoutParams();
        params.topMargin = statusBarHeight;
        mIvBack.setLayoutParams(params);
        mIvBack.setOnClickListener(v -> getActivity().onBackPressed());

        mPresenter.getInviteCode();//获取分享数据
    }


    private void initShare() {
        List<ShareBean> mDatas = new ArrayList<>();
        ShareBean qq = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_qq,
                getString(com.zhiyicx.baseproject.R.string.qq_share), SHARE_MEDIA.QQ);
        ShareBean qZone = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_zone,
                getString(com.zhiyicx.baseproject.R.string.qZone_share), SHARE_MEDIA.QZONE);
        ShareBean weChat = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_wechat,
                getString(com.zhiyicx.baseproject.R.string.weChat_share), SHARE_MEDIA.WEIXIN);
        ShareBean weCircle = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_friends,
                getString(com.zhiyicx.baseproject.R.string.weCircle_share), SHARE_MEDIA.WEIXIN_CIRCLE);
//        ShareBean weibo = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_weibo,
//                getString(com.zhiyicx.baseproject.R.string.weibo_share),SHARE_MEDIA.SINA);
        mDatas.add(qq);
        mDatas.add(qZone);
        mDatas.add(weChat);
        mDatas.add(weCircle);
//        mDatas.add(weibo);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 4);
        mRvShare.addItemDecoration(new ShareDecoration(getResources().getDimensionPixelSize(R.dimen.spacing_mid)));
        mRvShare.setLayoutManager(gridLayoutManager);

        mRvShare.setAdapter(new CommonAdapter<ShareBean>(mActivity, R.layout.item_share_popup_window, mDatas) {
            @Override
            protected void convert(ViewHolder holder, ShareBean shareBean, final int position) {
                holder.setImageResource(R.id.iv_share_type_image, shareBean.image);
                holder.setText(R.id.iv_share_type_name, shareBean.name);
                RxView.clicks(holder.getConvertView())
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> mPresenter.inviteShare(mScrollView, shareBean.shareMedia),
                                throwable -> throwable.printStackTrace());
            }
        });


    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            infoListDataBean = (InfoListDataBean) getArguments().getSerializable(BUNDLE_INFO);
            tvBull.setText(getString(R.string.bull) + infoListDataBean.getDigg_count());
            tvBearNews.setText(getString(R.string.bear_news) + infoListDataBean.getUndigg_count());
            tvTime.setText(TimeUtils.getTimeFriendlyNormal(infoListDataBean
                    .getCreated_at()));
            tvInfoTitle.setText(infoListDataBean.getTitle());
            tvInfoContent.setText(infoListDataBean.getContent());
            initLine();
        }

        closeLoadingView();

    }

    /**
     * 初始化利空和利好比例直线
     */
    private void initLine() {
        int sun = infoListDataBean.getDigg_count() + infoListDataBean.getUndigg_count();
        DecimalFormat df = new DecimalFormat("0.00");//格式化小数
        String numBull = df.format((float) infoListDataBean.getUndigg_count() / sun);//返回的是String类型
        String numFall = df.format((float) infoListDataBean.getDigg_count() / sun);//返回的是String类型
        float fBull = infoListDataBean.getUndigg_count() == 0 ? 1 : Float.valueOf(numBull) * 100;
        float fFall = infoListDataBean.getDigg_count() == 0 ? 1 : Float.valueOf(numFall) * 100;
        mBull.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) DeviceUtils.dpToPixel(getContext(), 2), fBull));
        mFall.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) DeviceUtils.dpToPixel(getContext(), 2), fFall));
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
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }


    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_flash_details;
    }

    @Override
    public void setInviteAndQrCode(InviteAndQrcode inviteAndQrCode) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
        mIvQrcode.setImageBitmap(ImageUtils.createQrcodeImage(inviteAndQrCode.short_url,
                getResources().getDimensionPixelSize(R.dimen.invite_img_size),
                bitmap));
    }

    private static class ShareBean {
        int image;
        String name;
        SHARE_MEDIA shareMedia;

        public ShareBean(int image, String name, SHARE_MEDIA shareMedia) {
            this.image = image;
            this.name = name;
            this.shareMedia = shareMedia;
        }
    }
}
