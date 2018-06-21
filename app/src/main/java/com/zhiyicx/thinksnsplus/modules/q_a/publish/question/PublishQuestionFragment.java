package com.zhiyicx.thinksnsplus.modules.q_a.publish.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.rxbinding.widget.RxTextView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.QAPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.qa.QAListInfoBean;
import com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity;
import com.zhiyicx.thinksnsplus.modules.q_a.publish.detail.EditeQuestionDetailActivity;
import com.zhiyicx.thinksnsplus.widget.UserInfoInroduceInputView;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.jetbrains.annotations.NotNull;
import org.simple.eventbus.Subscriber;

import java.util.List;

import butterknife.BindView;

import static com.zhiyicx.thinksnsplus.modules.q_a.detail.question.QuestionDetailActivity.BUNDLE_QUESTION_BEAN;


/**
 * @Describe
 * @Author zl
 * @Date 2017/7/25
 * @Contact master.jungle68@gmail.com
 */
public class PublishQuestionFragment extends TSListFragment<PublishQuestionContract.Presenter, QAListInfoBean>
        implements PublishQuestionContract.View, MultiItemTypeAdapter.OnItemClickListener {

    public static final String BUNDLE_PUBLISHQA_BEAN = "publish_bean";
    public static final String BUNDLE_PUBLISHQA_TOPIC = "publish_topic_id";

    @BindView(R.id.et_qustion)
    UserInfoInroduceInputView mEtQustion;

    @BindView(R.id.line)
    View mLine;

    private String mQuestionStr = "";

    /**
     * 退出编辑警告弹框
     */
    private ActionPopupWindow mEditWarningPopupWindow;

    public static QAPublishBean gDraftQuestion;

    public static PublishQuestionFragment newInstance(Bundle args) {
        PublishQuestionFragment fragment = new PublishQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_publish_qustion;
    }

    @Override
    protected int setLeftImg() {
        return 0;
    }

    @Override
    protected void setLeftClick() {
        onBackPressed();
    }

    @Override
    protected String setLeftTitle() {
        return getString(R.string.cancel);
    }

    @Override
    protected String setRightTitle() {
        return getString(R.string.qa_publish_next);
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.qa_publish);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected void setRightClick() {
        boolean isCorrectFormat = (mQuestionStr.endsWith("?") || mQuestionStr.endsWith("？")) && mQuestionStr.length() > 1;
        if (isCorrectFormat) {
            addTopic();
        } else {
            showSnackErrorMessage(getString(R.string.qa_publish_title_hint));
        }
    }

    private void addTopic() {
        mPresenter.checkQuestionConfig();
        Intent intent = new Intent(getActivity(), EditeQuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        saveQuestion();
        bundle.putParcelable(BUNDLE_PUBLISHQA_BEAN, gDraftQuestion);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void saveQuestion() {
        if (gDraftQuestion == null) {
            gDraftQuestion = new QAPublishBean();
            String mark = AppApplication.getmCurrentLoginAuth().getUser_id() + "" + System
                    .currentTimeMillis();
            gDraftQuestion.setCreated_at(TimeUtils.getCurrenZeroTimeStr());
            gDraftQuestion.setMark(Long.parseLong(mark));
        }
        gDraftQuestion.setSubject(mQuestionStr);
        mPresenter.saveQuestion(gDraftQuestion);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gDraftQuestion = getArguments().getParcelable(BUNDLE_PUBLISHQA_BEAN);
        }
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        if (gDraftQuestion != null) {
            mEtQustion.setText(gDraftQuestion.getSubject());
        }
        initListener();
    }

    @Override
    protected boolean isNeedRefreshAnimation() {
        return false;
    }

    @Override
    protected void requestNetData(Long maxId, boolean isLoadMore) {
        requestNetData(null, maxId, "all", isLoadMore);
    }

    private void requestNetData(String subject, Long maxId, String type, boolean isLoadMore) {
        if (TextUtils.isEmpty(mQuestionStr)) {
            return;
        }
        mPresenter.requestNetData(subject, maxId, type, isLoadMore);
    }

    @Override
    public void onNetResponseSuccess(@NotNull List<QAListInfoBean> data, boolean isLoadMore) {
        super.onNetResponseSuccess(data, isLoadMore);
        // 当搜索结果为空时保证空 view 不出现
        if (data.isEmpty()) {
            setEmptyViewVisiable(false);
        }
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        PublishQuestionAdapter adapter = new PublishQuestionAdapter(getContext(), R.layout
                .item_publish_question, mListDatas);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_QUESTION_BEAN, mListDatas.get(position));
        intent.putExtra(BUNDLE_QUESTION_BEAN, bundle);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /**
     * 初始化图片选择弹框
     */
    private void initEditWarningPop() {
        DeviceUtils.hideSoftKeyboard(getContext(), mEtQustion);
        boolean canSaveDraft = (gDraftQuestion != null && !gDraftQuestion.isHasAgainEdite()) ||
                gDraftQuestion == null;
        if (mEditWarningPopupWindow != null) {
            return;
        }
        mEditWarningPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.edit_quit))
                .item2Str(getString(canSaveDraft ? R.string.save_to_draft_box : R.string.empty))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item1ClickListener(() -> {
                    gDraftQuestion = null;
                    mEditWarningPopupWindow.hide();
                    getActivity().finish();
                })
                .item2ClickListener(() -> {
                    saveQuestion();
                    mEditWarningPopupWindow.hide();
                    gDraftQuestion = null;
                    getActivity().finish();
                })
                .bottomClickListener(() -> mEditWarningPopupWindow.hide()).build();
    }

    private void initListener() {
        RxTextView.textChanges(mEtQustion.getEtContent())
                .subscribe(charSequence -> {
                    mQuestionStr = charSequence.toString().trim();
                    mToolbarRight.setEnabled(!TextUtils.isEmpty(mQuestionStr));
                    requestNetData(mQuestionStr, 0L, "all", false);
                }, throwable -> mToolbarRight.setEnabled(false));
    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(mQuestionStr)) {
            super.setLeftClick();
        } else {
            initEditWarningPop();
            mEditWarningPopupWindow.show();
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Subscriber(tag = EventBusTagConfig.EVENT_PUBLISH_QUESTION)
    public void onPublishQuestionSuccess(Bundle bundle) {
        // 发布成功后关闭这个页面
        getActivity().finish();
    }
}
