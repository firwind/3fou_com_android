package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/6
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;

import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.List;

public class GroupListAdapter extends CommonAdapter<ChatGroupBean> {
    Context context;
    public GroupListAdapter(Context context, List<ChatGroupBean> datas) {
        super(context, R.layout.item_parent_group, datas);
        this.context = context;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private OnItemClickListener listener;
    @Override
    protected void convert(ViewHolder holder, ChatGroupBean groupInfoBean, int position) {
        ImageView imageView = holder.getView(R.id.iv_parent_on_off);
        RecyclerView recyclerView = holder.getView(R.id.group_child_rv);
        holder.setText(R.id.tv_parent_title,groupInfoBean.getmParentName());
        holder.setText(R.id.tv_parent_num,groupInfoBean.getmParentNum()+"");
        if (groupInfoBean.isExpand()) {
            recyclerView.setVisibility(View.GONE);
            imageView.setImageResource(R.mipmap.right_icon);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.mipmap.down_icon);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        //设置Item的间隔
        recyclerView.addItemDecoration(getItemDecoration());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        CommonAdapter adapter = new CommonAdapter<ChatGroupBean>(context, R.layout.item_group_list, groupInfoBean.getTreeBeanList()) {
            @Override
            protected void convert(ViewHolder holder, ChatGroupBean chatGroupBean, int position) {
                holder.setText(R.id.tv_group_name, chatGroupBean.getName());
                Glide.with(mContext)
                        .load(TextUtils.isEmpty(chatGroupBean.getGroup_face()) ? R.mipmap.ico_ts_assistant : chatGroupBean
                                .getGroup_face())
                        .error(R.mipmap.ico_ts_assistant)
                        .placeholder(R.mipmap.ico_ts_assistant)
                        .transform(new GlideCircleTransform(mContext))
                        .into(holder.getImageViwe(R.id.uv_group_head));

                int resId = ImageUtils.getGroupSignResId(chatGroupBean.getGroup_level());
                holder.getImageViwe(R.id.iv_group_sign).setVisibility(0 == resId ? View.INVISIBLE : View.VISIBLE);
                if (0 != resId)
                    holder.getImageViwe(R.id.iv_group_sign).setImageDrawable(context.getResources().getDrawable(resId));
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                ChatGroupBean groupBean = groupInfoBean.getTreeBeanList().get(position);
//                mPresenter.checkGroupExist(groupBean.getId());
                listener.onItemClick(groupBean);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        recyclerView.setAdapter(adapter);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new LinearDecoration(0, ConvertUtils.dp2px(getContext(), 0.5f), 0, 0);
    }

    public interface OnItemClickListener{
        void onItemClick(ChatGroupBean groupBean);
    }
}
