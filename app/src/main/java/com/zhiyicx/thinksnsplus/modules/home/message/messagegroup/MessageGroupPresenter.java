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

        Subscription subscribe = mRootView.isOnlyOfficialGroup()?mBaseMessageRepository.getOfficialGroupInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
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
                })


                /*Observable.just(maxId.intValue())
                .subscribeOn(Schedulers.io())
                .flatMap((Func1<Integer, Observable<List<ChatGroupBean>>>) integer -> {
                    try {
                        List<EMGroup> grouplist = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                        if (grouplist != null && !grouplist.isEmpty()) {
                            StringBuilder builder = new StringBuilder();
                            for (EMGroup group : grouplist) {
                                builder.append(group.getGroupId());
                                builder.append(",");
                            }

                            //return mBaseMessageRepository.getSimpleGroupList(builder.toString(),mRootView.isOnlyOfficialGroup()?1:0);
                            //筛选出官方群
                            if(mRootView.isOnlyOfficialGroup()){
                                return mBaseMessageRepository.getGroupInfoOnlyGroupFace(builder.toString())
                                        .map(chatGroupBeans -> {
                                            List<ChatGroupBean> list = new ArrayList<>();
                                            for (int i = 0; i < chatGroupBeans.size(); i++) {
                                                if(1 == chatGroupBeans.get(i).getGroup_level())
                                                    list.add(chatGroupBeans.get(i));
                                            }
                                            return list;
                                        });
                            }else {
                                return null;
                            }
                        }
                        return Observable.just(new ArrayList<>());
                    } catch (HyphenateException e) {
                        return Observable.just(new ArrayList<>());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<List<ChatGroupBean>>() {
                    @Override
                    protected void onSuccess(List<ChatGroupBean> data) {
                        mRootView.onNetResponseSuccess(data, isLoadMore);
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
                })*/:mBaseMessageRepository.getGroupInfoOnlyGroupFaceV2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ChatGroupBean>() {
                    @SuppressLint("LogNotUsed")
                    @Override
                    protected void onSuccess(ChatGroupBean data) {
                        List<ChatGroupBean> ChatGroupBeans = new ArrayList<>();

                        for (int i = 0;i<3;i++){
                            ChatGroupBean bean = new ChatGroupBean();
                            switch (i) {
                                case 0:
                                    bean.setmParentName("官方群聊");
                                    bean.setmParentNum(data.getOfficial().size());
                                    bean.setTreeBeanList(data.getOfficial());
                                    break;
                                case 1:
                                    bean.setmParentName("热门群聊");
                                    bean.setmParentNum(data.getHot().size());
                                    bean.setTreeBeanList(data.getHot());
                                    break;
                                case 2:
                                    bean.setmParentName("自建群聊");
                                    bean.setmParentNum(data.getCommon().size());
                                    bean.setTreeBeanList(data.getCommon());
                                    break;
                            }
                            ChatGroupBeans.add(bean);
                        }
                        mRootView.onNetResponseSuccess(ChatGroupBeans, isLoadMore);
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
    public void checkGroupExist( ChatGroupBean groupBean ) {
        if (mGroupExistSubscription != null && !mGroupExistSubscription.isUnsubscribed()) {
            mGroupExistSubscription.unsubscribe();
        }
        mGroupExistSubscription = Observable.just(groupBean.getId())
                .subscribeOn(Schedulers.io())
                .flatMap(s -> {
                    EMGroup group = null;
                    try {
                        group = EMClient.getInstance().groupManager().getGroupFromServer(s);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                    }
                    if (group!=null){
                        return mBaseMessageRepository.getChickIsAddGroup(groupBean.getId())
                                .map(new Func1<ChatGroupBean, String>() {
                                    @Override
                                    public String call(ChatGroupBean s) {
                                        if (s.getIs_in() == 1){
                                            return "";
                                        }else {
                                            return "";/*mChatInfoRepository.addGroupMember(groupBean.getId(), String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()),groupBean.getGroup_level())
                                                    .map(new Func1<Object, String>() {
                                                        @Override
                                                        public String call(Object o) {
                                                            return "";
                                                        }
                                                    });*/
                                        }

                                    }
                                });
                    }else {
                        return Observable.just(null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @SuppressLint("LogNotUsed")
                    @Override
                    protected void onSuccess(String data) {

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
