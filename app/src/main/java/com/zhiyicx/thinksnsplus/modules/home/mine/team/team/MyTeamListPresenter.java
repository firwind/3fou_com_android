package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
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
        Subscription subscribe = currencyRepository.getTeamList(mContext)
                .subscribe(new BaseSubscribeForV2<TeamBean>() {
                    @Override
                    protected void onSuccess(TeamBean data) {
                        mRootView.getTotal(data.getTotal(),data.getUnit());
                        mRootView.onNetResponseSuccess(data.getTeamList(), isLoadMore);
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