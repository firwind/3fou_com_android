package com.zhiyicx.thinksnsplus.modules.home.mine.friends;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.hyphenate.easeui.bean.ChatVerifiedBean;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.UserFollowerCountBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.beans.event.TSNewFriendsEvent;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseFriendsRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UserInfoRepository;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/22
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class MyFriendsListPresenter extends AppBasePresenter<MyFriendsListContract.View>
        implements MyFriendsListContract.Presenter {

    @Inject
    BaseFriendsRepository mBaseFriendsRepository;

    @Inject
    UserInfoRepository mUserInfoRepository;

    @Inject
    public MyFriendsListPresenter(MyFriendsListContract.View rootView) {
        super(rootView);
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        Subscription subscription;

        BaseSubscribeForV2 subscribeForV2 = new BaseSubscribeForV2<List<UserInfoBean>>() {
            @Override
            protected void onSuccess(List<UserInfoBean> data) {
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
        };

        if (mRootView.isHomePage()) {

            subscription = mBaseFriendsRepository.getFriendIdList()
                    .flatMap(ids -> {
                        if (ids.isEmpty()) {
                            return Observable.just(new ArrayList<UserInfoBean>());
                        } else {
                            return mUserInfoRepository.getUserInfoWithOutLocalByIds(ids);
                        }
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscribeForV2);

        } else {
            subscription = mBaseFriendsRepository.getUserFriendsList(maxId, "")
                    .subscribe(subscribeForV2);
            if (!isLoadMore)//只有当刷新和当前界面仅仅是获取好友列表的时候才清空
                addSubscrebe(mUserInfoRepository.clearUserMessageCount(UserFollowerCountBean.UserBean.MESSAGE_TYPE_FRIEND)
                        .subscribe(new BaseSubscribeForV2<Object>() {
                            @Override
                            protected void onSuccess(Object data) {
                            }
                        }));
        }

        addSubscrebe(subscription);

    }


    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        //获取本地相互关注的列表
        //List<UserInfoBean> followFansBeanList = mUserInfoBeanGreenDao.getUserFriendsList(maxId);
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<UserInfoBean> data, boolean isLoadMore) {
        mUserInfoBeanGreenDao.saveMultiData(data);
        return true;
    }

    /*@Override
    public List<ChatUserInfoBean> getChatUserList(UserInfoBean userInfoBean) {
        List<ChatUserInfoBean> list = new ArrayList<>();
        list.add(getChatUser(mUserInfoBeanGreenDao.getSingleDataFromCache(AppApplication.getMyUserIdWithdefault())));
        list.add(getChatUser(userInfoBean));
        return list;
    }*/

    @Override
    public void deleteFriend(int index, UserInfoBean userInfoBean) {
        mBaseFriendsRepository.deleteFriend(String.valueOf(userInfoBean.getUser_id()))
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage("请稍后..."))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriberV3<String>(mRootView) {
                    @Override
                    protected void onSuccess(String data) {
                        super.onSuccess(data);
                        mRootView.deleteFriendOk(index, userInfoBean);
                    }
                });
    }

    /*private ChatUserInfoBean getChatUser(UserInfoBean userInfoBean) {
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
    }*/

    @Subscriber(tag = EventBusTagConfig.EVENT_NEW_FRIEND_LIST)
    public void onNewFriendList(TSNewFriendsEvent event) {

        if(!mRootView.isHomePage())
            return;

        if (mRootView.getListDatas().isEmpty()) {
            requestNetData(0L, false);
            return;
        }

        for (int i = 0; i < event.getFriends().size(); i++) {
            LogUtils.d("***hashcode:"+MyFriendsListPresenter.this+"******userId:"+event.getFriends().get(i)+"*********");
        }

        //找出不存在本地列表的friend id
        List<Long> newFriendList = new ArrayList<>();
        List<UserInfoBean> friendList = mRootView.getListDatas();
        for (Long userId : event.getFriends()) {
            boolean isExit = false;
            for (UserInfoBean user : friendList) {
                if (null != userId && userId.toString().equals(user.getUser_id().toString())) {
                    isExit = true;
                    break;
                }
            }
            if (!isExit)
                newFriendList.add(userId);
        }

        if (!newFriendList.isEmpty()) {
            Observable.just(newFriendList)
                    .flatMap(ids -> mUserInfoRepository.getUserInfoWithOutLocalByIds(ids))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscribeForV2<List<UserInfoBean>>() {
                        @Override
                        protected void onSuccess(List<UserInfoBean> data) {

                            mRootView.getListDatas().addAll(0,data);
                            mRootView.refreshData();
                        }
                    });
        }

    }


}
