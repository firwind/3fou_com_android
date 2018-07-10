package com.zhiyicx.thinksnsplus.data.source.repository;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoContract;
import com.zhiyicx.thinksnsplus.utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoRepository extends BaseFriendsRepository implements ChatInfoContract.Repository {

    @Inject
    UpLoadRepository mUpLoadRepository;

    @Inject
    public ChatInfoRepository(ServiceManager manager) {
        super(manager);
    }

    @Override
    public Observable<List<ChatGroupBean>> getGroupChatInfo(String groupId) {
        return mEasemobClient.getGroupInfo(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ChatGroupNewBean>> getGroupChatNewInfo(String groupId) {
        return mEasemobClient.getNewGroupInfo(groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> openBannedPost(String im_group_id, String user_id, String times, String members) {
        return mEasemobClient.openBannedPost(im_group_id, user_id, times, members)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> addGroupRole(String im_group_id, String admin_id, String admin_type) {
        return mEasemobClient.addGroupRole(im_group_id, admin_id, admin_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> removeBannedPost(String im_group_id, String user_id, String members) {
        return mEasemobClient.removeBannedPost(im_group_id, user_id, members)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UserInfoBean>> getGroupHankInfo(String im_group_id) {
        return mEasemobClient.getGroupHankInfo(im_group_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> removeRole(String im_group_id, String removeadmin, String admin_type) {
        return mEasemobClient.removeRole(im_group_id, removeadmin, admin_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> setStick(String stick_id, String author, int isStick) {
        Observable<String> observable;
        if (isStick == 0) {
            observable = mEasemobClient.setStick(stick_id, author);//设置置顶
        } else {
            observable = mEasemobClient.cancelStick(stick_id, author);//取消置顶
        }
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<StickBean>> refreshSticks(String author) {
        return mEasemobClient.getSticks(author)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<UpgradeTypeBean>> getUpgradeGroups() {
        Observable observable = Observable.create((Observable.OnSubscribe<List<UpgradeTypeBean>>) subscriber -> {

            String json = JsonUtils.getJson("upgradeGroup", AppApplication.getContext());
            Type listType = new TypeToken<List<UpgradeTypeBean>>() {
            }.getType();
            //这里的json是字符串类型 = jsonArray.toString();
            List<UpgradeTypeBean> typeBeans = new Gson().fromJson(json, listType);
            subscriber.onNext(typeBeans);
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        return observable;
    }

    @Override
    public Observable<String> upgradeGroup(String groupId, int type) {
        return mEasemobClient.upgradeGroup(groupId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> reportGroup(String userId, String groupId, String reason, String tel) {
        return mEasemobClient.reportGroup(userId, groupId, reason, tel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ChatGroupNewBean> getNewGroupInfoV2(String group_id) {
        return mEasemobClient.getNewGroupInfoV2(group_id);
    }

    @Override
    public Observable<List<UserInfoBean>> getUserInfoInfo(String group_id, String searchKey) {
        return mEasemobClient.getGroupUserInfoInfo(group_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
