package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/21 13:37
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.ITEM_NOTICE_BEAN;

public class NoticeDetailsFragment extends TSFragment {

    @BindView(R.id.tv_notice_details_user_name)
    TextView mNoticeDetailsUserName;
    @BindView(R.id.tv_notice_details_time)
    TextView mNoticeDetailsTime;
    @BindView(R.id.tv_notice_details_content)
    TextView mNoticeDetailsContent;
    Unbinder unbinder;
    @BindView(R.id.tv_notice_details_title)
    TextView mNoticeDetailsTitle;
    private NoticeItemBean mNoticeBean;

    public static NoticeDetailsFragment newInstance(Bundle bundle) {
        NoticeDetailsFragment fragment = new NoticeDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.edit_circle_notice_details);
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mNoticeBean = getArguments().getParcelable(ITEM_NOTICE_BEAN);
            mNoticeDetailsTitle.setText(TextUtils.isEmpty(mNoticeBean.getTitle()) ? "" : mNoticeBean.getTitle());
            mNoticeDetailsUserName.setText(TextUtils.isEmpty(mNoticeBean.getAuthor()) ? "" : mNoticeBean.getAuthor());
            mNoticeDetailsContent.setText(TextUtils.isEmpty(mNoticeBean.getContent()) ? "" : mNoticeBean.getContent());
            mNoticeDetailsTime.setText(TimeUtils.getTimeFriendlyNormal(mNoticeBean.getCreated_at()));
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_notice_details;
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
