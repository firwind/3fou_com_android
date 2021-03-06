package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin;
/*
 * 文件名：设置管理员内置adapter
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 16:47
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SettingAdminItemAdapter extends CommonAdapter<UserInfoBean> {
    public void setListener(DeleteRoleListener listener) {
        this.listener = listener;
    }
    private String mType;
    private DeleteRoleListener listener;
    private int mPostion;
    public SettingAdminItemAdapter(Context context, int layoutId, List<UserInfoBean> datas,int type,int postion) {
        super(context, layoutId, datas);
        this.mType = String.valueOf(type);
        this.mPostion = postion;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        // 右边
        final SwipeLayout swipeLayout = holder.getView(R.id.swipe);
        swipeLayout.setSwipeEnabled(true);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
//                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
            }
        });
        LinearLayout mGroupRank = holder.getView(R.id.ll_group_rank);
        TextView mSettingRight = holder.getView(R.id.tv_setting_right);
        holder.setText(R.id.tv_add_rank_name, TextUtils.isEmpty(userInfoBean.getName())?"":userInfoBean.getName());
        UserAvatarView cbFriends = holder.getView(R.id.iv_user_portrait);
        ImageView ivDelete = holder.getView(R.id.iv_delete_admin);

        ImageUtils.loadCircleUserHeadPic(userInfoBean, cbFriends);
        if (userInfoBean.getIsSelected()==1){
            swipeLayout.open();
        }else {
            swipeLayout.close();
        }
        RxView.clicks(mSettingRight)//删除管理/讲师/主持人
                .subscribe(aVoid -> {
                    listener.onDeleteClick(userInfoBean,mType,mPostion,position);
                    swipeLayout.close();
                });

        RxView.clicks(mGroupRank)
                .subscribe(aVoid -> {
                    // 个人中心
                    PersonalCenterFragment.startToPersonalCenter(holder.getConvertView()
                            .getContext(), userInfoBean);
                });


    }
    public interface DeleteRoleListener {
        void onDeleteClick(UserInfoBean userInfoBean,String type,int parentPostion,int sonPostion);
    }
}
