package com.zhiyicx.thinksnsplus.modules.information.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.BaseListBean;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.config.MarkdownConfig;
import com.zhiyicx.common.base.BaseApplication;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.RegexUtils;
import com.zhiyicx.common.utils.SkinUtils;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.modules.settings.aboutus.CustomWEBActivity;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

public abstract class FlashListItem implements ItemViewDelegate<BaseListBean> {

    private boolean mIsShowContent;
    private Context mContext;
    public FlashListItem(boolean isShowContent, Context context) {
        this.mIsShowContent = isShowContent;
        mContext = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_flash;
    }

    @Override
    public boolean isForViewType(BaseListBean item, int position) {
        return item instanceof InfoListDataBean;
    }

    @Override
    public void convert(ViewHolder holder, BaseListBean baseListBean, BaseListBean lastT,
                        final int position, int itemCounts) {
        final InfoListDataBean realData = (InfoListDataBean) baseListBean;
        final TextView title = holder.getView(R.id.item_info_title);
        final TextView mTvContent = holder.getView(R.id.item_info_content);
        final TextView mTRise = holder.getView(R.id.tv_top_flag);
        final ImageView mIRise = holder.getView(R.id.iv_top_flag);
        final TextView mTFall = holder.getView(R.id.tv_bear_news);
        final ImageView mIFall = holder.getView(R.id.iv_bear_news);
        final LinearLayout mLRise = holder.getView(R.id.ll_top_flag);
        final LinearLayout mLFall = holder.getView(R.id.ll_bear_news);
        holder.setVisible(R.id.tvTopLine,position!=0? View.VISIBLE:View.INVISIBLE);//时间轴，判断当前item位置，如果是第一个则TopLine隐藏
        final TextView share = holder.getView(R.id.tv_from_share);
        // 记录点击过后颜色
        if (AppApplication.sOverRead.contains(realData.getId())) {
            title.setTextColor(SkinUtils.getColor(R.color.normal_for_assist_text));
        }
        holder.setText(R.id.item_info_content,TextUtils.isEmpty(realData.getContent())?"":realData.getContent());

        mTRise.setText(mContext.getString(R.string.bull)+realData.getDigg_count());
        mTFall.setText(mContext.getString(R.string.bear_news)+realData.getUndigg_count());
        if (realData.getDigg_count() == 0){
            mTRise.setTextColor(mContext.getResources().getColor(R.color.text_color));
            mIRise.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.rise_no_select));
        }else {
            mTRise.setTextColor(mContext.getResources().getColor(R.color.bull_title));
            mIRise.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.rise_select));
        }
        if (realData.getUndigg_count() == 0){
            mTFall.setTextColor(mContext.getResources().getColor(R.color.text_color));
            mIFall.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.fall_no_select));
        }else {
            mTFall.setTextColor(mContext.getResources().getColor(R.color.text_fall));
            mIFall.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.fall_select));
        }
        title.setText(realData.getTitle());

        int w = title.getContext().getResources().getDimensionPixelOffset(R.dimen
                .info_channel_list_image_width);
        int h = title.getContext().getResources().getDimensionPixelOffset(R.dimen
                .info_channel_list_height);

        RxView.clicks(share)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> itemClick(position, share, title,mTvContent, realData));
        RxView.clicks(mLRise)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid ->

                        bullClick(position, realData)
                );
        RxView.clicks(mLFall)
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                .subscribe(aVoid -> bearNewsClick(position, realData));

        // 被驳回和投稿中只显示内容
        holder.setVisible(R.id.ll_info, mIsShowContent ? View.GONE : View.VISIBLE);
        holder.setVisible(R.id.tv_info_content, mIsShowContent ? View.VISIBLE : View.GONE);
        String content = realData.getText_content();
        if (TextUtils.isEmpty(content)) {
            content = RegexUtils.replaceImageId(MarkdownConfig.IMAGE_FORMAT, realData.getContent());
            content = content.replaceAll(MarkdownConfig.NORMAL_FORMAT, "");
        }
        holder.setText(R.id.tv_info_content, content);

        String infoData =  TimeUtils.getTimeFriendlyNormal(realData
                        .getCreated_at());
        holder.setText(R.id.item_info_timeform, infoData);

        if (realData.getImage() == null) {

            if (realData.getUser_id() < 0) {// 广告

                holder.setText(R.id.item_info_timeform, TimeUtils.getTimeFriendlyNormal(realData
                        .getCreated_at()));

            }
        }

    }

    public abstract void itemClick(int position, TextView textView,TextView content, TextView title,
                                   InfoListDataBean realData);

    public abstract void bullClick(int position,InfoListDataBean realData);
    public abstract void bearNewsClick(int position,InfoListDataBean realData);

}