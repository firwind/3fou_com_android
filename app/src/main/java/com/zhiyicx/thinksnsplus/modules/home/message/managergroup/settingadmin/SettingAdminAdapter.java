package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 13:59
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.GroupHankBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction.JurisdictionActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SettingAdminAdapter extends CommonAdapter<GroupHankBean> {
    private SettingAdminItemAdapter settingAdminItemAdapter;
    private Context context;
    private ChatGroupBean mChatGroupBean;
    private RecyclerView.LayoutManager mLayoutManager;

    public SettingAdminItemAdapter.DeleteRoleListener getListener() {
        return listener;
    }

    public void setListener(SettingAdminItemAdapter.DeleteRoleListener listener) {
        this.listener = listener;
    }

    private SettingAdminItemAdapter.DeleteRoleListener listener;

    public SettingAdminAdapter(Context context, List<GroupHankBean> datas, ChatGroupBean chatGroupBean) {
        super(context, R.layout.item_setting_admin, datas);
        this.context = context;
        this.mChatGroupBean = chatGroupBean;
    }

    @Override
    protected void convert(ViewHolder holder, GroupHankBean groupHankBean, int position) {

        holder.setText(R.id.tv_hank_title, groupHankBean.getmHankName() + "(" + groupHankBean.getUserInfoBeans().size() + ")");
        holder.setText(R.id.tv_add_rank_name, "添加" + groupHankBean.getmHankName());
        holder.setVisible(R.id.tv_edit_info, groupHankBean.isOwner == 1 ? View.GONE : View.VISIBLE);
        TextView mEditBt = holder.getView(R.id.tv_edit_info);
        LinearLayout mAddHank = holder.getView(R.id.ll_add_group_rank);
        holder.setVisible(R.id.ll_add_group_rank, groupHankBean.isOwner == 1 ? View.GONE : View.VISIBLE);
        RecyclerView recyclerView = holder.getView(R.id.rv_setting_admin);
        mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        //设置Item的间隔
        recyclerView.addItemDecoration(getItemDecoration());
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(mLayoutManager);
        settingAdminItemAdapter = new SettingAdminItemAdapter(context, R.layout.item_group_admin, groupHankBean.getUserInfoBeans(), groupHankBean.getmType(),position);
        settingAdminItemAdapter.setListener(getListener());
        recyclerView.setAdapter(settingAdminItemAdapter);
        RxView.clicks(mAddHank)//跳转至添加管理员/讲师/主持人
                .subscribe(aVoid -> {
                    JurisdictionActivity.startSelectFriendActivity(context, mChatGroupBean, groupHankBean.getmHankName());
                });
        RxView.clicks(mEditBt)//编辑
                .subscribe(aVoid -> {
                    SettingAdminItemAdapter adminItemAdapter = (SettingAdminItemAdapter) recyclerView.getAdapter();
                    List<UserInfoBean> userInfoBeans = adminItemAdapter.getDatas();
                    for (UserInfoBean userInfoBean : userInfoBeans) {
                        if (groupHankBean.getmIsEdit() == 0) {
                            userInfoBean.setIsSelected(1);
                        } else {
                            userInfoBean.setIsSelected(0);
                        }
                    }
                    if (groupHankBean.getmIsEdit() == 1) {
                        groupHankBean.setmIsEdit(0);

                    } else {
                        groupHankBean.setmIsEdit(1);
                    }
                    notifyDataSetChanged();
                    adminItemAdapter.notifyDataSetChanged();
                });
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new LinearDecoration(0, ConvertUtils.dp2px(getContext(), 0.5f), 0, 0);
    }
}
