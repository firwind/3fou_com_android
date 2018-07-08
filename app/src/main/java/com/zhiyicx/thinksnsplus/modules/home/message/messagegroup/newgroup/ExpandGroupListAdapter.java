package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.newgroup;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupParentBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/7/7 15:21
 * description: 二级列表适配器
 * version:
 */

public class ExpandGroupListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<GroupParentBean> mList;

    public ExpandGroupListAdapter(Context mContext,List<GroupParentBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return null == mList.get(groupPosition).childs ? 0 : mList.get(groupPosition).childs.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        try {
            return Long.parseLong(mList.get(groupPosition).childs.get(groupPosition+childPosition).getId());
        }catch (Exception e){
            return groupPosition+childPosition;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(null == convertView)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_parent_group_list,
                    parent,false);
        ((TextView) convertView.findViewById(R.id.tv_parent_title)).setText(mList.get(groupPosition).title);
        ((TextView)convertView.findViewById(R.id.tv_parent_num)).setText(null == mList.get(groupPosition).childs ?
        "0":String.valueOf(mList.get(groupPosition).childs.size()));
        convertView.findViewById(R.id.iv_parent_on_off).setSelected(isExpanded);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(null == convertView)
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group_list,parent,false);
        ChatGroupBean chatGroupBean = mList.get(groupPosition).childs.get(childPosition);
        ((TextView) convertView.findViewById(R.id.tv_group_name)).setText(chatGroupBean.getName());
        FilterImageView imageView = ((FilterImageView) convertView.findViewById(R.id.uv_group_head));
        ImageView signImageView = ((ImageView) convertView.findViewById(R.id.iv_group_sign));
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

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
