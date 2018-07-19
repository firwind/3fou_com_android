package com.zhiyicx.thinksnsplus.modules.home.mine.team;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyTeamFragment extends TSListFragment<MyTeamContract.Presenter, TeamBean>
        implements MyTeamContract.View {
    @BindView(R.id.sp_team_spinner)
    Spinner mTeamSpinner;
    Unbinder unbinder;
    private MyTeamCurrencyAdapter adapter;

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
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    public void getCurrencyType(List<CurrencyTypeBean> bean) {
        adapter = new MyTeamCurrencyAdapter(getContext(),bean,R.layout.item_currency);
        adapter.setData(bean);
        mTeamSpinner.setAdapter(adapter);
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
}
