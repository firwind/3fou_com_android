package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.orhanobut.logger.Logger;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.em.manager.util.TSEMDateUtil;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 16:59
 *     desc :
 *     version : 1.0
 * <pre>
 */

public class MessageGroupAlbumFragment extends TSListFragment<MessageGroupAlbumContract.Presenter,MessageGroupAlbumBean>
        implements MessageGroupAlbumContract.View, PhotoSelectorImpl.IPhotoBackListener,View.OnClickListener {

    private PhotoSelectorImpl mPhotoSelector;
    private ArrayList<String> mImgList = new ArrayList<>();
    private boolean isNeedTranNewData = false;//是否向上一界面传递数据

    public static MessageGroupAlbumFragment newInstance(String group_id){
        MessageGroupAlbumFragment fragment = new MessageGroupAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.GROUP_ID,group_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        setCenterText("群相册");
        initPhotoSelector();
        //((MessageGroupAlbumActivity)mActivity).addTextView();
        TextView textView = new TextView(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity,125),
                DensityUtil.dip2px(mActivity,30));
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.shape_bg_main_top_radius_2dp);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText("上传照片");
        textView.setOnClickListener(this);
        ((RelativeLayout)rootView.findViewById(R.id.rl_parent)).addView(textView);
    }

    @Override
    protected boolean needCenterLoadingDialog() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return super.getBodyLayoutId();
    }

    @Override
    public String getGroupId() {
        return null != getArguments()?getArguments().getString(IntentKey.GROUP_ID):null;
    }

    @Override
    public void uploadOk() {
        isNeedTranNewData = true;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {

        CommonAdapter<MessageGroupAlbumBean> adapter = new CommonAdapter<MessageGroupAlbumBean>(mActivity,
                R.layout.item_message_group_album,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, MessageGroupAlbumBean messageGroupAlbumBean, int position) {
                holder.getTextView(R.id.tv_nick_name).setText(messageGroupAlbumBean.user_name);
                holder.getTextView(R.id.tv_date).setText(TSEMDateUtil.string2DateWithLine(messageGroupAlbumBean.created_at));
                ImageUtils.loadImageDefault(holder.getImageViwe(R.id.iv),
                        ImageUtils.imagePathConvertV2(messageGroupAlbumBean.file_id,
                                holder.itemView.findViewById(R.id.iv).getMeasuredWidth(),
                                holder.itemView.findViewById(R.id.iv).getMeasuredHeight(),
                                ImageZipConfig.IMAGE_80_ZIP));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                MessageGroupAlbumBean photo = adapter.getDatas().get(position);
                ImageBean imageBean = new ImageBean();
                imageBean.setImgUrl(ImageUtils.imagePathConvertV2(photo.file_id,0,0,
                        ImageZipConfig.IMAGE_80_ZIP));
                imageBean.setWidth(holder.itemView.findViewById(R.id.iv).getMeasuredWidth());
                imageBean.setHeight(holder.itemView.findViewById(R.id.iv).getMeasuredHeight());
                imageBean.setPosition(0);

                AnimationRectBean rect = AnimationRectBean.buildFromImageView((ImageView) (holder.itemView.findViewById(R.id.iv)));

                GalleryActivity.startToSingleGallery(mActivity,0,imageBean,rect);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return adapter;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new GridLayoutManager(mActivity,2);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        int x = getResources().getDimensionPixelSize(R.dimen.spacing_normal_18dp);
        return new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = parent.getChildAdapterPosition(view) % 2 == 0 ? x : x/2;
                outRect.right = parent.getChildAdapterPosition(view) % 2 == 0 ? x/2 : x;
                outRect.top = x;
                outRect.bottom = x;
            }
        };
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(null != mPhotoSelector)
            mPhotoSelector.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        showCenterLoading(getString(R.string.please_wait));
        mPresenter.requestUploadToAlbum(photoList);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<MessageGroupAlbumBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);

        if(isNeedTranNewData){
            if(null == data || data.size() == 0)
                return;
            EventBus.getDefault().post(data.size()>4?data.subList(0,4):data, EventBusTagConfig.EVENT_GROUP_UPLOAD_ALBUM_SUCCESS);
            isNeedTranNewData = false;
        }

    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        showSnackErrorMessage(errorMsg);
    }

    //上传图片点击
    @Override
    public void onClick(View view) {
        mPhotoSelector.getPhotoListFromSelector(9, mImgList);
    }
}
