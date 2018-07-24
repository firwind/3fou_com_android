package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.MyTeamActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings.EarningsDetailActivity;
import com.zhiyicx.thinksnsplus.widget.popwindow.TypeChoosePopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import javax.inject.Inject;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyTeamListFragment extends TSListFragment<MyTeamListContract.Presenter, TeamBean.TeamListBean>
        implements MyTeamListContract.View {
    public static String LEVEL_STATE = "level";
    @Inject
    MyTeamListPresenter myTeamListPresenter;
    Unbinder unbinder;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    private String mUnit;
    private CommonAdapter adapter;
    private TypeChoosePopupWindow mTypeChoosePopupWindow;// 类型选择框 付费、置顶

    public static MyTeamListFragment instance(int level) {
        MyTeamListFragment fragment = new MyTeamListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(LEVEL_STATE, level);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected void initView(View rootView) {
        DaggerMyTeamListComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .myTeamListPresenterModule(new MyTeamListPresenterModule(this))
                .build()
                .inject(this);
        super.initView(rootView);
    }

    @Override
    protected void initData() {
        super.initData();
        getTeamListData();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter commonAdapter = new CommonAdapter<TeamBean.TeamListBean>(getContext(), R.layout.item_team, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, TeamBean.TeamListBean teamBean, int position) {
                holder.setText(R.id.tv_team_username, teamBean.getUserName());
                holder.setText(R.id.tv_earnings_num, teamBean.getEarnings() + mUnit);
                holder.setText(R.id.tv_time, teamBean.getTime());
            }
        };
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                TeamBean.TeamListBean bean = (TeamBean.TeamListBean) commonAdapter.getDatas().get(position);
                EarningsDetailActivity.startEarningsDetailActivity(getContext(), bean.getId());
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        return commonAdapter;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
    }


    @Override
    public void getTotal(String total, String unit) {
        mUnit = unit;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取团队列表
     */
    private void getTeamListData() {
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_SELECT_CURRENCY)
    public void getCurrencyId(Long id){
        getTeamListData();
}
}
