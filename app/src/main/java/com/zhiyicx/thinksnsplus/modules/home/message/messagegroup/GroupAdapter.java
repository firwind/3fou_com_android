package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup;
/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/5
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.BaseViewHolder;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.ChildViewHolder;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.ItemClickListener;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder.ParentViewHolder;

import java.util.ArrayList;
import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private Context context;
    private List<ChatGroupBean> dataBeanList;
    private LayoutInflater mInflater;
    private OnScrollListener mOnScrollListener;

    public GroupAdapter(Context context, List<ChatGroupBean> dataBeanList) {
        this.context = context;
        this.dataBeanList = dataBeanList;
        this.mInflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(List<ChatGroupBean> dataBeanList) {
        this.dataBeanList = dataBeanList;
        notifyDataSetChanged();
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case ChatGroupBean.ONELIST:
                view = mInflater.inflate(R.layout.item_parent_group, parent, false);
                return new ParentViewHolder(context, view);
            case ChatGroupBean.TWOLIST:
                view = mInflater.inflate(R.layout.item_group_list, parent, false);
                return new ChildViewHolder(context, view);
            default:
                view = mInflater.inflate(R.layout.item_parent_group, parent, false);
                return new ParentViewHolder(context, view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return dataBeanList.get(position).getType();
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case ChatGroupBean.ONELIST:
                ParentViewHolder parentViewHolder = (ParentViewHolder) holder;
                parentViewHolder.bindView(dataBeanList.get(position), position, itemClickListener);
                break;
            case ChatGroupBean.TWOLIST:
                ChildViewHolder childViewHolder = (ChildViewHolder) holder;
                childViewHolder.bindView(dataBeanList.get(position), position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataBeanList.size();
    }

    /**
     * 滚动监听接口
     */
    public interface OnScrollListener {
        void scrollTo(int pos);

    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    private ItemClickListener itemClickListener = new ItemClickListener() {

        @Override
        public void onExpandChildren(ChatGroupBean bean) {
            int position = dataBeanList.lastIndexOf(bean);//确定当前点击的item位置
            List<ChatGroupBean> rdblist = getChildData(position);
            dataBeanList.get(position).setTreeBeanList(rdblist);
            //获取要展示的子布局数据对象，注意区分onHideChildren方法中的getChildBean()。
            if (rdblist == null || rdblist.size() == 0) {
                return;
            }
            for (int i = rdblist.size() - 1; i > -1; i--) {
                add(rdblist.get(i), position + 1);//在当前的item下方插入
            }
            if (position == dataBeanList.size() - 2 && mOnScrollListener != null) { //如果点击的item为最后一个
                mOnScrollListener.scrollTo(position + rdblist.size());//向下滚动，使子布局能够完全展示
            }
        }

        @Override
        public void onHideChildren(ChatGroupBean bean) {
            int position = dataBeanList.lastIndexOf(bean);//确定当前点击的item位置
            List<ChatGroupBean> children = bean.getTreeBeanList();//获取子布局对象
            if (children == null) {
                return;
            }

            for (int i = 1; i < children.size() + 1; i++) {
                remove(position + 1);//删除
            }
            if (mOnScrollListener != null) {
                mOnScrollListener.scrollTo(position);
            }
        }
    };

    @NonNull
    private List<ChatGroupBean> getChildData(int position) {
        List<ChatGroupBean> rdblist = new ArrayList<>();
        List<ChatGroupBean> ChildList = dataBeanList.get(position).getTreeBeanList();
        rdblist = addData(ChildList);
        return rdblist;
    }

    private List<ChatGroupBean> addData(List<ChatGroupBean> beans) {
        for (int j = 0; j < beans.size(); j++) {
            ChatGroupBean rdb = beans.get(j);
            rdb.setType(ChatGroupBean.TWOLIST);
            beans.set(j, rdb);
        }
        return beans;
    }

    /**
     * 在父布局下方插入一条数据
     *
     * @param bean
     * @param position
     */
    public void add(ChatGroupBean bean, int position) {
        notifyItemInserted(position);
        dataBeanList.add(position, bean);
    }

    /**
     * 移除子布局数据
     *
     * @param position
     */
    protected void remove(int position) {
        notifyDataSetChanged();
        notifyItemRemoved(position);
        dataBeanList.remove(position);
    }
}
