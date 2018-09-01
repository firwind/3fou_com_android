package com.zhiyicx.thinksnsplus.modules.home.common.invite;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jakewharton.rxbinding.view.RxView;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.StatusBarUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.ShareDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InviteAndQrcode;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:32
 *     desc :
 *     version : 1.0
 * <pre>
 */

public class InviteShareFragment extends TSFragment<InviteShareContract.Presenter> implements InviteShareContract.View{

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_invite_code)
    TextView mTvInviteCode;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.ll_content)
    LinearLayout mLLContent;
    @BindView(R.id.rv_share)
    RecyclerView mRvShare;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.tv_reward1)
    TextView mTvReward1;
    @BindView(R.id.tv_reward2)
    TextView mTvReward2;

    public static InviteShareFragment newInstance(){
        InviteShareFragment fragment = new InviteShareFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

        StatusBarUtils.statusBarDarkMode(mActivity);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
                    |WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            rootView.setFitsSystemWindows(false);

            int statusBarHeight = DeviceUtils.getStatuBarHeight(mActivity);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIvBack.getLayoutParams();
            params.topMargin = statusBarHeight;
            mIvBack.setLayoutParams(params);
            mIvBack.setOnClickListener(v ->
                    getActivity().onBackPressed());
            mLLContent.setPadding(0,statusBarHeight,0,0);
        }

        initShare();
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
    protected void initData() {

        showLoadingView();
        mPresenter.getInviteCode();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_invite_share;
    }


    @Override
    public void setInviteAndQrCode(InviteAndQrcode inviteAndQrCode) {
        mTvInviteCode.setText(inviteAndQrCode.user_code);
        mTvMsg.setText(inviteAndQrCode.user_msg);
        mTvReward1.setText(inviteAndQrCode.reward_msg1);
        mTvReward2.setText(inviteAndQrCode.reward_msg2);
        Bitmap bitmap;
        try {
            bitmap = null == mPresenter.getUserAvatar() ? BitmapFactory.decodeResource(getResources(),R.mipmap.icon)
                    : BitmapFactory.decodeFile(mPresenter.getUserAvatar());
        }catch (Exception e){
            bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.icon);
        }

        mIvQrcode.setImageBitmap(ImageUtils.createQrcodeImage(inviteAndQrCode.short_url,
                getResources().getDimensionPixelSize(R.dimen.invite_img_size),
                bitmap));
    }

    /**
     * 初始化分享
     */
    private void initShare(){
        List<ShareBean> mDatas = new ArrayList<>();
        ShareBean qq = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_qq,
                getString(com.zhiyicx.baseproject.R.string.qq_share),SHARE_MEDIA.QQ);
        ShareBean qZone = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_zone,
                getString(com.zhiyicx.baseproject.R.string.qZone_share),SHARE_MEDIA.QZONE);
        ShareBean weChat = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_wechat,
                getString(com.zhiyicx.baseproject.R.string.weChat_share),SHARE_MEDIA.WEIXIN);
        ShareBean weCircle = new ShareBean(com.zhiyicx.baseproject.R.mipmap.detail_share_friends,
                getString(com.zhiyicx.baseproject.R.string.weCircle_share),SHARE_MEDIA.WEIXIN_CIRCLE);
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
                        .subscribe(aVoid -> mPresenter.inviteShare(mLLContent,shareBean.shareMedia),
                                throwable -> throwable.printStackTrace());

            }
        });

    }


    private static class ShareBean {
        int image;
        String name;
        SHARE_MEDIA shareMedia;

        public ShareBean(int image, String name,SHARE_MEDIA shareMedia) {
            this.image = image;
            this.name = name;
            this.shareMedia = shareMedia;
        }
    }

}
