package com.zhiyicx.thinksnsplus.modules.chat.record;

import android.text.TextUtils;
import android.util.SparseArray;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.common.config.ConstantConfig;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatRecord;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author: huwenyong
 * date: 2018/6/28 11:11
 * description:
 * version:
 */

@FragmentScoped
public class ChatRecordPresenter extends AppBasePresenter<ChatRecordContract.View> implements ChatRecordContract.Presenter {

    @Inject
    UserInfoRepository mUserInfoRepository;

    private Subscription searchSubscribe;

    private List<EMMessage> messageList = null;

    @Inject
    public ChatRecordPresenter(ChatRecordContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        if(null != searchSubscribe && !searchSubscribe.isUnsubscribed()){
            searchSubscribe.unsubscribe();
        }

        searchSubscribe = loadChatRecord().subscribe(new BaseSubscribeForV2<List<ChatRecord>>() {
            @Override
            protected void onSuccess(List<ChatRecord> data) {
                mRootView.onNetResponseSuccess(data, isLoadMore);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRootView.onResponseError(e, isLoadMore);
            }
        });
    }

    /**
     * ①筛选出有匹配信息的 List<EMMessage>
     * ②去拿到List<UserInfo>
     * ③合并 List<EMMessage>和List<UserInfo>
     * ④得到List<ChatRecord>
     *
     * @param maxId      当前获取到数据的最小时间
     * @param isLoadMore 加载状态，是否是加载更多
     */
    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ChatRecord> data, boolean isLoadMore) {
        return false;
    }

    /**
     * 加载聊天记录
     *
     * @return
     */
    private rx.Observable<List<ChatRecord>> loadChatRecord() {

        return rx.Observable.create(new rx.Observable.OnSubscribe<List<EMMessage>>() {
            @Override
            public void call(Subscriber<? super List<EMMessage>> subscriber) {
                if (null == messageList){
                    messageList = EMClient.getInstance().chatManager().getConversation(mRootView.getConversationId()).getAllMessages();
                    //反转，按照时间倒序
                    Collections.reverse(messageList);
                }
                List<EMMessage> searchedList = new ArrayList<>();
                if (TextUtils.isEmpty(mRootView.getSearchText())) {
                    searchedList = messageList;
                } else {
                    for (int i = 0; i < messageList.size(); i++) {
                        //只匹配文本
                        if (EMMessage.Type.TXT == messageList.get(i).getType()
                                && (((EMTextMessageBody) messageList.get(i).getBody()).getMessage())
                                .contains(mRootView.getSearchText())) {
                            searchedList.add(messageList.get(i));
                        }
                    }
                }
                subscriber.onNext(searchedList);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<List<EMMessage>, rx.Observable<List<ChatRecord>>>() {
            @Override
            public rx.Observable<List<ChatRecord>> call(List<EMMessage> emMessages) {
                //本地数据库没有用户信息的 Message
                /*List<EMMessage> usersNotInDbMessage = new ArrayList<>();
                //本地数据库没有用户信息的id
                List<String> usersNotInDbId = new ArrayList<>();
                //本地数据库有用户信息的记录
                List<ChatRecord> chatRecords = new ArrayList<>();

                //筛选出没有用户信息的，并将有用户信息的聊天记录存在 chatRecords 中
                for (int i = 0; i < emMessages.size(); i++) {
                    String id = emMessages.get(i).getFrom();
                    UserInfoBean userInfo = mUserInfoBeanGreenDao.getUserInfoById(id);
                    if(null == userInfo){
                        usersNotInDbMessage.add(emMessages.get(i));
                        //如果list中没有该用户id就去查找
                        if(usersNotInDbId.indexOf(id) == -1)
                            usersNotInDbId.add(id);
                    }else {
                        ChatRecord chatRecord = new ChatRecord();
                        chatRecord.setEmMessage(emMessages.get(i));
                        chatRecord.setUserInfo(userInfo);
                        chatRecords.add(chatRecord);
                    }
                }*/

                //去重，得到没有重复的id
                List<String> noRepeatIds = new ArrayList<>();
                //线程安全
                StringBuffer userIds = new StringBuffer();
                for (int i = 0; i < emMessages.size(); i++) {
                    //去掉群聊信息中的admin
                    if (!"admin".equals(emMessages.get(i).getFrom()) &&
                            -1 == noRepeatIds.indexOf(emMessages.get(i).getFrom())) {

                        noRepeatIds.add(emMessages.get(i).getFrom());

                        userIds.append(emMessages.get(i).getFrom());
                        userIds.append(ConstantConfig.SPLIT_SMBOL);
                    }
                }
                //删除最后一个分隔符
                if (userIds.length() > 1) {
                    userIds.deleteCharAt(userIds.length() - 1);
                }

                return mUserInfoRepository.getUserInfoWithOutLocalByIds(userIds.toString())
                        .map(new Func1<List<UserInfoBean>, List<ChatRecord>>() {
                            @Override
                            public List<ChatRecord> call(List<UserInfoBean> userInfoBeans) {

                                List<ChatRecord> chatRecords = new ArrayList<>();
                                //提高性能，使用SparseArray
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (int i = 0; i < emMessages.size(); i++) {
                                    ChatRecord chatRecord = new ChatRecord();
                                    chatRecord.setEmMessage(emMessages.get(i));
                                    try {
                                        chatRecord.setUserInfo(userInfoBeanSparseArray.get(
                                                ((Long) Long.parseLong(emMessages.get(i).getFrom())).intValue()));
                                    } catch (Exception e) {
                                        continue;
                                    }
                                    chatRecords.add(chatRecord);
                                }

                                return chatRecords;
                            }
                        });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

    }


}
