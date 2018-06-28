package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.report;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/6/28
 * 描  述：
 * 版  权: 九曲互动
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ReportGroupFragment extends TSFragment<ReportGroupContract.Presenter> implements ReportGroupContract.View {
    @BindView(R.id.et_report_content)
    UserInfoInroduceInputView mReportContent;
    @BindView(R.id.ed_input_phone)
    DeleteEditText mInputPhone;
    Unbinder unbinder;

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_info_report);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_report_group;
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

    @OnClick(R.id.submit_bt)
    public void onClick() {
    }
}
