package com.zhiyicx.thinksnsplus.modules.gallery;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LiuChao
 * @describe 图片浏览器（画廊），用于网络图片的预览
 * @date 2017/2/8
 * @contact email:450127106@qq.com
 */
public class GalleryActivity extends TSActivity {

    @Override
    protected void componentInject() {

    }

    @Override
    protected Fragment getFragment() {
        return GalleryFragment.initFragment(getIntent().getExtras());
    }


    @Override
    public void onBackPressed() {
        ((BaseFragment) mContanierFragment).onBackPressed();
    }

    /**
     * 查看大图
     * @param context
     * @param position
     * @param imageBeanList
     * @param animationRectBeanList
     */
    public static void startToGallery(Context context, int position, List<ImageBean> imageBeanList, List<AnimationRectBean> animationRectBeanList) {
        startToGallery(context, position, imageBeanList, animationRectBeanList, false);
    }

    public static void startToGallery(Context context, int position, List<ImageBean> imageBeanList, List<AnimationRectBean> animationRectBeanList,
                                      boolean isNeedSartLoading) {
        Intent intent = new Intent(context, GalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(GalleryFragment.BUNDLE_IMAGS, (ArrayList<? extends Parcelable>) imageBeanList);
        bundle.putInt(GalleryFragment.BUNDLE_IMAGS_POSITON, position);
        bundle.putBoolean(GalleryFragment.BUNDLE_NEED_START_LOADING, isNeedSartLoading);
        bundle.putParcelableArrayList("rect", (ArrayList<? extends Parcelable>) animationRectBeanList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
