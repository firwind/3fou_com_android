package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
 * @Author Jliuer
 * @Date 2018/05/03/15:47
 * @Email Jliuer@aliyun.com
 * @Description
 */
@FragmentScoped
public class MessageGroupPresenter extends AppBasePresenter<MessageGroupContract.View>
        implements MessageGroupContract.Presenter {

    @Inject
    BaseMessageRepository mBaseMessageRepository;
    @Inject
    ChatInfoRepository mChatInfoRepository;
    private Subscription mGroupExistSubscription;

    @Inject
    public MessageGroupPresenter(MessageGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        // 搜索时的刷新，只处理本地数据
        if (!TextUtils.isEmpty(mRootView.getsearchKeyWord())) {
            mRootView.hideRefreshState(isLoadMore);
            return;
        }

        Subscription subscribe = mBaseMessageRepository.getOfficialGroupInfo()
                .map(chatGroupBeans -> {
                    try {
                        //同步自己加入的群组，此api获取的群组sdk会自动保存到内存和db。
                        EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    } catch (HyphenateException e) {
                        //e.printStackTrace();
                    }
                    return chatGroupBeans;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
                        mRootView.hideStickyMessage();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showStickyMessage(message);
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showStickyMessage(mContext.getString(R.string.chat_unconnected));
                        mRootView.onResponseError(throwable, false);
                    }
                });

        addSubscrebe(subscribe);
    }

    @Override
    public void checkGroupExist(ChatGroupBean groupBean) {
        if (mGroupExistSubscription != null && !mGroupExistSubscription.isUnsubscribed()) {
            mGroupExistSubscription.unsubscribe();
        }
        mGroupExistSubscription = Observable.just(groupBean.getId())
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
//                        EMGroup group = null;
//                        try {
//                            group = EMClient.getInstance().groupManager().getGroupFromServer(s);
//                        } catch (HyphenateException e) {
//                            e.printStackTrace();
//                        }
//                        if (group != null) {
                            try {
                                List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();//需异步处理
                                return Observable.just(grouplist).flatMap(new Func1<List<EMGroup>, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(List<EMGroup> emGroups) {
                                        boolean isAddGroup = false;
                                        for (EMGroup emGroup : emGroups) {
                                            String groupId = emGroup.getGroupId();
                                            if (groupBean.getId().equals(groupId)) {
                                                isAddGroup = true;
                                                break;
                                            } else {
                                                isAddGroup = false;
                                            }
                                        }
                                        if (isAddGroup) {
                                            return Observable.just(groupBean.getId());
                                        } else {
                                            return mChatInfoRepository.addGroupMember(groupBean.getId(),
                                                    String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()), groupBean.getGroup_level())
                                                    .flatMap(new Func1<Object, Observable<String>>() {
                                                        @Override
                                                        public Observable<String> call(Object o) {
                                                            return Observable.just(groupBean.getId());
                                                        }
                                                    });
                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showSnackErrorMessage("获取群组列表失败");
                                return Observable.just(groupBean.getId());
                            }
//                        } else {
//                            return Observable.just(groupBean.getId());
//                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @SuppressLint("LogNotUsed")
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.checkGroupExist(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);

                        mRootView.showStickyMessage(message);
                        mRootView.onResponseError(null, false);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);

                        mRootView.showStickyMessage(mContext.getString(R.string.chat_unconnected));
                        mRootView.onResponseError(throwable, false);
                    }
                });
        addSubscrebe(mGroupExistSubscription);

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {

    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<ChatGroupBean> data, boolean isLoadMore) {
        mBaseMessageRepository.saveChatGoup(data);
        return false;
    }
}
