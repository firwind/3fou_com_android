package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder;
/*
 * 文件名:子布局ViewHolder
 * 创建者：zhangl
 * 时  间：2018/7/5
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

public class ChildViewHolder extends BaseViewHolder {
    private Context mContext;
    private View view;

    public ChildViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }
    public void bindView(final ChatGroupBean chatGroupBean, final int pos) {

        ((TextView) view.findViewById(R.id.tv_group_name)).setText(chatGroupBean.getName());
        FilterImageView imageView = ((FilterImageView) view.findViewById(R.id.uv_group_head));
        ImageView signImageView = ((ImageView) view.findViewById(R.id.iv_group_sign));
        Glide.with(mContext)
                .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                        .getGroup_face())
                .error(R.mipmap.ico_ts_assistant)
                .placeholder(R.mipmap.ico_ts_assistant)
                .transform(new GlideCircleTransform(mContext))
                .into(imageView);

        int resId = ImageUtils.getGroupSignResId(chatGroupBean.getGroup_level());
        signImageView.setVisibility(0 == resId ? View.INVISIBLE : View.VISIBLE);
        if (0 != resId)
            signImageView.setImageDrawable(mContext.getResources().getDrawable(resId));
    }
}
