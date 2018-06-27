package com.zhiyicx.thinksnsplus.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.signature.StringSignature;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleBorderTransform;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.SendCertificationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.Hashtable;
import java.util.Locale;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.zhiyicx.baseproject.config.ImageZipConfig.IMAGE_100_ZIP;
import static com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2.ImagesBean.FILE_MIME_TYPE_GIF;

/**
 * @Describe
 * @Author zl
 * @Date 2017/3/7
 * @Contact master.jungle68@gmail.com
 */

public class ImageUtils {
    private static final String SHAREPREFERENCE_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE = "sharepreference_user_headpic_signature";

    private static final String SHAREPREFERENCE_USER_COVER_SIGNATURE = "sharepreference_user_cover_signature";
    private static final String SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE = "sharepreference_user_cover_signature";
    private static final long DEFAULT_USER_CACHE_TIME = 3 * 24 * 60_1000;
    private static final long DEFAULT_SHAREPREFERENCES_OFFSET_TIME = 10_1000;
    private static long laste_request_time;
    /**
     * 服务器支持的可剪切的最大长度边  阿里 4096
     */
    private static final int MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT = 4000;

    private static long mHeadPicSigture;
    private static long mCoverSigture;

    /**
     * mWidthPixels = DeviceUtils.getScreenWidth(context);
     * mHightPixels = DeviceUtils.getScreenHeight(context);
     * mMargin = 2 * context.getResources().getDimensionPixelSize(R.dimen
     * .dynamic_list_image_marginright);
     * mDiverwith = context.getResources().getDimensionPixelSize(R.dimen.spacing_small);
     * mImageContainerWith = mWidthPixels - mMargin;
     * // 最大高度是最大宽度的4/3 保持 宽高比 3：4
     * mImageMaxHeight = mImageContainerWith * 4 / 3;
     */

    public static int getmWidthPixels() {
        return DeviceUtils.getScreenWidth(AppApplication.getContext());
    }

    public static int getmHightPixels() {
        return DeviceUtils.getScreenHeight(AppApplication.getContext());
    }


    public static int getmMargin() {
        return 2 * AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.dynamic_list_image_marginright);
    }

    public static int getmDiverwith() {
        return AppApplication.getContext().getResources().getDimensionPixelSize(R.dimen.spacing_small);
    }

    public static int getmImageContainerWith() {
        int maxWidth = AppApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.dynamic_image_max_width);
        int with = getmWidthPixels() - getmMargin();
        with = with > maxWidth ? maxWidth : with;
        return with;
    }

    /**
     * 设计图 4：3
     *
     * @return
     */
    public static int getmImageMaxHeight() {
        return getmImageContainerWith() * 4 / 3;
    }

    public static void updateCurrentLoginUserHeadPicSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    public static void updateCurrentLoginUserCoverSignature(Context context) {
        SharePreferenceUtils.saveLong(context.getApplicationContext(), SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE, System
                .currentTimeMillis() - DEFAULT_USER_CACHE_TIME);
    }

    /**
     * 加载用户背景图
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadUserCover(UserInfoBean userInfoBean, ImageView imageView) {
        if (checkImageContext(imageView)) {
            return;
        }

        long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();

        if (userInfoBean.getUser_id() == currentLoginUerId) {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                    SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE);
        } else {
            mCoverSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(), SHAREPREFERENCE_USER_COVER_SIGNATURE);
        }
        if (System.currentTimeMillis() - mCoverSigture > DEFAULT_USER_CACHE_TIME) {
            mCoverSigture = System.currentTimeMillis();
        }
        SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_COVER__SIGNATURE :
                        SHAREPREFERENCE_USER_COVER_SIGNATURE, mHeadPicSigture);
        Glide.with(imageView.getContext())
                .load(userInfoBean.getCover())
//                .signature(new StringSignature(String.valueOf(mCoverSigture)))
                .placeholder(R.mipmap.default_pic_personal)
                .error(R.mipmap.default_pic_personal)
                .into(imageView);
    }

    public static boolean checkImageContext(View imageView) {
        if (imageView == null || imageView.getContext() == null) {
            return true;
        }
        if (imageView.getContext() instanceof Activity) {
            if (((Activity) imageView.getContext()).isFinishing()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 加载用户头像
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadCircleUserHeadPic(UserInfoBean userInfoBean, UserAvatarView imageView) {
        loadUserHead(userInfoBean, imageView, false);
    }

    /**
     * 加载用户头像
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     * @param anonymity    是否匿名展示
     */
    public static void loadCircleUserHeadPic(UserInfoBean userInfoBean, UserAvatarView imageView, boolean anonymity) {
        loadUserHead(userInfoBean, imageView, false, anonymity);
    }


    /**
     * 加载用户头像带有白色边框
     *
     * @param userInfoBean 用户信息
     * @param imageView    展示的控件
     */
    public static void loadCircleUserHeadPicWithBorder(UserInfoBean userInfoBean, UserAvatarView imageView) {
        loadUserHead(userInfoBean, imageView, true);
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     */
    public static void loadUserHead(UserInfoBean userInfoBean, UserAvatarView imageView, boolean withBorder) {
        if (checkImageContext(imageView)) {
            return;
        }

        loadUserAvatar(userInfoBean, imageView.getIvAvatar(), withBorder);
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(TextUtils.isEmpty(userInfoBean.getVerified().getIcon()) ? R.drawable.shape_default_image : userInfoBean.getVerified()
                            .getIcon())
//                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                    .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载聊天中的用户头像
     *
     * @param userInfoBean 用户信息
     * @param imageView    控件
     * @param withBorder   边框
     */
    public static void loadUserHead(ChatUserInfoBean userInfoBean, UserAvatarView imageView, boolean withBorder) {
        if (checkImageContext(imageView)) {
            return;
        }

        loadUserAvatar(userInfoBean, imageView.getIvAvatar(), withBorder);
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(TextUtils.isEmpty(userInfoBean.getVerified().getIcon()) ? R.drawable.shape_default_image : userInfoBean.getVerified()
                            .getIcon())
//                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                    .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     * @param anonymity    是否匿名展示
     */
    public static void loadUserHead(UserInfoBean userInfoBean, UserAvatarView imageView, boolean withBorder, boolean anonymity) {
        if (checkImageContext(imageView)) {
            return;
        }

        FilterImageView imageView1 = imageView.getIvAvatar();
        imageView1.setIsText(anonymity);
        loadUserAvatar(userInfoBean, imageView1, withBorder);
//        if (anonymity){
//            // 匿名用户不要认证图标
//            userInfoBean.setVerified(null);
//        }
        if (userInfoBean != null && userInfoBean.getVerified() != null && !TextUtils.isEmpty(userInfoBean.getVerified().getType())) {
            if (TextUtils.isEmpty(userInfoBean.getVerified().getIcon())) {
                userInfoBean.getVerified().setIcon("");
            }
            Glide.with(imageView.getContext())
                    .load(userInfoBean.getVerified().getIcon())
//                    .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                    .placeholder(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .error(userInfoBean.getVerified().getType().equals(SendCertificationBean.ORG) ? R.mipmap.pic_identi_company : R.mipmap
                            .pic_identi_individual)
                    .transform(withBorder ?
                            new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                    .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                            : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                    .into(imageView.getIvVerify());
            imageView.getIvVerify().setVisibility(View.VISIBLE);
        } else {
            imageView.getIvVerify().setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 加载用户圆形图像
     *
     * @param userInfoBean 用户信息
     * @param imageView    显示头像的控件
     * @param withBorder   是否需要边框
     */
    public static void loadUserHead(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        loadUserAvatar(userInfoBean, imageView, withBorder);
    }

    /**
     * 加载通用用户信息
     *
     * @param userInfoBean
     * @param imageView
     * @param withBorder
     */
    private static void loadUserAvatar(UserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        String avatar = "";
        if (userInfoBean != null && userInfoBean.getUser_id() != null) {
            avatar = TextUtils.isEmpty(userInfoBean.getAvatar()) ? "" : userInfoBean.getAvatar();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() ==
                    currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE :
                                SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        int defaultErrorAvatar = getDefaultAvatar(userInfoBean);
        if (!TextUtils.isEmpty(avatar) && FileUtils.isFile(avatar)) {

        }
        Glide.with(imageView.getContext())
                .load(TextUtils.isEmpty(avatar) ? R.drawable.shape_default_image : avatar)
//                .signature(new StringSignature(String.valueOf(mHeadPicSigture)));
                .placeholder(withBorder ? defaultErrorAvatar : defaultErrorAvatar)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);
    }

    /**
     * 加载聊天页用户信息
     *
     * @param userInfoBean
     * @param imageView
     * @param withBorder
     */
    private static void loadUserAvatar(ChatUserInfoBean userInfoBean, ImageView imageView, boolean withBorder) {
        String avatar = "";
        if (userInfoBean != null && userInfoBean.getUser_id() != null) {
            avatar = TextUtils.isEmpty(userInfoBean.getAvatar()) ? "" : userInfoBean.getAvatar();
            long currentLoginUerId = AppApplication.getmCurrentLoginAuth() == null ? 0 : AppApplication.getmCurrentLoginAuth().getUser_id();
            if (System.currentTimeMillis() - laste_request_time > DEFAULT_SHAREPREFERENCES_OFFSET_TIME || userInfoBean.getUser_id() ==
                    currentLoginUerId) {

                if (userInfoBean.getUser_id() == currentLoginUerId) {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE);
                } else {
                    mHeadPicSigture = SharePreferenceUtils.getLong(imageView.getContext().getApplicationContext(),
                            SHAREPREFERENCE_USER_HEADPIC_SIGNATURE);
                }
                if (System.currentTimeMillis() - mHeadPicSigture > DEFAULT_USER_CACHE_TIME) {
                    mHeadPicSigture = System.currentTimeMillis();
                }
                SharePreferenceUtils.saveLong(imageView.getContext().getApplicationContext()
                        , userInfoBean.getUser_id() == currentLoginUerId ? SHAREPREFERENCE_CURRENT_LOGIN_USER_HEADPIC_SIGNATURE :
                                SHAREPREFERENCE_USER_HEADPIC_SIGNATURE, mHeadPicSigture);
            }
            laste_request_time = System.currentTimeMillis();
        }
        int defaultAvatar = getDefaultAvatar(userInfoBean);
        Glide.with(imageView.getContext())
                .load(TextUtils.isEmpty(avatar) ? R.drawable.shape_default_image : avatar)
//                .signature(new StringSignature(String.valueOf(mHeadPicSigture)))
                .placeholder(withBorder ? defaultAvatar : defaultAvatar)
                .error(withBorder ? defaultAvatar : defaultAvatar)
                .transform(withBorder ?
                        new GlideCircleBorderTransform(imageView.getContext().getApplicationContext(), imageView.getResources()
                                .getDimensionPixelSize(R.dimen.spacing_tiny), ContextCompat.getColor(imageView.getContext(), R.color.white))
                        : new GlideCircleTransform(imageView.getContext().getApplicationContext()))
                .into(imageView);

    }

    /**
     * 获取用户头像地址
     *
     * @param userId user's  id
     */
    public static String getUserAvatar(Long userId) {
        if (userId == null) {
            userId = 0L;
        }
        return String.format(ApiConfig.IMAGE_AVATAR_PATH_V2, userId);

    }

    /**
     * 获取用户头像地址
     *
     * @param userInfoBean user's  info
     */
    public static String getUserAvatar(UserInfoBean userInfoBean) {
        if (userInfoBean == null || userInfoBean.getAvatar() == null) {
            return "";
        } else {
            return userInfoBean.getAvatar();
        }
    }

    /**
     * 获取用户默认头像
     *
     * @param userInfoBean user's  info
     */
    public static int getDefaultAvatar(UserInfoBean userInfoBean) {
        int defaultAvatar;
        if (userInfoBean == null) {
            return R.mipmap.pic_default_secret;
        }
        switch (userInfoBean.getSex()) {

            case UserInfoBean.FEMALE:
                defaultAvatar = R.mipmap.pic_default_woman;
                break;
            case UserInfoBean.MALE:
                defaultAvatar = R.mipmap.pic_default_man;

                break;
            case UserInfoBean.SECRET:
                defaultAvatar = R.mipmap.pic_default_secret;
                break;
            default:
                defaultAvatar = R.mipmap.pic_default_secret;

        }
        return defaultAvatar;
    }

    public static int getDefaultAvatar(ChatUserInfoBean userInfoBean) {
        int defaultAvatar;
        if (userInfoBean == null) {
            return R.mipmap.pic_default_secret;
        }
        switch (userInfoBean.getSex()) {

            case ChatUserInfoBean.FEMALE:
                defaultAvatar = R.mipmap.pic_default_woman;
                break;
            case ChatUserInfoBean.MALE:
                defaultAvatar = R.mipmap.pic_default_man;

                break;
            case ChatUserInfoBean.SECRET:
                defaultAvatar = R.mipmap.pic_default_secret;
                break;
            default:
                defaultAvatar = R.mipmap.pic_default_secret;

        }
        return defaultAvatar;
    }

    /**
     * 图片地址转换 V2 api
     *
     * @param canLook 是否可以查看
     * @param storage 图片对应的 id 号，也可能是本地的图片路径
     * @param part    压缩比例 0-100
     */
    public static GlideUrl imagePathConvertV2(boolean canLook, int storage, int w, int h, int part, String token) {

        String url = imagePathConvertV2(storage, w, h, part, canLook);
        return imagePathConvertV2(url, token);
    }


    /**
     * 图片地址转换 V2 api
     *
     * @param url   图片地址
     * @param token 图片token
     */
    public static GlideUrl imagePathConvertV2(String url, String token) {
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     * @param storage 图片资源id
     * @param w       宽
     * @param h       高
     * @param part    压缩比例
     * @param token   token
     */
    public static GlideUrl imagePathConvertV2(int storage, int w, int h, int part, String token) {
        return new GlideUrl(imagePathConvertV2(storage, w, h, part), new LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build());
    }

    /**
     * 通过 图片 id 、with、height、quality
     *
     * @param storage id
     * @param w       with
     * @param h       height
     * @param part    quality
     * @return 请求图片的地址
     */
    public static String imagePathConvertV2(int storage, int w, int h, int part) {
        if (part == IMAGE_100_ZIP) {
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2_ORIGIN, storage);
        } else {
            if (part < 40) {
                part = 40;
            }
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
        }

    }

    /**
     * 通过 图片 id 、with、height、quality
     *
     * @param storage id
     * @param w       with
     * @param h       height
     * @param part    quality
     * @return 请求图片的地址
     */
    public static String imagePathConvertV2(int storage, int w, int h, int part, boolean canLook) {
        if (canLook && part == IMAGE_100_ZIP) {
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2_ORIGIN, storage);
        } else {
            if (part < 40) {
                part = 40;
            }
            return String.format(Locale.getDefault(), ApiConfig.APP_DOMAIN + ApiConfig.IMAGE_PATH_V2, storage, w, h, part);
        }

    }

    /**
     * 通过 file path 获取 bitmap size
     *
     * @param url file path
     * @return bitmap size
     */
    public static long[] getBitmapSize(String url) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, op);
        return new long[]{op.outWidth, op.outHeight};
    }

    /**
     * 默认加载图片
     *
     * @param imageView target view to display image
     * @param url       image resuorce path
     */
    public static void loadImageDefault(ImageView imageView, String url) {
        if (checkImageContext(imageView)) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(R.drawable.shape_default_image)
                .error(R.drawable.shape_default_error_image)
                .into(imageView);

    }

    /**
     * 默认加载图片
     *
     * @param imageView target view to display image
     * @param url       image resuorce path
     */
    public static void loadImageDefaultNoHolder(ImageView imageView, String url) {
        if (checkImageContext(imageView)) {
            return;
        }
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.shape_default_error_image)
                .into(imageView);

    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param url
     */
    public static void loadCircleImageDefault(ImageView imageView, String url) {
        loadCircleImageDefault(imageView, url, R.drawable.shape_default_error_image, R.drawable.shape_default_image);
    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param url
     */
    public static void loadCircleImageDefault(ImageView imageView, String url, int errorResId, int placeResId) {
        Glide.with(imageView.getContext())
                .load(url)
                .placeholder(placeResId)
                .error(errorResId)
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 加载圆图
     *
     * @param imageView
     * @param resId
     */
    public static void loadCircleImageDefault(ImageView imageView, int resId, int errorResId, int placeResId) {
        Glide.with(imageView.getContext())
                .load(resId)
                .placeholder(placeResId)
                .error(errorResId)
                .bitmapTransform(new GlideCircleTransform(imageView.getContext()))
                .into(imageView);
    }

    /**
     * 获取 iamgeview 的 bitmap
     *
     * @param showImageView
     * @return
     */
    public static Bitmap getImageViewBitMap(ImageView showImageView) {
        showImageView.setDrawingCacheEnabled(true);

        Bitmap bitmap = showImageView.getDrawingCache();

        showImageView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 图片是否是 gif
     *
     * @param imageMinityp
     * @return
     */
    public static boolean imageIsGif(String imageMinityp) {
        return FILE_MIME_TYPE_GIF.equals(imageMinityp);
    }

    /**
     * 是否是长图
     *
     * @param netHeight
     * @param netWidth
     * @return
     */
    public static boolean isLongImage(float netHeight, float netWidth) {
        float ratio = netHeight / netWidth;
        float result = 0;
        if (ratio >= 3 || ratio <= .3f) {

            result = getmWidthPixels() / netWidth;

            if (result <= .3f) {

            } else {
                result = result * netHeight / getmHightPixels();
            }
        }
        return (result >= 3 || result <= .3f) && result > 0;

    }

    /**
     * 图片宽高是否超过了限制
     *
     * @return
     */
    public static boolean isWithOrHeightOutOfBounds(int with, int height) {
        return with > MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT
                || height > MAX_SERVER_SUPPORT_CUT_IMAGE_WITH_OR_HEIGHT;
    }


    /**
     * 生成二维码图片
     * @param text
     * @param dimension 宽高
     * @param logo
     * @return
     */
    public static Bitmap createQrcodeImage(String text,int dimension,Bitmap logo) {
        if (TextUtils.isEmpty(text)) {
            return null;
        }
        try {
            int w = dimension,h = dimension;
            Bitmap scaleLogo = getScaleLogo(logo,w,h);

            int offsetX = w / 2;
            int offsetY = h / 2;

            int scaleWidth = 0;
            int scaleHeight = 0;
            if (scaleLogo != null) {
                scaleWidth = scaleLogo.getWidth();
                scaleHeight = scaleLogo.getHeight();
                offsetX = (w - scaleWidth) / 2;
                offsetY = (h - scaleHeight) / 2;
            }
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错级别
            //hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);//这个默认就是L，可以不设置
            //设置空白边距的宽度
            hints.put(EncodeHintType.MARGIN, 0);
            BitMatrix bitMatrix = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h, hints);
            int[] pixels = new int[w * h];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    if(scaleLogo != null && x >= offsetX && x < offsetX + scaleWidth && y>= offsetY && y < offsetY + scaleHeight){
                        int pixel = scaleLogo.getPixel(x-offsetX,y-offsetY);
                        if(pixel == 0){
                            if(bitMatrix.get(x, y)){
                                pixel = BLACK;
                            }else{
                                pixel = WHITE;
                            }
                        }
                        pixels[y * w + x] = pixel;
                    }else{
                        if (bitMatrix.get(x, y)) {
                            pixels[y * w + x] = BLACK;
                        } else {
                            pixels[y * w + x] = WHITE;
                        }
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(w, h,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap getScaleLogo(Bitmap logo,int w,int h){
        if(logo == null)return null;
        Matrix matrix = new Matrix();
        float scaleFactor = Math.min(w * 1.0f / 5 / logo.getWidth(), h * 1.0f / 5 /logo.getHeight());
        matrix.postScale(scaleFactor,scaleFactor);
        Bitmap result = Bitmap.createBitmap(logo, 0, 0, logo.getWidth(),   logo.getHeight(), matrix, true);
        return result;
    }

    /**
     * 通过group_level得到等级的sign资源id
     * @param group_level
     * @return
     */
    public static int getGroupSignResId(int group_level){

        int resId = 0;
        switch (group_level){
            case 1:
                resId = R.mipmap.icon_official_group;
                break;
            case 2:
                resId = R.mipmap.icon_hot_group;
                break;
        }

        return resId;
    }

}
