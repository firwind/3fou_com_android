package com.zhiyicx.thinksnsplus.modules.information.infomain.flash;

import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.imsdk.core.autobahn.WampMessage;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.source.local.AllAdvertListBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.InfoListDataBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseInfoRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.InfoMainContract;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_UPDATE_LIST_DELETE;

/**
 * @Author Jliuer
 * @Date 2017/03/14
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class FlashListPresenter extends AppBasePresenter<InfoMainContract.FlashListView> implements InfoMainContract.FlashListPresenter {

    InfoListDataBeanGreenDaoImpl mInfoListDataBeanGreenDao;

    AllAdvertListBeanGreenDaoImpl mAllAdvertListBeanGreenDao;

    BaseInfoRepository mBaseInfoRepository;

    @Inject
    public FlashListPresenter(InfoMainContract.FlashListView rootInfoListView
            , InfoListDataBeanGreenDaoImpl infoListDataBeanGreenDao
            , AllAdvertListBeanGreenDaoImpl allAdvertListBeanGreenDao
            , BaseInfoRepository baseInfoRepository) {
        super(rootInfoListView);
        mInfoListDataBeanGreenDao = infoListDataBeanGreenDao;
        mAllAdvertListBeanGreenDao = allAdvertListBeanGreenDao;
        mBaseInfoRepository = baseInfoRepository;
    }

    @Override
    public void commitBull(InfoListDataBean bean) {
        Subscription subscription  = (bean.getHas_lihao()?mBaseInfoRepository.deteleBull(String.valueOf(bean.getId())):mBaseInfoRepository.commitBull(String.valueOf(bean.getId())))
                .flatMap(s -> bean.getHas_likong() ? mBaseInfoRepository.deteleBearNews(String.valueOf(bean.getId())) :Observable.just(s))
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (bean.getHas_lihao()){
                            bean.setHas_lihao(false);
                            bean.setDigg_count(bean.getDigg_count()-1);
                        }else {
                            bean.setHas_lihao(true);
                            bean.setDigg_count(bean.getDigg_count()+1);
                            if (bean.getHas_likong()){
                                bean.setHas_likong(false);
                                bean.setUndigg_count(bean.getUndigg_count()-1);
                            }
                        }
                        mRootView.commitResult(bean);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void commitBearNews(InfoListDataBean bean) {
        Subscription subscription  = (bean.getHas_likong()?mBaseInfoRepository.deteleBearNews(String.valueOf(bean.getId())):mBaseInfoRepository.commitBearNews(String.valueOf(bean.getId())))
                .flatMap(s -> bean.getHas_lihao() ? mBaseInfoRepository.deteleBull(String.valueOf(bean.getId())) :Observable.just(s))
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        if (bean.getHas_likong()){
                            bean.setHas_likong(false);
                            bean.setUndigg_count(bean.getUndigg_count()-1);
                        }else {
                            bean.setHas_likong(true);
                            bean.setUndigg_count(bean.getUndigg_count()+1);
                            if (bean.getHas_lihao()){
                                bean.setHas_lihao(false);
                                bean.setDigg_count(bean.getDigg_count()-1);
                            }
                        }
                        mRootView.commitResult(bean);
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void requestNetData(Long maxId, final boolean isLoadMore) {
        String typeString = mRootView.getInfoType();
        final long type = Long.parseLong(typeString);
        Subscription subscription = mBaseInfoRepository.getFlashListV2(mRootView.getInfoType().equals("-1") ? "" : mRootView.getInfoType()
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
}
