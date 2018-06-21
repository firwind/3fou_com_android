package com.zhiyicx.thinksnsplus.data.source.repository;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.StickTopAverageBean;
import com.zhiyicx.thinksnsplus.data.source.remote.CircleClient;
import com.zhiyicx.thinksnsplus.data.source.remote.DynamicClient;
import com.zhiyicx.thinksnsplus.data.source.remote.InfoMainClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.data.source.repository.i.IStickTopRepository;
import com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopContract;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_DYNAMIC;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_INFO;
import static com.zhiyicx.thinksnsplus.modules.wallet.sticktop.StickTopFragment.TYPE_POST;

/**
 * @Author Jliuer
 * @Date 2017/05/23/12:00
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class StickTopRepsotory implements IStickTopRepository {

    private DynamicClient mDynamicClient;
    private InfoMainClient mInfoMainClient;
    private CircleClient mCircleClient;


    @Inject
    public StickTopRepsotory(ServiceManager serviceManager) {
        mDynamicClient = serviceManager.getDynamicClient();
        mInfoMainClient = serviceManager.getInfoMainClient();
        mCircleClient = serviceManager.getCircleClient();
    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parentId, double amount, int day) {
        switch (type) {
            case TYPE_DYNAMIC:
                return mDynamicClient.stickTopDynamic(parentId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            case TYPE_INFO:
                return mInfoMainClient.stickTopInfo(parentId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            case TYPE_POST:
                return mCircleClient.stickTopPost(parentId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            default:
                return mDynamicClient.stickTopDynamic(parentId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        }

    }

    @Override
    public Observable<BaseJsonV2<Integer>> stickTop(String type, long parentId, long childId, double amount, int day) {
        switch (type) {
            case TYPE_DYNAMIC:
                return mDynamicClient.stickTopDynamicComment(parentId, childId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            case TYPE_INFO:
                return mInfoMainClient.stickTopInfoComment(parentId, childId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            case TYPE_POST:
                return mCircleClient.stickTopPostComment(parentId, childId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            default:
                return mDynamicClient.stickTopDynamicComment(parentId, childId, (long) amount, day)
                        .subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * @return
     */
    @Override
    public Observable<StickTopAverageBean> getDynamicAndCommentTopAverageNum() {
        return mDynamicClient.getDynamicAndCommentTopAverageNum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return
     */
    @Override
    public Observable<StickTopAverageBean> getInfoAndCommentTopAverageNum() {
        return mInfoMainClient.getInfoAndCommentTopAverageNum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @return
     */
    @Override
    public Observable<StickTopAverageBean> getCircleAndCommentTopAverageNum() {
        return mCircleClient.getCircleAndCommentTopAverageNum()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * @param type @link{#StickTopFragment.TYPE_DYNAMIC}
     * @return
     */
    @Override
    public Observable<StickTopAverageBean> getCircleAndCommentTopAverageNumByType(String type) {
        switch (type) {
            case TYPE_DYNAMIC:
                return getDynamicAndCommentTopAverageNum();
            case TYPE_INFO:
                return getInfoAndCommentTopAverageNum();
            case TYPE_POST:
                return getCircleAndCommentTopAverageNum();
            default:
                return getDynamicAndCommentTopAverageNum();
        }
    }
}
