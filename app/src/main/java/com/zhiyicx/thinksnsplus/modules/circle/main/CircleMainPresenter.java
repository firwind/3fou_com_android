package com.zhiyicx.thinksnsplus.modules.circle.main;

import android.os.Bundle;

import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AllAdverListBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleJoinedBean;
import com.zhiyicx.thinksnsplus.data.beans.CircleMembers;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserCertificationInfo;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.VerifiedBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.CircleInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserCertificationInfoGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.circle.main.adapter.BaseCircleItem;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;

/**
 * @author Jliuer
 * @Date 2017/11/14/11:35
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CircleMainPresenter extends AppBasePresenter<CircleMainContract.View>
        implements CircleMainContract.Presenter {

    @Inject
    CircleInfoGreenDaoImpl mCircleInfoGreenDao;
    @Inject
    UserCertificationInfoGreenDaoImpl mUserCertificationInfoDao;
    @Inject
    UserInfoRepository mCertificationDetailRepository;
    @Inject
    AllAdvertListBeanGreenDaoImpl mAdvertListBeanGreenDao;
    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    public CircleMainPresenter(CircleMainContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = Observable.zip(mBaseCircleRepository.getCircleCount(),
                isTourist() ? Observable.just(new ArrayList<CircleInfo>()) : mBaseCircleRepository.getMyJoinedCircle(CircleMainFragment.DATALIMIT, 0, CircleClient.MineCircleType.JOIN.value),
                mBaseCircleRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0, CircleClient.MineCircleType.RANDOM.value),
                (integerBaseJsonV2, myJoinedCircle, recommendCircle) -> {

                    mRootView.updateCircleCount(integerBaseJsonV2.getData());

                    // 游客模式没有 我加入的
                    if (!isTourist()) {
                        CircleInfo moreJoined = new CircleInfo();
                        moreJoined.setName(mContext.getString(R.string.joined_group));
                        moreJoined.setSummary(mContext.getString(R.string.more_group));
                        moreJoined.setId(BaseCircleItem.MYJOINEDCIRCLE);
                        myJoinedCircle.add(0, moreJoined);
                    }

                    CircleInfo changeCircle = new CircleInfo();
                    changeCircle.setName(mContext.getString(R.string.recommend_group));
                    changeCircle.setSummary(mContext.getString(R.string.exchange_group));
                    changeCircle.setId(BaseCircleItem.RECOMMENDCIRCLE);


                    myJoinedCircle.add(changeCircle);

                    mRootView.setJoinedCircles(new ArrayList<>(myJoinedCircle));
                    mRootView.setRecommendCircles(new ArrayList<>(recommendCircle));
                    myJoinedCircle.addAll(recommendCircle);
                    return myJoinedCircle;
                })
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.loadAllError();
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.loadAllError();
                    }
                });

        addSubscrebe(subscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public void getRecommendCircle() {
        Subscription subscription = mBaseCircleRepository.getRecommendCircle(CircleMainFragment.DATALIMIT, 0, CircleClient.MineCircleType.RANDOM.value)
                .subscribe(new BaseSubscribeForV2<List<CircleInfo>>() {
                    @Override
                    protected void onSuccess(List<CircleInfo> data) {
                        mRootView.setRecommendCircles(new ArrayList<>(data));
                        List<CircleInfo> subs = new ArrayList<>(mRootView.getJoinedCircles());
                        subs.addAll(data);
                        mRootView.getListDatas().clear();
                        mRootView.getListDatas().addAll(subs);
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<CircleInfo> data, boolean isLoadMore) {
        mCircleInfoGreenDao.saveMultiData(data);
        return false;
    }

    @Override
    public void dealCircleJoinOrExit(int position, CircleInfo circleInfo) {

        if (handleTouristControl()) {
            return;
        }
        if (circleInfo.getAudit() != 1) {
            mRootView.showSnackErrorMessage(mContext.getString(R.string.reviewing_circle));
            return;
        }
        boolean isJoined = circleInfo.getJoined() != null;

        boolean isPaid = CircleInfo.CirclePayMode.PAID.value.equals(circleInfo.getMode());
        boolean isPrivate = CircleInfo.CirclePayMode.PRIVATE.value.equals(circleInfo.getMode());

        Observable<BaseJsonV2<Object>> observable;
        if (isPaid) {
            observable = handleIntegrationBlance(circleInfo.getMoney())
                    .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                            .string.pay_alert_ing)))
                    .flatMap(o -> mBaseCircleRepository.dealCircleJoinOrExit(circleInfo));
        } else {
            observable = mBaseCircleRepository.dealCircleJoinOrExit(circleInfo)
                    .doOnSubscribe(() -> {
                                mRootView.dismissSnackBar();
                                mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing));
                            }
                    );

        }

        Subscription subscribe = observable
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));

                        if (isJoined) {
                            circleInfo.setJoined(null);
                            circleInfo.setUsers_count(circleInfo.getUsers_count() - 1);
                        } else {
                            // 如果是 封闭的或者 收费的 ，就不及时更新
                            if (isPrivate || isPaid) {
                                return;
                            }
                            CircleJoinedBean circleJoinedBean = new CircleJoinedBean(CircleMembers.MEMBER);
                            circleJoinedBean.setUser_id((int) AppApplication.getMyUserIdWithdefault());
                            circleJoinedBean.setUser(AppApplication.getmCurrentLoginAuth().getUser());
                            circleJoinedBean.setGroup_id(circleInfo.getId().intValue());
                            circleJoinedBean.setAudit(1);
                            circleInfo.setJoined(circleJoinedBean);
                            circleInfo.setUsers_count(circleInfo.getUsers_count() + 1);

                            if (mRootView.getJoinedCircles().size() < 7) {
                                mRootView.getListDatas().add(1, circleInfo);
                                mRootView.getJoinedCircles().add(1, circleInfo);
                            }
                        }
                        mCircleInfoGreenDao.updateSingleData(circleInfo);
                        mRootView.refreshData();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public List<RealAdvertListBean> getCircleTopAdvert() {
        AllAdverListBean adverBean = mAdvertListBeanGreenDao.getCircleTopAdvert();
        if (adverBean == null) {
            return null;
        } else {
            return adverBean.getMRealAdvertListBeen();
        }
    }

    /**
     * 检查认证状态信息
     */
    @Override
    public void checkCertification() {
        UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
        UserCertificationInfo userCertificationInfo = mUserCertificationInfoDao.getInfoByUserId();
        if (getSystemConfigBean() != null && getSystemConfigBean().getCircleGroup() != null && userCertificationInfo != null &&
                userCertificationInfo.getStatus() == UserCertificationInfo.CertifyStatusEnum.PASS.value) {
            mRootView.setUserCertificationInfo(userCertificationInfo);
            return;
        }

        Subscription subscribe = Observable.zip(mSystemRepository.getBootstrappersInfo(), mCertificationDetailRepository.getCertificationInfo(),
                (systemConfigBean, userCertificationInfo1) -> {
                    Map data = new HashMap();
                    data.put("systemConfigBean", systemConfigBean);
                    data.put("userCertificationInfo", userCertificationInfo1);
                    return data;
                })
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("信息加载中..."))
                .doAfterTerminate(() -> mRootView.dismissSnackBar())
                .subscribe(new BaseSubscribeForV2<Map>() {
                    @Override
                    protected void onSuccess(Map zipData) {
                        UserCertificationInfo data = (UserCertificationInfo) zipData.get("userCertificationInfo");
                        SystemConfigBean systemConfigBean = (SystemConfigBean) zipData.get("systemConfigBean");
                        mSystemRepository.saveComponentStatus(systemConfigBean, mContext);
                        mUserCertificationInfoDao.saveSingleData(data);
                        if (userInfoBean != null) {
                            if (userInfoBean.getVerified() != null) {
                                userInfoBean.getVerified().setStatus((int) data.getStatus());
                            } else {
                                VerifiedBean verifiedBean = new VerifiedBean();
                                verifiedBean.setStatus((int) data.getStatus());
                                userInfoBean.setVerified(verifiedBean);
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS, data);
                        EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_UPDATE_CERTIFICATION_SUCCESS);
                        mUserInfoBeanGreenDao.updateSingleData(userInfoBean);
                        mRootView.setUserCertificationInfo(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackSuccessMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.err_net_not_work));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_UPDATE_CIRCLE)
    public void updateCircle(CircleInfo circleInfo) {
        int index = -1;
        for (CircleInfo circle : mRootView.getListDatas()) {
            if (circle.equals(circleInfo)) {
                index = mRootView.getListDatas().indexOf(circle);
            }
        }
        if (index != -1) {
            mRootView.getListDatas().set(index, circleInfo);
        }
        mRootView.refreshData(index);
        LogUtils.d(EventBusTagConfig.EVENT_UPDATE_CIRCLE);
    }
}
