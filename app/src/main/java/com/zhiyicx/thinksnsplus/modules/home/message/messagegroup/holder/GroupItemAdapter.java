package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/5
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.baseproject.widget.imageview.FilterImageView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupItemAdapter extends SecondaryListAdapter<GroupItemAdapter.GroupItemViewHolder, GroupItemAdapter.SubItemViewHolder> {
    private Context context;
    private List<DataTree<ChatGroupBean, ChatGroupBean>> dts = new ArrayList<>();
    private OnScrollListener onScrollListener;
    public GroupItemAdapter(Context context,OnScrollListener onScrollListener) {
        this.context = context;
        this.onScrollListener = onScrollListener;
    }

    public void setData(List datas) {
        dts = datas;
        notifyNewData(dts);
    }

    @Override
    public RecyclerView.ViewHolder groupItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_parent_group, parent, false);
        return new GroupItemViewHolder(v);
    }

    @Override
    public RecyclerView.ViewHolder subItemViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_list, parent, false);

        return new SubItemViewHolder(v);
    }

    @Override
    public void onGroupItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex) {
        ChatGroupBean chatGroupBean = dts.get(groupItemIndex).getGroupItem();
//        ((GroupItemViewHolder) holder).tvGroupName.setText(chatGroupBean.getmParentName());
//        ((GroupItemViewHolder) holder).tvGroupNum.setText(chatGroupBean.getmParentNum());

    }

    @Override
    public void onSubItemBindViewHolder(RecyclerView.ViewHolder holder, int groupItemIndex, int subItemIndex) {
        ChatGroupBean chatGroupBean = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        ((SubItemViewHolder) holder).groupName.setText(chatGroupBean.getName());

        Glide.with(context)
                .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                        .getGroup_face())
                .error(R.mipmap.ico_ts_assistant)
                .placeholder(R.mipmap.ico_ts_assistant)
                .transform(new GlideCircleTransform(context))
                .into(((SubItemViewHolder) holder).imageView);

        int resId = ImageUtils.getGroupSignResId(chatGroupBean.getGroup_level());
        ((SubItemViewHolder) holder).signImageView.setVisibility(0 == resId ? View.INVISIBLE : View.VISIBLE);
        if (0 != resId)
            ((SubItemViewHolder) holder).signImageView.setImageDrawable(context.getResources().getDrawable(resId));
//        view.setOnClickListener(v -> scrollListener.checkIsGroup(chatGroupBean));
    }

    @Override
    public void onGroupItemClick(Boolean isExpand, GroupItemViewHolder holder, int groupItemIndex) {
        if (isExpand) {
            holder.imageView.setImageResource(R.mipmap.right_icon);
        } else {
            holder.imageView.setImageResource(R.mipmap.down_icon);
        }
    }

    @Override
    public void onSubItemClick(SubItemViewHolder holder, int groupItemIndex, int subItemIndex) {
        ChatGroupBean chatGroupBean = dts.get(groupItemIndex).getSubItems().get(subItemIndex);
        onScrollListener.checkIsGroup(chatGroupBean);
    }

    public static class GroupItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvGroupName;
        TextView tvGroupNum;
        ImageView imageView;

        public GroupItemViewHolder(View itemView) {
            super(itemView);
            tvGroupName = (TextView) itemView.findViewById(R.id.tv_parent_title);
//            tvGroupNum = (TextView) itemView.findViewById(R.id.tv_parent_group_num);
            imageView = (ImageView) itemView.findViewById(R.id.iv_parent_on_off);
        }

    }

    public static class SubItemViewHolder extends RecyclerView.ViewHolder {
        TextView groupName;
        FilterImageView imageView;
        ImageView signImageView;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            groupName = (TextView) itemView.findViewById(R.id.tv_group_name);
            imageView = ((FilterImageView) itemView.findViewById(R.id.uv_group_head));
            signImageView = ((ImageView) itemView.findViewById(R.id.iv_group_sign));
        }
    }
    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {

        void checkIsGroup(ChatGroupBean groupBean);
    }
}
