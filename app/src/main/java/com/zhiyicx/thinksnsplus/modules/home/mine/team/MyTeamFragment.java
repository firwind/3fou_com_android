package com.zhiyicx.thinksnsplus.modules.home.mine.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hyphenate.util.DensityUtil;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings.EarningsDetailActivity;
import com.zhiyicx.thinksnsplus.widget.popwindow.TypeChoosePopupWindow;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyTeamFragment extends TSListFragment<MyTeamContract.Presenter, TeamBean.TeamListBean>
        implements MyTeamContract.View {
    @BindView(R.id.sp_team_spinner)
    TextView mTeamSpinner;
    Unbinder unbinder;
    @BindView(R.id.tv_my_total)
    TextView mMyTotal;
    @BindView(R.id.swipe_target)
    RecyclerView swipeTarget;
    private String mUnit;
    private CommonAdapter adapter;
    private TypeChoosePopupWindow mTypeChoosePopupWindow;// 类型选择框 付费、置顶

    public static MyTeamFragment instance(Bundle bundle) {
        MyTeamFragment fragment = new MyTeamFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_my_team;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.team);
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.requestCurrencyType();//获取币种
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
                TeamBean.TeamListBean  bean = (TeamBean.TeamListBean) commonAdapter.getDatas().get(position);
                EarningsDetailActivity.startEarningsDetailActivity(getContext(),bean.getId());
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
    public void getCurrencyType(List<CurrencyTypeBean> bean) {
        mTeamSpinner.setText(getString(R.string.select_currency_hint));
        adapter = new CommonAdapter<CurrencyTypeBean>(getContext(), R.layout.item_currency, bean) {
            @Override
            protected void convert(ViewHolder holder, CurrencyTypeBean bean, int position) {
                holder.setText(R.id.tv_currency_name, bean.getCurrencyName());
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                CurrencyTypeBean typeBean = (CurrencyTypeBean) adapter.getDatas().get(position);
                mTeamSpinner.setText(typeBean.getCurrencyName());
                mTypeChoosePopupWindow.dismiss();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        initPop();
    }

    public void initPop() {
        mTypeChoosePopupWindow = TypeChoosePopupWindow.Builder()
                .with(mActivity)
                .adapter(adapter)
                .asVertical()
                .width(mTeamSpinner.getLayoutParams().width+ DensityUtil.dip2px(mActivity,30))
                .alpha(1.0f)
                .parentView(mTeamSpinner)
                .build();
    }

    @Override
    public void getTotal(String total, String unit) {
        mUnit = unit;
        mMyTotal.setText("我的总资产" + total + unit);
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

    @OnClick(R.id.sp_team_spinner)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sp_team_spinner:
                if (mTypeChoosePopupWindow != null) {
                    mTypeChoosePopupWindow.show();
                }
                break;
        }
    }
}
