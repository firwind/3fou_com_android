package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;
import com.zhiyicx.thinksnsplus.modules.findsomeone.contianer.FindSomeOneContainerActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.search.SearchFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerPresenter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

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

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return new MyFriendsListAdapter(getContext(), mListDatas, mPresenter);
    }

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
        return getString(R.string.tv_add_friends);
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

    /*@Override
    protected Long getMaxId(@NotNull List<UserInfoBean> data) {
        return (long) mListDatas.size();
    }*/

    @OnClick({R.id.tv_toolbar_left, R.id.tv_toolbar_right, R.id.tv_toolbar_center})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_toolbar_left:
                // 退出当前页面
                getActivity().finish();
                break;
            case R.id.tv_toolbar_right:
                // 进入找人页面
                Intent itFollow = new Intent(getActivity(), FindSomeOneContainerActivity.class);
                Bundle bundleFollow = new Bundle();
                itFollow.putExtras(bundleFollow);
                startActivity(itFollow);
                break;
            case R.id.tv_toolbar_center:
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

    /*@Subscriber(tag = EventBusTagConfig.EVENT_GROUP_UPLOAD_SET_STICK)
    public void updateStick(int stick) {
        mPresenter.requestCacheData(0L, false);
    }*/
}
