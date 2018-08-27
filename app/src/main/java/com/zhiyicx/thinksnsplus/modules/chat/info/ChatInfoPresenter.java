package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.os.Bundle;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.baseproject.em.manager.util.TSEMessageUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.base.EmptySubscribe;
import com.zhiyicx.thinksnsplus.config.DefaultUserInfoConfig;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBeanDao;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify.VerifyFriendOrGroupActivity;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_EDIT_NAME;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoPresenter extends AppBasePresenter<ChatInfoContract.View>
        implements ChatInfoContract.Presenter {

    @Inject
    ChatInfoRepository mRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;

    @Inject
    public ChatInfoPresenter(ChatInfoContract.View rootView) {
        super(rootView);
    }

    @Override
    public boolean isGroupAdminer() {
        if(null != EMClient.getInstance().groupManager().getGroup(mRootView.getChatId())){
            List<String> adminList = EMClient.getInstance().groupManager().getGroup(mRootView.getChatId()).getAdminList();
            for (String admin:adminList) {
                if(admin.equals(String.valueOf(AppApplication.getMyUserIdWithdefault())))
                    return true;
            }
        }

        return false;
    }

    @Override
    public boolean isGroupOwner() {
        if (null == mRootView.getGroupBean()) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(mRootView.getChatId());
            return group.getOwner().equals(String.valueOf(AppApplication.getMyUserIdWithdefault()));
        }else {
            return mRootView.getGroupBean().getOwner() == AppApplication.getMyUserIdWithdefault();
        }
    }

    /*@Override
    public void getIsInGroup() {
        Observable.create((Observable.OnSubscribe<Boolean>) subscriber -> {
            List<EMGroup> groupList = null;
            try {
                groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            } catch (HyphenateException e) {
                //e.printStackTrace();
            }
            boolean isInGroup = false;
            if(null != groupList && groupList.size() > 0 ){
                for (EMGroup group:groupList) {
                    if(group.getGroupId().equals(mRootView.getChatId())){
                        isInGroup = true;
                        break;
                    }
                }
            }
            subscriber.onNext(isInGroup);
            subscriber.onCompleted();

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<Boolean>() {
                    @Override
                    protected void onSuccess(Boolean data) {
                        mRootView.setIsInGroup(data);
                    }
                });
    }*/

    @Override
    public void destoryOrLeaveGroup(String chatId) {

        if(null == mRootView.getGroupBean())
            return;

        mRootView.showSnackLoadingMessage("请稍后...");
        if(isGroupOwner()){//解散群
            mRepository.deleteGroup(chatId)
                    .subscribe(new BaseSubscriberV3<String>(mRootView){
                        @Override
                        protected void onSuccess(String data) {
                            super.onSuccess(data);
                            EventBus.getDefault().post("解散群聊", EVENT_IM_DELETE_QUIT);
                            mRootView.closeCurrentActivity();
                        }
                    });
        }else if(mRootView.getIsInGroup()){//退群
            Observable.just(chatId)
                    .subscribeOn(Schedulers.io())
                    .flatMap(s -> {
                        // 退群
                        try {
                            EMClient.getInstance().groupManager().leaveGroup(chatId);
                        } catch (HyphenateException e) {
                            //e.printStackTrace();
                            return Observable.error(e);
                        }
                        TSEMessageUtils.saveExitGroupInLocal(chatId);
                        return mRepository.synExitGroup(chatId,mRootView.getGroupBean().getGroup_level());
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriberV3<String>(mRootView){
                        @Override
                        protected void onSuccess(String data) {
                            super.onSuccess(data);

                            TSEMessageUtils.sendEixtGroupMessage(mRootView.getChatId(),
                                    mRootView.getGroupBean().getName(),TSEMConstants.TS_ATTR_GROUP_EXIT);

                            EventBus.getDefault().post("退出群聊", EVENT_IM_DELETE_QUIT);
                            mRootView.closeCurrentActivity();
                        }
                    });
        }else {//加入群聊
            mRepository.verifyEnterGroup(chatId,null,false)
                    .subscribe(new BaseSubscriberV3<String>(mRootView){
                        @Override
                        protected void onSuccess(String data) {
                            super.onSuccess(data);
                            mRootView.goChatActivity();
                        }

                        @Override
                        protected void onFailure(String message, int code) {
                            //super.onFailure(message, code);
                            mRootView.dismissSnackBar();
                            if(code == 504){//等待验证
                                VerifyFriendOrGroupActivity.startVerifyGroupActivity(mContext,chatId);
                            }else if(code == 503){
                                mRootView.showSnackErrorMessage(mContext.getString(R.string.chat_group_not_allow_anyone_enter));
                            }else {
                                super.onFailure(message,code);
                            }
                        }
                    });
        }
    }

    @Override
    public void openOrCloseGroupMessage(boolean isChecked, String chatId) {
        Observable.empty()
                .observeOn(Schedulers.io())
                .subscribe(new EmptySubscribe<Object>() {
                    @Override
                    public void onCompleted() {
                        try {
                            EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                            if (isChecked) {
                                EMClient.getInstance().groupManager().blockGroupMessage(chatId);
                                // 提示屏蔽信息
                                message.addBody(new EMTextMessageBody(mContext.getString(R.string.shield_group_msg)));
                            } else {
                                EMClient.getInstance().groupManager().unblockGroupMessage(chatId);
                                message.addBody(new EMTextMessageBody(mContext.getString(R.string.unshield_group_msg)));
                            }
                            message.setFrom("admin");
                            message.setTo(chatId);
                            message.setAttribute(TSEMConstants.TS_ATTR_BLOCK, true);
                            message.setAttribute(TSEMConstants.TS_ATTR_TAG, AppApplication.getMyUserIdWithdefault());
                            message.setChatType(EMMessage.ChatType.GroupChat);
                            EMClient.getInstance().chatManager().saveMessage(message);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                        }
                    }
                });
    }

    @Override
    public void openBannedPost(String im_group_id, String user_id, String times, String members) {
        Subscription subscription = mRepository.openBannedPost(im_group_id, user_id, times, members)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        mRootView.setBannedPostSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                });
        addSubscrebe(subscription);
    }

    @Override
    public void removeBannedPost(String im_group_id, String user_id, String members) {
        Subscription subscription = mRepository.removeBannedPost(im_group_id, user_id, members)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {

                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                });
        addSubscrebe(subscription);
    }

    @Override
    public void setSticks(String stick_id, String author, int isStick) {
        Subscription subscription = mRepository.setStick(stick_id, author, isStick)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
//                        mRootView.setSticksSuccess();
                        mRootView.setStickState(isStick==0?true:false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getResources().getString(R.string.err_net_not_work));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean, boolean isEditGroupFace) {
        // 这里不是修改群主，所以newOwner直接传空
        Subscription subscription = mRepository.updateGroup(chatGroupBean.getId(), chatGroupBean.getName(), chatGroupBean.getDescription(), 1, 200,
                chatGroupBean.isMembersonly(),
                0, chatGroupBean.getGroup_face(), isEditGroupFace, "", chatGroupBean.getGroup_level())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("修改中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        mRootView.dismissSnackBar();
                        /*mChatGroupBeanGreenDao.saveSingleData(data);

                        mRootView.updateGroup(data);
                        mRootView.dismissSnackBar();*/

                        //EventBus.getDefault().post(mRootView.getGroupBean(), EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO);
                        EventBus.getDefault().post(true,EventBusTagConfig.EVENT_IM_GROUP_UPDATE_INFO);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                });
        addSubscrebe(subscription);
    }

    @Override
    public void getGroupChatInfo(String groupId) {

        addSubscrebe(Observable.zip(mRepository.getNewGroupInfoV2(groupId),
                getIsInGroupObservable(), (chatGroupNewBean, aBoolean) -> {
                    chatGroupNewBean.setIs_in(aBoolean ? 1 : 0);
                    return chatGroupNewBean;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupNewBean>() {
                    @Override
                    protected void onSuccess(ChatGroupNewBean data) {
                        mChatGroupBeanGreenDao.saveSingleData(data);
                        mRootView.getGroupInfoSuccess(data);
                        mRootView.isShowEmptyView(false, true);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.isShowEmptyView(false, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.isShowEmptyView(false, false);
                    }
                }));
    }

    @Override
    public void createGroupFromSingleChat() {
        String name = AppApplication.getmCurrentLoginAuth().getUser().getName() + "、" + getUserInfoFromLocal(mRootView.getToUserId()).getName();
        String member = AppApplication.getMyUserIdWithdefault() + "," + mRootView.getToUserId();
        Subscription subscription = mRepository.createGroup(name, "暂无", true,
                200, false, true, AppApplication.getMyUserIdWithdefault(), member)
                .doOnSubscribe(() -> {
                    // 这里的占位文字都没提供emm
                    mRootView.showSnackLoadingMessage(mContext.getString(R.string.circle_dealing));
                })
                .flatMap(groupInfo -> {
                    try {
                        // 获取环信群组信息
                        EMClient.getInstance().groupManager().getGroupFromServer(groupInfo.getId());

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
                        data.setName(name);
                        data.setMembersonly(true);
                        data.setMaxusers(200);
                        data.setAllowinvites(false);
                        data.setIsPublic(false);
                        data.setOwner(AppApplication.getMyUserIdWithdefault());
                        data.setAffiliations_count(2);
                        mChatGroupBeanGreenDao.saveSingleData(data);
                        mRootView.dismissSnackBar();
//                        EventBus.getDefault().post(data, EventBusTagConfig.EVENT_IM_GROUP_CREATE_FROM_SINGLE);
                        mRootView.createGroupSuccess(data);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EVENT_IM_GROUP_EDIT_NAME)
    public void onGroupNameChanged(String newName) {
        mRootView.getGroupBean().setName(newName);
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        updateGroup(chatGroupBean, false);
    }

    /*@Subscriber(tag = EVENT_IM_GROUP_DATA_CHANGED)
    public void onGroupOwnerChanged(ChatGroupBean chatGroupBean) {
        mRootView.updateGroupOwner(chatGroupBean);
    }

    @Subscriber(tag = EVENT_IM_GROUP_REMOVE_MEMBER)
    public void onGroupMemberRemoved(Bundle bundle) {
        List<UserInfoBean> removedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_REMOVE_MEMBER);
        if (removedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        List<UserInfoBean> originalList = new ArrayList<>();
        originalList.addAll(chatGroupBean.getAffiliations());
        for (int i = 0; i < removedList.size(); i++) {
            for (UserInfoBean userInfoBean : chatGroupBean.getAffiliations()) {
                if (removedList.get(i).getUser_id().equals(userInfoBean.getUser_id())) {
                    originalList.remove(userInfoBean);
                    break;
                }
            }
        }
        chatGroupBean.setAffiliations_count(chatGroupBean.getAffiliations_count() - removedList.size());
        chatGroupBean.setAffiliations(originalList);
        mRootView.updateGroup(chatGroupBean);
    }

    @Subscriber(tag = EVENT_IM_GROUP_ADD_MEMBER)
    public void onGroupMemberAdded(Bundle bundle) {
        List<UserInfoBean> addedList = bundle.getParcelableArrayList(EVENT_IM_GROUP_ADD_MEMBER);
        if (addedList == null) {
            return;
        }
        ChatGroupBean chatGroupBean = mRootView.getGroupBean();
        chatGroupBean.getAffiliations().addAll(addedList);
        chatGroupBean.setAffiliations_count(chatGroupBean.getAffiliations_count() + addedList.size());
        mRootView.updateGroup(chatGroupBean);
    }*/

    @Override
    public UserInfoBean getUserInfoFromLocal(String id) {
        try {
            return mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(id));
        } catch (NumberFormatException e) {
            return DefaultUserInfoConfig.getDefaultDeletUserInfo(mContext, 0);
        }
    }

    @Override
    public boolean checkImhelper(String chatId) {
        try {
            return mSystemRepository.checkUserIsImHelper(Long.parseLong(chatId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void saveGroupInfo(ChatGroupBean chatGroupBean) {
        mChatGroupBeanGreenDao.saveSingleData(chatGroupBean);
    }

    @Override
    public void getConversationStickList(String chatId) {
        mRepository.refreshSticks(String.valueOf(AppApplication.getMyUserIdWithdefault()))
                .subscribe(new BaseSubscribeForV2<List<StickBean>>() {
                    @Override
                    protected void onSuccess(List<StickBean> data) {
                        for (int i = 0; i < data.size(); i++) {
                            if(null != data.get(i).getChatGroupBean()){
                                if(chatId.equals(data.get(i).getChatGroupBean().id)){
                                    mRootView.setStickState(true);
                                    return;
                                }
                            }else if(null != data.get(i).getUserInfoBean()){
                                if(chatId.equals(data.get(i).getUserInfoBean().id)){
                                    mRootView.setStickState(true);
                                    return;
                                }
                            }
                        }
                        mRootView.setStickState(false);
                    }
                });
    }


    /**
     * 获取是否在群里的observable
     * @return
     */
    private Observable<Boolean> getIsInGroupObservable(){
        return Observable.create(subscriber -> {
            List<EMGroup> groupList = null;
            try {
                groupList = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
            } catch (HyphenateException e) {
                //e.printStackTrace();
                Observable.error(e);
            }
            boolean isInGroup = false;
            if(null != groupList && groupList.size() > 0 ){
                for (EMGroup group:groupList) {
                    if(group.getGroupId().equals(mRootView.getChatId())){
                        isInGroup = true;
                        break;
                    }
                }
            }
            subscriber.onNext(isInGroup);
            subscriber.onCompleted();
        });
    }


}
