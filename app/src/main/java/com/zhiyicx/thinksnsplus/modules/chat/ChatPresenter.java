package com.zhiyicx.thinksnsplus.modules.chat;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMRefreshEvent;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Describe
 * @Author zl
 * @Date 2017/1/6
 * @Contact master.jungle68@gmail.com
 */

public class ChatPresenter extends AppBasePresenter<ChatContract.View> implements ChatContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;
    @Inject
    BaseFriendsRepository mBaseFriendsRepository;
    @Inject
    ChatInfoRepository mRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;

    @Inject
    public ChatPresenter(ChatContract.View rootView) {
        super(rootView);
    }

    @Override
    public boolean isImHelper() {
        List<SystemConfigBean.ImHelperBean> list = mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
        for (SystemConfigBean.ImHelperBean helperBean:list) {
            if(helperBean.getUid().equals(mRootView.getChatId()))
                return true;
        }
        return false;
    }

    @Override
    public String getChatGroupName() {
        //这里拿环信的group人数信息，这样子人数每次都是最新的

        if(null != mChatGroupBeanGreenDao.getChatGroupBeanById(mRootView.getChatId())){
            return String.format("%s(%s)",mChatGroupBeanGreenDao.getChatGroupBeanById(mRootView.getChatId()).getName(),
                    mChatGroupBeanGreenDao.getChatGroupBeanById(mRootView.getChatId()).getAffiliations_count());
        }else if(null != EMClient.getInstance().groupManager().getGroup(mRootView.getChatId())){
            return String.format("%s(%s)",EMClient.getInstance().groupManager().getGroup(mRootView.getChatId()).getGroupName(),
                    EMClient.getInstance().groupManager().getGroup(mRootView.getChatId()).getMemberCount());
        }else {
            return "该群已解散";
        }
    }

    @Override
    public void dealMessages(List<EMMessage> messages) {
        Observable.just(messages)
                .subscribeOn(Schedulers.io())
                .flatMap(emMessages -> {
                    List<Object> userIds = new ArrayList<>();
                    for (EMMessage msg : emMessages) {
                        Long userId = null;
                        try {
                            userId = Long.parseLong(msg.getFrom());
                        } catch (NumberFormatException ignore) {
                        }
                        if (userId != null) {
                            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                            if (userInfoBean == null) {
                                userIds.add(userId);
                            }
                        }
                    }
                    if (userIds.isEmpty()) {
                        return Observable.just(emMessages);
                    } else {
                        return mUserInfoRepository.getUserInfo(userIds)
                                .flatMap(userInfoBeans -> Observable.just(emMessages));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<EMMessage>>() {
                    @Override
                    protected void onSuccess(List<EMMessage> data) {
                        mRootView.onMessageReceivedWithUserInfo(data);
                    }
                });
    }

    @Override
    public ChatGroupBean getChatGroupInfoFromLocal() {
        return mChatGroupBeanGreenDao.getChatGroupBeanById(mRootView.getChatId());
    }

    @Override
    public UserInfoBean getUserInfoFromLocal() {
        return mUserInfoBeanGreenDao.getUserInfoById(mRootView.getChatId());
    }

    @Override
    public UserInfoBean getUserInfoFromLocal(String user_id) {
        return mUserInfoBeanGreenDao.getUserInfoById(user_id);
    }

    @Override
    public void getUserInfoFromServer() {
        List<Object> list = new ArrayList<>();
        list.add(mRootView.getChatId());
        mUserInfoRepository.getUserInfo(list)
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        if(null != data && data.size() > 0)
                            mRootView.updateUserInfo(data.get(0));
                    }
                });
    }

    @Override
    public void getChatGroupInfoFromServer() {
        Subscription subscription = mRepository.getGroupChatInfo(mRootView.getChatId())
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mChatGroupBeanGreenDao.saveMultiData(data);
                        mRootView.updateChatGroupInfo(mChatGroupBeanGreenDao.getChatGroupBeanById(mRootView.getChatId()));
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.updateChatGroupInfo(null);
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateGroupName(ChatGroupBean chatGroupBean) {
        if (chatGroupBean == null) {
            return;
        }
        Subscription subscription = mBaseFriendsRepository.updateGroup(chatGroupBean.getId(), chatGroupBean.getName(), chatGroupBean.getDescription
                        (), 1, 200, chatGroupBean.isMembersonly(),
                0, chatGroupBean.getGroup_face(), false, "",chatGroupBean.getGroup_level())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("修改中..."))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<ChatGroupBean>(mRootView){

                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        super.onSuccess(data);
                        mChatGroupBeanGreenDao.saveSingleData(data);
                        mRootView.updateChatGroupInfo(data);
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * @param id    群 id
     * @param count 变动 数量
     * @param add   是否 加法
     * @return
     */
    @Override
    public boolean updateChatGroupMemberCount(String id, int count, boolean add) {
        return mChatGroupBeanGreenDao.updateChatGroupMemberCount(id, count, add);
    }

    @Override
    public void getCurrentTalkingState(String groupId) {
        addSubscrebe(mRepository.getTalkingState(groupId)
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Boolean>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Boolean> data) {
                        if(null != data){
                            mRootView.setTalkingState(data.getData(),mContext.getString(R.string.chat_no_talking_silent));
                        }
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        if(code == 404)
                            mRootView.setTalkingState(false,mContext.getString(R.string.chat_no_talking_had_destory));
                        else if(code == 405)
                            mRootView.setTalkingState(false,mContext.getString(R.string.chat_no_talking_not_in_group));
                        else//接口调用失败，不影响聊天
                            mRootView.setTalkingState(true,"");
                    }
                }));
    }

    @Override
    public void handleNotRoamingMessageList(List<EMMessage> messages) {
        Observable.just(messages)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<List<EMMessage>, Observable<List<Object>>>() {
                    @Override
                    public Observable<List<Object>> call(List<EMMessage> emMessages) {
                        List<Object> userIds = new ArrayList<>();
                        for (EMMessage msg : emMessages) {
                            Long userId = null;
                            try {
                                userId = Long.parseLong(msg.getFrom());
                            } catch (NumberFormatException ignore) {
                            }
                            if (userId != null) {
                                UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                                if (userInfoBean == null) {
                                    userIds.add(userId);
                                }
                            }
                        }
                        if (userIds.isEmpty()) {
                            return Observable.just(null);
                        } else {
                            return mUserInfoRepository.getUserInfo(userIds)
                                    .flatMap(userInfoBeans -> Observable.just(userIds));
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<Object>>() {
                    @Override
                    protected void onSuccess(List<Object> data) {
                        if(null != data)
                            mRootView.handleNotRoamingMessageWithUserInfo();
                    }
                });
    }

}
