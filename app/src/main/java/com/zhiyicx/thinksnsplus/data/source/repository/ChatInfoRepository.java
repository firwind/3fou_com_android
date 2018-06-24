package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.chat.info.ChatInfoContract;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
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
        return mEasemobClient.removeBannedPost(im_group_id,user_id,members)
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
        return mEasemobClient.removeRole(im_group_id,removeadmin,admin_type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
