package com.zhiyicx.thinksnsplus.modules.dynamic.send;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.jakewharton.rxbinding.widget.RxRadioGroup;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.tym.shortvideo.media.VideoInfo;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.button.CombinationButton;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ActivityHandler;
import com.zhiyicx.common.utils.AndroidBug5497Workaround;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.imsdk.utils.common.DeviceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.GroupDynamicListBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupSendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean;
import com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBeanV2;
import com.zhiyicx.thinksnsplus.modules.dynamic.send.picture_toll.PictureTollActivity;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewActivity;
import com.zhiyicx.thinksnsplus.modules.photopicker.PhotoViewFragment;
import com.zhiyicx.thinksnsplus.modules.shortvideo.cover.CoverActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.videostore.VideoSelectActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.IconTextView;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLLBUNDLE;
import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.TOLL_TYPE;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.DOWNLOAD_TOLL_TYPE;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.LOOK_TOLL;
import static com.zhiyicx.baseproject.impl.photoselector.Toll.LOOK_TOLL_TYPE;
import static com.zhiyicx.thinksnsplus.data.beans.SendDynamicDataBean.TEXT_ONLY_DYNAMIC;


/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 * @See
 */
public class SendDynamicFragment extends TSFragment<SendDynamicContract.Presenter> implements
        SendDynamicContract.View, PhotoSelectorImpl.IPhotoBackListener {

    /**
     * recyclerView的每行item个数
     */
    private static final int ITEM_COLUM = 4;

    /**
     * 一共可选的图片数量
     */
    public static final int MAX_PHOTOS = 9;

    @BindView(R.id.rv_photo_list)
    RecyclerView mRvPhotoList;
    @BindView(R.id.et_dynamic_title)
    UserInfoInroduceInputView mEtDynamicTitle;
    @BindView(R.id.et_dynamic_content)
    UserInfoInroduceInputView mEtDynamicContent;
    @BindView(R.id.tv_toll)
    CombinationButton mTvToll;
    @BindView(R.id.send_dynamic_ll_toll)
    LinearLayout mLLToll;
    @BindView(R.id.ll_send_dynamic)
    LinearLayout mLlSendDynamic;
    @BindView(R.id.tv_choose_tip)
    TextView mTvChooseTip;
    @BindView(R.id.tv_word_limit)
    TextView mTvWordsLimit;
    @BindView(R.id.rb_one)
    RadioButton mRbOne;
    @BindView(R.id.rb_two)
    RadioButton mRbTwo;
    @BindView(R.id.rb_three)
    RadioButton mRbThree;
    @BindView(R.id.rb_days_group)
    RadioGroup mRbDaysGroup;
    @BindView(R.id.et_input)
    EditText mEtInput;
    @BindView(R.id.sl_send_dynamic)
    ScrollView mSlSendDynamic;
    @BindView(R.id.v_line_toll)
    View mTollLine;
    @BindView(R.id.v_horizontal_line)
    View mHoriLine;

    @BindView(R.id.tv_custom_money)
    TextView mCustomMoney;

    /**
     * // 已经选择的图片
     */
    private List<ImageBean> selectedPhotos;

    private CommonAdapter<ImageBean> mCommonAdapter;

    private List<ImageBean> cachePhotos;

    /**
     * 图片选择弹框
     */
    private ActionPopupWindow mPhotoPopupWindow;

    private ActionPopupWindow mCanclePopupWindow;
    private PhotoSelectorImpl mPhotoSelector;

    /**
     * 状态值用来判断发送状态
     */
    private boolean hasContent;

    /**
     * 需要发送的动态类型
     */
    private int dynamicType;

    /**
     * 是否开启收费
     */
    private boolean isToll;

    private boolean isFromGroup;

    /**
     * 是否有图片设置了收费
     */
    private boolean hasTollPic;

    /**
     * 文字收费选择
     */
    private ArrayList<Float> mSelectMoney;

    /**
     * 文字收费金额
     */
    private double mTollMoney;

    /**
     * 各类提示信息弹窗
     */
    private ActionPopupWindow mInstructionsPopupWindow;

    private SendDynamicDataBean mSendDynamicDataBean;

    private int mCurrentTollImageBean;

    public static SendDynamicFragment initFragment(Bundle bundle) {
        SendDynamicFragment sendDynamicFragment = new SendDynamicFragment();
        sendDynamicFragment.setArguments(bundle);
        return sendDynamicFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_send_dynamic;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.send_dynamic);
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.publish);
    }

    @Override
    protected void setLeftClick() {
        handleBack();
    }

    @Override
    public void onBackPressed() {
        handleBack();
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    /**
     * 处理取消发布动态
     */
    private void handleBack() {
        boolean noPic = selectedPhotos == null || !isToll && selectedPhotos.size() <= 1;
        if (hasContent || !noPic) {
            DeviceUtils.hideSoftKeyboard(getContext(), mEtDynamicContent);
            initCanclePopupWindow();
            mCanclePopupWindow.show();
        } else {
            super.setLeftClick();
        }
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        mTvToll.setLeftTextSize(getResources().getInteger(R.integer.dynamic_toll_tip_text_size));
        // 设置右边发布文字监听
        initSendDynamicBtnState();
        // 设置左边取消文字的颜色为主题色
        setLeftTextColor();
        initDynamicType();
        setSendDynamicState();
        initWordsToll();
        // 配置收费按钮隐藏显示
        initTollState();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            AndroidBug5497Workaround.assistActivity(getActivity());
        }
    }

    private void initTollState() {
        boolean canPay = mPresenter.getSystemConfigBean().getFeed().hasPaycontrol();
        mTvToll.setVisibility(canPay && dynamicType != SendDynamicDataBean.VIDEO_TEXT_DYNAMIC ? View.VISIBLE : View.GONE);
        if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
            mHoriLine.setVisibility(View.GONE);
            mTollLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initData() {
        mSelectMoney = new ArrayList<>();
        mSelectMoney.add(1f);
        mSelectMoney.add(5f);
        mSelectMoney.add(10f);

        mSystemConfigBean = mPresenter.getSystemConfigBean();

        if (mSystemConfigBean != null) {
            int[] amount = new int[]{};
            if (mSystemConfigBean.getFeed() != null) {
                amount = mSystemConfigBean.getFeed().getItems();
                int wordLimit = mSystemConfigBean.getFeed().getLimit();
                mTvWordsLimit.setText(String.format(getString(R.string.dynamic_send_toll_notes), wordLimit > 0 ? wordLimit : 50));
            }
            if (amount != null && amount.length > 2) {
                mSelectMoney.add(0, (float) amount[0]);
                mSelectMoney.add(1, (float) amount[1]);
                mSelectMoney.add(2, (float) amount[2]);
            }
        }
        initSelectMoney(mSelectMoney);
        mCustomMoney.setText(mPresenter.getGoldName());

        ActivityHandler.getInstance().removeActivity(PhotoAlbumDetailsActivity.class);
    }

    @Override
    public double getTollMoney() {
        return mTollMoney;
    }

    @Override
    public boolean hasTollVerify() {
        return isToll && !hasTollPic;
    }

    private void initSelectMoney(List<Float> mSelectDays) {
        mRbOne.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(0)));
        mRbTwo.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(1)));
        mRbThree.setText(String.format(getString(R.string.dynamic_send_toll_select_money),
                mSelectDays.get(2)));
    }

    private void initWordsToll() {
        mTvChooseTip.setText(R.string.dynamic_send_toll_words_count);
        RxTextView.textChanges(mEtInput).subscribe(charSequence -> {
            String spaceReg = "\n|\t";
            if (TextUtils.isEmpty(charSequence.toString().replaceAll(spaceReg, ""))) {
                return;
            }
            mRbDaysGroup.clearCheck();
            mTollMoney = Double.parseDouble(charSequence.toString());
        }, throwable -> mTollMoney = 0);

        RxRadioGroup.checkedChanges(mRbDaysGroup)
                .compose(this.bindToLifecycle())
                .subscribe(checkedId -> {
                    if (checkedId != -1) {
                        setCustomMoneyDefault();
                    }
                    switch (checkedId) {
                        case R.id.rb_one:
                            mTollMoney = mSelectMoney.get(0);
                            break;
                        case R.id.rb_two:
                            mTollMoney = mSelectMoney.get(1);
                            break;
                        case R.id.rb_three:
                            mTollMoney = mSelectMoney.get(2);
                            break;
                        default:
                            break;
                    }
                });
    }

    /**
     * 设置自定义金额数量
     */
    private void setCustomMoneyDefault() {
        mEtInput.setText("");
    }

    /**
     * 初始化图片选择弹框
     * 现在不提供图片选择弹窗，进入界面是选择动态类型
     */
    @Deprecated
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.choose_from_photo))
                .item2Str(getString(R.string.choose_from_camera))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(getActivity())
                .item1ClickListener(() -> {
                    ArrayList<String> photos = new ArrayList<>();
                    // 最后一张是占位图
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        ImageBean imageBean = selectedPhotos.get(i);
                        if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                            photos.add(imageBean.getImgUrl());
                        }
                    }
                    mPhotoSelector.getPhotoListFromSelector(MAX_PHOTOS, photos);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    ArrayList<String> photos = new ArrayList<>();
                    // 最后一张是占位图
                    for (int i = 0; i < selectedPhotos.size(); i++) {
                        ImageBean imageBean = selectedPhotos.get(i);
                        if (!TextUtils.isEmpty(imageBean.getImgUrl())) {
                            photos.add(imageBean.getImgUrl());
                        }
                    }
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(photos);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    /**
     * 初始化取消选择弹框
     */
    private void initCanclePopupWindow() {
        if (mCanclePopupWindow != null) {
            return;
        }

        if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
            mCanclePopupWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .desStr(getString(R.string.video_dynamic_send_cancel_hint))
                    .item2Str(getString(R.string.save))
                    .bottomStr(getString(R.string.drop))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item2ClickListener(() -> {
                        if (FileUtils.isFileExists(mSendDynamicDataBean.getVideoInfo().getPath())) {
                            String content = mEtDynamicContent.getInputContent();
                            if (TextUtils.isEmpty(content)) {
                                content = SharePreferenceUtils.VIDEO_DYNAMIC;
                            }
                            mSendDynamicDataBean.getVideoInfo().setDynamicContent(content);
                            SharePreferenceUtils.saveObject(mActivity, SharePreferenceUtils.VIDEO_DYNAMIC, mSendDynamicDataBean);
                        }
                        mCanclePopupWindow.hide();
                        mActivity.finish();
                    })
                    .bottomClickListener(() -> {
                        SharePreferenceUtils.remove(mActivity, SharePreferenceUtils.VIDEO_DYNAMIC);
                        mCanclePopupWindow.hide();
                        mActivity.finish();
                    })
                    .build();
        } else {
            mCanclePopupWindow = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.dynamic_send_cancel_hint))
                    .item2Str(getString(R.string.determine))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(0.8f)
                    .with(getActivity())
                    .item2ClickListener(() -> {
                        mCanclePopupWindow.hide();
                        getActivity().finish();
                    })
                    .bottomClickListener(() -> mCanclePopupWindow.hide()).build();
        }

    }

    /**
     * 初始化图片选择器
     */
    private void initPhotoSelector() {
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        if (isPhotoListChanged(cachePhotos, photoList)) {
            // 图片改变了
            hasTollPic = false;
            selectedPhotos.clear();
            selectedPhotos.addAll(photoList);
            addPlaceHolder();
            setSendDynamicState();// 每次刷新图片后都要判断发布按钮状态
            mCommonAdapter.notifyDataSetChanged();
        } else {
            // 图片没改边
            if (selectedPhotos != null) {
                hasTollPic = false;
                for (ImageBean selectedPhoto : selectedPhotos) {
                    if (selectedPhoto.getToll_type() > 0) {
                        hasTollPic = true;
                        return;
                    }
                }
            }
        }
    }

    private void addPlaceHolder() {
        if (selectedPhotos.size() < MAX_PHOTOS) {
            // 占位缺省图
            ImageBean camera = new ImageBean();
            selectedPhotos.add(camera);
        }

    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        ToastUtils.showToast(errorMsg);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null && dynamicType != SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
            if (requestCode == PhotoViewFragment.REQUEST_CODE) {
                // 设置收费金额快捷入口返回
                Toll toll = data.getBundleExtra(TOLL_TYPE).getParcelable(TOLL_TYPE);
                selectedPhotos.get(mCurrentTollImageBean).setToll(toll);
                mCommonAdapter.notifyDataSetChanged();
                return;
            }
            // 图片选择器界面数据保存操作
            if (data != null) {
                Bundle tollBundle = new Bundle();
                data.putExtra(TOLLBUNDLE, tollBundle);
                List<ImageBean> oldData = mCommonAdapter.getDatas();
                if (oldData == null) {
                    oldData = new ArrayList<>();
                }
                tollBundle.putParcelableArrayList(TOLLBUNDLE, new ArrayList<>(oldData));
            }
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        } else if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC && resultCode == Activity.RESULT_OK) {
            selectedPhotos.clear();
            addPlaceHolder();
            setSendDynamicState();// 每次刷新图片后都要判断发布按钮状态
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setPresenter(SendDynamicContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void sendDynamicComplete() {
        com.zhiyicx.common.utils.DeviceUtils.hideSoftKeyboard(getContext(), mToolbarRight);
        // 让主页先刷新
        if (getView() != null) {
            getView().postDelayed(() -> {
                if (getActivity() != null) {
                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().overridePendingTransition(0, R.anim.fade_out);
                }
            }, 100);
        }


    }

    @Override
    public SendDynamicDataBean getDynamicSendData() {
        Bundle bundle = getArguments();
        SendDynamicDataBean sendDynamicDataBean = null;
        if (bundle != null) {
            sendDynamicDataBean = bundle.getParcelable(SendDynamicActivity.SEND_DYNAMIC_DATA);
        }
        return sendDynamicDataBean;
    }

    @Override
    protected void setRightClick() {
        // 圈子
        if (isFromGroup) {
            mPresenter.sendGroupDynamic(packageGroupDynamicData());
        } else {
            mPresenter.sendDynamicV2(packageDynamicData());
        }
    }

    /**
     * 封装动态上传的数据
     */
    @Override
    public void packageDynamicStorageDataV2(SendDynamicDataBeanV2 sendDynamicDataBeanV2) {
        List<SendDynamicDataBeanV2.StorageTaskBean> storageTask = new ArrayList<>();
        List<ImageBean> photos = new ArrayList<>();
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    SendDynamicDataBeanV2.StorageTaskBean taskBean = new SendDynamicDataBeanV2.StorageTaskBean();
                    ImageBean imageBean = selectedPhotos.get(i);
                    photos.add(imageBean);
                    taskBean.setAmount(imageBean.getToll_monye() > 0 ? imageBean.getToll_monye() : null);
                    taskBean.setType(imageBean.getToll_monye() * imageBean.getToll_type() > 0
                            ? (imageBean.getToll_type() == LOOK_TOLL ? LOOK_TOLL_TYPE : DOWNLOAD_TOLL_TYPE) : null);
                    storageTask.add(taskBean);
                }
            }
        }
        if (sendDynamicDataBeanV2.getVideoInfo() != null) {
            sendDynamicDataBeanV2.getVideoInfo().setNeedGetCoverFromVideo(needGetCoverFromVideo());
            sendDynamicDataBeanV2.getVideoInfo().setNeedCompressVideo(needCompressVideo());
            sendDynamicDataBeanV2.getVideoInfo().setDuration(mSendDynamicDataBean.getVideoInfo().getDuration());
        }
        sendDynamicDataBeanV2.setPhotos(photos);
        sendDynamicDataBeanV2.setStorage_task(storageTask);
    }

    @Override
    public void packageGroupDynamicStorageData(GroupSendDynamicDataBean sendDynamicDataBeanV2) {
        List<ImageBean> photos = new ArrayList<>();
        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    SendDynamicDataBeanV2.StorageTaskBean taskBean = new SendDynamicDataBeanV2.StorageTaskBean();
                    ImageBean imageBean = selectedPhotos.get(i);
                    photos.add(imageBean);
                    taskBean.setAmount(imageBean.getToll_monye() > 0 ? imageBean.getToll_monye() : null);
                    taskBean.setType(imageBean.getToll_monye() * imageBean.getToll_type() > 0
                            ? (imageBean.getToll_type() == LOOK_TOLL ? LOOK_TOLL_TYPE : DOWNLOAD_TOLL_TYPE) : null);
                }
            }
        }
        sendDynamicDataBeanV2.setPhotos(photos);
    }

    @Override
    public boolean wordsNumLimit() {
        return mLLToll.getVisibility() == View.VISIBLE;
    }

    private void setLeftTextColor() {
        mToolbarLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.themeColor));
    }

    /**
     * 发布按钮的状态监听
     */
    private void initSendDynamicBtnState() {

        mEtDynamicContent.setContentChangedListener(s -> {
            hasContent = !TextUtils.isEmpty(s.toString().trim());
            setSendDynamicState();
        });

        mTvToll.setRightImageClickListener(v -> {
            if (!mPresenter.getSystemConfigBean().getFeed().hasPaycontrol()) {
                showSnackErrorMessage(getString(R.string.dynamic_close_pay));
                return;
            }
            isToll = !isToll;
            mTvToll.setRightImage(isToll ? R.mipmap.btn_open : R.mipmap.btn_close);
            mTollLine.setVisibility(isToll && dynamicType == TEXT_ONLY_DYNAMIC ? View.GONE : View.VISIBLE);
            if (dynamicType == TEXT_ONLY_DYNAMIC) {
                mLLToll.setVisibility(isToll ? View.VISIBLE : View.GONE);
                if (!isToll) {
                    mTollMoney = 0;
                }
                mSlSendDynamic.smoothScrollTo(0, 0);
            } else {
                mCommonAdapter.notifyDataSetChanged();
            }
        });

    }

    /**
     * 设置动态发布按钮的点击状态
     */
    private void setSendDynamicState() {
        // 没有内容，并且只有占位图时不能够发送
        boolean noPic = selectedPhotos == null || !isToll && selectedPhotos.size() <= 1;
        if (!hasContent && noPic) {
            mToolbarRight.setEnabled(false);
        } else {
            // 有内容或者有图片时都可以发送
            mToolbarRight.setEnabled(true);
        }
    }

    @Override
    public boolean needCompressVideo() {
        if (mSendDynamicDataBean == null || mSendDynamicDataBean.getVideoInfo() == null || dynamicType != SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
            return false;
        }
        return mSendDynamicDataBean.getVideoInfo().needCompressVideo();
    }

    @Override
    public boolean needGetCoverFromVideo() {
        if (mSendDynamicDataBean == null || mSendDynamicDataBean.getVideoInfo() == null || dynamicType != SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
            return false;
        }
        return mSendDynamicDataBean.getVideoInfo().needGetCoverFromVideo();
    }

    /**
     * 封装动态上传的数据
     */
    private DynamicDetailBeanV2 packageDynamicData() {

        DynamicDetailBeanV2 dynamicDetailBeanV2 = new DynamicDetailBeanV2();

        long userId = AppApplication.getmCurrentLoginAuth() != null ? AppApplication
                .getmCurrentLoginAuth().getUser_id() : 0;
        String feedMarkString = userId + "" + System.currentTimeMillis();
        long feedMark = Long.parseLong(feedMarkString);

        // 浏览量没有 0 ，从1 开始
        dynamicDetailBeanV2.setFeed_view_count(1);
        dynamicDetailBeanV2.setFeed_mark(feedMark);
        dynamicDetailBeanV2.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        dynamicDetailBeanV2.setFeed_content(mEtDynamicContent.getInputContent());
        dynamicDetailBeanV2.setFeed_from(ApiConfig.ANDROID_PLATFORM);
        // 因为关注里面需要显示我的动态
        dynamicDetailBeanV2.setIsFollowed(true);
        dynamicDetailBeanV2.setState(DynamicDetailBeanV2.SEND_ING);
        dynamicDetailBeanV2.setComments(new ArrayList<>());
        dynamicDetailBeanV2.setUser_id(userId);
        dynamicDetailBeanV2.setAmount((long) mTollMoney);

        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {

            List<DynamicDetailBeanV2.ImagesBean> images = new ArrayList<>();
            // 最后一张占位图，扔掉
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    DynamicDetailBeanV2.ImagesBean imagesBean = new DynamicDetailBeanV2.ImagesBean();
                    imagesBean.setImgUrl(selectedPhotos.get(i).getImgUrl());
                    BitmapFactory.Options options = DrawableProvider.getPicsWHByFile
                            (selectedPhotos.get(i).getImgUrl());
                    imagesBean.setHeight(options.outHeight);
                    imagesBean.setWidth(options.outWidth);
                    imagesBean.setImgMimeType(options.outMimeType);
                    images.add(imagesBean);
                }
            }
            dynamicDetailBeanV2.setImages(images);

            if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
                VideoInfo videoInfo = mSendDynamicDataBean.getVideoInfo();
                DynamicDetailBeanV2.Video video = new DynamicDetailBeanV2.Video();
                video.setCreated_at(dynamicDetailBeanV2.getCreated_at());
                video.setHeight(videoInfo.getHeight());
                video.setCover(videoInfo.getCover());
                video.setWidth(videoInfo.getWidth());
                video.setUrl(videoInfo.getPath());
                dynamicDetailBeanV2.setVideo(video);
                SharePreferenceUtils.remove(mActivity, SharePreferenceUtils.VIDEO_DYNAMIC);
            }
        }

        return dynamicDetailBeanV2;
    }

    private GroupDynamicListBean packageGroupDynamicData() {
        GroupDynamicListBean groupSendDynamicDataBean = new GroupDynamicListBean();
        long userId = AppApplication.getmCurrentLoginAuth() != null ? AppApplication
                .getmCurrentLoginAuth().getUser_id() : 0;

        String feedMarkString = userId + "" + System.currentTimeMillis();
        long feedMark = Long.parseLong(feedMarkString);

        groupSendDynamicDataBean.setViews(1);
        groupSendDynamicDataBean.setFeed_mark(feedMark);
        groupSendDynamicDataBean.setGroup_id((int) getDynamicSendData().getDynamicChannlId());
        groupSendDynamicDataBean.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
        groupSendDynamicDataBean.setContent(mEtDynamicContent.getInputContent());
        groupSendDynamicDataBean.setTitle(mEtDynamicTitle.getInputContent());
        groupSendDynamicDataBean.setNew_comments(new ArrayList<>());
        groupSendDynamicDataBean.setUser_id(userId);


        if (selectedPhotos != null && !selectedPhotos.isEmpty()) {
            List<GroupDynamicListBean.ImagesBean> images = new ArrayList<>();
            // 最后一张占位图，扔掉
            for (int i = 0; i < selectedPhotos.size(); i++) {
                if (!TextUtils.isEmpty(selectedPhotos.get(i).getImgUrl())) {
                    GroupDynamicListBean.ImagesBean imagesBean = new GroupDynamicListBean.ImagesBean();
                    imagesBean.setImgUrl(selectedPhotos.get(i).getImgUrl());
                    BitmapFactory.Options options = DrawableProvider.getPicsWHByFile
                            (selectedPhotos.get(i).getImgUrl());
                    imagesBean.setHeight(options.outHeight);
                    imagesBean.setWidth(options.outWidth);
                    imagesBean.setImgMimeType(options.outMimeType);
                    images.add(imagesBean);
                }
            }
            groupSendDynamicDataBean.setImages(images);
        }

        return groupSendDynamicDataBean;
    }

    /**
     * 初始化图片列表
     */
    private void initPhotoList(Bundle bundle) {
        if (selectedPhotos == null) {
            selectedPhotos = new ArrayList<>();
        }
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), ITEM_COLUM);
        mRvPhotoList.setLayoutManager(gridLayoutManager);
        // 设置recyclerview的item之间的空白
        int witdh = ConvertUtils.dp2px(getContext(), 5);
        mRvPhotoList.addItemDecoration(new GridDecoration(witdh, witdh));
        // 占位缺省图
        addPlaceHolder();
        mCommonAdapter = new CommonAdapter<ImageBean>(getContext(), R.layout
                .item_send_dynamic_photo_list, selectedPhotos) {
            @Override
            protected void convert(ViewHolder holder, final ImageBean imageBean, final int
                    position) {
                // 固定每个item的宽高
                // 屏幕宽高减去左右margin以及item之间的空隙
                int width = UIUtils.getWindowWidth(getContext()) - getResources()
                        .getDimensionPixelSize(R.dimen.spacing_large) * 2
                        - ConvertUtils.dp2px(getContext(), 5) * (ITEM_COLUM - 1);
                View convertView = holder.getConvertView();
                convertView.getLayoutParams().width = width / ITEM_COLUM;
                convertView.getLayoutParams().height = width / ITEM_COLUM;
                final FilterImageView imageView = holder.getView(R.id.iv_dynamic_img);
                final IconTextView paintView = holder.getView(R.id.iv_dynamic_img_paint);
                final View filterView = holder.getView(R.id.iv_dynamic_img_filter);
                if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                    // 最后一项作为占位图
                    paintView.setVisibility(View.GONE);
                    filterView.setVisibility(View.GONE);
                    holder.setVisible(R.id.iv_dynamic_img_video, View.GONE);
                    // 换成摄像图标
                    imageView.setImageResource(dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC ?
                            R.mipmap.ico_video_remake : R.mipmap.img_edit_photo_frame);
                    holder.setVisible(R.id.tv_record, dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC ? View.VISIBLE : View.GONE);
                } else {
                    holder.setVisible(R.id.iv_dynamic_img_video, dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC ? View.VISIBLE : View.GONE);
                    paintView.setVisibility(isToll ? View.VISIBLE : View.GONE);
                    filterView.setVisibility(isToll ? View.VISIBLE : View.GONE);
                    holder.setVisible(R.id.tv_record, View.GONE);
                    if (!isToll) {
                        imageBean.setToll(null);
                    }

                    if (imageBean.getToll_type() > 0) {
                        hasTollPic = true;
                        filterView.setVisibility(View.VISIBLE);
                        paintView.setIconRes(R.mipmap.ico_lock);
                    } else {
                        paintView.setIconRes(R.mipmap.ico_edit_pen);
                        filterView.setVisibility(View.GONE);
                    }
                    Glide.with(getContext())
                            .load(imageBean.getImgUrl())
                            .asBitmap()
                            .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .placeholder(R.drawable.shape_default_image)
                            .error(R.drawable.shape_default_error_image)
                            .override(convertView.getLayoutParams().width, convertView.getLayoutParams().height)
                            .into(imageView);
                    imageView.setIshowGifTag(ImageUtils.imageIsGif(imageBean.getImgMimeType()));

                }
                paintView.setOnClickListener(v -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), v);
                    Intent intent = new Intent(getActivity(), PictureTollActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putParcelable(PhotoViewFragment.OLDTOLL, imageBean);
                    mCurrentTollImageBean = position;
                    intent.putExtra(PhotoViewFragment.OLDTOLL, bundle1);
                    startActivityForResult(intent, PhotoViewFragment.REQUEST_CODE);
                });
                imageView.setOnClickListener(v -> {
                    try {
                        DeviceUtils.hideSoftKeyboard(getContext(), v);
                        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
                            if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
                                startActivity(new Intent(getActivity(), VideoSelectActivity.class));
                                return;
                            }
                            ArrayList<String> photos = new ArrayList<>();
                            // 最后一张是占位图
                            for (int i = 0; i < selectedPhotos.size(); i++) {
                                ImageBean imageBean1 = selectedPhotos.get(i);
                                if (!TextUtils.isEmpty(imageBean1.getImgUrl())) {
                                    photos.add(imageBean1.getImgUrl());
                                }
                            }
                            mPhotoSelector.getPhotoListFromSelector(MAX_PHOTOS, photos);
                        } else {
                            if (dynamicType == SendDynamicDataBean.VIDEO_TEXT_DYNAMIC) {
                                ArrayList<String> srcList = new ArrayList<>();
                                srcList.add(mSendDynamicDataBean.getVideoInfo().getPath());
                                CoverActivity.startCoverActivity(mActivity, srcList, true, false, false);
                                return;
                            }

                            // 预览图片
                            ArrayList<String> photos = new ArrayList<>();
                            // 最后一张是占位图
                            for (int i = 0; i < selectedPhotos.size(); i++) {
                                ImageBean imageBean1 = selectedPhotos.get(i);
                                if (!TextUtils.isEmpty(imageBean1.getImgUrl())) {
                                    photos.add(imageBean1.getImgUrl());
                                }
                            }
                            ArrayList<AnimationRectBean> animationRectBeanArrayList
                                    = new ArrayList<>();
                            for (int i = 0; i < photos.size(); i++) {

                                if (i < gridLayoutManager.findFirstVisibleItemPosition()) {
                                    // 顶部，无法全部看见的图片
                                    AnimationRectBean rect = new AnimationRectBean();
                                    animationRectBeanArrayList.add(rect);
                                } else if (i > gridLayoutManager.findLastVisibleItemPosition()) {
                                    // 底部，无法完全看见的图片
                                    AnimationRectBean rect = new AnimationRectBean();
                                    animationRectBeanArrayList.add(rect);
                                } else {
                                    View view = gridLayoutManager
                                            .getChildAt(i - gridLayoutManager
                                                    .findFirstVisibleItemPosition());
                                    ImageView imageView1 = (ImageView) view.findViewById(R.id
                                            .iv_dynamic_img);
                                    // 可以完全看见的图片
                                    AnimationRectBean rect = AnimationRectBean.buildFromImageView
                                            (imageView1);
                                    animationRectBeanArrayList.add(rect);
                                }
                            }
                            ArrayList<ImageBean> datas = new ArrayList<>();
                            datas.addAll(selectedPhotos);
                            cachePhotos = new ArrayList<>(selectedPhotos);
                            PhotoViewActivity.startToPhotoView(SendDynamicFragment.this,
                                    photos, photos, animationRectBeanArrayList, MAX_PHOTOS,
                                    position, isToll, datas);
                        }
                    } catch (Exception ignored) {
                        ignored.printStackTrace();
                    }
                });
            }
        };

        mRvPhotoList.setAdapter(mCommonAdapter);
    }

    /**
     * 根据 dynamicType 初始化界面布局，比如发纯文字动态就隐藏图片
     */
    private void initDynamicType() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSendDynamicDataBean = bundle.getParcelable(SendDynamicActivity
                    .SEND_DYNAMIC_DATA);
            if (mSendDynamicDataBean == null) {
                throw new IllegalArgumentException("SEND_DYNAMIC_DATA can not be null");
            }
            dynamicType = mSendDynamicDataBean.getDynamicType();
            restoreVideoDraft(mSendDynamicDataBean);
            List<ImageBean> originPhotos = mSendDynamicDataBean.getDynamicPrePhotos();
            if (originPhotos != null) {
                selectedPhotos = new ArrayList<>(MAX_PHOTOS);
                selectedPhotos.addAll(originPhotos);
            }
            if (mSendDynamicDataBean.getDynamicBelong() == SendDynamicDataBean.GROUP_DYNAMIC) {
                isFromGroup = true;
                mTvToll.setVisibility(View.GONE);
            }
        }
        switch (dynamicType) {
            case SendDynamicDataBean.VIDEO_TEXT_DYNAMIC:
                mEtDynamicContent.getEtContent().setHint(getString(R.string
                        .dynamic_content_no_pic_hint));
            case SendDynamicDataBean.PHOTO_TEXT_DYNAMIC:
                // 没有图片就初始化这些
                initPhotoSelector();
                initPhotoList(bundle);
                break;
            case TEXT_ONLY_DYNAMIC:
                hasTollPic = true;
                // 隐藏图片控件
                mRvPhotoList.setVisibility(View.GONE);
                mEtDynamicContent.getEtContent().setHint(getString(R.string
                        .dynamic_content_no_pic_hint));
                break;
            default:
        }
    }

    private void restoreVideoDraft(SendDynamicDataBean sendDynamicDataBean) {
        VideoInfo videoInfo = sendDynamicDataBean.getVideoInfo();
        if (videoInfo != null) {

            // 如果没有经过选择封面步骤，则cover是 null
            if (TextUtils.isEmpty(videoInfo.getCover())) {
                videoInfo.setNeedGetCoverFromVideo(true);
            } else {
                videoInfo.setNeedGetCoverFromVideo(false);
            }

            if (!TextUtils.isEmpty(videoInfo.getDynamicContent())) {
                String content = videoInfo.getDynamicContent();
                if (SharePreferenceUtils.VIDEO_DYNAMIC.equals(content)) {
                    return;
                }
                mEtDynamicContent.setText(content);
            }
        }

    }

    /**
     * 图片列表返回后，判断图片列表内容以及顺序是否发生变化，如果没变，就可以不用刷新
     */
    private boolean isPhotoListChanged(List<ImageBean> oldList, List<ImageBean> newList) {
//        if (!newList.isEmpty()) {
//            return true;
//        }
        // 取消了所有选择的图片
        if (newList == null || newList.isEmpty()) {
            return oldList.size() > 1;
        } else {
            boolean oldListIsNull = oldList == null || oldList.isEmpty();
            if (oldListIsNull) {
                return true;
            }
            int oldSize = 0;
            // 最后一张是占位图
            if (TextUtils.isEmpty(oldList.get(oldList.size() - 1).getImgUrl())) {
                oldSize = oldList.size() - 1;
            } else {
                oldSize = oldList.size();
            }
            if (oldSize != newList.size()) {
                // 如果长度不同，那肯定改变了
                return true;
            } else {
                // 继续判断内容和顺序变了没有
                for (int i = 0; i < newList.size(); i++) {
                    ImageBean newImageBean = newList.get(i);
                    ImageBean oldImageBean = oldList.get(i);
                    boolean tollIsSame = newImageBean.getToll() != null && !newImageBean.getToll().equals(oldImageBean.getToll
                            ());
                    if (!tollIsSame) {
                        return true;
                    }
                    if (!oldImageBean.equals(newImageBean)) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Override
    public void initInstructionsPop(String des) {
        DeviceUtils.hideSoftKeyboard(getContext(), mRootView);
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .item1Str(des)
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(des)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

}
