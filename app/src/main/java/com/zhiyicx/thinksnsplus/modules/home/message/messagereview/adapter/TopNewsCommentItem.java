package com.zhiyicx.thinksnsplus.modules.home.message.messagereview.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/09/11/16:16
 * @Email Jliuer@aliyun.com
 * @Description 资讯评论置顶审核
 */
public class TopNewsCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopNewsCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopNewsCommentListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int position, int itemCounts) {
        TopNewsCommentListBean dynamicCommentBean = (TopNewsCommentListBean) baseListBean;
        ImageUtils.loadCircleUserHeadPic(dynamicCommentBean.getCommentUser(), holder.getView(R.id.iv_headpic));

        boolean newsIsDeleted = dynamicCommentBean.getNews() == null;
        boolean commentIsDeleted = dynamicCommentBean.getComment() == null;

        boolean hasImage = !newsIsDeleted && dynamicCommentBean.getNews().getImage() != null;

        TextView reviewFlag = holder.getTextView(R.id.tv_review);
        TextView payNum = holder.getTextView(R.id.tv_pay_num);
        if (dynamicCommentBean.getState() == TopNewsCommentListBean.TOP_REVIEW) {
            reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.dyanmic_top_flag));
            reviewFlag.setText(holder.itemView.getResources().getString(R.string.review));
            reviewFlag.setBackgroundResource(R.drawable.shape_bg_circle_box_radus_green);

            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, dynamicCommentBean.getAmount
                    (), mPresenter.getGoldName(), dynamicCommentBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.money_gold_light));
            payNum.setVisibility(View.VISIBLE);
        } else {
            reviewFlag.setBackground(null);
            if (dynamicCommentBean.getState() == TopNewsCommentListBean.TOP_REFUSE) {
                reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
                reviewFlag.setText(holder.itemView.getResources().getString(R.string.review_refuse));
            } else {
                reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.general_for_hint));
                reviewFlag.setText(holder.itemView.getResources().getString(R.string.review_approved));
            }
            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, dynamicCommentBean.getAmount
                    (), mPresenter.getGoldName(), dynamicCommentBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.general_for_hint));
            payNum.setVisibility(View.VISIBLE);
        }

        holder.setVisible(R.id.fl_image_container, hasImage ? View.VISIBLE : View.GONE);

        if (hasImage) {
            holder.setVisible(R.id.fl_image_container, View.VISIBLE);
            String url;

            holder.setVisible(R.id.iv_video_icon, View.GONE);
            url = ImageUtils.imagePathConvertV2(dynamicCommentBean.getNews().getImage().getId()
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , ImageZipConfig.IMAGE_38_ZIP);
            Glide.with(holder.getConvertView().getContext())
                    .load(url)
                    .error(R.drawable.shape_default_error_image)
                    .into((ImageView) holder.getView(R.id.iv_detail_image));
        }

        holder.setText(R.id.tv_deatil, newsIsDeleted ?
                holder.getConvertView().getResources().getString(R.string.review_content_deleted)
                : dynamicCommentBean.getNews().getContent());

        holder.setText(R.id.tv_content,
                String.format(Locale.getDefault(), holder.itemView.getContext().getString(R.string.stick_type_dynamic_commnet_message),
                        commentIsDeleted ?" ": RegexUtils.replaceImageIdAndNeedSpaceString(MarkdownConfig.IMAGE_FORMAT,
                        dynamicCommentBean.getComment().getComment_content())));

        if (newsIsDeleted || commentIsDeleted) {
            reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
            reviewFlag.setText(holder.itemView.getResources().getString(newsIsDeleted ?
                    R.string.review_dynamic_deleted : R.string.review_comment_deleted));
            reviewFlag.setBackground(null);
            payNum.setVisibility(View.GONE);
        }

        List<Link> links = setLinks(holder.itemView.getContext());
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, dynamicCommentBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicCommentBean.getUpdated_at()));


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.itemView.performClick());
        // 去详情
        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (dynamicCommentBean.getNews() == null || dynamicCommentBean.getComment() == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(dynamicCommentBean.getComment());
                });

        RxView.clicks(reviewFlag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    handleReView(position, dynamicCommentBean);
                });
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    handleReView(position, dynamicCommentBean);
                });

    }

    private void handleReView(int position, TopNewsCommentListBean dynamicCommentBean) {
        if (dynamicCommentBean.getNews() != null
                && dynamicCommentBean.getComment() != null) {
            initReviewPopWindow(dynamicCommentBean, position);
        }
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopNewsCommentListBean newsCommentListBean = (TopNewsCommentListBean) data;
        newsCommentListBean.setExpires_at(TimeUtils.millis2String(System.currentTimeMillis() + 1000000));
        newsCommentListBean.setState(TopNewsCommentListBean.TOP_SUCCESS);
        BaseListBean result = newsCommentListBean;
        mPresenter.approvedTopComment((long) newsCommentListBean.getNews().getId(),
                newsCommentListBean.getComment().getId().intValue(), (int) newsCommentListBean.getId(), result, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopNewsCommentListBean newsCommentListBean = (TopNewsCommentListBean) data;
        newsCommentListBean.setExpires_at(TimeUtils.getCurrenZeroTimeStr());
        newsCommentListBean.setState(TopNewsCommentListBean.TOP_REFUSE);
        mPresenter.refuseTopComment((int) newsCommentListBean.getId(), data, position);
    }
}
