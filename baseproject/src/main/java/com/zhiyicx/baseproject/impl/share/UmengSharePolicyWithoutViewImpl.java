package com.zhiyicx.baseproject.impl.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.UmengConfig;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.log.LogUtils;

import static com.umeng.socialize.bean.SHARE_MEDIA.*;


/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 12:40
 *     desc :
 *     version : 1.0
 * <pre>
 */
public class UmengSharePolicyWithoutViewImpl implements SharePolicy, OnShareCallbackListener {

    /**
     * 友盟初始化
     */
    static {
        PlatformConfig.setQQZone(UmengConfig.QQ_APPID, UmengConfig.QQ_SECRETKEY);
        PlatformConfig.setWeixin(UmengConfig.WEIXIN_APPID, UmengConfig.WEIXIN_SECRETKEY);
        PlatformConfig.setSinaWeibo(UmengConfig.SINA_APPID, UmengConfig.SINA_SECRETKEY, UmengConfig.SINA_RESULT_RUL);
    }

    private Context mContext;
    private ShareContent mShareContent;

    public void setOnShareCallbackListener(OnShareCallbackListener onShareCallbackListener) {
        mOnShareCallbackListener = onShareCallbackListener;
    }

    OnShareCallbackListener mOnShareCallbackListener;

    public UmengSharePolicyWithoutViewImpl(Context mContext) {
        mOnShareCallbackListener = this;
        this.mContext = mContext;
        init(mContext);
    }

    public UmengSharePolicyWithoutViewImpl(ShareContent mShareContent, Context mContext) {
        this.mContext = mContext;
        this.mShareContent = mShareContent;
        init(mContext);
    }

    private void init(Context mContext) {
        UMShareAPI.get(mContext);
        Config.DEBUG = true;
    }

    public ShareContent getShareContent() {
        return mShareContent;
    }

    /**
     * 如果使用的是 qq 或者新浪精简版 jar，需要在您使用分享或授权的 Activity（ fragment 不行）中添加如下回调代码
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data, Context context) {
        UMShareAPI.get(context).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 在使用分享或者授权的Activity中，重写onDestory()方法：
     *
     * @param activity
     */
    public static void onDestroy(Activity activity) {
        UMShareAPI.get(activity).release();
    }

    /**
     * 设置分享内容
     *
     * @param shareContent
     */
    @Override
    public void setShareContent(ShareContent shareContent) {
        mShareContent = shareContent;
    }

    /**
     * 微信朋友分享
     */
    @Override
    public void shareMoment(Activity activity, final OnShareCallbackListener l) {
        if (checkShareContent()) {
            return;
        }
        shareActionConfig(activity, l, WEIXIN_CIRCLE);

    }


    /**
     * QQ分享
     */
    @Override
    public void shareQQ(Activity activity, OnShareCallbackListener l) {
        if (checkShareContent()) {
            return;
        }
        shareActionConfig(activity, l, QQ);
    }

    /**
     * QQ空间分享
     */
    @Override
    public void shareZone(Activity activity, OnShareCallbackListener l) {
        if (checkShareContent()) {
            return;
        }
        shareActionConfig(activity, l, QZONE);

    }

    @Override
    public void showShare(Activity activity) {

    }

    /**
     * 开始分享
     * @param share_media
     */
    public void startShare(SHARE_MEDIA share_media){

        if(!UMShareAPI.get(mContext).isInstall((Activity) mContext, share_media)){
            installThirdAppTip();
            return;
        }else {
            switch (share_media){
                case QQ:
                    shareQQ((Activity) mContext, mOnShareCallbackListener);
                    break;
                case QZONE:
                    shareZone((Activity) mContext, mOnShareCallbackListener);
                    break;
                case WEIXIN:
                    shareWechat((Activity) mContext, mOnShareCallbackListener);
                    break;
                case WEIXIN_CIRCLE:
                    shareMoment((Activity) mContext, mOnShareCallbackListener);
                    break;
                case SINA:
                    shareWeibo((Activity) mContext, mOnShareCallbackListener);
                    break;
            }
        }

    }

    /**
     * 微博分享
     */
    @Override
    public void shareWeibo(Activity activity, OnShareCallbackListener l) {
        if (checkShareContent()) {
            return;
        }
        shareActionConfig(activity, l, SINA);
    }

    /**
     * 微信分享
     */
    @Override
    public void shareWechat(Activity activity, OnShareCallbackListener l) {
        if (checkShareContent()) {
            return;
        }
        shareActionConfig(activity, l, WEIXIN);
    }

    /**
     * 检测分享内容是否为空
     *
     * @return
     */
    private boolean checkShareContent() {
        if (mShareContent == null) {
            throw new NullPointerException(mContext.getResources().getString(R.string.err_share_no_content));
        }
        return false;
    }

    /**
     * 配置分享内容
     *
     * @param activity
     * @param l
     * @param share_media
     */
    private void shareActionConfig(Activity activity, final OnShareCallbackListener l, SHARE_MEDIA share_media) {
        ShareAction shareAction = new ShareAction(activity);
        shareAction.setPlatform(share_media);
        UMImage image = null;
        if (!TextUtils.isEmpty(mShareContent.getContent())) {
            shareAction.withText(mShareContent.getContent());
        }
        if (!TextUtils.isEmpty(mShareContent.getImage())) {
            image = new UMImage(activity, mShareContent.getImage());
        }
        if (0 != mShareContent.getResImage()) {
            image = new UMImage(activity, mShareContent.getResImage());
        }
        if (null != mShareContent.getBitmap()) {
            image = new UMImage(activity, mShareContent.getBitmap());
        }
        if (null != mShareContent.getFile()) {
            image = new UMImage(activity, mShareContent.getFile());
        }
        if (!TextUtils.isEmpty(mShareContent.getUrl())) {
            UMWeb web = new UMWeb(mShareContent.getUrl());
//            UMWeb web = new UMWeb(APP_PATH_SHARE_DEFAULT); // 由于后台还未开发完毕，暂时使用
            if (!TextUtils.isEmpty(mShareContent.getTitle())) {
                web.setTitle(mShareContent.getTitle());//标题
            }
            if (!TextUtils.isEmpty(mShareContent.getContent())) {
                web.setDescription(mShareContent.getContent());
            }
            if (image != null) {
                web.setThumb(image);
            }
            shareAction.withMedia(web);
        } else {
            shareAction.withMedia(image);
        }
        shareAction.setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                if (l != null) {
                    Share share = changeShare(share_media);
                    l.onStart(share);
                }
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                if (l != null) {
                    Share share = changeShare(share_media);
                    l.onSuccess(share);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                if (l != null) {
                    Share share = changeShare(share_media);
                    l.onError(share, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                if (l != null) {
                    Share share = changeShare(share_media);
//                    l.onCancel(share);
                }
            }
        });
        shareAction.share();
    }

    /**
     * 转换分享平台标识，方便修改三方时回调标识错误
     *
     * @param share_media
     * @return
     */
    private Share changeShare(SHARE_MEDIA share_media) {
        Share share = null;
        switch (share_media) {
            case QQ:
                share = Share.QQ;
                break;
            case QZONE:
                share = Share.QZONE;
                break;
            case WEIXIN:
                share = Share.WEIXIN;
                break;
            case WEIXIN_CIRCLE:
                share = Share.WEIXIN_CIRCLE;
                break;
            case SINA:
                share = Share.SINA;
                break;
            default:
                break;

        }
        return share;
    }

    @Override
    public void onStart(Share share) {
        LogUtils.i(" share start");
    }

    @Override
    public void onSuccess(Share share) {
        LogUtils.i(" share success");

    }

    @Override
    public void onError(Share share, Throwable throwable) {
        LogUtils.i(" share onError");
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Share share) {
        LogUtils.i(" share cancle");
    }


    private void installThirdAppTip() {
        try {
            ((TSFragment) ((TSActivity) mContext).getContanierFragment()).showSnackErrorMessage
                    (mContext.getString(R
                            .string.please_install_app));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
