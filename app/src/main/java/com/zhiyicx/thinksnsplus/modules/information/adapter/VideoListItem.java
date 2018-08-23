package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.StringSignature;
import com.hyphenate.util.DensityUtil;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.information.infomain.list.InfoListPresenter;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.LinkedHashMap;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.jzvd.JZVideoPlayer.URL_KEY_DEFAULT;

public abstract class VideoListItem implements ItemViewDelegate<BaseListBean> {

    private Context mContext;
    private InfoListPresenter mPresenter;


    public VideoListItem(Context mContext, InfoListPresenter mPresenter) {
        this.mContext = mContext;
        this.mPresenter = mPresenter;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_video_info;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListDataBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                        final int position, int itemCounts) {
        final InfoListDataBean info = (InfoListDataBean) baseListBean;

        holder.getTextView(R.id.tv_title).setText(info.getTitle());
        holder.getTextView(R.id.tv_comment_count).setText(String.valueOf(info.getComment_count()) );
        holder.getTextView(R.id.tv_dig_count).setText( String.valueOf(info.getDigg_count()) );
        holder.getTextView(R.id.tv_dig_count).setSelected(info.getHas_like());
        holder.getTextView(R.id.tv_user_name).setText(info.getUser_name());
        ImageUtils.loadCircleImageDefault(holder.getImageViwe(R.id.user_avatar),info.getAvatar());

        initVideoView(info,ImageUtils.getVideoUrl(info.getVideo()),holder.getView(R.id.videoplayer),position);

        holder.getView(R.id.tv_dig_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mPresenter)
                    mPresenter.handleLike(info);
            }
        });


    }

    private void initVideoView(InfoListDataBean infoBean,String videoUrl, ZhiyiVideoView view,int position){

        String imageUrl = ImageUtils.imagePathConvertV2(infoBean.getImage().getId(), 0, 0, ImageZipConfig.IMAGE_100_ZIP);

        Glide.with(mContext)
                .load(imageUrl)
                .signature(new StringSignature(imageUrl + infoBean.getCreated_at()))
                .placeholder(R.mipmap.default_image_for_video)
                .error(R.mipmap.default_image_for_video)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean
                            isFromMemoryCache, boolean isFirstResource) {
                        Observable.just(resource)
                                .subscribeOn(Schedulers.io())
                                .map(glideDrawable -> {
                                    Bitmap bitmap = FastBlur.blurBitmapForShortVideo(ConvertUtils.drawable2Bitmap(resource), resource
                                            .getIntrinsicWidth(), resource
                                            .getIntrinsicHeight());
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

        if (JZVideoPlayerManager.getFirstFloor() != null
                && JZVideoPlayerManager.getFirstFloor().positionInList == position
                && !JZVideoPlayerManager.getCurrentJzvd().equals(view)) {

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
                    if (!"".equals(videoView.mVideoFrom)) {
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
        } else {
            view.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
        }

        view.positionInList = position;

    }

    public abstract void itemClick(int position, ImageView imageView, TextView title,
                                   InfoListDataBean realData);

}