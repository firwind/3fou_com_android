package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.Toll;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.base.BaseWebLoad;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.data.beans.InfoPublishBean;
import com.zhiyicx.thinksnsplus.data.beans.RealAdvertListBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsCountBean;
import com.zhiyicx.thinksnsplus.data.beans.RewardsListBean;
import com.zhiyicx.thinksnsplus.data.beans.UserTagBean;
import com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailAdvertHeader;
import com.zhiyicx.thinksnsplus.modules.edit_userinfo.UserInfoTagsAdapter;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.information.dig.InfoDigListActivity;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.information.videoinfodetails.VideoInfoDetailsActivity;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.modules.wallet.reward.RewardType;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.MarkDownRule;
import com.zhiyicx.thinksnsplus.widget.DynamicHorizontalStackIconView;
import com.zhiyicx.thinksnsplus.widget.ReWardView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.tiagohm.markdownview.MarkdownView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhiyicx.baseproject.config.ApiConfig.API_VERSION_2;
import static com.zhiyicx.baseproject.config.ApiConfig.APP_DOMAIN;
import static com.zhiyicx.baseproject.config.MarkdownConfig.IMAGE_FORMAT;
import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/9
 * @contact email:648129313@qq.com
 */

public class VideoInfoDetailHeaderView {

    private TextView mTitle;
    private FrameLayout mCommentHintView;
    private TextView mCommentCountView;
    private FrameLayout mInfoRelateList;
    private TagFlowLayout mFtlRelate;
    private RecyclerView mRvRelateInfo;
    private View mInfoDetailHeader;
    private Context mContext;
    private List<ImageBean> mImgList;
    private ImageView mIvDetail;
    private boolean isReviewIng;
    private TextView mTvDigCount;
    private TextView mTvPlayCount;
    private LinearLayout mLlHeader;
    private ImageView mIvLoading;
    private LinearLayout mLlInfoCount;

    public View getInfoDetailHeader() {
        return mInfoDetailHeader;
    }

    public VideoInfoDetailHeaderView(Context context) {
        this.mContext = context;
        mImgList = new ArrayList<>();
        mInfoDetailHeader = LayoutInflater.from(context).inflate(R.layout
                .item_video_info_comment, null);
        mInfoDetailHeader.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout
                .LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
        mTitle = (TextView) mInfoDetailHeader.findViewById(R.id.tv_info_title);
        mCommentHintView = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_detail_comment);
        mCommentCountView = (TextView) mInfoDetailHeader.findViewById(R.id.tv_comment_count);
        mInfoRelateList = (FrameLayout) mInfoDetailHeader.findViewById(R.id.info_relate_list);
        mFtlRelate = (TagFlowLayout) mInfoDetailHeader.findViewById(R.id.fl_tags);
        mRvRelateInfo = (RecyclerView) mInfoDetailHeader.findViewById(R.id.rv_relate_info);
        mIvDetail = (ImageView) mInfoDetailHeader.findViewById(R.id.iv_detail);
        mTvDigCount = (TextView) mInfoDetailHeader.findViewById(R.id.tv_dig_count);
        mTvPlayCount = (TextView) mInfoDetailHeader.findViewById(R.id.tv_play_count);
        mIvLoading = (ImageView) mInfoDetailHeader.findViewById(R.id.iv_loading);
        mLlHeader = (LinearLayout) mInfoDetailHeader.findViewById(R.id.ll_header);
        mLlInfoCount = (LinearLayout) mInfoDetailHeader.findViewById(R.id.ll_info_count);
    }

    public void setDetail(InfoListDataBean infoMain) {

        if (infoMain != null) {
            mTitle.setText(infoMain.getTitle());
            mTvDigCount.setText( String.valueOf(infoMain.getDigg_count()) );
            mTvDigCount.setSelected(infoMain.getHas_like());
            mTvPlayCount.setText("播放量"+infoMain.getHits());
            // 评论信息
            updateCommentView(infoMain);
        }
    }

    public void updateDetail(InfoListDataBean infoMain) {

        if (infoMain != null) {
            mLlInfoCount.setVisibility(VISIBLE);
            mTitle.setText(infoMain.getTitle());
            mTvDigCount.setText( String.valueOf(infoMain.getDigg_count()) );
            mTvDigCount.setSelected(infoMain.getHas_like());
            mTvPlayCount.setText("播放量"+infoMain.getHits());
            // 评论信息
            updateCommentView(infoMain);
        }
    }



    /**
     * 更新评论页面
     */
    public void updateCommentView(InfoListDataBean infoMain) {
        // 评论信息
        if (infoMain.getComment_count() != 0) {
            mCommentHintView.setVisibility(View.VISIBLE);
            mCommentCountView.setText(mContext.getString(R.string.dynamic_comment_count, infoMain.getComment_count() + ""));
        } else {
            mCommentHintView.setVisibility(View.GONE);
        }
    }


    public void setRelateInfo(InfoListDataBean infoMain) {

        mLlHeader.setVisibility(VISIBLE);
        mIvLoading.setVisibility(GONE);

        List<InfoListDataBean> infoListDataBeen = infoMain.getRelateInfoList();
        if (infoListDataBeen != null && infoListDataBeen.size() > 0) {
            if (!isReviewIng) {
                mInfoRelateList.setVisibility(VISIBLE);
                mFtlRelate.setVisibility(VISIBLE);
                mRvRelateInfo.setVisibility(VISIBLE);
            }

            // 标签
            List<UserTagBean> tagBeanList = infoMain.getTags();
            if (tagBeanList != null && tagBeanList.size() > 0) {
                UserInfoTagsAdapter mUserInfoTagsAdapter = new UserInfoTagsAdapter(tagBeanList, mContext);
                mFtlRelate.setOnTagClickListener((view, position, parent) -> false);
                mFtlRelate.setAdapter(mUserInfoTagsAdapter);
            }
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mRvRelateInfo.setLayoutManager(manager);
            mRvRelateInfo.setNestedScrollingEnabled(false);
            CommonAdapter adapter = new CommonAdapter<InfoListDataBean>(mContext, R.layout.item_video_details_info, infoListDataBeen) {
                @Override
                protected void convert(ViewHolder holder, InfoListDataBean infoListDataBean, int position) {
                    final TextView title = holder.getView(R.id.item_info_title);
                    final ImageView imageView = holder.getView(R.id.item_info_imag);

                    // 记录点击过后颜色
                    if (AppApplication.sOverRead.contains(infoListDataBean.getId())) {
                        title.setTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
                    }

                    title.setText(infoListDataBean.getTitle());
                    if (infoListDataBean.getImage() == null) {
                        imageView.setVisibility(View.GONE);
                    } else {
                        imageView.setVisibility(View.VISIBLE);
                        Glide.with(BaseApplication.getContext())
                                .load(ImageUtils.imagePathConvertV2(infoListDataBean.getImage().getId(), imageView.getWidth(), imageView.getHeight(),
                                        ImageZipConfig.IMAGE_80_ZIP))
                                .placeholder(R.mipmap.default_image_for_video)
                                .error(R.mipmap.default_image_for_video)
                                .override(imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_image_width)
                                        , imageView.getContext().getResources().getDimensionPixelOffset(R.dimen.info_channel_list_height))
                                .into(imageView);
                    }
//                    // 来自单独分开
//                    String category = infoListDataBean.getCategory() == null ? "" : infoListDataBean.getCategory().getName();
//                    holder.setText(R.id.tv_from_channel, category);
                    // 投稿来源，浏览数，时间
                    String from = infoListDataBean.getFrom().equals(title.getContext().getString(R.string.info_publish_original)) ?
                            infoListDataBean.getAuthor() : infoListDataBean.getFrom();
                    String infoData = String.format(Locale.getDefault(), title.getContext().getString(R.string.info_list_count)
                            , from, String.valueOf(infoListDataBean.getHits()), TimeUtils.getTimeFriendlyNormal(infoListDataBean
                                    .getUpdated_at()));
                    holder.setText(R.id.item_play_num,  String.valueOf(infoListDataBean.getHits()));
                    // 是否置顶
//                    holder.setVisible(R.id.tv_top_flag, infoListDataBean.isTop() ? View.VISIBLE : View.GONE);
                    holder.itemView.setOnClickListener(v -> {
                        if (!AppApplication.sOverRead.contains(infoListDataBean.getId())) {
                            AppApplication.sOverRead.add(infoListDataBean.getId().intValue());
                        }
                        FileUtils.saveBitmapToFile(mContext, ConvertUtils.drawable2BitmapWithWhiteBg(getContext()
                                , imageView.getDrawable(), R.mipmap.icon), "info_share.jpg");
                        title.setTextColor(mContext.getResources()
                                .getColor(R.color.normal_for_assist_text));
                        // 跳转到新的视频详情页
                        VideoInfoDetailsActivity.startVideoInfoDetailsActivity(getContext(),infoListDataBeen.get(position),-1);
                    });
                }
            };
            mRvRelateInfo.setAdapter(adapter);
        } else {
            mInfoRelateList.setVisibility(GONE);
            mFtlRelate.setVisibility(GONE);
            mRvRelateInfo.setVisibility(GONE);
        }
    }


    /**
     * @param visible 0 正常，
     */
    public void setInfoReviewIng(int visible) {
        isReviewIng = true;
        mInfoRelateList.setVisibility(visible);
        mFtlRelate.setVisibility(visible);
        mRvRelateInfo.setVisibility(visible);
    }

    /**
     * 暴露给外部
     * @return
     */
    public TextView getDigView(){
        return mTvDigCount;
    }

    /**
     * 点赞
     * @param isDig
     * @param count
     */
    public void setDigCount(boolean isDig,int count){

        mTvDigCount.setSelected(isDig);
        mTvDigCount.setText(String.valueOf(count));

    }

}
