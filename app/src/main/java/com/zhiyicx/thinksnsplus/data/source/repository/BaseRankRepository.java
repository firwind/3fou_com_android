package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.SharePreferenceUtils;
import com.zhiyicx.thinksnsplus.config.SharePreferenceTagConfig;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.remote.RankClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/23
 * @contact email:648129313@qq.com
 */

public class BaseRankRepository implements IBaseRankRepository {

    protected RankClient mRankClient;
    @Inject
    Application mContext;

    @Inject
    public BaseRankRepository(ServiceManager manager) {
        mRankClient = manager.getRankClien();
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankFollower(int size) {
        return mRankClient.getRankFollower(TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankRiches(int size) {
        return mRankClient.getRankRiches(TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankIncome(int size) {
        return mRankClient.getRankIncome(TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankCheckIn(int size) {
        SystemConfigBean systemConfigBean = SharePreferenceUtils.getObject(mContext, SharePreferenceTagConfig
                .SHAREPREFERENCE_TAG_SYSTEM_BOOTSTRAPPERS);
        // 如果已经签到了，则不再展示签到
        if (systemConfigBean != null && systemConfigBean.isCheckin()) {
            return mRankClient.getRankCheckIn(TSListFragment.DEFAULT_PAGE_SIZE, size);
        } else {
            return Observable.just(new ArrayList<>());
        }
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankQuestionExpert(int size) {
        return mRankClient.getRankQuestionExpert(TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankQuestionLikes(int size) {
        return mRankClient.getRankQuestionLikes(TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param type
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankAnswer(String type, int size) {
        return mRankClient.getRankAnswer(type, TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param type
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankDynamic(String type, int size) {
        return mRankClient.getRankDynamic(type, TSListFragment.DEFAULT_PAGE_SIZE, size);
    }

    /**
     * @param type
     * @param size
     * @return
     */
    @Override
    public Observable<List<UserInfoBean>> getRankInfo(String type, int size) {
        return mRankClient.getRankInfo(type, TSListFragment.DEFAULT_PAGE_SIZE, size);
    }
}
