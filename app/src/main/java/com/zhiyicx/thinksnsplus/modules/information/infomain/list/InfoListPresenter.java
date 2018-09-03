package com.zhiyicx.thinksnsplus.modules.information.infomain.list;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoRecommendBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.TSShareUtils;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_INFO_DETAILS_FORMAT;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_PATH_VIDEO_DETAILS_FORMAT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_LIST_DELETE;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class InfoListPresenter extends AppBasePresenter<InfoMainContract.InfoListView> implements InfoMainContract.InfoListPresenter ,OnShareCallbackListener {

    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;

    BaseInfoRepository mBaseInfoRepository;

    public SharePolicy mSharePolicy;
    @Inject
    public InfoListPresenter(InfoMainContract.InfoListView rootInfoListView
            , InfoListDataBeanGreenDaoImpl infoListDataBeanGreenDao
            , AllAdvertListBeanGreenDaoImpl allAdvertListBeanGreenDao
            , BaseInfoRepository baseInfoRepository) {
        super(rootInfoListView);
        mInfoListDataBeanGreenDao = infoListDataBeanGreenDao;
        mAllAdvertListBeanGreenDao = allAdvertListBeanGreenDao;
        mBaseInfoRepository = baseInfoRepository;
    }

    @Override
    public List<RealAdvertListBean> getBannerAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getInfoBannerAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getInfoBannerAdvert().getMRealAdvertListBeen();
    }

    @Override
    public List<RealAdvertListBean> getListAdvert() {
        if (!com.zhiyicx.common.BuildConfig.USE_ADVERT || mAllAdvertListBeanGreenDao.getInfoListAdvert() == null) {
            return new ArrayList<>();
        }
        return mAllAdvertListBeanGreenDao.getInfoListAdvert().getMRealAdvertListBeen();
    }

    @Override
    public void handleLike(InfoListDataBean dataBean) {

        mBaseInfoRepository.handleLike(!dataBean.isHas_like(), String.valueOf(dataBean.getId()) );

        dataBean.setHas_like(!dataBean.isHas_like());
        dataBean.setDigg_count(dataBean.isHas_like()?dataBean.getDigg_count()+1:dataBean.getDigg_count()-1);
        mRootView.refreshData();
    }

    @Override
    public void shareVideo(InfoListDataBean infoListDataBean) {
        if (mSharePolicy == null) {
            if (mRootView instanceof Fragment) {
                mSharePolicy = new UmengSharePolicyImpl(((Fragment) mRootView).getActivity());
            } else {
                return;
            }
        }
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(infoListDataBean.getTitle());
        shareContent.setContent("    ");
        shareContent.setImage(ImageUtils.imagePathConvertV2(infoListDataBean.getImage().getId(),0, 0, ImageZipConfig.IMAGE_80_ZIP));
//        shareContent.setVideoUrl(ApiConfig.APP_SHARE_VIDEO+infoListDataBean.getId());
        shareContent.setVideoUrl(TSShareUtils.convert2ShareUrl(String.format(APP_PATH_VIDEO_DETAILS_FORMAT,
                infoListDataBean.getId(), mUserInfoBeanGreenDao.getUserInfoById(String.valueOf(AppApplication.getMyUserIdWithdefault())).getUser_code())));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
    }

    @Override
    public void shareVideo(InfoListDataBean infoListDataBean, SHARE_MEDIA type) {
        if (mSharePolicy == null) {
            if (mRootView instanceof Fragment) {
                mSharePolicy = new UmengSharePolicyImpl(((Fragment) mRootView).getActivity());
            } else {
                return;
            }
        }
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(infoListDataBean.getTitle());
        shareContent.setContent("  ");
        if (infoListDataBean.getImage() != null) {
            shareContent.setImage(ImageUtils.imagePathConvertV2(infoListDataBean.getImage().getId(),0, 0, ImageZipConfig.IMAGE_80_ZIP));
        } else {
            shareContent.setBitmap(ConvertUtils.drawBg4Bitmap(Color.WHITE, BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.icon)));
        }
        shareContent.setUrl(ApiConfig.APP_SHARE_VIDEO+infoListDataBean.getId());
        mSharePolicy.setShareContent(shareContent);
        switch (type) {
            case QQ:
                mSharePolicy.shareQQ(((TSFragment) mRootView).getActivity(), this);
                break;
            case QZONE:
                mSharePolicy.shareZone(((TSFragment) mRootView).getActivity(), this);
                break;
            case WEIXIN:
                mSharePolicy.shareWechat(((TSFragment) mRootView).getActivity(), this);
                break;
            case WEIXIN_CIRCLE:
                mSharePolicy.shareMoment(((TSFragment) mRootView).getActivity(), this);
                break;
            case SINA:
                mSharePolicy.shareWeibo(((TSFragment) mRootView).getActivity(), this);
                break;
            default:
        }
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        String typeString = mRootView.getInfoType();
        final long type = Long.parseLong(typeString);
        Subscription subscription = mBaseInfoRepository.getInfoListV2(mRootView.getInfoType().equals("-1") ? "" : mRootView.getInfoType()
                , "", maxId, mRootView.getPage(), mRootView.isRecommend())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<InfoListDataBean>>() {
                    @Override
                    protected void onSuccess(List<InfoListDataBean> data) {
                        List<BaseListBean> list = new ArrayList<>();
                        for (InfoListDataBean listDataBean : data) {
                            listDataBean.setInfo_type(type);
                        }
                        list.addAll(data);
                        mInfoListDataBeanGreenDao.saveMultiData(data);
                        mRootView.onNetResponseSuccess(list, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, final boolean isLoadMore) {
        String typeString = mRootView.getInfoType();
        final long type = Long.parseLong(typeString);
        Subscription subscription = Observable.just(mInfoListDataBeanGreenDao)
                .observeOn(Schedulers.io())
                .map(infoListDataBeanGreenDao -> infoListDataBeanGreenDao
                        .getInfoByType(type))
                .filter(infoListBean -> infoListBean != null)
                .map(data -> {
                    List<BaseListBean> localData = new ArrayList<>();

                    if (data != null) {
                        localData.addAll(data);
                    }
                    for (InfoListDataBean listDataBean : data) {
                        listDataBean.setInfo_type(type);
                    }
                    return localData;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> mRootView.onCacheResponseSuccess(result, isLoadMore), Throwable::printStackTrace);
        addSubscrebe(subscription);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<BaseListBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void getInfoList(String cateId, long maxId, long limit, final long page) {

    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_UPDATE_LIST_DELETE)
    public void updateDeleteInfo(InfoListDataBean infoListDataBean) {
        for (BaseListBean listBean : mRootView.getListDatas()) {
            if (listBean instanceof InfoListDataBean && ((InfoListDataBean) listBean).getId() == infoListDataBean.getId()) {
                mRootView.getListDatas().remove(listBean);
                mRootView.refreshData();
                break;
            }
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
