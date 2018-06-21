package com.zhiyicx.thinksnsplus.modules.home.message.messagelike;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.baseproject.widget.popwindow.PayPopWindow;
import com.zhiyicx.common.utils.TextViewUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.DigedBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.music_fm.music_album_detail.MusicDetailActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_FEED;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_MUSIC;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_LIKE_NEWS;
import static com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow.POPUPWINDOW_ALPHA;
import static com.zhiyicx.thinksnsplus.modules.home.message.messagecomment.MessageCommentAdapter.BUNDLE_SOURCE_ID;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE;
import static com.zhiyicx.thinksnsplus.modules.music_fm.music_comment.MusicCommentFragment.CURRENT_COMMENT_TYPE_MUSIC;

/**
 * @Describe 消息赞
 * @Author zl
 * @Date 2017/1/17
 * @Contact master.jungle68@gmail.com
 */
public class MessageLikeFragment extends TSListFragment<MessageLikeContract.Presenter, DigedBean>
        implements MessageLikeContract.View, MultiItemTypeAdapter.OnItemClickListener, TextViewUtils.OnSpanTextClickListener {
    private ActionPopupWindow mInstructionsPopupWindow;

    private PayPopWindow mPayImagePopWindow;

    public MessageLikeFragment() {
    }

    public static MessageLikeFragment newInstance() {
        MessageLikeFragment fragment = new MessageLikeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.liked);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected CommonAdapter<DigedBean> getAdapter() {
        MessageLikeAdapter adapter = new MessageLikeAdapter(getActivity(), R.layout.item_message_like_list, mListDatas) {
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
        adapter.setOnSpanTextClickListener(this);
        adapter.setOnItemClickListener(this);
        return adapter;
    }

    @Override
    protected int setEmptView() {
        return R.mipmap.img_default_nothing;
    }

    public void initInstructionsPop(int resDesStr) {
        if (mInstructionsPopupWindow != null) {
            mInstructionsPopupWindow = mInstructionsPopupWindow.newBuilder()
                    .desStr(getString(resDesStr))
                    .build();
            mInstructionsPopupWindow.show();
            return;
        }
        mInstructionsPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.instructions))
                .desStr(getString(resDesStr))
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .bottomClickListener(() -> mInstructionsPopupWindow.hide())
                .build();
        mInstructionsPopupWindow.show();
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
        if (mListDatas.get(position).getLikeable() == null) {
            initInstructionsPop(R.string.review_dynamic_deleted);
        } else {
            toDetail(mListDatas.get(position));
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
        return false;
    }

    /**
     * 根据不同的type 进入不同的 详情
     *
     * @param digedBean
     */
    private void toDetail(DigedBean digedBean) {
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_SOURCE_ID, digedBean.getLikeable_id());
        switch (digedBean.getLikeable_type()) {

            case APP_LIKE_FEED:
                intent = new Intent(getActivity(), DynamicDetailActivity.class);
                intent.putExtras(bundle);
                break;
            case APP_LIKE_MUSIC:
                intent = new Intent(getActivity(), MusicDetailActivity.class);
                bundle.putString(CURRENT_COMMENT_TYPE, CURRENT_COMMENT_TYPE_MUSIC);
                intent.putExtra(CURRENT_COMMENT, bundle);
                break;
            case APP_LIKE_NEWS:
                intent = new Intent(getActivity(), InfoDetailsActivity.class);
                intent.putExtra(BUNDLE_INFO, bundle);
                break;
            default:
                return;

        }
        getActivity().startActivity(intent);
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

    @Override
    public void paySuccess() {

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
                    mPresenter.payNote(dynamicPosition, amout, imagePosition, note, isImage);
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
