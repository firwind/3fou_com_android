package com.zhiyicx.thinksnsplus.modules.circle.create;

import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.CircleTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.circle.CreateCircleBean;
import com.zhiyicx.thinksnsplus.data.source.local.CircleTypeBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.information.infomain.container.InfoContainerFragment;

import java.util.List;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @Author Jliuer
 * @Date 2017/11/21/16:59
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CreateCirclePresenter extends AppBasePresenter<CreateCircleContract.View>
        implements CreateCircleContract.Presenter {

    @Inject
    CircleTypeBeanGreenDaoImpl mCircleTypeBeanGreenDao;

    @Inject
    BaseCircleRepository mBaseCircleRepository;

    @Inject
    public CreateCirclePresenter(CreateCircleContract.View rootView) {
        super(rootView);
    }

    @Override
    public String getCircleCategoryName(int category) {
        return mCircleTypeBeanGreenDao.getCategoryNameById(category);
    }

    @Override
    public void createCircle(CreateCircleBean createCircleBean) {
        Subscription subscription = mBaseCircleRepository.createCircle(createCircleBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CircleInfo>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CircleInfo> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        mRootView.setCircleInfo(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void updateCircle(CreateCircleBean createCircleBean) {
        Subscription subscription = mBaseCircleRepository.updateCircle(createCircleBean)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R.string.apply_doing)))
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<CircleInfo>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<CircleInfo> data) {
                        mRootView.showSnackSuccessMessage(data.getMessage().get(0));
                        mRootView.setCircleInfo(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }
                });
        addSubscrebe(subscription);
    }

    @Override
    public void getRule() {
        Subscription subscription = mBaseCircleRepository.getCircleRule()
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<String>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<String> data) {
                        mRootView.setCircleRule(data.getData());
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(throwable.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        mRootView.closeLoadingView();
                    }
                });

        addSubscrebe(subscription);
    }

    @Override
    public void getallCategroys() {
        mBaseCircleRepository.getCategroiesList(0, 0)
                .subscribe(new BaseSubscribeForV2<List<CircleTypeBean>>() {

                    @Override
                    protected void onSuccess(List<CircleTypeBean> data) {
                        // 创建默认推荐数据，服务器不反会，测试又要要，让我们自己加
                        CircleTypeBean circleTypeBean = new CircleTypeBean();
                        circleTypeBean.setId(Long.valueOf(InfoContainerFragment.RECOMMEND_INFO));
                        circleTypeBean.setName(mContext.getString(R.string.info_recommend));
                        data.add(circleTypeBean);
                        mCircleTypeBeanGreenDao.saveMultiData(data);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                    }
                });
    }
}
