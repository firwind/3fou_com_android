package com.zhiyicx.thinksnsplus.modules.home.mine.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/23
 * 描  述：
 * 版  权: 九曲互动
 */

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.mvp.BasePresenter;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.source.repository.CurrencyRepository;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.team.MyTeamListContract;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;

@FragmentScoped
public class MyTeamPresenter extends AppBasePresenter<MyTeamContract.View> implements MyTeamContract.Presenter {
    MyTeamContract.View mView;
    @Inject
    CurrencyRepository currencyRepository;
    @Inject
    public MyTeamPresenter(MyTeamContract.View rootView) {
        super(rootView);
        this.mView = rootView;
    }

    @Override
    public void requestCurrencyType() {

        Subscription subscribe = currencyRepository.getCurrencyType(mContext)
                .subscribe(new BaseSubscribeForV2<List<CurrencyTypeBean>>() {
                    @Override
                    protected void onSuccess(List<CurrencyTypeBean> data) {
//                        mRootView.getCurrencyType(data);
                        mRootView.getCurrencyType(data);
                    }
                });

        addSubscrebe(subscribe);
    }
}
