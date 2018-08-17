package com.zhiyicx.thinksnsplus.modules.follow_fans;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.imsdk.core.ChatType;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.home.mine.friends.verify.VerifyFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/20
 * @contact email:450127106@qq.com
 */

public class FollowFansListAdapter extends CommonAdapter<UserInfoBean> {
    private int pageType;
    private FollowFansListContract.Presenter mPresenter;

    public FollowFansListAdapter(Context context, int layoutId, List<UserInfoBean> datas, int pageType, FollowFansListContract.Presenter presenter) {
        super(context, layoutId, datas);
        this.pageType = pageType;
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        setItemData(holder, userInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final UserInfoBean userInfoBean1, final int position) {
        if (userInfoBean1 == null) {
            // 这种情况一般不会发生，为了防止崩溃，做处理
            return;
        }
        /*if(userInfoBean1.isFollowing()&&userInfoBean1.isFollower()){
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed_eachother);
        }else if(userInfoBean1.isFollower()){
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_followed);
        }else {
            holder.setImageResource(R.id.iv_user_follow, R.mipmap.ico_me_follow);
        }*/
        holder.getTextView(R.id.tv_follow).setText(userInfoBean1.isFollower()?"已关注":"+ 关注");
        holder.getTextView(R.id.tv_friend).setText(userInfoBean1.isIs_my_friend()?"聊天":"+ 加友");

        RxView.clicks(holder.getView(R.id.tv_follow/*iv_user_follow*/))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    // 添加关注，或者取消关注
                    // 关注列表的逻辑操作：关注，互相关注 ---》未关注
                    // 粉丝列表的逻辑操作：互相关注 ---》未关注

                    if(userInfoBean1.isFollowing()&&userInfoBean1.isFollower()){
                        mPresenter.cancleFollowUser(position, userInfoBean1);
                    }else if(userInfoBean1.isFollower()){
                        mPresenter.cancleFollowUser(position, userInfoBean1);
                    }else {
                        mPresenter.followUser(position, userInfoBean1);
                    }

                });

        RxView.clicks(holder.getView(R.id.tv_friend))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    // 添加关注，或者取消关注
                    // 关注列表的逻辑操作：关注，互相关注 ---》未关注
                    // 粉丝列表的逻辑操作：互相关注 ---》未关注

                    if(userInfoBean1.isIs_my_friend()){
                        ChatActivity.startChatActivity(mContext,String.valueOf(userInfoBean1.getUser_id()), EaseConstant.CHATTYPE_SINGLE);
                    }else {
                        if(userInfoBean1.getFriends_set() == 0){
                            mPresenter.addFriend(position, userInfoBean1);
                        }else if(userInfoBean1.getFriends_set() == 1){
                            VerifyFriendsActivity.startVerifyFriendsActivity(mContext,String.valueOf(userInfoBean1.getUser_id()));
                        }else {
                            ToastUtils.showToast(mContext,"该用户已拒绝好友申请！");
                        }
                    }

                });


        /**
         * 如果关注粉丝列表中出现了自己，需要隐藏关注按钮
         */
        holder.getView(R.id.tv_follow/*iv_user_follow*/).setVisibility(
                userInfoBean1.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id() ? View.GONE : View.VISIBLE);
        holder.getView(R.id.tv_friend).setVisibility(
                userInfoBean1.getUser_id() == AppApplication.getmCurrentLoginAuth().getUser_id() ? View.GONE : View.VISIBLE);
        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean1.getName());

        holder.setText(R.id.tv_user_signature, TextUtils.isEmpty(userInfoBean1.getIntro())?getContext().getString(R.string.intro_default):userInfoBean1.getIntro());
        // 修改点赞数量颜色
        String digCountString = userInfoBean1.getExtra().getLikes_count()+"";
        // 当前没有获取到点赞数量，设置为0，否则ColorPhrase会抛出异常
        if (TextUtils.isEmpty(digCountString)) {
            digCountString = 0 + "";
        }
        String digContent = "点赞 " + "<" + digCountString + ">";
        CharSequence charSequence = ColorPhrase.from(digContent).withSeparator("<>")
                .innerColor(ContextCompat.getColor(getContext(), R.color.themeColor))
                .outerColor(ContextCompat.getColor(getContext(), R.color.normal_for_assist_text))
                .format();
        TextView digCount = holder.getView(R.id.tv_dig_count);
        digCount.setText(charSequence);
        // 头像加载
        ImageUtils.loadCircleUserHeadPic(userInfoBean1, holder.getView(R.id.iv_headpic));
        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        toUserCenter(getContext(), userInfoBean1);
                    }
                });
    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

}
