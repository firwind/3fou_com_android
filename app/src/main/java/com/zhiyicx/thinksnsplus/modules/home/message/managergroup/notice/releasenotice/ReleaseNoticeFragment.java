package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/20 11:54
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

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.util.TSEMessageUtils;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails.NoticeDetailsActivity.ITEM_NOTICE_BEAN;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.releasenotice.ReleaseNoticeActivity.GROUP_INFO_ID;

public class ReleaseNoticeFragment extends TSFragment<ReleaseNoticeContract.Presenter> implements ReleaseNoticeContract.View {

    @BindView(R.id.et_release_notice_title)
    DeleteEditText mReleaseNoticeTitle;
    @BindView(R.id.et_release_notice_content)
    DeleteEditText mReleaseNoticeContent;
    Unbinder unbinder;
    private String mGroupId;
    private String mNoticeTitle, mNoticeContent;
    private UserInfoBean mUserInfoBean;
    private NoticeItemBean noticeItemBean;

    public static ReleaseNoticeFragment newInstance(Bundle bundle) {
        ReleaseNoticeFragment fragment = new ReleaseNoticeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString(GROUP_INFO_ID, groupId);
//        bundle.putParcelable(ITEM_NOTICE_BEAN, noticeItemBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_btn);
    }

    @Override
    protected String setCenterTitle() {
        if (noticeItemBean == null)
            return getString(R.string.chat_edit_group_notice);
        else
            return getString(R.string.chat_release_group_announcement);
    }

    @Override
    protected void initView(View rootView) {
        mToolbarRight.setText(setRightTitle());
        mPresenter.getUserInfoFromDB();//获取用户信息
    }

    @Override
    protected void setRightClick() {
        mNoticeTitle = mReleaseNoticeTitle.getText().toString().trim();
        mNoticeContent = mReleaseNoticeContent.getText().toString().trim();

        if (TextUtils.isEmpty(mNoticeTitle)) {
            ToastUtils.showToast("请输入公告标题");
        } else if (mNoticeTitle.length() < 4) {
            ToastUtils.showToast("公告标题不能少于4个字");
        } else if (mNoticeTitle.length() > 40) {
            ToastUtils.showToast("公告标题不能多于40个字");
        } else if (TextUtils.isEmpty(mNoticeContent)) {
            ToastUtils.showToast("请输入公告内容");
        } else if (mNoticeContent.length() < 15) {
            ToastUtils.showToast("公告内容不能少于15个字");
        } else if (mNoticeContent.length() > 500) {
            ToastUtils.showToast("公告内容不能多于500个字");
        } else {
            if (noticeItemBean == null) {
                mPresenter.releaseNotice(mGroupId, mNoticeTitle, mNoticeContent, mUserInfoBean.getName(),0);
            }else {
                mPresenter.releaseNotice(mGroupId, mNoticeTitle, mNoticeContent, mUserInfoBean.getName(),1);
            }
        }
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            mGroupId = getArguments().getString(GROUP_INFO_ID);
            noticeItemBean = getArguments().getParcelable(ITEM_NOTICE_BEAN);
        }
        if (noticeItemBean != null) {
            mReleaseNoticeTitle.setText(noticeItemBean.getTitle());
            mReleaseNoticeContent.setText(noticeItemBean.getContent());
        }

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_release_notice;
    }

    @Override
    public void relaseSuccess() {
        TSEMessageUtils.sendNoticeGroupMessage(mUserInfoBean.getName(), String.valueOf(System.currentTimeMillis()), mNoticeContent, mNoticeTitle, mGroupId, true, TSEMConstants.TS_ATTR_RELEASE_NOTICE);
//        EventBus.getDefault().post(mNoticeContent, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_NOTICE);
        EventBus.getDefault().post(true,EventBusTagConfig.EVENT_IM_GROUP_UPDATE_INFO);
        getActivity().finish();
    }

    @Override
    public void setUserInfo(UserInfoBean userInfoBean) {
        this.mUserInfoBean = userInfoBean;
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
