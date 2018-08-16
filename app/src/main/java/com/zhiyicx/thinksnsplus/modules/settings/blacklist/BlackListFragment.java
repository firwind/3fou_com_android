package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

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

/**
 * @Describe 黑名单列表
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
public class BlackListFragment extends TSListFragment<BlackListContract.Presenter, UserInfoBean> implements BlackListContract.View {

    @Inject
    BlackListPresenter mBlackListPresenter;


    public static BlackListFragment initFragment(Bundle bundle) {
        BlackListFragment followFansListFragment = new BlackListFragment();
        followFansListFragment.setArguments(bundle);
        return followFansListFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerBlackListPresenterComponent
                .builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .blackListPresenterModule(new BlackListPresenterModule(BlackListFragment.this))
                .build()
                .inject(this);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.ts_blacklist);
    }

    @Override
    protected CommonAdapter<UserInfoBean> getAdapter() {
        return new BlackListAdapter(getContext(), R.layout.item_black_list, mListDatas, mPresenter);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) data.size();
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nobody;
    }

    @Override
    public void removeSuccess(int position) {
        mListDatas.remove(position);
        refreshData();
        if(mListDatas.isEmpty()){
            startRefrsh();
        }
    }
}
