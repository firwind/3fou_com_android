package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.TSShareUtils;

import javax.inject.Inject;

import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_SHARE_USERINFO_QR;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyCodePresenter extends AppBasePresenter<MyCodeContract.View>
        implements MyCodeContract.Presenter, OnShareCallbackListener {

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public MyCodePresenter(MyCodeContract.View rootView) {
        super(rootView);
    }

    @Override
    public void createUserCodePic(Bitmap logo) {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            mRootView.getEmptyView().setErrorType(EmptyView.STATE_NETWORK_LOADING);
            // 生成用户二维码，二维码的内容为uid=xx的格式
            String qrCodeContent = TSShareUtils.convert2ShareUrl(String.format(APP_PATH_SHARE_USERINFO_QR, userInfoBean.getUser_id()));
            Subscription subscribe = Observable.just(qrCodeContent)
                    .subscribeOn(Schedulers.newThread())
                    .map(s -> QRCodeEncoder.syncEncodeQRCode(s, BGAQRCodeUtil.dp2px(mContext, 150), Color.parseColor("#000000"),
                            getRoundedCornerBitmap(logo, 10)))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(bitmap -> {
                        mRootView.setMyCode(bitmap);
                        mRootView.getEmptyView().setErrorType(EmptyView.STATE_HIDE_LAYOUT);
                    });
            addSubscrebe(subscribe);

        }
    }

    private Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
//
//        final Rect rectbg = new Rect(0, 0, bitmap.getWidth()+100, bitmap.getHeight()+100);
//        final RectF rectFbg = new RectF(rectbg);
//        paint.setAntiAlias(true);
//        paint.setColor(Color.WHITE);
//        canvas.drawRoundRect(rectFbg, roundPx, roundPx, paint);

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    @Override
    public void getUserInfo() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            mRootView.setUserInfo(userInfoBean);
        }
    }

    @Override
    public void shareMyQrCode(Bitmap bitmap) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        if (userInfoBean != null) {
            ShareContent shareContent = new ShareContent();
            shareContent.setTitle(userInfoBean.getName());
            shareContent.setContent(TextUtils.isEmpty(userInfoBean.getIntro()) ? mContext.getString(R.string.intro_default) : userInfoBean.getIntro
                    ());
            if (null != bitmap) {
                shareContent.setBitmap(bitmap);
            } else {
                shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
            }
            shareContent.setUrl(TSShareUtils.convert2ShareUrl(String.format(ApiConfig.APP_PATH_SHARE_USERINFO, userInfoBean.getUser_id()
                    == null ? "" : userInfoBean.getUser_id())));
            mSharePolicy.setShareContent(shareContent);
            mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
        }
    }

    @Override
    public void onStart(Share share) {

    }

    @Override
    public void onSuccess(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_sccuess));
    }

    @Override
    public void onError(Share share, Throwable throwable) {
        mRootView.showSnackErrorMessage(mContext.getString(R.string.share_fail));
    }

    @Override
    public void onCancel(Share share) {
        mRootView.showSnackSuccessMessage(mContext.getString(R.string.share_cancel));
    }
}
