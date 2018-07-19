package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 11:17
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.BaseStateConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class SettingAdminPresenter extends AppBasePresenter<SettingAdminContract.View> implements SettingAdminContract.Presenter {
    @Inject
    ChatInfoRepository mRepository;

    List<GroupHankBean> data = new ArrayList<>();

    @Inject
    public SettingAdminPresenter(SettingAdminContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
//        List<UserInfoBean> userInfoBeans = mRootView.getGroupInfo().getAffiliations();
        Subscription subscription = mRepository.getGroupHankInfo(mRootView.getGroupInfo().getId())
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> userInfoBeans) {
                        mRootView.getUserInfoBeans(userInfoBeans);
                        data.clear();
                        for (int i = 0; i < 4; i++) {
                            GroupHankBean groupHankBean = new GroupHankBean();
                            List<UserInfoBean> userInfoBeanss = null;
                            switch (i) {
                                case 0:
                                    groupHankBean.setmHankName("群主");
                                    groupHankBean.setOwner(1);
                                    userInfoBeanss = new ArrayList<>();
                                    groupHankBean.setUserInfoBeans(userInfoBeanss);
                                    break;
                                case 1:
                                    groupHankBean.setmHankName("管理员");
                                    groupHankBean.setmType(i);
                                    userInfoBeanss = new ArrayList<>();
                                    groupHankBean.setUserInfoBeans(userInfoBeanss);
                                    break;
                                case 2:
                                    groupHankBean.setmHankName("主持人");
                                    groupHankBean.setmType(i);
                                    userInfoBeanss = new ArrayList<>();
                                    groupHankBean.setUserInfoBeans(userInfoBeanss);
                                    break;
                                case 3:
                                    groupHankBean.setmHankName("讲师");
                                    userInfoBeanss = new ArrayList<>();
                                    groupHankBean.setmType(i);
                                    groupHankBean.setUserInfoBeans(userInfoBeanss);
                                    break;
                            }
                            data.add(groupHankBean);
                        }
                        for (UserInfoBean userInfoBean : userInfoBeans) {
                            switch (userInfoBean.getAdmin_type()) {
                                case BaseStateConfig.ADMIN_TYPE:
                                    List<UserInfoBean> userInfoBeans1 = data.get(BaseStateConfig.ADMIN_TYPE).getUserInfoBeans();
                                    userInfoBeans1.add(userInfoBean);
                                    data.get(BaseStateConfig.ADMIN_TYPE).setUserInfoBeans(userInfoBeans1);
                                    break;
                                case BaseStateConfig.LECTURER_TYPE:
                                    List<UserInfoBean> userInfoBeans2 = data.get(BaseStateConfig.LECTURER_TYPE).getUserInfoBeans();
                                    userInfoBeans2.add(userInfoBean);
                                    data.get(BaseStateConfig.LECTURER_TYPE).setUserInfoBeans(userInfoBeans2);
                                    break;
                                case BaseStateConfig.COMPERE_TYPE:
                                    List<UserInfoBean> userInfoBeans3 = data.get(BaseStateConfig.COMPERE_TYPE).getUserInfoBeans();
                                    userInfoBeans3.add(userInfoBean);
                                    data.get(BaseStateConfig.COMPERE_TYPE).setUserInfoBeans(userInfoBeans3);
                                    break;
                                case 0:

                                    break;
                            }
                        }
                        //将群主添加进去
                        List<UserInfoBean> userInfoBeans4 = data.get(0).getUserInfoBeans();
                        if (mRootView.getGroupInfo().getAffiliations().get(0).getIs_owner() == 1) {
                            userInfoBeans4.add(mRootView.getGroupInfo().getAffiliations().get(0));
                            data.get(0).setUserInfoBeans(userInfoBeans4);
                        }
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });

        addSubscrebe(subscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<GroupHankBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void deleteRole(UserInfoBean userInfoBean, String role_type) {
        Subscription subscription = mRepository.removeRole(mRootView.getGroupInfo().getId()
                , String.valueOf(userInfoBean.getUser_id()), role_type)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.clean_success));
                        mRootView.startRefrsh();
                        //EventBus.getDefault().post(mListDatas, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE);
                        EventBus.getDefault().post(true,EventBusTagConfig.EVENT_IM_GROUP_UPDATE_INFO);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.showSnackErrorMessage(e.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }
}
