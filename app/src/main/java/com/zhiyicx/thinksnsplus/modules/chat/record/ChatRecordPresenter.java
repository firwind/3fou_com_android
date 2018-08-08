package com.zhiyicx.thinksnsplus.modules.chat.record;

import android.text.TextUtils;
import android.util.SparseArray;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.baseproject.base.TSListFragment;
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
import rx.Scheduler;
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

    private final int PAGE_SIZE = 20;

    @Inject
    public ChatRecordPresenter(ChatRecordContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        EMConversation conversation = EMClient.getInstance()
                .chatManager().getConversation(mRootView.getConversationId());

        if (null != searchSubscribe && !searchSubscribe.isUnsubscribed()) {
            searchSubscribe.unsubscribe();
        }

        if(TextUtils.isEmpty(mRootView.getSearchText()) ){
            mRootView.onNetResponseSuccess(null,false);
            return;
        }
        searchSubscribe = Observable.create((Observable.OnSubscribe<List<EMMessage>>) subscriber -> {

            List<EMMessage> searchedList = new ArrayList<>();
            long timestamp = mRootView.getListDatas().size() == 0 ? -1 :
                    mRootView.getListDatas().get(mRootView.getListDatas().size()-1).getEmMessage().getMsgTime();
            searchedList.addAll(conversation.searchMsgFromDB(mRootView.getSearchText(),
                    timestamp, PAGE_SIZE,null, EMConversation.EMSearchDirection.UP));

            subscriber.onNext(searchedList);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(emMessages -> {
                    List<ChatRecord> chatRecordList = new ArrayList<>();
                    List<Object> notExitUsers = new ArrayList<>();
                    for (EMMessage message : emMessages) {
                        ChatRecord record = new ChatRecord();
                        record.setEmMessage(message);
                        try {
                            Long userId = Long.parseLong(message.getFrom());
                            UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                            if (null != userInfoBean)
                                record.setUserInfo(userInfoBean);
                            else
                                notExitUsers.add(userId);
                        } catch (Exception e) {
                            continue;
                        }
                        chatRecordList.add(record);
                    }

                    if (notExitUsers.size() == 0) {
                        return Observable.just(chatRecordList);
                    } else
                        return mUserInfoRepository.getUserInfo(notExitUsers)
                                .flatMap(userInfoBeans -> {
                                    //提高性能，使用SparseArray
                                    SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                    for (UserInfoBean userInfoBean : userInfoBeans) {
                                        userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                    }
                                    for (ChatRecord record : chatRecordList) {
                                        if (null == record.getUserInfo()) {
                                            try {
                                                record.setUserInfo(
                                                        userInfoBeanSparseArray.get(Integer.parseInt(record.getEmMessage().getFrom())));
                                            } catch (Exception e) {
                                                continue;
                                            }
                                        }
                                    }
                                    return Observable.just(chatRecordList);
                                });
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ChatRecord>>() {
                    @Override
                    protected void onSuccess(List<ChatRecord> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.onResponseError(null, isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                });
        addSubscrebe(searchSubscribe);
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

}
