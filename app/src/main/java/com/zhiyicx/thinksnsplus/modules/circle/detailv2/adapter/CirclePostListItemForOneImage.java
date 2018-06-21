package com.zhiyicx.thinksnsplus.modules.circle.detailv2.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.ImagesBean.FILE_MIME_TYPE_GIF;

/**
 * @author Jliuer
 * @Date 2017/11/30/14:32
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CirclePostListItemForOneImage extends CirclePostListBaseItem {

    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    public CirclePostListItemForOneImage(Context context) {
        super(context);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_dynamic_list_one_image;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public void convert(ViewHolder holder, CirclePostListBean circlePostListBean, CirclePostListBean lastT, int position, int itemCounts) {
        super.convert(holder, circlePostListBean, lastT, position, itemCounts);
        initImageView(holder, holder.getView(R.id.siv_0), circlePostListBean, 0, 1);
    }

    /**
     * 设置 imageview 点击事件，以及显示
     *
     * @param view               the target
     * @param circlePostListBean item data
     * @param positon            image item position
     * @param part               this part percent of imageContainer
     */
    @Override
    protected void initImageView(final ViewHolder holder, FilterImageView view,
                                 final CirclePostListBean circlePostListBean, final int positon, int part) {
        int with;
        int height;
        CirclePostListBean.ImagesBean imageBean = circlePostListBean.getImages().get(0);
        if (imageBean.getImageViewWidth() == 0) {
            circlePostListBean.handleData();
        }
        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            with = imageBean.getImageViewWidth();
            height = imageBean.getImageViewHeight();
            if (with * height == 0) {
                with = DEFALT_IMAGE_WITH;
                height = DEFALT_IMAGE_HEIGHT;
            }
            // 是否是 gif
            view.setIshowGifTag(ImageUtils.imageIsGif(imageBean.getImgMimeType()));
            // 是否是长图
            view.showLongImageTag(imageBean.hasLongImage());
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));
            Glide.with(mContext)
                    .load(imageBean.getGlideUrl())
                    .asBitmap()
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(view);
        } else {
            // 本地
            BitmapFactory.Options option = DrawableProvider.getPicsWHByFile(imageBean.getImgUrl());
            with = imageBean.getCurrentWith();
            height = imageBean.getHeight();
            if (height == 0 && option.outWidth == 0) {
                height = with;
            } else {
                height = with * option.outHeight / option.outWidth;
                height = height > mImageMaxHeight ? mImageMaxHeight : height;
            }
            if (height <= 0) {
                height = DEFALT_IMAGE_HEIGHT;
            }
            if (with <= 0) {
                with = DEFALT_IMAGE_WITH;
            }
            // 是否是 gif
            view.setIshowGifTag(ImageUtils.imageIsGif(option.outMimeType));
            // 是否是长图
            view.showLongImageTag(isLongImage(option.outHeight, option.outWidth));
            view.setLayoutParams(new LinearLayout.LayoutParams(with, height));

            Glide.with(mContext)
                    .load(imageBean.getImgUrl())
                    .asBitmap()
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(view);
        }

        RxView.clicks(view)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (mOnImageClickListener != null) {
                        mOnImageClickListener.onImageClick(holder, circlePostListBean, positon);
                    }
                });
    }


    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}
