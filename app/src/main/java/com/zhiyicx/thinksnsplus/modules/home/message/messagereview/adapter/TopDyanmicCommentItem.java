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
import com.zhiyicx.baseproject.impl.imageloader.glide.GlideImageConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.PinnedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopDynamicCommentBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @Author Jliuer
 * @Date 2017/09/11/10:50
 * @Email Jliuer@aliyun.com
 * @Description 动态评论置顶审核
 */
public class TopDyanmicCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopDyanmicCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_message_review_list;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopDynamicCommentBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean topDynamicCommentBean, BaseListBean lastT, int position, int itemCounts) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) topDynamicCommentBean;
        ImageUtils.loadCircleUserHeadPic(dynamicCommentBean.getUserInfoBean(), holder.getView(R.id.iv_headpic));

        boolean dynamicIsDeleted = dynamicCommentBean.getFeed() == null;
        boolean commentIsDeleted = dynamicCommentBean.getComment() == null;

        boolean hasImage = !dynamicIsDeleted && dynamicCommentBean.getFeed().getImages() != null && !dynamicCommentBean.getFeed().getImages()
                .isEmpty();
        boolean hasVideo = !dynamicIsDeleted && dynamicCommentBean.getFeed().getVideo() != null;

        boolean hasConentImage = hasImage || hasVideo;

        TextView reviewFlag = holder.getTextView(R.id.tv_review);
        TextView payNum = holder.getTextView(R.id.tv_pay_num);
        if (dynamicCommentBean.getExpires_at() == null) {
            reviewFlag.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.dyanmic_top_flag));
            reviewFlag.setText(holder.itemView.getResources().getString(R.string.review));
            reviewFlag.setBackgroundResource(R.drawable.shape_bg_circle_box_radus_green);

            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, dynamicCommentBean.getAmount
                    (), mPresenter.getGoldName(), dynamicCommentBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.money_gold_light));
            payNum.setVisibility(View.VISIBLE);
        } else {
            reviewFlag.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.general_for_hint));
            reviewFlag.setText(holder.itemView.getResources().getString(R.string.review_done));
            reviewFlag.setBackground(null);

            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, dynamicCommentBean.getAmount
                    (), mPresenter.getGoldName(), dynamicCommentBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.general_for_hint));
            payNum.setVisibility(View.VISIBLE);
        }

        holder.setVisible(R.id.fl_image_container, hasConentImage ? View.VISIBLE : View.GONE);
        if (hasVideo) {
            holder.setVisible(R.id.iv_video_icon, View.VISIBLE);
            String url = ImageUtils.imagePathConvertV2(dynamicCommentBean.getFeed().getVideo().getCover_id()
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , ImageZipConfig.IMAGE_38_ZIP);
            Glide.with(holder.getConvertView().getContext())
                    .load(url)
                    .error(R.drawable.shape_default_image_themcolor)
                    .into((ImageView) holder.getView(R.id.iv_detail_image));
        } else if (hasImage) {
            holder.setVisible(R.id.iv_video_icon, View.GONE);
            String url = ImageUtils.imagePathConvertV2(dynamicCommentBean.getFeed().getImages().size() > 0 ? dynamicCommentBean.getFeed().getImages()
                            .get(0).getFile() : 0
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , ImageZipConfig.IMAGE_38_ZIP);
            Glide.with(holder.getConvertView().getContext())
                    .load(url)
                    .error(R.drawable.shape_default_error_image)
                    .into((ImageView) holder.getView(R.id.iv_detail_image));
        }

        holder.setText(R.id.tv_deatil, dynamicIsDeleted ?
                holder.getConvertView().getResources().getString(R.string.review_content_deleted)
                : dynamicCommentBean.getFeed().getFeed_content());

        holder.setText(R.id.tv_content,
                String.format(Locale.getDefault(), holder.itemView.getContext().getString(R.string.stick_type_dynamic_commnet_message),
                        commentIsDeleted ? " " : dynamicCommentBean.getComment().getComment_content()));

        if (dynamicIsDeleted || commentIsDeleted) {
            reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
            reviewFlag.setText(holder.itemView.getResources().getString(dynamicIsDeleted ?
                    R.string.review_dynamic_deleted : R.string.review_comment_deleted));
            reviewFlag.setBackground(null);
            payNum.setVisibility(View.GONE);
        }

        List<Link> links = setLinks(holder.itemView.getContext());
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, dynamicCommentBean.getUserInfoBean().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(dynamicCommentBean.getUpdated_at()));

        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(), dynamicCommentBean.getUserInfoBean()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.itemView.performClick());

        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (dynamicCommentBean.getFeed() == null || dynamicCommentBean.getComment() == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(dynamicCommentBean.getComment());
                });

        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> handleReview(position, dynamicCommentBean));

        RxView.clicks(reviewFlag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> handleReview(position, dynamicCommentBean));
    }

    private void handleReview(int position, TopDynamicCommentBean dynamicCommentBean) {
        if (dynamicCommentBean.getExpires_at() == null
                && dynamicCommentBean.getFeed() != null
                && dynamicCommentBean.getComment() != null) {
            initReviewPopWindow(dynamicCommentBean, position);
        }
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) data;
        dynamicCommentBean.getFeed().setPinned(PinnedBean.TOP_SUCCESS);
        dynamicCommentBean.setExpires_at(TimeUtils.millis2String(System.currentTimeMillis() + 1000000));
        BaseListBean result = dynamicCommentBean;
        mPresenter.approvedTopComment(dynamicCommentBean.getFeed().getId(),
                dynamicCommentBean.getComment().getId().intValue(), dynamicCommentBean.getId().intValue(), result, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopDynamicCommentBean dynamicCommentBean = (TopDynamicCommentBean) data;
        dynamicCommentBean.getFeed().setPinned(PinnedBean.TOP_REFUSE);
        dynamicCommentBean.setExpires_at(TimeUtils.getCurrenZeroTimeStr());
        mPresenter.refuseTopComment(dynamicCommentBean.getId().intValue(), data, position);
    }

}
