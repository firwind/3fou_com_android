package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.base.BaseSubscriberV3;
import com.zhiyicx.thinksnsplus.data.beans.ExpandChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ExpandOfficialChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupParentBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.ChatInfoRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.functions.Func1;

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

    @Inject
    public NewMessageGroupPresenter(NewMessageGroupContract.View rootView) {
        super(rootView);
    }

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        addSubscrebe( mRootView.isOnlyOfficial()?
                mBaseMessageRepository.getOfficialGroupListV2().map(expandOfficialChatGroupBeans -> {
                    try {
                        //同步自己加入的群组，此api获取的群组sdk会自动保存到内存和db。
                        EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    } catch (HyphenateException e) {
                        //e.printStackTrace();
                    }
                    return expandOfficialChatGroupBeans;
                }).subscribe(getOfficialGroupListSubscriber(isLoadMore)) :
                mBaseMessageRepository.getGroupInfoOnlyGroupFaceV2().subscribe(getMySelfGroupListSubscriber(isLoadMore)));
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


    /**
     * 获取企业群聊的subscriber
     * @param isLoadMore
     * @return
     */
    private Subscriber getOfficialGroupListSubscriber(boolean isLoadMore){

        return new BaseSubscribeForV2<List<ExpandOfficialChatGroupBean>>(){

            @Override
            protected void onSuccess(List<ExpandOfficialChatGroupBean> data) {
                mRootView.dismissSnackBar();
                mRootView.hideStickyMessage();
                List<GroupParentBean> list = new ArrayList<>();
                for (ExpandOfficialChatGroupBean expandData:data) {
                    list.add(new GroupParentBean(expandData.getName(),expandData.getGroup()));
                }
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
                mRootView.showStickyMessage(mContext.getString(R.string.network_anomalies));
                mRootView.onResponseError(throwable, false);
            }
        };

    }


    /**
     * 获取自己所加入的群聊的Subscriber
     * @param isLoadMore
     * @return
     */
    private Subscriber getMySelfGroupListSubscriber(boolean isLoadMore){
        return new BaseSubscribeForV2<ExpandChatGroupBean>() {
            @Override
            protected void onSuccess(ExpandChatGroupBean data) {
                mRootView.dismissSnackBar();
                mRootView.hideStickyMessage();
                List<GroupParentBean> list = new ArrayList<>();
                GroupParentBean bean1 = new GroupParentBean("企业群聊",data.official);
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
                mRootView.showStickyMessage(mContext.getString(R.string.network_anomalies));
                mRootView.onResponseError(throwable, false);
            }
        };
    }

}
