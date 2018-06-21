package com.zhiyicx.thinksnsplus.modules.q_a.reward;

import com.google.gson.Gson;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseQARepository;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.inject.Inject;

import rx.Subscription;

/**
 * @author Catherine
 * @describe 问题悬赏界面
 * @date 2017/7/25
 * @contact email:648129313@qq.com
 */
@FragmentScoped
public class QARewardPresenter extends AppBasePresenter<QARewardContract.View>
        implements QARewardContract.Presenter {


    @Inject
    BaseQARepository mBaseQARepository;

    @Inject
    public QARewardPresenter( QARewardContract.View rootView) {
        super( rootView);
    }

    @Override
    public void publishQuestion(final QAPublishBean qaPublishBean) {
        Subscription subscribe = handleIntegrationBlance((long) qaPublishBean.getAmount())
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.publish_doing)))
                .flatMap(o -> qaPublishBean.isHasAgainEdite() ? mBaseQARepository.updateQuestion(qaPublishBean)
                        : mBaseQARepository.publishQuestion(qaPublishBean))
                .subscribe(new BaseSubscribeForV2<Object>() {
                    @Override
                    protected void onSuccess(Object data) {
                        // 解析数据，在跳转到问题详情页时需要用到
                        if (data == null) {
                            QAListInfoBean qaListInfoBean = new QAListInfoBean();
                            qaListInfoBean.setId(qaPublishBean.getId());
                            qaListInfoBean.setUser_id(AppApplication.getMyUserIdWithdefault());
                            mRootView.publishQuestionSuccess(qaListInfoBean);
                            mRootView.showSnackMessage("编辑成功", Prompt.DONE);
                        } else {
                            try {
                                JSONObject jsonObject = new JSONObject(new Gson().toJson(data));
                                QAListInfoBean qaListInfoBean = new Gson().fromJson
                                        (jsonObject.getString("question"), QAListInfoBean.class);
                                mRootView.publishQuestionSuccess(qaListInfoBean);
                                JSONArray array = jsonObject.getJSONArray("message");
                                mRootView.showSnackMessage(array.getString(0), Prompt.DONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                                mRootView.showSnackErrorMessage(e.toString());
                            }
                        }
                        mBaseQARepository.deleteQuestion(qaPublishBean);
                        qaPublishBean.setMark(qaPublishBean.getMark() - 1);
                        mBaseQARepository.deleteQuestion(qaPublishBean);
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.bill_doing_fialed));
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public void resetReward(Long question_id, double amount) {


        Subscription subscribe = handleIntegrationBlance((long) amount)
                .doOnSubscribe(() -> mRootView.showSnackLoadingMessage(mContext.getString(R
                        .string.ts_pay_check_handle_doing)))
                .flatMap(o -> mBaseQARepository.resetReward(question_id, amount))
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<Object>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<Object> data) {
                        mRootView.showSnackSuccessMessage(mContext.getString(R.string.qa_reset_reward_success));
                        mRootView.resetRewardSuccess();
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        if (isIntegrationBalanceCheck(throwable)) {
                            return;
                        }
                        mRootView.dismissSnackBar();
                    }
                });
        addSubscrebe(subscribe);
    }

    @Override
    public QAPublishBean getDraftQuestion(long qestion_mark) {
        return mBaseQARepository.getDraftQuestion(qestion_mark);
    }

    @Override
    public void saveQuestion(QAPublishBean qestion) {
        mBaseQARepository.saveQuestion(qestion);
    }
}
