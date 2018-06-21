package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.config.NotificationConfig;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.FlushMessageBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.FollowFansBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
@FragmentScoped
public class BlackListPresenter extends AppBasePresenter<
        BlackListContract.View> implements BlackListContract.Presenter {

    @Inject
    FollowFansBeanGreenDaoImpl mFollowFansBeanGreenDao;
    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public BlackListPresenter(
            BlackListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription =
                mUserInfoRepository.getUserBlackList(maxId)
                        .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                            @Override
                            protected void onSuccess(List<UserInfoBean> data) {
                                mRootView.onNetResponseSuccess(data, isLoadMore);
                            }

                            @Override
                            protected void onFailure(String message, int code) {
                                Throwable throwable = new Throwable(message);
                                mRootView.onResponseError(throwable, isLoadMore);
                            }

                            @Override
                            protected void onException(Throwable throwable) {
                                LogUtils.e(throwable, throwable.getMessage());
                                mRootView.onResponseError(throwable, isLoadMore);
                            }
                        });
        addSubscrebe(subscription);
    }

    /**
     * @param maxId      当前获取到数据的最小时间
     * @param isLoadMore 加载状态，是否是加载更多
     */
    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    /**
     * 插入数据库
     *
     * @param data       要保存的数据
     * @param isLoadMore 加载状态，是否是加载更多
     * @return true, 插入成功
     */
    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        mUserInfoBeanGreenDao.insertOrReplace(data);
        return true;
    }

    /**
     * 移除黑名单
     */
    @Override
    public void removeBlackList(int position) {
        mUserInfoRepository.removeUserFromBlackList(mRootView.getListDatas().get(position).getUser_id())
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        try {
                            mRootView.removeSuccess(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        try {
                            mRootView.showSnackErrorMessage(message);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        try {
                            showErrorTip(throwable);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 用户被移除黑名单后回调
     *
     * @param userId
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_USER_REMOVE_FROM_BLACK_LIST)
    public void userRemovedFromBlackList(long userId) {

        Subscription subscribe = Observable.just(userId)
                .subscribeOn(Schedulers.io())
                .map(aLong -> {
                    int size = mRootView.getListDatas().size();
                    for (int i = 0; i < size; i++) {
                        if (mRootView.getListDatas().get(i).getUser_id() == userId) {
                            return i;
                        }
                    }
                    return -1;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Integer>() {
                    @Override
                    protected void onSuccess(Integer data) {
                        if (data == -1) {
                            return;
                        }
                        try {
                            mRootView.getListDatas().remove(data);
                            mRootView.refreshData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
        addSubscrebe(subscribe);
    }
}
