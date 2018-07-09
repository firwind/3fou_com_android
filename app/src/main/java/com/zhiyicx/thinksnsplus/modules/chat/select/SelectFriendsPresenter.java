package com.zhiyicx.thinksnsplus.modules.chat.select;


import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.util.TSEMDateUtil;
import com.zhiyicx.baseproject.em.manager.util.TSEMessageUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.SelectFriendsRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;


/**
 * @author Catherine
 * @describe
 * @date 2018/1/11
 * @contact email:648129313@qq.com
 */

public class SelectFriendsPresenter extends AppBasePresenter<SelectFriendsContract.View>
        implements SelectFriendsContract.Presenter {

    @Inject
    SelectFriendsRepository mRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;

    @Inject
    ChatInfoRepository mChatInfoRepository;

    private Subscription mSearchSub;

    @Inject
    public SelectFriendsPresenter(SelectFriendsContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        String keyWord = mRootView.getSearchKeyWord();
        if (mSearchSub != null && !mSearchSub.isUnsubscribed()) {
            mSearchSub.unsubscribe();
        }
        // 删除用户不需要获取网络数据
        if (!mRootView.getIsDeleteMember()) {
            mSearchSub = mRepository.getUserFriendsList(maxId, keyWord)
                    .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                        @Override
                        protected void onSuccess(List<UserInfoBean> data) {
                            if (!data.isEmpty()) {
                                for (UserInfoBean userInfoBean : data) {
                                    userInfoBean.setIsSelected(0);
                                }
                            }
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            Throwable throwable = new Throwable(message);
                            mRootView.onResponseError(throwable, isLoadMore);
                        }

                        @Override
                        protected void onException(Throwable throwable) {
                            LogUtils.e(throwable, throwable.getMessage());
                            mRootView.onResponseError(throwable, isLoadMore);
                        }
                    });
            addSubscrebe(mSearchSub);
        } else {
            getLocalUser(keyWord);
        }

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        if (!mRootView.getIsDeleteMember()) {

            mRootView.onCacheResponseSuccess(new ArrayList<>(), isLoadMore);
        } else {
            getLocalUser("");
        }
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void createConversation(List<UserInfoBean> list) {
        // 没有添加当前用户的情况下 添加在第一个
        if (list.get(0).getUser_id() != AppApplication.getMyUserIdWithdefault()) {
            UserInfoBean mySelf = mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault());
            if (mySelf == null) {
                mRootView.showSnackErrorMessage("当前用户信息不存在");
                return;
            }
            list.add(0, mySelf);
        }
        if (list.size() == 2) {
            String id = String.valueOf(list.get(1).getUser_id());
            // 创建单聊，判断当前是否与该用户的会话，没有创建会话
            mRootView.createConversionResult(getChatUser(list), EMConversation.EMConversationType.Chat, EaseConstant.CHATTYPE_SINGLE, id);
        } else {
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
                            200, false, false, list.get(0).getUser_id(), members.substring(0, members.length() - 1))
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
    }

    @Override
    public void dealGroupMember(List<UserInfoBean> list) {
        StringBuilder id = new StringBuilder();
        for (UserInfoBean userInfoBean : list) {
            id.append(userInfoBean.getUser_id()).append(",");
        }
        id = new StringBuilder(id.substring(0, id.length() - 1));
        Observable<Object> observable;
        if (mRootView.getIsDeleteMember()) {
            // 删除
            observable = mRepository.removeGroupMember(mRootView.getGroupData().getId(),
                    id.toString(), mRootView.getGroupData().getGroup_level());
        } else {
            observable = mRepository.addGroupMember(mRootView.getGroupData().getId(), id.toString()
                    , mRootView.getGroupData().getGroup_level());
        }
        Subscription subscription = observable
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing)))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        Bundle bundle = new Bundle();
                        if (mRootView.getIsDeleteMember()) {
                            bundle.putParcelableArrayList(EventBusTagConfig.EVENT_IM_GROUP_REMOVE_MEMBER, (ArrayList<? extends Parcelable>) list);
                            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_IM_GROUP_REMOVE_MEMBER);
                        } else {
                            bundle.putParcelableArrayList(EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER, (ArrayList<? extends Parcelable>) list);
                            EventBus.getDefault().post(bundle, EventBusTagConfig.EVENT_IM_GROUP_ADD_MEMBER);
                        }
                        mRootView.dealGroupMemberResult();
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

    /**
     * 如果是删除用户 那么则不需要获取网络数据
     */
    private void getLocalUser(String key) {
        if (mRootView.getGroupData() == null) {
            return;
        }
//        List<UserInfoBean> list = mRootView.getGroupData().getAffiliations();
        mChatInfoRepository.getUserInfoInfo(mRootView.getGroupData().getId(), key)//从服务器获取该群的用户数据
                .flatMap(userInfoBeans -> {
                    return // 移除自己
                            Observable.just(userInfoBeans)
                                    .map(list1 -> {
                                        int position = -1;
                                        for (int i = 0; i < list1.size(); i++) {
                                            list1.get(i).setIsSelected(0);
                                            if (list1.get(i).getUser_id().equals(AppApplication.getMyUserIdWithdefault())) {
                                                position = i;
                                            }
                                        }
                                        if (position != -1) {
                                            list1.remove(position);
                                        }
                                        return list1;
                                    });
                })
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        // 有key表示是搜素，没有就是全部 直接获取就好了
                        if (TextUtils.isEmpty(key)) {
                            mRootView.onNetResponseSuccess(data, false);
                        } else {
                            List<UserInfoBean> searchResult = new ArrayList<>();
                            for (UserInfoBean userInfoBean : mRootView.getGroupData().getAffiliations()) {
                                if (!TextUtils.isEmpty(userInfoBean.getName()) && userInfoBean.getName().toLowerCase().contains(key.toLowerCase())) {
                                    searchResult.add(userInfoBean);
                                }
                            }
                            mRootView.onNetResponseSuccess(searchResult, false);
                        }

                    }
                });

    }
}
