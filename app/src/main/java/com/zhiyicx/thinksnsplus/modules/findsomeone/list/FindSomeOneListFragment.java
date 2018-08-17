package com.zhiyicx.thinksnsplus.modules.findsomeone.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhy.adapter.recyclerview.CommonAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Describe 找人列表页
 * @Author zl
 * @Date 2017/8/10
 * @Contact master.jungle68@gmail.com
 */
public class FindSomeOneListFragment extends TSListFragment<FindSomeOneListContract.Presenter, UserInfoBean> implements FindSomeOneListContract.View {
    /**
     * 找人的分类
     */
    public static final int TYPE_HOT = 0;
    public static final int TYPE_NEW = 1;
    public static final int TYPE_RECOMMENT = 2;
    public static final int TYPE_NEARBY = 3;

    /**
     * 获取页面类型的key
     */
    public static final String PAGE_TYPE = "page_type";

    @Inject
    FindSomeOneListPresenter mFindSomeOneListPresenter;
    /**
     * 页面类型，由上一个页面决定
     */
    private int pageType;

    /**
     * 后台推荐用户数量
     */
    private int mRecommentUserSize = 0;

    private FindSomeOneListAdapter mFindSomeOneListAdapter;

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        mFindSomeOneListAdapter=new FindSomeOneListAdapter(getContext(), R.layout.item_follow_fans_list/*item_find_some_list*/, mListDatas, mPresenter);
        return mFindSomeOneListAdapter;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageType = getArguments().getInt(PAGE_TYPE, TYPE_HOT);
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        Observable.create(subscriber -> {
            DaggerFindSomeOneListPresenterComponent
                    .builder()
                    .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                    .findSomeOneListPresenterModule(new FindSomeOneListPresenterModule(FindSomeOneListFragment.this))
                    .build().inject(FindSomeOneListFragment.this);
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
            mFindSomeOneListAdapter.setPresenter(mPresenter);
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
    protected boolean sethasFixedSize() {
        return true;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        if (mPresenter != null) {
            mPresenter.requestNetData(maxId, isLoadMore, pageType);
        }
    }

    public static FindSomeOneListFragment initFragment(Bundle bundle) {
        FindSomeOneListFragment followFansListFragment = new FindSomeOneListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }

    @Override
    public void upDateFollowFansState(int index) {
        refreshData(index);
    }

    @Override
    public void upDateFollowFansState() {
        refreshData();
    }

    @Override
    public int getPageType() {
        return pageType;
    }

    /**
     * 此时需要的是之前数据的总和 {@see offset https://github.com/slimkit/thinksns-plus/blob/master/docs/zh-CN/api2/find-users.md}
     *
     * @param data
     * @return
     */
    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) (mListDatas.size() - mRecommentUserSize);
    }

    @Override
    public void setRecommentUserSize(int recommentUserSize) {
        this.mRecommentUserSize = recommentUserSize;
    }

}
