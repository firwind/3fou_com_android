package com.zhiyicx.thinksnsplus.modules.feedback;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.Subscription;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/06/02/16:05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class FeedBackFragment extends TSFragment<FeedBackContract.Presenter> implements FeedBackContract.View {

    @BindView(R.id.et_feedback_content)
    UserInfoInroduceInputView mEtDynamicContent;
    @BindView(R.id.tv_feedback_contract)
    EditText mTvFeedbackContract;
    Subscription subscription;

    private ActionPopupWindow mFeedBackInstructionsPopupWindow;

    public static FeedBackFragment newInstance() {
        return new FeedBackFragment();
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.feed_back);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.submit);
    }

    @Override
    protected void initView(View rootView) {
        initRightSubmit();
    }

    @Override
    protected void initData() {

    }

    @Override
    public void showSnackSuccessMessage(String message) {
        super.showSnackSuccessMessage(message);
        subscription = Observable.timer(1, TimeUnit.SECONDS).subscribe(aLong -> getActivity().finish());


    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_feedback;
    }

    private void initRightSubmit() {
        mToolbarRight.setEnabled(false);

//        Observable.combineLatest(RxTextView.textChanges(mTvFeedbackContract), RxTextView.textChanges(mEtDynamicContent.getEtContent()),
//                (charSequence, charSequence2) -> charSequence.toString().replaceAll(" ", "").length() > 0 && charSequence2.toString().replaceAll
// (" ", "").length() > 0)
//                .subscribe(enable -> mToolbarRight.setEnabled(enable), throwable -> throwable.printStackTrace());

        RxTextView.textChanges(mEtDynamicContent.getEtContent())
                .map(charSequence -> !TextUtils.isEmpty(charSequence.toString().trim())).subscribe(aBoolean ->
                mToolbarRight.setEnabled(aBoolean)
        );

        RxView.clicks(mToolbarRight)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .compose(this.bindToLifecycle())
                .subscribe(aVoid -> {
                    DeviceUtils.hideSoftKeyboard(getContext(), mEtDynamicContent);
                    mPresenter.submitFeedBack(mEtDynamicContent.getInputContent(), mTvFeedbackContract.getText().toString());
                });
    }

    @Override
    public void showWithdrawalsInstructionsPop() {
        if (mFeedBackInstructionsPopupWindow != null) {
            mFeedBackInstructionsPopupWindow.show();
            return;
        }
        mFeedBackInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.input_instructions))
                .desStr(getString(R.string.input_instructions_detail))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mFeedBackInstructionsPopupWindow.hide())
                .build();
        mFeedBackInstructionsPopupWindow.show();
    }

    @Override
    public void onDestroyView() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroyView();
    }
}
