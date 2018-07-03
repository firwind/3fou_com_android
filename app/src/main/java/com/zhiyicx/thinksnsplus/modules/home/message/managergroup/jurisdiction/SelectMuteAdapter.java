package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/23 9:50
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

public class SelectMuteAdapter extends CommonAdapter<UserInfoBean> {
    private OnUserSelectedListener mListener;
    private static final int STATE_SELECTED = 1;
    private static final int STATE_UNSELECTED = 0;
    private static final int STATE_CAN_NOT_BE_CHANGED = -1;
    private boolean mIsMute;

    public SelectMuteAdapter(Context context, List<UserInfoBean> datas, SelectMuteAdapter.OnUserSelectedListener listener, boolean isMute) {
        super(context, R.layout.item_select_friends, datas);
        this.mListener = listener;
        this.mIsMute = isMute;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        ImageView cbFriends = holder.getView(R.id.cb_friends);
        UserAvatarView ivUserPortrait = holder.getView(R.id.iv_user_portrait);
        TextView tvUserName = holder.getTextView(R.id.tv_user_name);
        ImageUtils.loadCircleUserHeadPic(userInfoBean, ivUserPortrait);
        tvUserName.setText(userInfoBean.getName());
        setSelectedState(cbFriends, userInfoBean);
        RxView.clicks(holder.getConvertView())
                .subscribe(aVoid -> {
                    if (!mIsMute) {
                        if (mListener != null && userInfoBean.getIsSelected() != STATE_CAN_NOT_BE_CHANGED) {
                            if (userInfoBean.getIsSelected() == STATE_SELECTED) {
                                userInfoBean.setIsSelected(STATE_UNSELECTED);
                            } else {
                                userInfoBean.setIsSelected(STATE_SELECTED);
                            }
                            setSelectedState(cbFriends, userInfoBean);
                            mListener.onUserSelected(userInfoBean);
                        }
                    } else {
                        if (mListener != null && userInfoBean.getMember_mute() != STATE_CAN_NOT_BE_CHANGED) {
                            if (userInfoBean.getMember_mute() == STATE_SELECTED) {
                                userInfoBean.setMember_mute(STATE_UNSELECTED);
                            } else {
                                userInfoBean.setMember_mute(STATE_SELECTED);
                            }
                            setSelectedState(cbFriends, userInfoBean);
                            mListener.onUserSelected(userInfoBean);
                        }
                    }
                });
        RxView.clicks(ivUserPortrait)
                .subscribe(aVoid -> {
                    // 个人中心
                    PersonalCenterFragment.startToPersonalCenter(holder.getConvertView()
                            .getContext(), userInfoBean);
                });
    }

    /**
     * 设置选中状态
     *
     * @param imageView    icon
     * @param userInfoBean user
     */
    private void setSelectedState(ImageView imageView, UserInfoBean userInfoBean) {
        if (!mIsMute) {
            switch (userInfoBean.getIsSelected()) {
                case STATE_UNSELECTED:
                    imageView.setImageResource(R.mipmap.msg_box);
                    break;
                case STATE_SELECTED:
                    imageView.setImageResource(R.mipmap.msg_box_choose_now);
                    break;
            }
        } else {
            switch (userInfoBean.getMember_mute()) {
                case STATE_SELECTED:
                    imageView.setImageResource(R.mipmap.msg_box_choose_now);
                    break;
                case STATE_UNSELECTED:
                    imageView.setImageResource(R.mipmap.msg_box);
                    break;
                case STATE_CAN_NOT_BE_CHANGED:
                    imageView.setImageResource(R.mipmap.msg_box_choose_before);
                    break;
                default:
            }
        }
    }

    /**
     * 选中item监听
     */
    public interface OnUserSelectedListener {
        /**
         * 选中好友
         *
         * @param userInfoBean 用户信息
         */
        void onUserSelected(UserInfoBean userInfoBean);
    }
}
