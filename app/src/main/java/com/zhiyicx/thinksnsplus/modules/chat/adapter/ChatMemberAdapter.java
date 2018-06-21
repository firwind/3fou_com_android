package com.zhiyicx.thinksnsplus.modules.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Objects;

import static com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListFragment.DEFAULT_COLUMS;

/**
 * @author Catherine
 * @describe 群信息的成员item
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatMemberAdapter extends CommonAdapter<UserInfoBean> {

    private Long mOwnerId;
    private boolean mIsNeedPadding;

    public ChatMemberAdapter(Context context, List<UserInfoBean> datas, long ownerId, boolean isNeedPadding) {
        super(context, isNeedPadding ? R.layout.item_chat_member_top_padding : R.layout.item_chat_member, datas);
        this.mOwnerId = ownerId;
        this.mIsNeedPadding = isNeedPadding;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean chatUserInfoBean, int position) {
        boolean isNeedTop = mIsNeedPadding && position <= DEFAULT_COLUMS - 1;
        int remainder = getItemCount() % DEFAULT_COLUMS;
        boolean isNeedBottom = mIsNeedPadding && getItemCount() > 0 && position >= (remainder == 0 ? (getItemCount() - DEFAULT_COLUMS) : (getItemCount
                () - getItemCount() % DEFAULT_COLUMS));

        holder.getView(R.id.fl_container).setPadding(0
                , isNeedTop ? holder.getConvertView().getResources()
                        .getDimensionPixelOffset(R.dimen.spacing_large) : 0
                , 0
                , isNeedBottom ? holder.getConvertView().getResources()
                        .getDimensionPixelOffset(R
                                .dimen
                                .spacing_large) : 0);
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        if (chatUserInfoBean.getUser_id() == -1L) {
            // 加号
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_add);
            holder.setVisible(R.id.tv_owner_flag, View.GONE);
            holder.setVisible(R.id.tv_user_name, View.GONE);
            ivUserPortrait.getIvVerify().setVisibility(View.GONE);
        } else if (chatUserInfoBean.getUser_id() == -2L) {
            ivUserPortrait.getIvAvatar().setImageResource(R.mipmap.btn_chatdetail_reduce);
            holder.setVisible(R.id.tv_owner_flag, View.GONE);
            holder.setVisible(R.id.tv_user_name, View.GONE);
            ivUserPortrait.getIvVerify().setVisibility(View.GONE);
        } else {
            ImageUtils.loadUserHead(chatUserInfoBean, ivUserPortrait, false);
            holder.setText(R.id.tv_user_name, chatUserInfoBean.getName());
            holder.setVisible(R.id.tv_owner_flag, mOwnerId.equals(chatUserInfoBean.getUser_id()) ? View.VISIBLE : View.GONE);
            holder.setVisible(R.id.tv_user_name, View.VISIBLE);
            ivUserPortrait.getIvVerify().setVisibility(View.VISIBLE);
        }
    }

    public void setOwnerId(Long ownerId) {
        mOwnerId = ownerId;
    }
}
