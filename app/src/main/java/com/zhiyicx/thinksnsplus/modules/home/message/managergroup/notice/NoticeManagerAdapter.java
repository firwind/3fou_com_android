package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/19 16:36
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.text.TextUtils;

import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.sql.Time;
import java.util.List;

public class NoticeManagerAdapter extends CommonAdapter<NoticeItemBean>{
    public NoticeManagerAdapter(Context context, List<NoticeItemBean> datas) {
        super(context, R.layout.item_group_notice, datas);
    }

    @Override
    protected void convert(ViewHolder holder, NoticeItemBean noticeItemBean, int position) {
        holder.setText(R.id.tv_notice_title, TextUtils.isEmpty(noticeItemBean.getTitle())?"":noticeItemBean.getTitle());
        holder.setText(R.id.tv_notice_content,TextUtils.isEmpty(noticeItemBean.getContent())?"":noticeItemBean.getContent());
        holder.setText(R.id.tv_notice_user_name,TextUtils.isEmpty(noticeItemBean.getAuthor())?"":noticeItemBean.getAuthor());
        String time = TimeUtils.millis2String(noticeItemBean.getCreated_at());
        holder.setText(R.id.tv_notice_time, TimeUtils.getTimeFriendlyNormal(time));
    }
}
