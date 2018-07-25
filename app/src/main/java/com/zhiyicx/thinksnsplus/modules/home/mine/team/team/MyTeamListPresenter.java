package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

@FragmentScoped
public class MyTeamListPresenter extends AppBasePresenter<MyTeamListContract.View>
        implements MyTeamListContract.Presenter {
    MyTeamListContract.View mView;
    @Inject
    CurrencyRepository currencyRepository;

    @Inject
    public MyTeamListPresenter(MyTeamListContract.View rootView) {
        super(rootView);
        this.mView = rootView;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = currencyRepository.getTeamList(mRootView.getCurrencyType(),mRootView.getLevel())
                .subscribe(new BaseSubscribeForV2<List<TeamBean.TeamListBean>>() {
                    @Override
                    protected void onSuccess(List<TeamBean.TeamListBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        Throwable throwable = new Throwable(message);
                        mRootView.onResponseError(throwable, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        LogUtils.e(throwable, throwable.getMessage());
                        mRootView.onResponseError(throwable, isLoadMore);
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
