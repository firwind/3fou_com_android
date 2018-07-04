package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 11:07
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;

import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerFragment;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_NOTICE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_USER_INFO;
import static com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin.SettingAdminActivity.GROUP_INFO_BEAN;

public class SettingAdminFragment extends TSListFragment<SettingAdminContract.Presenter, GroupHankBean> implements SettingAdminContract.View,
        SettingAdminItemAdapter.DeleteRoleListener {
    private ChatGroupBean mChatGroupBean;
    private List<UserInfoBean> mUserInfoBeans;
    private SettingAdminAdapter adminAdapter;
    /**
     * 删除确认弹框
     */
    private ActionPopupWindow mCheckSurePop;
    public static SettingAdminFragment newInstance(Bundle bundle) {
        SettingAdminFragment settingAdminFragment = new SettingAdminFragment();
        settingAdminFragment.setArguments(bundle);
        return settingAdminFragment;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        adminAdapter = new SettingAdminAdapter(getContext(), mListDatas, mChatGroupBean);
        adminAdapter.setListener(this);

        return adminAdapter;

    }

    @Override
    public void onNetResponseSuccess(@NotNull List<GroupHankBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.administrator);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatGroupBean = getArguments().getParcelable(GROUP_INFO_BEAN);
    }

    @Override
    protected void initData() {
        super.initData();
        getAdminListData();
    }

    /**
     * 获取管理员以及讲师或者主持人列表
     */
    private void getAdminListData() {
        if (mPresenter != null) {
            if (mListDatas.isEmpty()) {
                mRefreshlayout.autoRefresh(0);
            } else {
                mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
            }
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist;
    }

    @Override
    public ChatGroupBean getGroupInfo() {
        return mChatGroupBean;
    }

    @Override
    public void getUserInfoBeans(List<UserInfoBean> userInfoBeans) {
        mUserInfoBeans = userInfoBeans;
    }

    private int mParentPostion,mSonPostion;
    private UserInfoBean userInfoBean;
    @Override
    public void onDeleteClick(UserInfoBean userInfoBean,String type,int parentPostion,int sonPostion) {
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
                    mPresenter.deleteRole(userInfoBean,type);
                    this.userInfoBean = userInfoBean;
                    mParentPostion = parentPostion;
                    mSonPostion = sonPostion;
                    mCheckSurePop.hide();
                })
                .bottomClickListener(() -> mCheckSurePop.hide())
                .build();
        mCheckSurePop.show();

    }

    @Override
    protected void setLeftClick() {
        EventBus.getDefault().post(mUserInfoBeans, EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER);
        super.setLeftClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EventBus.getDefault().post(mUserInfoBeans, EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS){
            EventBus.getDefault().post(mListDatas, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE);
            getAdminListData();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
    @Subscriber(tag = EVENT_IM_GROUP_UPDATE_GROUP_USER_INFO)
    public void onPublishGroupSuccess(boolean isSuccess) {
//        requestCacheData(0L,false);
        getAdminListData();
    }

    @Subscriber(tag = EVENT_IM_GROUP_UPDATE_GROUP_MUTE)
    public void onPublishGroupSuccess(List<UserInfoBean> userInfoBean) {
//        requestCacheData(0L,false);
        mUserInfoBeans = userInfoBean;
    }
}
