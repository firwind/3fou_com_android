package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMMultipleMessagesEvent;
import com.zhiyicx.baseproject.em.manager.eventbus.TSEMRefreshEvent;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.MessageConversationRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;
import com.zhiyicx.thinksnsplus.modules.chat.call.TSEMHyphenate;
import com.zhiyicx.thinksnsplus.modules.home.message.container.MessageContainerFragment;
import com.zhiyicx.thinksnsplus.utils.MessageTimeAndStickSort;
import com.zhiyicx.thinksnsplus.utils.badge.CommonBadgeUtil;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MessageConversationPresenter extends AppBasePresenter<MessageConversationContract.View>
        implements MessageConversationContract.Presenter {

    @Inject
    MessageConversationRepository mRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    ChatInfoRepository mChatInfoRepository;
    /**
     * 复制的所有原数据
     */
    private List<MessageItemBeanV2> mCopyConversationList;

    /**
     * 是否是第一次连接 im
     */
    private boolean mIsFristConnectedIm = true;
    private Subscription mSearchSub;
    private Subscription mCacheConversatonSub;//缓存会话请求
    private Subscription mStickConversationSub;//置顶请求

    private List<StickBean> mStickList = new ArrayList<>();//当前缓存的置顶信息


    @Inject
    public MessageConversationPresenter(MessageConversationContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        getAllConversationV2(isLoadMore);
    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageItemBeanV2> data, boolean isLoadMore) {
        return false;
    }

    /*@Override
    public void refreshSticks(String author) {
        Subscription subscription = mChatInfoRepository.refreshSticks(author)
                .subscribe(new BaseSubscribeForV2<List<StickBean>>() {
                    @Override
                    protected void onSuccess(List<StickBean> data) {
                        mRootView.getSticksList(data);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.getSticksFailure();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.getSticksFailure();
                    }
                });
        addSubscrebe(subscription);
    }*/

    @Override
    public void setSticks(MessageItemBeanV2 message, String author) {
        Observable<String> observable = mChatInfoRepository.setStick(message.getEmKey(), author,message.getIsStick());

        Subscription subscription = observable
                .subscribe(new BaseSubscribeForV2<String>() {

                    @Override
                    protected void onSuccess(String data) {
                        message.setIsStick(message.getIsStick()==0?1:0);
                        //重新进行排序
                        Collections.sort(mRootView.getListDatas(),new MessageTimeAndStickSort());
                        mRootView.refreshData();

                        mRootView.setSticksSuccess(message.getEmKey());//置顶成功
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
    public void refreshConversationReadMessage() {
        Subscription represhSu = Observable.just("")
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(s -> {
                    checkBottomMessageTip();
                    return mRootView.getListDatas();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> mRootView.refreshData(), Throwable::printStackTrace);
        addSubscrebe(represhSu);
    }

    @Override
    public void deleteConversation(int position) {
        // 改为环信的删除
        MessageItemBeanV2 messageItemBeanV2 = mRootView.getListDatas().get(position);
        Subscription subscription = Observable.just(messageItemBeanV2)
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemBeanV2 -> {
                    mRootView.getListDatas().remove(itemBeanV2);
                    mRootView.refreshData();
                    checkBottomMessageTip();
                    EMClient.getInstance().chatManager().deleteConversation(itemBeanV2.getEmKey(), true);
                });
        addSubscrebe(subscription);
    }

    @Override
    public List<ChatUserInfoBean> getChatUserList(int position) {
        List<ChatUserInfoBean> chatUserInfoBeans = new ArrayList<>();
        // 当前用户
        if (mRootView.getListDatas().get(position).getConversation().getType() == EMConversation.EMConversationType.Chat) {
            chatUserInfoBeans.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
            chatUserInfoBeans.add(getChatUser(mRootView.getListDatas().get(position).getUserInfo()));
        } else {
            if (mRootView.getListDatas().get(position) != null) {
                for (UserInfoBean userInfoBean : mRootView.getListDatas().get(position).getList()) {
                    chatUserInfoBeans.add(getChatUser(userInfoBean));
                }
            }
        }
        return chatUserInfoBeans;
    }

    @Override
    public void searchList(String key) {
        if (mSearchSub != null && !mSearchSub.isUnsubscribed()) {
            mSearchSub.unsubscribe();
        }
        if (TextUtils.isEmpty(key)) {
            mRootView.onNetResponseSuccess(mCopyConversationList, false);
            return;
        }

        mSearchSub = Observable.just(key)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(s -> {
                    List<MessageItemBeanV2> newList = new ArrayList<>();
                    for (MessageItemBeanV2 itemBeanV2 : mCopyConversationList) {
                        String name = "";
                        if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat) {
                            if (itemBeanV2.getUserInfo() != null) {
                                name = itemBeanV2.getUserInfo().getName();
                            }
                        } else {
                            EMGroup group = EMClient.getInstance().groupManager().getGroup(itemBeanV2.getEmKey());
                            if (group != null) {
                                name = group.getGroupName();
                            }
                        }
                        if (name.toLowerCase().contains(s.toLowerCase())) {
                            newList.add(itemBeanV2);
                        }
                    }
                    if (newList.size() > 1) {
                        // 数据大于一个才排序
                        Collections.sort(newList, new EmTimeSortClass());
                    }
                    return newList;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> mRootView.onNetResponseSuccess(list, false));
        addSubscrebe(mSearchSub);
    }

    private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean) {
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
        return chatUserInfoBean;
    }

    /**
     * 获取环信的所有会话列表
     *
     * @param isLoadMore 是否加载更多
     */
    private void getAllConversationV2(boolean isLoadMore) {
        // 已连接才去获取
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()) {

            if (!TextUtils.isEmpty(mRootView.getsearchKeyWord())) {
                mRootView.hideRefreshState(isLoadMore);
                return;
            }

            if(mCacheConversatonSub!=null && mCacheConversatonSub.isUnsubscribed())
                mCacheConversatonSub.unsubscribe();

            mCacheConversatonSub = mRepository.getConversationList((int) AppApplication.getMyUserIdWithdefault())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .map(new Func1<List<MessageItemBeanV2>, List<MessageItemBeanV2>>() {
                        @Override
                        public List<MessageItemBeanV2> call(List<MessageItemBeanV2> messageItemBeanV2s) {
                            return mStickList.size() == 0?messageItemBeanV2s:handleStickAndCacheConversation(mStickList,messageItemBeanV2s);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getConversationSubscriber(isLoadMore,true));
            addSubscrebe(mCacheConversatonSub);

        } else {
            if (!mIsFristConnectedIm) {
                mRootView.showStickyMessage(mContext.getString(R.string.chat_unconnected));
                mRootView.hideLoading();
            } else {
                mIsFristConnectedIm = false;
            }
            // 尝试重新登录，在homepresenter接收
            mAuthRepository.loginIM();
        }
    }

    private rx.Subscriber<List<MessageItemBeanV2>> getConversationSubscriber(boolean isLoadMore,boolean isRequestStick){

        return  new BaseSubscribeForV2<List<MessageItemBeanV2>>() {
            @Override
            protected void onSuccess(List<MessageItemBeanV2> data) {
                if (mCopyConversationList == null) {
                    mCopyConversationList = new ArrayList<>();
                }
                mCopyConversationList = data;
                mRootView.onNetResponseSuccess(data, isLoadMore);
                mRootView.hideStickyMessage();
                checkBottomMessageTip();

                if(isRequestStick){
                    requestStickConversation(isLoadMore);
                }

            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                if(code != 433)
                    mRootView.showStickyMessage(message);
                mRootView.onResponseError(null, false);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.showStickyMessage(throwable.getMessage());
                mRootView.onResponseError(throwable, false);
            }
        };

    }

    /**
     * 请求置顶会话
     * @param isLoadMore
     */
    private void requestStickConversation(boolean isLoadMore){

        if(mStickConversationSub != null && mStickConversationSub.isUnsubscribed())
            mStickConversationSub.unsubscribe();
        mStickConversationSub = mChatInfoRepository.refreshSticks(String.valueOf(AppApplication.getMyUserIdWithdefault()))
                .observeOn(Schedulers.io())
                .map(new Func1<List<StickBean>, List<MessageItemBeanV2>>() {
                    @Override
                    public List<MessageItemBeanV2> call(List<StickBean> stickBeans) {
                        Gson gson = new Gson();
                        if(!gson.toJson(mStickList).equals(gson.toJson(stickBeans))){
                            mStickList = stickBeans;
                            List<MessageItemBeanV2> list = new ArrayList<>();
                            list.addAll(mRootView.getListDatas());
                            return handleStickAndCacheConversation(stickBeans,list);
                        }else {
                            return null;
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<MessageItemBeanV2>>() {
                    @Override
                    protected void onSuccess(List<MessageItemBeanV2> data) {
                        if(null != data){
                            if (mCopyConversationList == null) {
                                mCopyConversationList = new ArrayList<>();
                            }
                            mCopyConversationList = data;
                            mRootView.onNetResponseSuccess(data, isLoadMore);
                            mRootView.hideStickyMessage();
                            checkBottomMessageTip();
                        }
                    }
                });
    }


    /**
     * 处理置顶和缓存会话
     * @param stickList
     * @param cacheConversationList
     * @return
     */
    private List<MessageItemBeanV2> handleStickAndCacheConversation(List<StickBean> stickList,List<MessageItemBeanV2> cacheConversationList){

        List<String> topIds = new ArrayList<>();
        for (int i = 0; i < stickList.size(); i++) {
            if(null != stickList.get(i).getChatGroupBean()){
                topIds.add(String.valueOf(stickList.get(i).getChatGroupBean().id));
            }else if(null != stickList.get(i).getUserInfoBean()){
                topIds.add(String.valueOf(stickList.get(i).getUserInfoBean().id));
            }
        }

        for (int i = 0; i < cacheConversationList.size(); i++) {
            try {
                if (topIds.indexOf(cacheConversationList.get(i).getEmKey()) != -1) {
                    cacheConversationList.get(i).setIsStick(1);
                    topIds.remove(cacheConversationList.get(i).getEmKey());
                } else {
                    cacheConversationList.get(i).setIsStick(0);
                }
            } catch (Exception e) {
                cacheConversationList.get(i).setIsStick(0);
                continue;
            }
        }

        //完善未匹配到的置顶消息填充到 消息List
        if(topIds.size() > 0)
            fillStickBeanInMessage(cacheConversationList,topIds,stickList);

        //根据置顶和消息时间进行排序
        Collections.sort(cacheConversationList,new MessageTimeAndStickSort());

        return cacheConversationList;

    }


    /**
     * 将匹配到id填充数据到消息列表
     * @param originList 原始消息List
     * @param topIds 匹配的id源
     * @param stickBeans 待插入的消息List
     *
     * 这里要 将未匹配到的消息调用环信api 存入本地数据库
     *EMClient.getInstance().chatManager().getConversation(tsHelper.getEmKey(), EMConversation.EMConversationType.Chat, true)
     */
    private void fillStickBeanInMessage(List<MessageItemBeanV2> originList,List<String> topIds,List<StickBean> stickBeans){
        //未被匹配到的 置顶id
        for (int i = 0; i < topIds.size(); i++) {
            for (int j = 0; j < stickBeans.size(); j++) {
                String id;
                try {
                    id = null == stickBeans.get(j).getUserInfoBean() ?
                            stickBeans.get(j).getChatGroupBean().id :
                            stickBeans.get(j).getUserInfoBean().id;
                }catch (Exception e){
                    continue;
                }
                //找出未被匹配到的置顶信息
                if(topIds.get(i).equals(id)){
                    MessageItemBeanV2 message = new MessageItemBeanV2();
                    message.setEmKey(topIds.get(i));
                    message.setIsStick(1);

                    if(null != stickBeans.get(j).getChatGroupBean()){
                        //补充type数据
                        message.setType(EMConversation.EMConversationType.GroupChat);
                        //补充群组信息
                        StickBean.StickChatGroupBean stickChatGroupBean = stickBeans.get(j).getChatGroupBean();
                        ChatGroupBean chatGroupBean = new ChatGroupBean();
                        chatGroupBean.setId(stickChatGroupBean.id);
                        chatGroupBean.setName(stickChatGroupBean.name);
                        chatGroupBean.setGroup_face(stickChatGroupBean.group_face);
                        chatGroupBean.setAffiliations_count(stickChatGroupBean.affiliations_count);

                        // 创建会话的 conversation 要传入用户名 ts+采用用户Id作为用户名，聊天类型 单聊
                        EMConversation conversation =
                                EMClient.getInstance().chatManager().getConversation(stickChatGroupBean.id, EMConversation
                                        .EMConversationType
                                        .GroupChat, true);

                        message.setConversation(conversation);
                        message.setChatGroupBean(chatGroupBean);

                    }else if(null != stickBeans.get(j).getUserInfoBean()){
                        //补充type数据
                        message.setType(EMConversation.EMConversationType.Chat);
                        //补充user数据
                        StickBean.StickUserInfoBean stickUserInfoBean = stickBeans.get(j).getUserInfoBean();
                        UserInfoBean userInfoBean = new UserInfoBean();
                        userInfoBean.setUser_id(Long.parseLong(stickUserInfoBean.id));
                        userInfoBean.setName(stickUserInfoBean.name);
                        userInfoBean.setAvatar(stickUserInfoBean.avatar);
                        message.setUserInfo(userInfoBean);

                        EMConversation conversation =
                                EMClient.getInstance().chatManager().getConversation(stickUserInfoBean.id, EMConversation
                                        .EMConversationType
                                        .Chat, true);

                        message.setConversation(conversation);

                    }else {
                        //结束掉内循环，进行下一个外循环
                        break;
                    }
                    originList.add(0,message);
                    // //结束掉内循环，进行下一个外循环
                    break;
                }
            }

        }

    }


    /**
     * 检测底部小红点是否需要显示
     */
    private void checkBottomMessageTip() {
        Subscription subscribe = Observable.just(true)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(aBoolean -> {
                    // 是否显示底部红点
                    /*boolean isShowMessageTip = false;
                    for (MessageItemBeanV2 messageItemBean : mRootView.getListDatas()) {
                        if (messageItemBean.getConversation() != null && messageItemBean.getConversation().getUnreadMsgCount() > 0) {
                            isShowMessageTip = true;
                            break;
                        } else {
                            isShowMessageTip = false;
                        }
                    }*/
                    int unreadCount = TSEMHyphenate.getInstance().getUnreadMsgCount();
                    //这里通知桌面更新角标
                    //ShortcutBadgeUtil.getInstance().toChangeBadge(mRootView.getCurrentFragment().getContext(),unreadCount);
                    CommonBadgeUtil.setBadge(mRootView.getCurrentFragment().getContext(),unreadCount);

                    return unreadCount>0;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(isShowMessageTip -> {
                    Fragment containerFragment = mRootView.getCurrentFragment().getParentFragment();
                    if (containerFragment != null && containerFragment instanceof MessageContainerFragment) {
                        //环信消息
                        ((MessageContainerFragment) containerFragment).setNewMessageNoticeState(isShowMessageTip, 1);
                    }
                }, Throwable::printStackTrace);
        addSubscrebe(subscribe);


    }

    /**
     * 请求 群聊天会话信息列表  成功的回调
     *
     * @param bundle
     */
    @Subscriber(tag = EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)
    public void getGroupList(Bundle bundle) {
        if (bundle != null && bundle.containsKey(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO)) {
            List<ChatGroupBean> list = bundle.getParcelableArrayList(EventBusTagConfig.EVENT_IM_GET_GROUP_INFO);
            if (list == null || list.isEmpty()) {
                return;
            }
            List<MessageItemBeanV2> messageItemBeanList = new ArrayList<>();
            for (ChatGroupBean chatGroupBean : list) {
                // 如果列表已经有  那么就不再追加
                boolean canAdded = true;
                for (MessageItemBeanV2 exitItem : mRootView.getListDatas()) {
                    if (exitItem.getConversation().conversationId().equals(chatGroupBean.getId())) {
                        exitItem.setEmKey(chatGroupBean.getId());
                        exitItem.setList(chatGroupBean.getAffiliations());
                        exitItem.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                        exitItem.setChatGroupBean(chatGroupBean);
                        canAdded = false;
                        break;
                    }
                }
                if (canAdded) {
                    MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                    itemBeanV2.setEmKey(chatGroupBean.getId());
                    itemBeanV2.setList(chatGroupBean.getAffiliations());
                    itemBeanV2.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                    itemBeanV2.setChatGroupBean(chatGroupBean);
                    messageItemBeanList.add(itemBeanV2);
                }
            }
            if (!messageItemBeanList.isEmpty()) {
                mRootView.getListDatas().addAll(messageItemBeanList);
                if (mCopyConversationList == null) {
                    mCopyConversationList = new ArrayList<>();
                }
                mCopyConversationList = mRootView.getListDatas();
            }
        }
        mRootView.refreshData();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(mode = ThreadMode.MAIN)
    public void onTSEMRefreshEventEventBus(TSEMRefreshEvent event) {
        if (TSEMRefreshEvent.TYPE_USER_EXIT == event.getType()) {
            getUserInfoForRefreshList(event);
        }
    }

    private void getUserInfoForRefreshList(TSEMRefreshEvent event) {
        Subscription subscription = mUserInfoRepository.getUserInfoWithOutLocalByIds(event.getStringExtra())
                .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                    @Override
                    protected void onSuccess(List<UserInfoBean> data) {
                        if (data == null || data.isEmpty()) {
                            return;
                        }
                        EMTextMessageBody textBody = new EMTextMessageBody(mContext.getResources().getString(R.string.userup_exit_group, data.get
                                (0).getName()));
                        event.getMessage().addBody(textBody);
                        EMClient.getInstance().chatManager().saveMessage(event.getMessage());
                        mRootView.refreshData();
                    }
                });
        addSubscrebe(subscription);
    }

    /**
     * 收到聊天消息
     *
     * @param messagesEvent 聊天类容
     */
    @Subscriber(mode = ThreadMode.MAIN)
    public void onMessageReceived(TSEMMultipleMessagesEvent messagesEvent) {
        if (messagesEvent.getMessages() == null || messagesEvent.getMessages().isEmpty()) {
            return;
        }
        List<EMMessage> list = messagesEvent.getMessages();

        Subscription subscribe = Observable.just(list)
                .subscribeOn(Schedulers.io())
                .flatMap(messageList -> {

                    List<MessageItemBeanV2> exitInViewList = mRootView.getListDatas();

                    List<EMMessage> notExitInViewList = new ArrayList<>();
                    notExitInViewList.addAll(messageList);

                    for (MessageItemBeanV2 itemBean: exitInViewList) {

                        for (EMMessage message: messageList) {

                            if(itemBean.getEmKey().equals(message.conversationId())){
                                //直接替换会话
                                itemBean.setConversation(EMClient.getInstance().chatManager()
                                        .getConversation(message.conversationId()));
                                //对已存在的message移除
                                notExitInViewList.remove(message);
                            }
                        }

                        //如果不存在的会话
                        if(notExitInViewList.size() == 0)
                            break;

                    }

                    List<MessageItemBeanV2> composeList = new ArrayList<>();
                    //则重组会话
                    if(notExitInViewList.size() > 0){
                        for (EMMessage message:
                                notExitInViewList) {
                            //判断重组会话中是否已经存在会话
                            boolean isExit = false;
                            for (int i = 0; i < composeList.size(); i++) {
                                if(composeList.get(i).getEmKey().equals(message.conversationId())){
                                    isExit = true;
                                    break;
                                }
                            }
                            if(!isExit){
                                MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                                //其实到这里时，获取的conversation已经是最新的了
                                itemBeanV2.setConversation(EMClient.getInstance().chatManager()
                                        .getConversation(message.conversationId()));
                                itemBeanV2.setEmKey(message.conversationId());
                                composeList.add(itemBeanV2);
                            }
                        }
                    }
                    composeList.addAll(exitInViewList);
                    return mRepository.completeEmConversation(composeList);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<MessageItemBeanV2>>() {
                    @Override
                    protected void onSuccess(List<MessageItemBeanV2> data) {
                        mRootView.getListDatas().clear();
                        mRootView.getListDatas().addAll(data);
                        //根据时间和置顶再次排序
                        Collections.sort(mRootView.getListDatas(), new MessageTimeAndStickSort());


                        mRootView.refreshData();
                        // 小红点是否要显示
                        checkBottomMessageTip();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
        addSubscrebe(subscribe);
    }

    /**
     * 删除群
     */
    @Override
    @Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_DELETE_QUIT)
    public void deleteGroup(String id) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        MessageItemBeanV2 deleteItem = null;
        for (MessageItemBeanV2 messageItemBeanV2 : mRootView.getListDatas()) {
            if (messageItemBeanV2.getConversation().conversationId().equals(id)) {
                deleteItem = messageItemBeanV2;
                break;
            }
        }
        if (deleteItem != null) {
            mRootView.getListDatas().remove(deleteItem);
            mRootView.refreshData();
        }
    }

    /**
     * @param userId 用户 id
     * @return
     */
    @Override
    public boolean checkUserIsImHelper(long userId) {
        return mSystemRepository.checkUserIsImHelper(userId);
    }

    /**
     * 更新群信息
     * OnResume()中已经做了更新
     */
    /*@Subscriber(mode = ThreadMode.MAIN, tag = EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_INFO)
    public void updateGroup(ChatGroupBean chatGroupBean) {
        for (MessageItemBeanV2 itemBeanV2 : mRootView.getListDatas()) {
            if (itemBeanV2.getEmKey().equals(chatGroupBean.getId())) {
                itemBeanV2.setChatGroupBean(chatGroupBean);
            }
        }
        mRootView.refreshData();

    }*/
}
