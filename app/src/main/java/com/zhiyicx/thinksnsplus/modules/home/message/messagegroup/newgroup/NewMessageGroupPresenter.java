package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
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
public class NewMessageGroupPresenter extends AppBasePresenter<NewMessageGroupContract.View>
        implements NewMessageGroupContract.Presenter {

    @Inject
    BaseMessageRepository mBaseMessageRepository;
    @Inject
    ChatInfoRepository mChatInfoRepository;
    private Subscription mGroupExistSubscription;

    @Inject
    public NewMessageGroupPresenter(NewMessageGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {
        // 搜索时的刷新，只处理本地数据
        if (!TextUtils.isEmpty(mRootView.getsearchKeyWord())) {
            mRootView.hideRefreshState(isLoadMore);
            return;
        }

        Subscription subscribe = mBaseMessageRepository.getGroupInfoOnlyGroupFaceV2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<ExpandChatGroupBean>() {
                    @Override
                    protected void onSuccess(ExpandChatGroupBean data) {
                        List<GroupParentBean> list = new ArrayList<>();
                        GroupParentBean bean1 = new GroupParentBean("官方群聊",data.official);
                        GroupParentBean bean2 = new GroupParentBean("热门群聊",data.hot);
                        GroupParentBean bean3 = new GroupParentBean("自建群聊",data.common);
                        list.add(bean1);
                        list.add(bean2);
                        list.add(bean3);
                        mRootView.onNetResponseSuccess(list, isLoadMore);
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
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        EMGroup group = null;
                        try {
                            group = EMClient.getInstance().groupManager().getGroupFromServer(s);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                        if (group!=null){
                            return mBaseMessageRepository.getChickIsAddGroup(groupBean.getId())
                                    .flatMap(new Func1<ChatGroupBean, Observable<String>>() {
                                        @Override
                                        public Observable<String> call(ChatGroupBean chatGroupBean) {
                                            if(chatGroupBean.getIs_in() == 1){
                                                return Observable.just(groupBean.getId());
                                            }else {
                                                return mChatInfoRepository.addGroupMember(groupBean.getId(),
                                                        String.valueOf(AppApplication.getmCurrentLoginAuth().getUser_id()),groupBean.getGroup_level())
                                                        .flatMap(new Func1<Object, Observable<String>>() {
                                                            @Override
                                                            public Observable<String> call(Object o) {
                                                                return Observable.just(groupBean.getId());
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }else {
                            return Observable.just(groupBean.getId());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<String>() {
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
    public boolean insertOrUpdateData(@NotNull List<GroupParentBean> data, boolean isLoadMore) {

        if(null != data){
            for (int i = 0; i < data.size(); i++) {
                mBaseMessageRepository.saveChatGoup(data.get(i).childs);
            }
        }
        return false;
    }

}
