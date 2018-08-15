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
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import static com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings.EarningsDetailActivity.CURRENCY;
import static com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings.EarningsDetailActivity.MEMBER_ID;

public class EarningsDetailFragment extends TSListFragment<EarningsDetailContract.Presenter, TeamBean.TeamListBean> implements EarningsDetailContract.View{

    private int mId;
    private String mCurrency;

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
                holder.setText(R.id.tv_earning_username,teamListBean.getName());
                holder.setText(R.id.tv_begin_time, TimeUtils.millis2String(teamListBean.getCreated_at()));
//                holder.setText(R.id.tv_end_time,teamListBean.getEndTime());
                holder.setText(R.id.tv_earnings_num, teamListBean.getBalance());
            }
        };
        return commonAdapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_earnings_details;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.earnings_details);
    }

    @Override
    protected void initData() {
        super.initData();
        mId = getArguments().getInt(MEMBER_ID);
        mCurrency = getArguments().getString(CURRENCY);
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
        return mId;
    }

    @Override
    public String getCurrency() {
        return mCurrency;
    }
}
