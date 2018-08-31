package com.zhiyicx.thinksnsplus.modules.information.infomain.smallvideo;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.TextUtils;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ApiConfig;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.thridmanager.share.OnShareCallbackListener;
import com.zhiyicx.common.thridmanager.share.Share;
import com.zhiyicx.common.thridmanager.share.ShareContent;
import com.zhiyicx.common.thridmanager.share.SharePolicy;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseDynamicRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.TSShareUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

/**
 * author: huwenyong
 * date: 2018/8/27 10:02
 * description:
 * version:
 */
@FragmentScoped
public class SmallVideoListPresenter extends AppBasePresenter<InfoMainContract.SmallVideoListView> implements
        InfoMainContract.SmallVideoListPresenter, OnShareCallbackListener {

    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    BaseInfoRepository mBaseInfoRepository;

    @Inject
    BaseDynamicRepository mDynamicRepository;

    @Inject
    public SharePolicy mSharePolicy;

    @Inject
    public SmallVideoListPresenter(InfoMainContract.SmallVideoListView smallVideoListView
            , InfoListDataBeanGreenDaoImpl infoListDataBeanGreenDao
            , AllAdvertListBeanGreenDaoImpl allAdvertListBeanGreenDao
            , BaseInfoRepository baseInfoRepository) {
        super(smallVideoListView);
        mInfoListDataBeanGreenDao = infoListDataBeanGreenDao;
        mBaseInfoRepository = baseInfoRepository;
    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {

        mDynamicRepository.getSmallVideoList(maxId, (long) 0)
                .subscribe(new BaseSubscribeForV2<DynamicBeanV2>() {
                    @Override
                    protected void onSuccess(DynamicBeanV2 data) {
                        mRootView.onNetResponseSuccess(data.getFeeds(),isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        mRootView.showMessage(message);
                        mRootView.onResponseError(null, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                });

    }

    @Override
    public void requestCacheData(Long maxId, final boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null,isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<DynamicDetailBeanV2> data, boolean isLoadMore) {
        return false;
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void shareVideo(DynamicDetailBeanV2 dynamicBean) {
        ((UmengSharePolicyImpl) mSharePolicy).setOnShareCallbackListener(this);
        ShareContent shareContent = new ShareContent();
        shareContent.setTitle(dynamicBean.getFeed_content());
        shareContent.setContent("    ");
        shareContent.setImage(ImageUtils.getVideoUrl(dynamicBean.getVideo().getCover_id()));
        shareContent.setVideoUrl(TSShareUtils.convert2ShareUrl(String.format(ApiConfig.APP_PATH_SHARE_DYNAMIC, dynamicBean
                .getId()
                == null ? "" : dynamicBean.getId())));
        mSharePolicy.setShareContent(shareContent);
        mSharePolicy.showShare(((TSFragment) mRootView).getActivity());
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
