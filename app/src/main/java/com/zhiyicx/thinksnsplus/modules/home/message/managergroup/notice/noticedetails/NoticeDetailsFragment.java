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
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice.ReleaseNoticeActivity;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.GROUP_ID;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.IS_GROUP;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.ITEM_NOTICE_BEAN;

public class NoticeDetailsFragment extends TSFragment<NoticeDetailsContract.Presenter> implements NoticeDetailsContract.View {

    @BindView(R.id.tv_notice_details_user_name)
    TextView mNoticeDetailsUserName;
    @BindView(R.id.tv_notice_details_time)
    TextView mNoticeDetailsTime;
    @BindView(R.id.tv_notice_details_content)
    TextView mNoticeDetailsContent;
    Unbinder unbinder;
    @BindView(R.id.tv_notice_details_title)
    TextView mNoticeDetailsTitle;
    @BindView(R.id.et_edit_group_notice)
    Button etEditGroupNotice;
    private NoticeItemBean mNoticeBean;
    private String mGroupId;
    private boolean isGroupOwner;
    /**
     * 删除确认弹框
     */
    private ActionPopupWindow mCheckSurePop;

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
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.info_delete);
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();
        mCheckSurePop = ActionPopupWindow.builder()
                .item1Str(getString(R.string.chat_delete_sure))
                .item2Str(getString(R.string.ts_delete))
                .item2Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .with(mActivity)
                .item2ClickListener(() -> {//删除公告
                    mPresenter.deleteNotice(mNoticeBean.getNotice_id());
                    mCheckSurePop.hide();
                })
                .bottomClickListener(() -> mCheckSurePop.hide())
                .build();
        mCheckSurePop.show();
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mNoticeBean = getArguments().getParcelable(ITEM_NOTICE_BEAN);
            isGroupOwner = getArguments().getBoolean(IS_GROUP);
            mGroupId = getArguments().getString(GROUP_ID);
            mNoticeDetailsTitle.setText(TextUtils.isEmpty(mNoticeBean.getTitle()) ? "" : mNoticeBean.getTitle());
            mNoticeDetailsUserName.setText(TextUtils.isEmpty(mNoticeBean.getAuthor()) ? "" : mNoticeBean.getAuthor());
            mNoticeDetailsContent.setText(TextUtils.isEmpty(mNoticeBean.getContent()) ? "" : mNoticeBean.getContent());
            mNoticeDetailsTime.setText(TimeUtils.getTimeFriendlyNormal(mNoticeBean.getCreated_at()*1000));
        }
        if (isGroupOwner) {
            mToolbarRight.setText(setRightTitle());
            etEditGroupNotice.setVisibility(View.VISIBLE);
        } else {
            mToolbarRight.setText("");
            etEditGroupNotice.setVisibility(View.GONE);
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

    /*@Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS) {
            EventBus.getDefault().post("", EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_NOTICE);
            getActivity().finish();
        }
    }*/

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.et_edit_group_notice)
    public void onClick() {
        startActivity(ReleaseNoticeActivity.newIntent(getContext(),mNoticeBean.getNotice_id(),mNoticeBean));
        getActivity().finish();
    }
}
