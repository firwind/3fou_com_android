package com.zhiyicx.thinksnsplus.modules.personal_center.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DrawableProvider;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.jzvd.JZVideoPlayer.URL_KEY_DEFAULT;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicListAdvert.DEFAULT_ADVERT_FROM_TAG;

/**
 * @author LiuChao
 * @describe
 * @date 2017/3/10
 * @contact email:450127106@qq.com
 */


public class PersonalCenterDynamicListItemForShortVideo extends PersonalCenterDynamicListBaseItem {

    private static final int IMAGE_COUNTS = 1;// 动态列表图片数量
    private static final int CURREN_CLOUMS = 1; // 当前列数

    private ZhiyiVideoView.ShareInterface mShareInterface;

    public PersonalCenterDynamicListItemForShortVideo(Context context, ZhiyiVideoView.ShareInterface shareInterface) {
        super(context);
        mShareInterface = shareInterface;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_personal_center_dynamic_list_one_video;
    }

    @Override
    public int getImageCounts() {
        return IMAGE_COUNTS;
    }

    @Override
    public void convert(ViewHolder holder, final DynamicDetailBeanV2 dynamicBean, DynamicDetailBeanV2 lastT, int position, int itemCounts) {
        super.convert(holder, dynamicBean, lastT, position, itemCounts);
        initVideoView(holder, holder.getView(R.id.videoplayer), dynamicBean, position);
    }

    /**
     * 设置 imageview 点击事件，以及显示
     *
     * @param view        the target
     * @param dynamicBean item data
     * @param positon     image item position
     */
    private void initVideoView(final ViewHolder holder, JZVideoPlayerStandard view, final DynamicDetailBeanV2 dynamicBean, final int positon) {
        int with;
        int height;

        String videoUrl;
        DynamicDetailBeanV2.Video video = dynamicBean.getVideo();
        if (TextUtils.isEmpty(video.getUrl())) {

            videoUrl = String.format(ApiConfig.APP_DOMAIN + ApiConfig.FILE_PATH,
                    dynamicBean.getVideo().getVideo_id());
            if (view instanceof ZhiyiVideoView) {
                ZhiyiVideoView zhiyiVideoView = (ZhiyiVideoView) view;
                zhiyiVideoView.setShareInterface(mShareInterface);
            }
            with = video.getWidth();
            height = video.getHeight();
            view.getLayoutParams().height = height;
            Glide.with(mContext)
                    .load(video.getGlideUrl())
                    .override(with, height)
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, GlideUrl model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, GlideUrl model, Target<GlideDrawable> target, boolean
                                isFromMemoryCache, boolean isFirstResource) {
                            Observable.just(resource)
                                    .subscribeOn(Schedulers.io())
                                    .map(glideDrawable -> {
                                        Bitmap bitmap = FastBlur.blurBitmapForShortVideo(ConvertUtils.drawable2Bitmap(glideDrawable), glideDrawable
                                                .getIntrinsicWidth(), glideDrawable.getIntrinsicHeight());
                                        return new BitmapDrawable(mContext.getResources(), bitmap);
                                    })
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(background -> {
                                        if (view != null) {
                                            // 防止被回收
                                            view.setBackground(background);
                                        }
                                    }, Throwable::printStackTrace);
                            return false;
                        }
                    })
                    .into(view.thumbImageView);
        } else{
            // 本地
            videoUrl = video.getUrl();
            with = video.getWidth();
            height = video.getHeight();

            view.getLayoutParams().height = height;
            Glide.with(mContext)
                    .load(video.getCover())
                    .override(with, height)
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .placeholder(R.drawable.shape_default_image)
                    .error(R.drawable.shape_default_image)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean
                                isFromMemoryCache, boolean isFirstResource) {

                            Bitmap bitmap = FastBlur.blurBitmap(ConvertUtils.drawable2Bitmap(resource), resource.getIntrinsicWidth(), resource
                                    .getIntrinsicHeight());
                            view.setBackground(new BitmapDrawable(mContext.getResources(), bitmap));
                            return false;
                        }
                    })
                    .into(view.thumbImageView);

        }


        if (JZVideoPlayerManager.getFirstFloor() != null
                && JZVideoPlayerManager.getFirstFloor().positionInList == positon
                && !JZVideoPlayerManager.getCurrentJzvd().

                equals(view))

        {

            boolean isDetailBackToList = false;
            LinkedHashMap<String, Object> map = (LinkedHashMap) JZVideoPlayerManager.getFirstFloor().dataSourceObjects[0];
            if (map != null) {
                isDetailBackToList = videoUrl.equals(map.get(URL_KEY_DEFAULT).toString());
            }

            if (isDetailBackToList) {
                view.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);

                JZVideoPlayer first = JZVideoPlayerManager.getFirstFloor();
                if (first instanceof ZhiyiVideoView) {
                    ZhiyiVideoView videoView = (ZhiyiVideoView) first;
                    if (!videoFrom().equals(videoView.mVideoFrom)) {
                        return;
                    }
                }
                first.textureViewContainer.removeView(JZMediaManager.textureView);
                view.setState(first.currentState);
                view.addTextureView();
                JZVideoPlayerManager.setFirstFloor(view);
                view.startProgressTimer();
            } else {
                view.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
            }
        } else

        {
            view.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
        }

        view.positionInList = positon;

    }

    protected String videoFrom() {
        return "";
    }

    @Override
    public boolean isForViewType(DynamicDetailBeanV2 item, int position) {
        return item.getVideo() != null && item.getFeed_from() != DEFAULT_ADVERT_FROM_TAG;
    }

    @Override
    protected int getCurrenCloums() {
        return CURREN_CLOUMS;
    }
}
