package com.zhiyicx.thinksnsplus.modules.certification.send;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.modules.certification.send.SendCertificationActivity.BUNDLE_SEND_CERTIFICATION;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/3
 * @contact email:648129313@qq.com
 */

public class SendCertificationFragment extends TSFragment<SendCertificationContract.Presenter>
        implements SendCertificationContract.View, PhotoSelectorImpl.IPhotoBackListener {

    private static final int PIC_ONE = 0;
    private static final int PIC_TWO = 1;

    @BindView(R.id.iv_pic_one)
    ImageView mIvPicOne;
    @BindView(R.id.fl_upload_pic_one)
    FrameLayout mFlUploadPicOne;
    @BindView(R.id.iv_pic_two)
    ImageView mIvPicTwo;
    @BindView(R.id.fl_upload_pic_two)
    FrameLayout mFlUploadPicTwo;
    @BindView(R.id.tv_type_hint)
    TextView mTvTypeHint;

    private PhotoSelectorImpl mPhotoSelector;
    private SendCertificationBean mSendBean;
    private List<ImageBean> selectedPhotos;

    private int mCurrentPosition = PIC_ONE;

    public SendCertificationFragment instance(Bundle bundle) {
        SendCertificationFragment fragment = new SendCertificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        initPhotoSelector();
        int width = UIUtil.getScreenWidth(getContext()) - getResources().getDimensionPixelSize(R.dimen.spacing_mid) * 2;
        // with : height = 4 ： 3
        mFlUploadPicOne.getLayoutParams().height = width * 3 / 4;
        mFlUploadPicTwo.getLayoutParams().height = width * 3 / 4;
        mFlUploadPicTwo.setVisibility(View.GONE);
    }

    @Override
    protected void initData() {
        mSendBean = getArguments().getParcelable(BUNDLE_SEND_CERTIFICATION);
        if (mSendBean != null && mSendBean.getType().equals(SendCertificationBean.ORG)) {
            mTvTypeHint.setText(getString(R.string.send_certification_company));
        }
        setRightClickable();
        selectedPhotos = new ArrayList<>();
        initListener();
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_send_certification;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.send_certification_data);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.submit);
    }


    @Override
    protected void setRightClick() {
        mPresenter.sendCertification(mSendBean);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (photoList == null || photoList.size() == 0) {
            return;
        }
        if (mCurrentPosition == PIC_ONE) {
            Glide.with(getContext())
                    .load(photoList.get(0).getImgUrl())
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .into(mIvPicOne);
            if (selectedPhotos.size() == 0) {
                selectedPhotos.add(photoList.get(0));
            } else {
                selectedPhotos.set(0, photoList.get(0));
            }
            if (mSendBean.getType().equals(SendCertificationBean.USER)) {
                mFlUploadPicTwo.setVisibility(View.VISIBLE);
            }
        } else if (mCurrentPosition == PIC_TWO) {
            Glide.with(getContext())
                    .load(photoList.get(0).getImgUrl())
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .into(mIvPicTwo);
            if (selectedPhotos.size() == 1) {
                selectedPhotos.add(photoList.get(0));
            } else {
                selectedPhotos.set(1, photoList.get(0));
            }
        }
        mSendBean.setPicList(selectedPhotos);
        setRightClickable();
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    private void initListener() {
        RxView.clicks(mFlUploadPicOne)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mCurrentPosition = PIC_ONE;
                    mPhotoSelector.getPhotoListFromSelector(1, getSelectedPhotoPath(PIC_ONE), true,false);
                });
        RxView.clicks(mFlUploadPicTwo)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    mCurrentPosition = PIC_TWO;
                    mPhotoSelector.getPhotoListFromSelector(1, getSelectedPhotoPath(PIC_TWO), true,false);
                });
    }

    /**
     * @return 已经选择了图片的地址
     */
    @NonNull
    private ArrayList<String> getSelectedPhotoPath(int position) {
        ArrayList<String> photos = new ArrayList<>();

        if (position == 0 && !selectedPhotos.isEmpty()) {
            ImageBean imageBean = selectedPhotos.get(0);
            if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                photos.add(imageBean.getImgUrl());
            }
        } else if (position == 1 && selectedPhotos.size() >= 2) {
            ImageBean imageBean = selectedPhotos.get(1);
            if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                photos.add(imageBean.getImgUrl());
            }
        }

        return photos;
    }

    /**
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build()
                .photoSelectorImpl();
    }

    private void setRightClickable() {
        boolean clickable = false;
        if (mSendBean.getType().equals(SendCertificationBean.USER)
                && mSendBean.getPicList() != null
                && mSendBean.getPicList().size() == 2) {
            clickable = true;
        }
        if (mSendBean.getType().equals(SendCertificationBean.ORG)
                && mSendBean.getPicList() != null
                && mSendBean.getPicList().size() == 1) {
            clickable = true;
        }
        mToolbarRight.setClickable(clickable);
        mToolbarRight.setTextColor(clickable ? ContextCompat.getColorStateList(getContext(), com.zhiyicx.baseproject.R.color.selector_text_color)
                : ContextCompat.getColorStateList(getContext(), com.zhiyicx.baseproject.R.color.normal_for_assist_text));
    }

    @Override
    public void updateSendState(boolean isSending, boolean isSuccess, String message) {
        if (isSending) {
            showSnackLoadingMessage(message);
            mFlUploadPicOne.setEnabled(false);
            mFlUploadPicTwo.setEnabled(false);
        } else {
            if (isSuccess) {
                mNetTip = message;
                showSnackSuccessMessage(message);
            } else {
                showSnackErrorMessage(message);
            }
            mFlUploadPicOne.setEnabled(true);
            mFlUploadPicTwo.setEnabled(true);
        }
    }

    private String mNetTip;

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt, String message) {
        super.snackViewDismissWhenTimeOut(prompt, message);
        if (getActivity() != null && Prompt.SUCCESS == prompt && message.equals(mNetTip)) {
            getActivity().finish();
        }
    }
}
