package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.search.SearchFriendsActivity;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;

import static com.zhiyicx.thinksnsplus.modules.home.mine.friends.MyFriendsListActivity.IS_SHOW_TOOR_BAR;

/**
 * @author Catherine
 * @describe 我的好友列表 有搜索和跳转聊天的功能
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */

public class MyFriendsListFragment extends TSListFragment<MyFriendsListContract.Presenter, UserInfoBean>
        implements MyFriendsListContract.View {
    private boolean isShowToolBar = false;


    /**
     * 仅用于构造
     */
    @Inject
    MyFriendsListPresenter mFriendsListPresenter;

    public static MyFriendsListFragment newInstance(Bundle bundle) {
        MyFriendsListFragment fragment = new MyFriendsListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static MyFriendsListFragment newInstance() {
        MyFriendsListFragment fragment = new MyFriendsListFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initView(View rootView) {
        DaggerMyFriendsListFComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myFriendsListPresenterModule(new MyFriendsListPresenterModule(this))
                .build()
                .inject(this);
        super.initView(rootView);

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_friends_list;
    }

    @Override
    protected boolean showToolbar() {
        if (getArguments()!=null) {
            isShowToolBar = getArguments().getBoolean(IS_SHOW_TOOR_BAR, false);
        }
        return isShowToolBar;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.tv_my_friends);
    }

    @Override
    protected boolean showToolBarDivider() {
        return isShowToolBar;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected boolean isLayzLoad() {
        return true;
    }


    @Override
    protected RecyclerView.Adapter getAdapter() {
        MyFriendsListAdapter mAdapter = new MyFriendsListAdapter(getContext(), mListDatas, mPresenter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                new AlertDialog.Builder(mActivity)
                        .setTitle("提示")
                        .setMessage("确认删除？")
                        .setPositiveButton("删除",
                                (dialog, which) -> mPresenter.deleteFriend(position,mListDatas.get(position)))
                        .create()
                        .show();
                return true;
            }
        });
        return mAdapter;
    }

    @Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }

    @OnClick({R.id.search_bar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_bar:
                // 进入搜索页面
                startActivity(new Intent(getActivity(), SearchFriendsActivity.class));
                break;
            default:
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void deleteFriendOk(int index, UserInfoBean userInfoBean) {
        mListDatas.remove(userInfoBean);
        mHeaderAndFooterWrapper.notifyItemRemoved(index);
        refreshData();
    }

    /*@Subscriber(tag = EventBusTagConfig.EVENT_GROUP_UPLOAD_SET_STICK)
    public void updateStick(int stick) {
        mPresenter.requestCacheData(0L, false);
    }*/
}
