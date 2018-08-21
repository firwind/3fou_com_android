package com.zhiyicx.thinksnsplus.modules.chat.select.organization;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/20 0020
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.em.manager.util.TSEMessageUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.OrganizationBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.SelectFriendsRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

public class SelectOrganizationPresenter extends AppBasePresenter<SelectOrganizationContract.View> implements SelectOrganizationContract.Presenter {
    @Inject
    SelectFriendsRepository mRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;

    @Inject
    public SelectOrganizationPresenter(SelectOrganizationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        Subscription subscribe = mRepository.getOrganizationList(mRootView.getPage(), mRootView.getSearchKeyWord())
                .flatMap(organizationBeans -> {
                    for (OrganizationBean organizationBean : organizationBeans) {
                        //如果是更换状态则默认选中原组织
                        if (organizationBean.getId() == mRootView.getOrganizationId()) {
                            organizationBean.setSelector(true);
                        }
                    }
                    return Observable.just(organizationBeans);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<OrganizationBean>>() {
                    @Override
                    protected void onSuccess(List<OrganizationBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<OrganizationBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void createConversation(List<UserInfoBean> list, int organizationId) {
        // 没有添加当前用户的情况下 添加在第一个
        if (list.get(0).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            UserInfoBean mySelf = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
            if (mySelf == null) {
                mRootView.showSnackErrorMessage("当前用户信息不存在");
                return;
            }
            list.add(0, mySelf);
        }

        StringBuilder members = new StringBuilder();
        StringBuilder groupNames = new StringBuilder();
        for (UserInfoBean userInfoBean : list) {
            members.append(String.valueOf(userInfoBean.getUser_id())).append(",");
            groupNames.append(userInfoBean.getName()).append("、");
        }
        groupNames.deleteCharAt(groupNames.length() - 1);

        // 创建群组会话
        String groupName = groupNames.toString();

        // 群简介并没有地方展示 随便写写啦
        String groupIntro = "暂无";

        Subscription subscription = mRepository
                .createGroup(groupName, groupIntro, true,
                        200, false, false, list.get(0).getUser_id(), members.substring(0, members.length() - 1), organizationId)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .flatMap(groupInfo -> {
                    EMGroup group = null;
                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(groupInfo.getId());
                        TSEMessageUtils.sendCreateGroupMessage(mContext.getString(R.string.super_edit_group_name),
                                groupInfo.getId(), AppApplication.getMyUserIdWithdefault());

                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    return Observable.just(groupInfo);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        // 创建成功 跳转聊天详情页面
                        String id = data.getId();
                        data.setName(groupName);
                        data.setMembersonly(false);
                        data.setMaxusers(200);
                        data.setAllowinvites(false);
                        data.setIsPublic(false);
                        data.setOwner(list.get(0).getUser_id());
                        data.setDescription(groupIntro);
                        data.setAffiliations_count(list.size());
                        data.setAffiliations(list);
                        mChatGroupBeanGreenDao.saveSingleData(data);
                        mUserInfoBeanGreenDao.saveMultiData(data.getAffiliations());
                        mRootView.createConversionResult(getChatUser(list), EMConversation.EMConversationType.GroupChat, EaseConstant
                                .CHATTYPE_GROUP, id);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.dismissSnackBar();
                    }
                });
        addSubscrebe(subscription);

    }

    @Override
    public void changOrganization() {
        Subscription subscribe = mRepository.changOrganization(mRootView.getGroupId(),mRootView.getOrganizationId())
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.showSnackSuccessMessage("更换成功");
                    }
                });
        addSubscrebe(subscribe);
    }

    private List<ChatUserInfoBean> getChatUser(List<UserInfoBean> userInfoBeanList) {
        List<ChatUserInfoBean> list = new ArrayList<>();
        for (UserInfoBean userInfoBean : userInfoBeanList) {
            ChatUserInfoBean chatUserInfoBean = new ChatUserInfoBean();
            chatUserInfoBean.setUser_id(userInfoBean.getUser_id());
            chatUserInfoBean.setAvatar(userInfoBean.getAvatar());
            chatUserInfoBean.setName(userInfoBean.getName());
            chatUserInfoBean.setSex(userInfoBean.getSex());
            if (userInfoBean.getVerified() != null) {
                ChatVerifiedBean verifiedBean = new ChatVerifiedBean();
                verifiedBean.setDescription(userInfoBean.getVerified().getDescription());
                verifiedBean.setIcon(userInfoBean.getVerified().getIcon());
                verifiedBean.setStatus(userInfoBean.getVerified().getStatus());
                verifiedBean.setType(userInfoBean.getVerified().getType());
                chatUserInfoBean.setVerified(verifiedBean);
            }
            list.add(chatUserInfoBean);
        }
        return list;
    }
}
