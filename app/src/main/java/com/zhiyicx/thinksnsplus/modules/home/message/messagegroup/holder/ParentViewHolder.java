package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder;
/*
 * 文件名:父布局ViewHolder TimeTeleconMode
 * 创建者：zhangl
 * 时  间：2018/7/5
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

public class ParentViewHolder extends BaseViewHolder {
    private Context mContext;
    private View view;
    private ImageView imageView;

    public ParentViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.view = itemView;
    }

    public void bindView(final ChatGroupBean rdb, final int position, final ItemClickListener listener) {
        ((TextView) view.findViewById(R.id.tv_parent_title)).setText(rdb.getmParentName());
        imageView = (ImageView) view.findViewById(R.id.iv_parent_on_off);
//        ((TextView) view.findViewById(R.id.tv_parent_num)).setText(rdb.getmParentNum());
        //父布局OnClick监听
        view.setOnClickListener(v -> parentListener(rdb, listener));
    }

    public void parentListener(ChatGroupBean rdb, ItemClickListener listener) {
        if (listener != null) {
            if (rdb.isExpand()) {
                rdb.setExpand(false);
                imageView.setImageResource(R.mipmap.right_icon);
                listener.onHideChildren(rdb);
            } else {
                rdb.setExpand(true);
                imageView.setImageResource(R.mipmap.down_icon);
                listener.onExpandChildren(rdb);
            }
        }
    }
}
