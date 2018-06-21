package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.follow_fans.FollowFansListContract;
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
 * @Describe 黑名单列表 item
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
public class BlackListAdapter extends CommonAdapter<UserInfoBean> {
    private BlackListContract.Presenter mPresenter;

    BlackListAdapter(Context context, int layoutId, List<UserInfoBean> datas, BlackListContract.Presenter presenter) {
        super(context, layoutId, datas);
        this.mPresenter = presenter;
    }

    @Override
    protected void convert(ViewHolder holder, UserInfoBean userInfoBean, int position) {
        setItemData(holder, userInfoBean, position);
    }

    private void setItemData(final ViewHolder holder, final UserInfoBean userInfoBean1, final int position) {

        RxView.clicks(holder.getView(R.id.tv_remove_black_list))
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    // 黑名单操作,移除黑名单
                    mPresenter.removeBlackList(position);
                });

        // 设置用户名，用户简介
        holder.setText(R.id.tv_name, userInfoBean1.getName());

        holder.setText(R.id.tv_user_signature, TextUtils.isEmpty(userInfoBean1.getIntro()) ? getContext().getString(R.string.intro_default) :
                userInfoBean1.getIntro());
        // 头像加载
        ImageUtils.loadCircleUserHeadPic(userInfoBean1, holder.getView(R.id.iv_headpic));
        // 添加点击事件
        RxView.clicks(holder.getConvertView())
                .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(aVoid -> toUserCenter(getContext(), userInfoBean1));
    }


    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

}
