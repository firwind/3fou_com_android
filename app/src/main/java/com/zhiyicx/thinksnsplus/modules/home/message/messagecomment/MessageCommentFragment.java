package com.zhiyicx.thinksnsplus.modules.home.message.messagecomment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.InputLimitView;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.UIUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Describe 消息评论
 * @Author zl
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageCommentFragment extends TSListFragment<MessageCommentContract.Presenter,
        CommentedBean> implements MessageCommentContract.View, InputLimitView
        .OnSendClickListener, MultiItemTypeAdapter.OnItemClickListener, TextViewUtils.OnSpanTextClickListener {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_toolbar_center)
    TextView mTvToolBarCenter;
    @BindView(R.id.tv_toolbar_left)
    TextView mTvToolBarLeft;
    @BindView(R.id.tv_toolbar_right)
    TextView mTvToolBarRight;
    @BindView(R.id.ilv_comment)
    InputLimitView mIlvComment;
    @BindView(R.id.v_shadow)
    View mVShadow;
    private long mReplyUserId;// 被评论者的 id ,评论动态 id = 0
    private int mCurrentPostion;// 当前点击的 item 位置

    private PayPopWindow mPayImagePopWindow;

    public static MessageCommentFragment newInstance(Bundle bundle) {
        MessageCommentFragment fragment = new MessageCommentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void paySuccess() {

    }

    @Override
    protected boolean showToolbar() {
        return false;
    }

    @Override
    protected boolean showToolBarDivider() {
        return false;
    }

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_list_with_input_and_toolbar;
    }

    @Override
    protected String setCenterTitle() {
        return super.setCenterTitle();
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        initToolbar();
        initInputView();
    }

    @Override
    protected CommonAdapter<CommentedBean> getAdapter() {
        MessageCommentAdapter commonAdapter = new MessageCommentAdapter(mActivity, R.layout
                .item_message_comment_list, mListDatas) {
            @Override
            public int getStartPosition() {
                if (mPresenter.getSystemConfigBean() == null) {
                    return 10;
                }
                if (mPresenter.getSystemConfigBean().getFeed() == null) {
                    return 10;
                }
                return mPresenter.getSystemConfigBean().getFeed().getLimit();
            }
        };
        commonAdapter.setOnSpanTextClickListener(this);
        commonAdapter.setOnItemClickListener(this);
        return commonAdapter;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    private void initToolbar() {
        mToolbar.setBackgroundResource(R.color.white);
        mToolbar.setPadding(0, setUseStatusView() ? 0 : DeviceUtils.getStatuBarHeight(getContext
                ()), 0, 0);
        mTvToolBarCenter.setText(R.string.critical);
        mTvToolBarLeft.setCompoundDrawables(UIUtils.getCompoundDrawables(getContext(), setLeftImg
                ()), null, null, null);
        mTvToolBarLeft.setOnClickListener(v -> setLeftClick());
    }

    private void initInputView() {
        mVShadow.setOnClickListener(v -> closeInputView());
        RxView.clicks(mVShadow)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> closeInputView());
        mIlvComment.setOnSendClickListener(this);
    }

    @Override
    public void onSendClick(View v, String text) {
        DeviceUtils.hideSoftKeyboard(getContext(), v);
        mIlvComment.setVisibility(View.GONE);
        mVShadow.setVisibility(View.GONE);
        mPresenter.sendComment(mCurrentPostion, mReplyUserId, text);
    }

    @Override
    public void closeInputView() {
        if (mIlvComment.getVisibility() == View.VISIBLE) {
            mIlvComment.setVisibility(View.GONE);
            DeviceUtils.hideSoftKeyboard(getActivity(), mIlvComment.getEtContent());
        }
        mVShadow.setVisibility(View.GONE);
    }

    public void showCommentView() {
        // 评论
        mIlvComment.setVisibility(View.VISIBLE);
        mIlvComment.setSendButtonVisiable(true);
        mIlvComment.getFocus();
        mVShadow.setVisibility(View.VISIBLE);
        DeviceUtils.showSoftKeyboard(getActivity(), mIlvComment.getEtContent());
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        // 过滤自己的 or 内容被删除的
        if (mListDatas.get(position).getUser_id() == AppApplication.getmCurrentLoginAuth()
                .getUser_id() || mListDatas.get(position).getIsDelete()) {

        } else {
            mReplyUserId = mListDatas.get(position).getUser_id();
            mCurrentPostion = position;
            showCommentView();
            String contentHint = getString(R.string.reply, mListDatas.get(position)
                    .getCommentUserInfo().getName());
            mIlvComment.setEtContentHint(contentHint);
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    @Override
    public void setSpanText(int position, int note, long amount, TextView view, boolean canNotRead) {
        if (note == 0) {
            showSnackErrorMessage("完善中..");
            return;
        }
        position -= mHeaderAndFooterWrapper.getHeadersCount();
        initImageCenterPopWindow(position, position, amount,
                note, R.string.buy_pay_words_desc, false);
    }

    /**
     * @param dynamicPosition 动态位置
     * @param imagePosition   图片位置
     * @param amout           费用
     * @param note            支付节点
     * @param strRes          文字说明
     * @param isImage         是否是图片收费
     */
    private void initImageCenterPopWindow(final int dynamicPosition, final int imagePosition,
                                          long amout,
                                          final int note, int strRes, final boolean isImage) {

        mPayImagePopWindow = PayPopWindow.builder()
                .with(getActivity())
                .isWrap(true)
                .isFocus(true)
                .isOutsideTouch(true)
                .buildLinksColor1(R.color.themeColor)
                .buildLinksColor2(R.color.important_for_content)
                .contentView(R.layout.ppw_for_center)
                .backgroundAlpha(POPUPWINDOW_ALPHA)
                .buildDescrStr(String.format(getString(strRes) + getString(R
                        .string.buy_pay_member), amout, mPresenter.getGoldName()))
                .buildLinksStr(getString(R.string.buy_pay_member))
                .buildTitleStr(getString(R.string.buy_pay))
                .buildItem1Str(getString(R.string.buy_pay_in))
                .buildItem2Str(getString(R.string.buy_pay_out))
                .buildMoneyStr(String.format(getString(R.string.buy_pay_integration), amout))
                .buildCenterPopWindowItem1ClickListener(() -> {
                    mPresenter.payNote(dynamicPosition,amout, imagePosition, note, isImage);
                    mPayImagePopWindow.hide();
                })
                .buildCenterPopWindowItem2ClickListener(() -> mPayImagePopWindow.hide())
                .buildCenterPopWindowLinkClickListener(new PayPopWindow
                        .CenterPopWindowLinkClickListener() {
                    @Override
                    public void onLongClick() {

                    }

                    @Override
                    public void onClicked() {

                    }
                })
                .build();
        mPayImagePopWindow.show();

    }
}
