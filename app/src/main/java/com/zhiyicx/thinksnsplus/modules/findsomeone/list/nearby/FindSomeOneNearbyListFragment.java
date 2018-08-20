package com.zhiyicx.thinksnsplus.modules.findsomeone.list.nearby;

import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.NearbyBean;
import com.zhiyicx.thinksnsplus.modules.findsomeone.list.FindSomeOneListPresenter;
import com.zhy.adapter.recyclerview.CommonAdapter;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 附近的人列表页
 * @Author zl
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneNearbyListFragment extends TSListFragment<FindSomeOneNearbyListContract.Presenter, NearbyBean> implements FindSomeOneNearbyListContract.View {

    @Inject
    FindSomeOneNearbyListPresenter mFollowFansListPresenter;

    private FindSomeOneNearbyListAdapter mFindSomeOneNearbyListAdapter;

    public static FindSomeOneNearbyListFragment initFragment() {
        FindSomeOneNearbyListFragment followFansListFragment = new FindSomeOneNearbyListFragment();
        return followFansListFragment;
    }


    @Override
    protected CommonAdapter<NearbyBean> getAdapter() {
        mFindSomeOneNearbyListAdapter=new FindSomeOneNearbyListAdapter(getContext(), R.layout.item_follow_fans_list/*item_find_some_list*/, mListDatas, mPresenter);
        return mFindSomeOneNearbyListAdapter;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        Observable.create(subscriber -> {
            DaggerFindSomeOneNearbyListPresenterComponent
                    .builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .findSomeOneNearbyListPresenterModule(new FindSomeOneNearbyListPresenterModule(this))
                    .build().inject(this);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        initData();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Object o) {
                    }
                });
    }

    @Override
    protected void initData() {
        if (mPresenter != null) {
            mFindSomeOneNearbyListAdapter.setPresenter(mPresenter);
            super.initData();
        }
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    public void showLoading() {
        mRefreshlayout.autoRefresh();
    }

    @Override
    public void hideLoading() {
        mRefreshlayout.finishRefresh();
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        mPresenter.requestNetData(maxId, isLoadMore);
    }


    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }

    @Override
    public void upDateFollowFansState() {
        refreshData();
    }

}
