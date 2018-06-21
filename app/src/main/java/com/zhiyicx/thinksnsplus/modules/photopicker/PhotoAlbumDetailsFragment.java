package com.zhiyicx.thinksnsplus.modules.photopicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.GridDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.iwf.photopicker.adapter.PhotoGridAdapter;
import me.iwf.photopicker.entity.PhotoDirectory;
import me.iwf.photopicker.utils.MediaStoreHelper;

import static android.app.Activity.RESULT_OK;
import static com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl.CAMERA_PHOTO_CODE;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.ALL_PHOTOS;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NAME;
import static com.zhiyicx.thinksnsplus.modules.photopicker.PhotoAlbumListFragment.SELECTED_DIRECTORY_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_COLUMN_NUMBER;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_MAX_COUNT;
import static me.iwf.photopicker.PhotoPicker.DEFAULT_REQUST_ALBUM;
import static me.iwf.photopicker.PhotoPicker.EXTRA_PREVIEW_ENABLED;
import static me.iwf.photopicker.PhotoPicker.EXTRA_SHOW_GIF;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/6
 * @contact email:450127106@qq.com
 */
public class PhotoAlbumDetailsFragment extends TSFragment implements PhotoSelectorImpl.IPhotoBackListener {
    public static final int COMPLETE_REQUEST_CODE = 1000;
    public static final int TO_ALBUM_LIST_REQUEST_CODE = 2000;
    public static final String EXTRA_BACK_HERE = "back_here";// 回到当前图片列表页面，是否停留
    public final static String EXTRA_ORIGIN = "ORIGINAL_PHOTOS";
    public final static String EXTRA_VIEW_INDEX = "view_index";
    public final static String EXTRA_CAMERA = "SHOW_CAMERA";
    private final static String EXTRA_COLUMN = "column";
    public static final String EXTRA_VIEW_ALL_PHOTOS = "view_photos";
    public static final String EXTRA_VIEW_SELECTED_PHOTOS = "view_selected_photos";

    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";

    @BindView(R.id.ll_bottom_container)
    LinearLayout mLlBottomContainer;
    @BindView(R.id.rv_album_details)
    RecyclerView mRvAlbumDetails;
    @BindView(R.id.tv_preview)
    TextView mTvPreview;
    @BindView(R.id.bt_complete)
    TextView mBtComplete;
    private PhotoGridAdapter photoGridAdapter;
    //所有photos的路径
    private List<PhotoDirectory> directories;
    //传入的已选照片
    private ArrayList<String> originalPhotos;
    private RequestManager mGlideRequestManager;
    private PhotoSelectorImpl mPhotoSelector;
    private int column;// 图片列数
    private int selected_directory;// 获取被选中的目录位置
    private boolean canPreview = true;// 是否能够预览
    private int maxCount = DEFAULT_MAX_COUNT;

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        List<String> photos = photoGridAdapter.getSelectedPhotos();
        if (photos.size() >= 9) {
            return;
        }
        for (ImageBean imageBean : photoList) {
            photos.add(imageBean.getImgUrl());
        }
        mBtComplete.setEnabled(photos.size() > 0);
        mTvPreview.setEnabled(photos.size() > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, photoList.size(), maxCount));
        photoGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void getPhotoFailure(String errorMsg) {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_photo_album_details;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setCenterTitle() {
        String centerTitle = getArguments().getString(SELECTED_DIRECTORY_NAME);
        return TextUtils.isEmpty(centerTitle) ? getString(R.string.all_photos) : centerTitle;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setLeftClick() {
        // 回到相册列表页面，同时将当前数据传递过去
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(EXTRA_ORIGIN, photoGridAdapter.getSelectedPhotoPaths());
        bundle.putInt("iamges", getActivity().getTaskId());
        Intent intent = new Intent();
        intent.setClass(getActivity(), PhotoAlbumListActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, TO_ALBUM_LIST_REQUEST_CODE);
//        getActivity().finish();// finish后出栈
        getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    protected void setRightClick() {
        getActivity().finish();
    }

    @Override
    protected void initView(View rootView) {

        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .NO_CRAFT))
                .build().photoSelectorImpl();

        mGlideRequestManager = Glide.with(this);
        directories = getArguments().getParcelableArrayList(ALL_PHOTOS);
        if (directories == null) {
            directories = new ArrayList<>();
        }
        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        originalPhotos = getArguments().getStringArrayList(EXTRA_ORIGIN);
        selected_directory = getArguments().getInt(SELECTED_DIRECTORY_NUMBER, 0);
        maxCount = getArguments().getInt(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
        canPreview = getArguments().getBoolean(EXTRA_PREVIEW_ENABLED);
        LogUtils.i(TAG + " ccanPreview " + canPreview);

        // 如果不能预览图片，就隐藏下方的预览和完成按钮
        mLlBottomContainer.setVisibility(canPreview ? View.VISIBLE : View.GONE);
        photoGridAdapter = new PhotoGridAdapter(getActivity(), mGlideRequestManager, directories, originalPhotos, column);
        boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, false);
        photoGridAdapter.setShowCamera(showCamera);
        photoGridAdapter.setPreviewEnable(canPreview);

        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        mRvAlbumDetails.setLayoutManager(layoutManager);
        mRvAlbumDetails.setItemAnimator(new DefaultItemAnimator());
        mRvAlbumDetails.setAdapter(photoGridAdapter);
        mRvAlbumDetails.addItemDecoration(new GridDecoration(getContext(), com.zhiyicx.baseproject.R.drawable
                .shape_recyclerview_divider_white_small));
        photoGridAdapter.setCurrentDirectoryIndex(selected_directory);
        photoGridAdapter.notifyDataSetChanged();
        // 设置图片选择的监听
        photoGridAdapter.setOnItemCheckListener((position, photo, selectedItemCount) -> {
            boolean isEnable = true;
            mBtComplete.setEnabled(selectedItemCount > 0);
            // 设置预览按钮的状态
            mTvPreview.setEnabled(selectedItemCount > 0);
            // 单张选择
            if (maxCount <= 1 && !canPreview) {
                List<String> photos = photoGridAdapter.getSelectedPhotos();
                // 当前选择的图片，没有被选择过
                if (!photos.contains(photo.getPath())) {
                    // 之前已经选择过该图片，就需要 -1 张
                    if (!photos.isEmpty()) {
                        selectedItemCount -= 1;
                    }
                    photos.clear();
                    //photoGridAdapter.notifyDataSetChanged();
                }
                // 设置当前选择的数量
                mBtComplete.setText(getString(R.string.album_selected_count, selectedItemCount, maxCount));
                isEnable = true;
            } else {
                // 数量超过时，进行提示
                if (selectedItemCount > maxCount) {
                    ToastUtils.showToast(getString(R.string.choose_max_photos, maxCount));
                    isEnable = false;
                } else {
                    // 设置当前选择的数量
                    mBtComplete.setText(getString(R.string.album_selected_count, selectedItemCount, maxCount));
                    isEnable = true;
                }
            }
            return isEnable;
        });
        photoGridAdapter.setOnCameraClickListener(v -> mPhotoSelector.getPhotoFromCamera(originalPhotos));
        // 设置图片 item 的点击事件
        photoGridAdapter.setOnPhotoClickListener((v, position, showCamera1) -> {
            int index = showCamera1 ? position - 1 : position;
            List<String> allPhotos = photoGridAdapter.getCurrentPhotoPaths();
            ArrayList<String> selectedPhotos = photoGridAdapter.getSelectedPhotoPaths();
            ArrayList<AnimationRectBean> animationRectBeanArrayList
                    = new ArrayList<>();
            for (int i = 0; i < allPhotos.size(); i++) {

                if (i < layoutManager.findFirstVisibleItemPosition()) {
                    // 顶部，无法看见的图片
                    animationRectBeanArrayList.add(null);
                } else if (i > layoutManager.findLastVisibleItemPosition()) {
                    // 底部，无法看见的图片
                    animationRectBeanArrayList.add(null);
                } else {
//                        View view = layoutManager
//                                .getChildAt(i - layoutManager.findFirstVisibleItemPosition() + (showCamera ? 1 : 0));// 照相机占位
                    ImageView imageView = (ImageView) v;
                    // 可以完全看见的图片
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView);
                    animationRectBeanArrayList.add(rect);
                }
            }
            PhotoViewActivity.startToPhotoView(PhotoAlbumDetailsFragment.this, (ArrayList<String>) allPhotos
                    , selectedPhotos, animationRectBeanArrayList, maxCount, index, false, new ArrayList<ImageBean>());
        });
    }

    @Override
    protected void initData() {
        // 页面没有从上级页面获取相册数据，那么自己重新获取一次
        if (directories.isEmpty()) {
            Bundle mediaStoreArgs = new Bundle();
            boolean isShowGif = getArguments().getBoolean(EXTRA_SHOW_GIF, true);
            mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, isShowGif);
            MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                    dirs -> {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                    });
        }
        int selectedCount = photoGridAdapter.getSelectedPhotoPaths().size();
        // 初始化数据
        mBtComplete.setEnabled(selectedCount > 0);
        mTvPreview.setEnabled(selectedCount > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, selectedCount, maxCount));
    }

    public static PhotoAlbumDetailsFragment initFragment(Bundle bundle) {
        PhotoAlbumDetailsFragment photoAlbumDetailsFragment = new PhotoAlbumDetailsFragment();
        photoAlbumDetailsFragment.setArguments(bundle);
        return photoAlbumDetailsFragment;
    }

    @OnClick({R.id.tv_preview, R.id.bt_complete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_preview:
                // 预览图片和选择图片是相同的
                ArrayList<String> allPhotos = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<String> selectedPhoto = photoGridAdapter.getSelectedPhotoPaths();
                ArrayList<AnimationRectBean> animationRectBeanArrayList = new ArrayList<>();
                for (String path : selectedPhoto) {
                    animationRectBeanArrayList.add(null);
                }
                PhotoViewActivity.startToPhotoView(this, allPhotos, selectedPhoto,
                        animationRectBeanArrayList, maxCount, 0, false, new ArrayList<>());
                break;
            case R.id.bt_complete:
                Intent it = new Intent();
                it.putStringArrayListExtra("photos", photoGridAdapter.getSelectedPhotoPaths());
                getActivity().setResult(Activity.RESULT_OK, it);
                getActivity().finish();
                break;
            default:
        }
    }

    // @Subscriber(tag = EventBusTagConfig.EVENT_SELECTED_PHOTO_UPDATE)
    public void refreshDataAndUI(List<String> selectedPhoto) {
        int selectedCount = selectedPhoto.size();
        List<String> oldSelectedPhotos = photoGridAdapter.getSelectedPhotos();
        oldSelectedPhotos.clear();
        oldSelectedPhotos.addAll(selectedPhoto);
        photoGridAdapter.notifyDataSetChanged();
        mBtComplete.setEnabled(selectedCount > 0);
        // 设置预览按钮的状态
        mTvPreview.setEnabled(selectedCount > 0);
        mBtComplete.setText(getString(R.string.album_selected_count, selectedCount, maxCount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == CAMERA_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            Intent it = new Intent();
            it.putStringArrayListExtra("photos", photoGridAdapter.getSelectedPhotoPaths());
            getActivity().setResult(Activity.RESULT_OK, it);
            getActivity().finish();
            return;
        }
        if (requestCode == COMPLETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            boolean stayHere = data.getBooleanExtra(EXTRA_BACK_HERE, false);
            if (stayHere) {
                // 如果停在该页面，刷新图片列表选择状态
                List<String> selectedPhoto = data.getStringArrayListExtra("photos");
                refreshDataAndUI(selectedPhoto);
            } else {
                // 否则，直接将结果返回到该去的地方
                getActivity().setResult(Activity.RESULT_OK, data);
                getActivity().finish();
                return;
            }
        }
        // 从相册列表返回
        if (requestCode == TO_ALBUM_LIST_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            // 刷新相册名称
            mToolbarCenter.setText(bundle.getString(SELECTED_DIRECTORY_NAME));
            // 刷新图片列表
            selected_directory = bundle.getInt(SELECTED_DIRECTORY_NUMBER, 0);
            List<PhotoDirectory> newDirectories = bundle.getParcelableArrayList(ALL_PHOTOS);
            directories.clear();
            directories.addAll(newDirectories);

            //originalPhotos = bundle.getStringArrayList(EXTRA_ORIGIN);
            photoGridAdapter.setCurrentDirectoryIndex(selected_directory);
            photoGridAdapter.notifyDataSetChanged();
        }


    }


    @Subscriber(tag = EventBusTagConfig.EVENT_SEND_DYNAMIC_PHOT_FIRST_OPEN_SEND_DYNAMIC_PAGE)
    public void sendDynamicPhotFirstOpenSendDynamicPage(Intent data) {
        // 获取图片选择器返回结果
        if (mPhotoSelector != null) {
            mPhotoSelector.onActivityResult(DEFAULT_REQUST_ALBUM, RESULT_OK, data);
        }
    }

}
