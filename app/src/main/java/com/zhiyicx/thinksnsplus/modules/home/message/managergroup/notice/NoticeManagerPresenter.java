package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 14:37
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;


@FragmentScoped
public class NoticeManagerPresenter extends AppBasePresenter<NoticeManagerContract.View>
        implements NoticeManagerContract.Presenter {

    @Inject
    BaseMessageRepository mBaseMessageRepository;

    private String mGroupId;

    public void setmGroupId(String mGroupId) {
        this.mGroupId = mGroupId;
    }

    @Inject
    public NoticeManagerPresenter(NoticeManagerContract.View rootView) {
        super(rootView);
    }


    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscription = mBaseMessageRepository.noticeList(mRootView.getGroupId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<NoticeItemBean>>() {
                    @Override
                    protected void onSuccess(List<NoticeItemBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<NoticeItemBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }


}
