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
import com.zhiyicx.thinksnsplus.data.beans.CirclePostListBean;
import com.zhiyicx.thinksnsplus.data.beans.CommentedBean;
import com.zhiyicx.thinksnsplus.data.beans.TopNewsCommentListBean;
import com.zhiyicx.thinksnsplus.data.beans.TopPostCommentListBean;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.post.CirclePostDetailActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.messagereview.MessageReviewContract;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author Jliuer
 * @Date 2017/12/08/17:02
 * @Email Jliuer@aliyun.com
 * @Description 帖子评论置顶审核
 */
public class TopPostCommentItem extends BaseTopItem implements BaseTopItem.TopReviewEvetnInterface {

    public TopPostCommentItem(Context context, MessageReviewContract.Presenter presenter) {
        super(context, presenter);
        setTopReviewEvetnInterface(this);
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof TopPostCommentListBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT, int
            position, int itemCounts) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) baseListBean;
        ImageUtils.loadCircleUserHeadPic(postCommentListBean.getCommentUser(), holder.getView(R
                .id.iv_headpic));

        boolean postIsDeleted = postCommentListBean.getPost() == null;
        boolean commentIsDeleted = postCommentListBean.getComment() == null;

        boolean hasImage = !postIsDeleted && postCommentListBean.getPost().getImages() != null && !postCommentListBean.getPost().getImages().isEmpty();

        TextView reviewFlag = holder.getTextView(R.id.tv_review);
        TextView payNum = holder.getTextView(R.id.tv_pay_num);

        if (postCommentListBean.getStatus() == TopPostCommentListBean.TOP_REVIEW) {
            reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .dyanmic_top_flag));
            reviewFlag.setText(holder.itemView.getResources().getString(R.string.review));
            reviewFlag.setBackgroundResource(R.drawable.shape_bg_circle_box_radus_green);

            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, postCommentListBean.getAmount
                    (), mPresenter.getGoldName(), postCommentListBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.money_gold_light));
            payNum.setVisibility(View.VISIBLE);
        } else {
            reviewFlag.setBackground(null);
            if (postCommentListBean.getStatus() == TopPostCommentListBean.TOP_REFUSE) {
                reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.message_badge_bg));
                reviewFlag.setText(holder.itemView.getResources().getString(R.string.review_refuse));
            } else {
                reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color.general_for_hint));
                reviewFlag.setText(holder.itemView.getResources().getString(R.string.review_approved));
            }
            payNum.setText(holder.itemView.getResources().getString(R.string.integration_pinned_pay_format, postCommentListBean.getAmount
                    (), mPresenter.getGoldName(), postCommentListBean.getDay()));
            payNum.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.general_for_hint));
            payNum.setVisibility(View.VISIBLE);
        }

        holder.setVisible(R.id.fl_image_container, hasImage ? View.VISIBLE : View.GONE);
        if (hasImage) {
            holder.setVisible(R.id.fl_image_container, View.VISIBLE);
            String url;
            holder.setVisible(R.id.iv_video_icon, View.GONE);
            url = ImageUtils.imagePathConvertV2(postCommentListBean.getPost().getImages()
                            .get(0).getFile_id()
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , mContext.getResources().getDimensionPixelOffset(R.dimen.headpic_for_user_center)
                    , ImageZipConfig.IMAGE_38_ZIP);
            Glide.with(holder.getConvertView().getContext())
                    .load(url)
                    .error(R.drawable.shape_default_error_image)
                    .into((ImageView) holder.getView(R.id.iv_detail_image));
        }

        holder.setText(R.id.tv_deatil, postIsDeleted  ?
                holder.getConvertView().getResources().getString(R.string.review_content_deleted)
                : postCommentListBean.getPost().getSummary());

        holder.setText(R.id.tv_content,
                String.format(Locale.getDefault(),holder.itemView.getContext().getString(R.string.stick_type_dynamic_commnet_message),
                        commentIsDeleted ?" ":RegexUtils.replaceImageIdAndNeedSpaceString(MarkdownConfig.IMAGE_FORMAT,
                                postCommentListBean.getComment().getComment_content())));

        if (postIsDeleted || commentIsDeleted) {
            reviewFlag.setTextColor(holder.itemView.getResources().getColor(R.color
                    .message_badge_bg));
            reviewFlag.setText(holder.itemView.getResources().getString(postIsDeleted ?
                    R.string.review_dynamic_deleted : R.string.review_comment_deleted));
            reviewFlag.setBackground(null);
            payNum.setVisibility(View.GONE);
        }

        List<Link> links = setLinks(holder.itemView.getContext());
        if (!links.isEmpty()) {
            ConvertUtils.stringLinkConvert(holder.getView(R.id.tv_content), links);
        }

        holder.setTextColorRes(R.id.tv_name, R.color.important_for_content);
        holder.setText(R.id.tv_name, postCommentListBean.getCommentUser().getName());
        holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(postCommentListBean.getUpdated_at()) + "    想用" + postCommentListBean.getAmount
                () + mPresenter.getGoldName() + "置顶" + postCommentListBean.getDay() + "天");


        // 响应事件
        RxView.clicks(holder.getView(R.id.tv_name))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        postCommentListBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.iv_headpic))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> toUserCenter(holder.itemView.getContext(),
                        postCommentListBean.getCommentUser()));
        RxView.clicks(holder.getView(R.id.tv_content))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> holder.itemView.performClick());
        // 去详情
        RxView.clicks(holder.getView(R.id.fl_detial))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> {
                    if (postCommentListBean.getPost() == null || postCommentListBean.getComment()
                            == null) {
                        initInstructionsPop(R.string.review_content_deleted);
                        return;
                    }
                    toDetail(postCommentListBean.getPost(), true);
                });

        RxView.clicks(reviewFlag)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> handleReview(position, postCommentListBean));
        RxView.clicks(holder.itemView)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> handleReview(position, postCommentListBean));
    }

    private void handleReview(int position, TopPostCommentListBean postCommentListBean) {
        if (postCommentListBean.getStatus() == TopPostCommentListBean.TOP_REVIEW
                && postCommentListBean.getPost() != null
                && postCommentListBean.getComment() != null) {
            initReviewPopWindow(postCommentListBean, position);
        }
    }

    @Override
    public void onReviewApprovedClick(BaseListBean data, int position) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) data;
        postCommentListBean.setExpires_at(TimeUtils.millis2String(System.currentTimeMillis() +
                1000000));
        postCommentListBean.setStatus(TopNewsCommentListBean.TOP_SUCCESS);
        BaseListBean result = postCommentListBean;
        mPresenter.approvedTopComment(0L,
                postCommentListBean.getComment().getId().intValue(), 0, result, position);
    }

    @Override
    public void onReviewRefuseClick(BaseListBean data, int position) {
        TopPostCommentListBean postCommentListBean = (TopPostCommentListBean) data;
        postCommentListBean.setExpires_at(TimeUtils.getCurrenZeroTimeStr());
        postCommentListBean.setStatus(TopNewsCommentListBean.TOP_REFUSE);
        mPresenter.refuseTopComment(postCommentListBean.getComment().getId().intValue(), data, position);
    }

    @Override
    protected void toDetail(CommentedBean commentedBean) {
    }

    protected void toDetail(CirclePostListBean postListBean, boolean isLookMoreComment) {
        CirclePostDetailActivity.startActivity(mContext, postListBean, isLookMoreComment, true);
    }
}
