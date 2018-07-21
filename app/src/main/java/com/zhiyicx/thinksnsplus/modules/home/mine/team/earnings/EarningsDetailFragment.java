package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

public class EarningsDetailFragment extends TSListFragment<EarningsDetailContract.Presenter, TeamBean.TeamListBean> implements EarningsDetailContract.View{

    public static EarningsDetailFragment newInstance(Bundle bundle){
        EarningsDetailFragment fragment = new EarningsDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter commonAdapter = new CommonAdapter<TeamBean.TeamListBean>(getContext(), R.layout.item_earning, mListDatas) {

            @Override
            protected void convert(ViewHolder holder, TeamBean.TeamListBean teamListBean, int position) {
                holder.setText(R.id.tv_earning_username,teamListBean.getUserName());
                holder.setText(R.id.tv_begin_time,teamListBean.getTime());
                holder.setText(R.id.tv_end_time,teamListBean.getEndTime());
                holder.setText(R.id.tv_earnings_num, teamListBean.getEarnings());
            }
        };
        return commonAdapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.earnings_details);
    }

    @Override
    protected void initData() {
        super.initData();
        getEarningListData();
    }

    private void getEarningListData() {
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
        }
    }

    @Override
    public int getEarningId() {
        return 0;
    }
}
