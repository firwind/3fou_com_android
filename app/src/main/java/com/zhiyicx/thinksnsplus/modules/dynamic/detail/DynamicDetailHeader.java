package com.zhiyicx.thinksnsplus.modules.dynamic.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.GifRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.klinker.android.link_builder.Link;
import com.klinker.android.link_builder.LinkMetadata;
import com.klinker.android.link_builder.NetUrlHandleBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FastBlur;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDigListBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.dig_list.DigListFragment;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerStandard;

import static android.support.v4.view.ViewCompat.LAYER_TYPE_HARDWARE;
import static android.support.v4.view.ViewCompat.LAYER_TYPE_NONE;
import static android.support.v4.view.ViewCompat.LAYER_TYPE_SOFTWARE;
import static cn.jzvd.JZVideoPlayer.URL_KEY_DEFAULT;

/**
 * @Describe 动态详情头部信息
 * @Author zl
 * @Date 2017/3/10
 * @Contact master.jungle68@gmail.com
 */

public class DynamicDetailHeader {
    private static final int DEFAULT_PART_TOTAL = 100;

    private LinearLayout mPhotoContainer;
    private TextView mContent;
    private TextView mTitle;
    private View mDynamicDetailHeader;
    private FrameLayout mFlcommentcountcontainer;
    private ReWardView mReWardView;
    private Context mContext;
    private int screenWidth;
    private int screenHeight;
    private int picWidth;
    private Bitmap sharBitmap;
    private OnImageClickLisenter mOnImageClickLisenter;
    private TextViewUtils.OnSpanTextClickListener mOnSpanTextClickListener;

    /**
     * Gif 是否直接播放
     */
    private boolean mIsGifPlay = false;
    private boolean isListToDetail = false;

    View getDynamicDetailHeader() {
        return mDynamicDetailHeader;
    }

    DynamicDetailHeader(Context context, List<RealAdvertListBean> adverts) {
        this.mContext = context;
        mDynamicDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .view_header_dynamic_detial, null);
        mDynamicDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_title);
        mContent = (TextView) mDynamicDetailHeader.findViewById(R.id.tv_dynamic_content);
        initAdvert(context, adverts);
        mFlcommentcountcontainer = (FrameLayout) mDynamicDetailHeader.findViewById(R.id
                .fl_comment_count_container);
        mPhotoContainer = (LinearLayout) mDynamicDetailHeader.findViewById(R.id
                .ll_dynamic_photos_container);
        picWidth = UIUtils.getWindowWidth(context);
        screenWidth = UIUtils.getWindowWidth(context);
        screenHeight = UIUtils.getWindowHeight(context) - DeviceUtils.getStatuBarHeight(mContext)
                - mContext.getResources().getDimensionPixelOffset(com.zhiyicx.baseproject.R.dimen
                .toolbar_height)
                - mContent.getResources().getDimensionPixelOffset(com.zhiyicx.baseproject.R.dimen
                .divider_line);
//        picWidth = UIUtils.getWindowWidth(context) - context.getResources().getDimensionPixelSize
//                (R.dimen.spacing_normal) * 2;
        picWidth = screenWidth;
        mReWardView = (ReWardView) mDynamicDetailHeader.findViewById(R.id.v_reward);
    }

    private void initAdvert(Context context, List<RealAdvertListBean> adverts) {
        DynamicDetailAdvertHeader dynamicDetailAdvertHeader = new DynamicDetailAdvertHeader
                (context, mDynamicDetailHeader
                        .findViewById(R.id.ll_advert));
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || adverts.isEmpty()) {
            dynamicDetailAdvertHeader.hideAdvert();
            return;
        }
        dynamicDetailAdvertHeader.setTitle(context.getString(R.string.advert));
        dynamicDetailAdvertHeader.setAdverts(adverts);
        dynamicDetailAdvertHeader.setOnItemClickListener((v, position1, url) ->
                toAdvert(adverts.get(position1).getAdvertFormat().getImage().getLink(), adverts
                        .get(position1).getTitle())
        );
    }

    private void toAdvert(String link, String title) {
        CustomWEBActivity.startToWEBActivity(mContext, link, title);
    }

    /**
     * 设置头部动态信息
     *
     * @param dynamicBean
     */
    public void setDynamicDetial(DynamicDetailBeanV2 dynamicBean, int state, ZhiyiVideoView
            .ShareInterface shareInterface) {
        String titleText = "";
        if (TextUtils.isEmpty(titleText)) {
            mTitle.setVisibility(View.GONE);
        } else {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(titleText);
        }
        String contentText = dynamicBean.getFeed_content();
        if (TextUtils.isEmpty(contentText)) {
            mContent.setVisibility(View.GONE);
        } else {
//            dealTollWords(dynamicBean, contentText);// 处理文字收费
            mContent.setVisibility(View.VISIBLE);
            dealLinkWords(dynamicBean, contentText);
        }

        final Context context = mTitle.getContext();
        // 设置图片
        List<DynamicDetailBeanV2.ImagesBean> photoList = dynamicBean.getImages();
        boolean hasImage = photoList != null && !photoList.isEmpty();
        boolean hasVideo = dynamicBean.getVideo() != null;
        if (!hasImage && !hasVideo) {
            mPhotoContainer.setVisibility(View.GONE);
        } else {

            mPhotoContainer.setVisibility(View.VISIBLE);
            DynamicDetailBeanV2.Video video = dynamicBean.getVideo();
            if (hasVideo) {
                ZhiyiVideoView videoView = new ZhiyiVideoView(mContext);
                videoView.setShareInterface(shareInterface);
                mPhotoContainer.addView(videoView);

                int width = picWidth;
                int height = (video.getHeight() * picWidth / video.getWidth());
                if (height > screenHeight) {
                    height = screenHeight;
                    width = (screenHeight / video.getHeight()) * video.getWidth();
                }
                videoView.getLayoutParams().height = height;
                videoView.getLayoutParams().width = width;

                String videoUrl = String.format(ApiConfig.APP_DOMAIN + ApiConfig.FILE_PATH,
                        dynamicBean.getVideo().getVideo_id());


                LogUtils.d(JZMediaManager.getCurrentDataSource());
                if (JZVideoPlayerManager.getFirstFloor() != null
                        && !JZVideoPlayerManager.getCurrentJzvd().equals(videoView)) {

                    LinkedHashMap<String, Object> map = (LinkedHashMap) JZVideoPlayerManager.getFirstFloor().dataSourceObjects[0];
                    if (map != null) {
                        isListToDetail = videoUrl.equals(map.get(URL_KEY_DEFAULT).toString());
                    }

                    if (isListToDetail) {
                        videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
                        videoView.positionInList = 0;

                        videoView.setState(state);
                        videoView.positionInList = JZVideoPlayerManager.getFirstFloor().positionInList;
                        videoView.addTextureView();
                        if (JZVideoPlayerManager.getFirstFloor() instanceof ZhiyiVideoView) {
                            ZhiyiVideoView firstFloor = (ZhiyiVideoView) JZVideoPlayerManager.getFirstFloor();
                            videoView.mVideoFrom = firstFloor.mVideoFrom;
                        }
                        JZVideoPlayerManager.setFirstFloor(videoView);
                        videoView.startProgressTimer();
                        if (state == ZhiyiVideoView.CURRENT_STATE_PAUSE) {
                            videoView.startButton.callOnClick();
                        }
                    } else {
                        videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
                        videoView.positionInList = 0;
                    }

                } else {
                    videoView.setUp(videoUrl, JZVideoPlayerStandard.SCREEN_WINDOW_LIST);
                    videoView.positionInList = 0;
                }

                Glide.with(mContext)
                        .load(video.getGlideUrl())
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.shape_default_image)
                        .listener(new RequestListener<GlideUrl, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, GlideUrl model,
                                                       Target<GlideDrawable> target, boolean
                                                               isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, GlideUrl
                                    model, Target<GlideDrawable> target, boolean
                                                                   isFromMemoryCache, boolean
                                                                   isFirstResource) {
                                sharBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(mContext, resource, R.mipmap.icon);
                                Bitmap bitmap = FastBlur.blurBitmapForShortVideo(ConvertUtils.drawable2Bitmap(resource), resource
                                        .getIntrinsicWidth(), resource
                                        .getIntrinsicHeight());
                                videoView.setBackground(new BitmapDrawable(mContext.getResources
                                        (), bitmap));
                                return false;
                            }
                        })
                        .into(videoView.thumbImageView);


            } else {
                for (int i = 0; i < photoList.size(); i++) {
                    showContentImage(context, photoList, i, i == photoList.size() - 1, mPhotoContainer);
                }
                setImageClickListener(photoList, dynamicBean);
            }

        }
    }

    /**
     * 文字网页链接点击处理
     *
     * @param dynamicBean
     * @param content
     */
    private void dealLinkWords(DynamicDetailBeanV2 dynamicBean, String content) {
        content = content.replaceAll(MarkdownConfig.NETSITE_FORMAT, Link.DEFAULT_NET_SITE);
        mContent.setText(content);
        ConvertUtils.stringLinkConvert(mContent, setLiknks(dynamicBean, mContent.getText()
                .toString()), false);
    }

    /**
     * 处理详情文字收费
     *
     * @param dynamicBean
     * @param contentText
     */
    private void dealTollWords(DynamicDetailBeanV2 dynamicBean, String contentText) {
        if (contentText.length() == 50 && dynamicBean.getPaid_node() != null && !dynamicBean
                .getPaid_node().isPaid()) {
            contentText += mContext.getString(R.string.words_holder);
        }

        TextViewUtils textViewUtils = TextViewUtils.newInstance(mContent, contentText)
                .spanTextColor(SkinUtils.getColor(R
                        .color.normal_for_assist_text))
                .position(50, contentText.length())
                .maxLines(mContext.getResources().getInteger(R.integer
                        .dynamic_list_content_show_lines))
                .onSpanTextClickListener(mOnSpanTextClickListener)
                .disPlayText(true);

        if (dynamicBean.getPaid_node() != null) {// 有文字收费
            textViewUtils.note(dynamicBean.getPaid_node().getNode())
                    .amount(dynamicBean.getPaid_node().getAmount())
                    .disPlayText(dynamicBean.getPaid_node().isPaid());
        }
        textViewUtils.build();
    }

    Bitmap getSharBitmap() {
        ImageView imageView;
        try {
            if (mPhotoContainer.getChildAt(0) instanceof ZhiyiVideoView) {
                ZhiyiVideoView videoView = (ZhiyiVideoView) mPhotoContainer.getChildAt(0);
                imageView = videoView.thumbImageView;
            } else {
                imageView = (ImageView) mPhotoContainer.getChildAt(0).findViewById
                        (R.id.dynamic_content_img);
            }
            if (imageView != null) {
                sharBitmap = ConvertUtils.drawable2BitmapWithWhiteBg(mContext, imageView
                        .getDrawable(), R.mipmap.icon);
            }
        } catch (Exception e) {

        }
        if (sharBitmap == null) {
            sharBitmap = ConvertUtils.drawBg4Bitmap(ContextCompat.getColor(mContext, R.color.white), BitmapFactory.decodeResource(mContent
                    .getResources(), R.mipmap.icon).copy(Bitmap.Config.RGB_565, true));
        }
        return sharBitmap;
    }

    /**
     * 更新动态的图片加载
     *
     * @param dynamicBean
     */
    void updateImage(DynamicDetailBeanV2 dynamicBean) {
        if (dynamicBean.getVideo() != null) {
            return;
        }
        List<DynamicDetailBeanV2.ImagesBean> photoList = dynamicBean.getImages();
        mPhotoContainer.removeAllViews();
        for (int i = 0; i < photoList.size(); i++) {
            showContentImage(mContext, photoList, i, i == photoList.size() - 1, mPhotoContainer);
        }

        setImageClickListener(photoList, dynamicBean);
    }

    /**
     * 更新喜欢的人
     *
     * @param dynamicBean
     */
    void updateHeaderViewData(final DynamicDetailBeanV2 dynamicBean) {

        DynamicHorizontalStackIconView dynamicHorizontalStackIconView =
                (DynamicHorizontalStackIconView) mDynamicDetailHeader.findViewById(R.id
                        .detail_dig_view);

        dynamicHorizontalStackIconView.setDigCount(dynamicBean.getFeed_digg_count());
        dynamicHorizontalStackIconView.setPublishTime(dynamicBean.getCreated_at());
        dynamicHorizontalStackIconView.setViewerCount(dynamicBean.getFeed_view_count());
        // 设置点赞头像
        List<DynamicDigListBean> userInfoList = dynamicBean.getDigUserInfoList();
        dynamicHorizontalStackIconView.setDigUserHeadIcon(userInfoList);

        // 设置跳转到点赞列表
        dynamicHorizontalStackIconView.setDigContainerClickListener(digContainer -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(DigListFragment.DIG_LIST_DATA, dynamicBean);
            Intent intent = new Intent(mDynamicDetailHeader.getContext(), DigListActivity
                    .class);
            intent.putExtras(bundle);
            mDynamicDetailHeader.getContext().startActivity(intent);
        });
        if (dynamicBean.getFeed_comment_count() <= 0) {
            mFlcommentcountcontainer.setVisibility(View.GONE);
        } else {
            ((TextView) mDynamicDetailHeader.findViewById(R.id.tv_comment_count)).setText
                    (mDynamicDetailHeader.getResources().getString(R.string
                            .dynamic_comment_count, ConvertUtils.numberConvert(dynamicBean
                            .getFeed_comment_count())));
            mFlcommentcountcontainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 更新打赏内容
     *
     * @param sourceId         source id  for this reward
     * @param data             reward's users
     * @param rewardsCountBean all reward data
     * @param rewardType       reward type
     */
    public void updateReward(long sourceId, List<RewardsListBean> data, RewardsCountBean
            rewardsCountBean,
                             RewardType rewardType, String moneyName) {
        mReWardView.initData(sourceId, data, rewardsCountBean, rewardType, moneyName);
    }

    /**
     * 显示内容中的 图片
     *
     * @param context
     * @param photoList
     * @param position
     * @param lastImg
     * @param photoContainer
     */
    private void showContentImage(Context context, List<DynamicDetailBeanV2.ImagesBean>
            photoList, final int position,
                                  boolean lastImg, LinearLayout photoContainer) {
        DynamicDetailBeanV2.ImagesBean imageBean = photoList.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.view_dynamic_detail_photos, null);
        FilterImageView imageView = (FilterImageView) view.findViewById(R.id.dynamic_content_img);
        imageView.setLayerType(LAYER_TYPE_NONE,null);
        // 隐藏最后一张图的下间距
        if (lastImg) {
            view.findViewById(R.id.img_divider).setVisibility(View.GONE);
        }

        int height = (imageBean.getHeight() * picWidth / imageBean.getWidth());
        // 提前设置图片控件的大小，使得占位图显示
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(picWidth, height);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        imageView.setLayoutParams(layoutParams);

        if (TextUtils.isEmpty(imageBean.getImgUrl())) {
            int part = (picWidth / imageBean.getWidth()) * DEFAULT_PART_TOTAL;
            if (part > DEFAULT_PART_TOTAL) {
                part = DEFAULT_PART_TOTAL;
            }
            boolean canLook = !(imageBean.isPaid() != null && !imageBean.isPaid()
                    && imageBean.getType().equals(Toll.LOOK_TOLL_TYPE));
            boolean isGif = ImageUtils.imageIsGif(imageBean.getImgMimeType());
            boolean isImageOutOfBounds = ImageUtils.isWithOrHeightOutOfBounds(picWidth, height);
            boolean isLongImage = ImageUtils.isLongImage(height, picWidth);
            boolean isNeedOrigin = isGif || isImageOutOfBounds || isLongImage;

            imageView.setIshowGifTag(isGif);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            if (ImageUtils.imageIsGif(imageBean.getImgMimeType()) && mIsGifPlay) {
                GifRequestBuilder requestBuilder = Glide.with(mContext)
                        .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), canLook
                                        ? picWidth : 0,
                                canLook ? height : 0, isNeedOrigin ? ImageZipConfig.IMAGE_100_ZIP : part, AppApplication.getTOKEN()))
                        .asGif()
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(picWidth, height)
                        .error(R.drawable.shape_default_image);
                if (!canLook) {// 切换展位图防止闪屏
                    requestBuilder.placeholder(R.drawable.shape_default_image);
                }
                requestBuilder.into(imageView);
            } else {
                BitmapRequestBuilder requestBuilder = Glide.with(mContext)
                        .load(ImageUtils.imagePathConvertV2(canLook, imageBean.getFile(), canLook
                                        ? picWidth : 0,
                                canLook ? height : 0, isNeedOrigin ? ImageZipConfig.IMAGE_100_ZIP : part, AppApplication.getTOKEN()))
                        .asBitmap()
                        .placeholder(R.drawable.shape_default_image)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(picWidth, height)
                        .error(R.drawable.shape_default_error_image);
                if (!canLook) {
                    // 切换展位图防止闪屏
                    requestBuilder.placeholder(R.drawable.shape_default_image);
                }
                requestBuilder.into(imageView);
            }
        } else {
            Glide.with(mContext)
                    .load(imageBean.getImgUrl())
                    .override(picWidth, height)
                    .placeholder(R.drawable.shape_default_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.shape_default_image)
                    .into(imageView);
        }
        photoContainer.addView(view);
    }

    /**
     * 设置图片点击事件
     */
    private void setImageClickListener(final List<DynamicDetailBeanV2.ImagesBean> photoList,
                                       final DynamicDetailBeanV2 dynamicBean) {
        final ArrayList<AnimationRectBean> animationRectBeanArrayList
                = new ArrayList<>();
        final List<ImageBean> imageBeans = new ArrayList<>();
        for (int i = 0; i < mPhotoContainer.getChildCount(); i++) {
            final View photoView = mPhotoContainer.getChildAt(i);
            int imagePosition = i;
            ImageView imageView = (ImageView) photoView.findViewById(R.id.dynamic_content_img);
            imageView.setOnClickListener(v -> {
                animationRectBeanArrayList.clear();
                DynamicDetailBeanV2.ImagesBean img = photoList.get(imagePosition);
                Boolean canLook = !(img.isPaid() != null && !img.isPaid() && img.getType().equals
                        (Toll.LOOK_TOLL_TYPE));
                if (!canLook) {
                    mOnImageClickLisenter.onImageClick(imagePosition, img.getAmount(), photoList
                            .get(imagePosition).getPaid_node());
                    return;
                }
                imageBeans.clear();
                for (int i1 = 0; i1 < mPhotoContainer.getChildCount(); i1++) {
                    View photoView1 = mPhotoContainer.getChildAt(i1);
                    ImageView imageView1 = (ImageView) photoView1.findViewById(R.id
                            .dynamic_content_img);
                    DynamicDetailBeanV2.ImagesBean task = photoList.get(i1);
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(task.getImgUrl());// 本地地址，也许有
                    Toll toll = new Toll(); // 收费信息
                    toll.setPaid(task.isPaid());// 是否已經付費
                    toll.setToll_money(task.getAmount());// 付费金额
                    toll.setToll_type_string(task.getType());// 付费类型
                    toll.setPaid_node(task.getPaid_node());// 付费节点
                    imageBean.setToll(toll);
                    imageBean.setFeed_id(dynamicBean.getId());// 动态id
                    imageBean.setListCacheUrl(task.getGlideUrl());
                    imageBean.setWidth(task.getWidth());// 图片宽高
                    imageBean.setHeight(task.getHeight());
                    imageBean.setStorage_id(task.getFile());// 图片附件id
                    imageBean.setImgMimeType(task.getImgMimeType());
                    imageBeans.add(imageBean);
                    AnimationRectBean rect = AnimationRectBean.buildFromImageView(imageView1);//
                    // 动画矩形
                    animationRectBeanArrayList.add(rect);
                }

                GalleryActivity.startToGallery(mContext, mPhotoContainer.indexOfChild
                        (photoView), imageBeans, animationRectBeanArrayList);
            });
        }
    }

    public OnImageClickLisenter getOnImageClickLisenter() {
        return mOnImageClickLisenter;
    }

    void setOnImageClickLisenter(OnImageClickLisenter onImageClickLisenter) {
        mOnImageClickLisenter = onImageClickLisenter;
    }

    void setReWardViewVisible(int visible) {
        mReWardView.setVisibility(visible);
    }

    protected List<Link> setLiknks(final DynamicDetailBeanV2 dynamicDetailBeanV2, String content) {
        List<Link> links = new ArrayList<>();
        if (content.contains(Link.DEFAULT_NET_SITE)) {
            Link commentNameLink = new Link(Link.DEFAULT_NET_SITE)
                    .setTextColor(ContextCompat.getColor(mContext, R.color
                            .themeColor))
                    .setLinkMetadata(LinkMetadata.builder()
                            .putSerializableObj(LinkMetadata.METADATA_KEY_COTENT, new
                                    NetUrlHandleBean(dynamicDetailBeanV2.getFeed_content()))
                            .putSerializableObj(LinkMetadata.METADATA_KEY_TYPE, LinkMetadata
                                    .SpanType.NET_SITE)
                            .build())
                    .setTextColorOfHighlightedLink(ContextCompat.getColor(mContext, R.color
                            .general_for_hint))
                    .setHighlightAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .setOnClickListener((clickedText, linkMetadata) -> {
                        LogUtils.d(clickedText);
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri contentUrl = Uri.parse(clickedText);
                        intent.setData(contentUrl);
                        mContext.startActivity(intent);
                    })
                    .setOnLongClickListener((clickedText, linkMetadata) -> {

                    })
                    .setUnderlined(false);
            links.add(commentNameLink);
        }
        return links;
    }

    int scrollCommentToTop() {
        return mReWardView.getBottom();
    }

    public interface OnImageClickLisenter {
        void onImageClick(int iamgePosition, long amount, int note);
    }

    public boolean isListToDetail() {
        return isListToDetail;
    }
}
