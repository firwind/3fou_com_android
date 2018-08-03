package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
public class EarningsDetailPresenter extends AppBasePresenter<EarningsDetailContract.View> implements EarningsDetailContract.Presenter {
    @Inject
    CurrencyRepository currencyRepository;

    @Inject
    public EarningsDetailPresenter(EarningsDetailContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        getEarningList(maxId, isLoadMore);
    }

    private void getEarningList(Long maxId, boolean isLoadMore) {
        Subscription subscribe = currencyRepository.getEarningList(mRootView.getEarningId(),mRootView.getCurrency())
                .subscribe(new BaseSubscribeForV2<List<TeamBean.TeamListBean>>() {
                    @Override
                    protected void onSuccess(List<TeamBean.TeamListBean> data) {

                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<TeamBean.TeamListBean> data, boolean isLoadMore) {
        return false;
    }
}
