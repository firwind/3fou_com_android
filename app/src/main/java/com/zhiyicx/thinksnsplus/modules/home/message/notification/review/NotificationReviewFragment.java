package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.jakewharton.rxbinding.view.RxView;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.common.widget.popwindow.CustomPopupWindow.POPUPWINDOW_ALPHA;

/**
 * author: huwenyong
 * date: 2018/8/16 11:59
 * description:
 * version:
 */

public class NotificationReviewFragment extends TSListFragment<NotificationReviewContract.Presenter,GroupOrFriendReviewBean>
        implements NotificationReviewContract.View{

    private ActionPopupWindow mClearReviewPop;

    public static NotificationReviewFragment newInstance(int type){
        NotificationReviewFragment fragment = new NotificationReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(IntentKey.NOTIFICATION_REVIEW_TYPE,type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean isLayzLoad() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return isFriendReview()?"好友申请":"加群申请";
    }

    @Override
    protected int setRightImg() {
        return R.mipmap.topbar_more_black;
    }

    @Override
    protected void setRightClick() {
        super.setRightClick();

        showClearNotificationReviewPop();

    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return isFriendReview()?getFriendReviewAdapter():getGroupReviewAdapter();
    }


    @Override
    protected Long getMaxId(@NotNull List<GroupOrFriendReviewBean> data) {
        return Long.valueOf(mListDatas.size());
    }

    /**
     * 前往用户个人中心
     */
    private void toUserCenter(Context context, UserInfoBean userInfoBean) {
        PersonalCenterFragment.startToPersonalCenter(context, userInfoBean);
    }

    /**
     * 清空验证信息弹窗
     */
    private void showClearNotificationReviewPop(){
        if(null == mClearReviewPop){
            mClearReviewPop = ActionPopupWindow
                    .builder()
                    .item1Str("清空验证信息")
                    .item1Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(POPUPWINDOW_ALPHA)
                    .with(mActivity)
                    .item1ClickListener(() -> {
                        mClearReviewPop.hide();
                        mPresenter.clearApplyList();
                    })
                    .bottomClickListener(() -> mClearReviewPop.hide())
                    .build();
        }
        mClearReviewPop.show();
    }


    @Override
    public boolean isFriendReview() {
        return getArguments().getInt(IntentKey.NOTIFICATION_REVIEW_TYPE,IntentKey.NOTIFICATION_REVIEW_FRIEND)
                == IntentKey.NOTIFICATION_REVIEW_FRIEND;
    }

    /**
     * 加群申请adapter
     * @return
     */
    private CommonAdapter getGroupReviewAdapter(){

        CommonAdapter<GroupOrFriendReviewBean> mAdapter = new CommonAdapter<GroupOrFriendReviewBean>(mActivity,
                R.layout.item_notification_review,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, GroupOrFriendReviewBean groupOrFriendReviewBean, int position) {

                // 添加点击事件
                RxView.clicks(holder.getView(R.id.iv_headpic))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> toUserCenter(getContext(), groupOrFriendReviewBean.getUser_data()));

                ImageUtils.loadCircleUserHeadPic(groupOrFriendReviewBean.getUser_data(), holder.getView(R.id.iv_headpic));
                holder.getTextView(R.id.tv_name).setText(groupOrFriendReviewBean.getGroup_data().getName());
                holder.getTextView(R.id.tv_reason).setText( (TextUtils.isEmpty(groupOrFriendReviewBean.getInformation())?
                        "申请加入群":"理由："+groupOrFriendReviewBean.getInformation()) );
                try {
                    holder.getTextView(R.id.tv_time).setText(TimeUtils.getTimeFriendlyNormal(
                            TimeUtils.string2MillisDefaultLocal(groupOrFriendReviewBean.getCreated_at())));
                }catch (Exception e){}

                if(groupOrFriendReviewBean.getStatus() != 0){
                    String reviewName = "";//审核的人不为空，并且不是本人审核的，加上审核人的名字
                    if(null != groupOrFriendReviewBean.getShenhe_user_data() &&
                            groupOrFriendReviewBean.getShenhe_user_data().getUser_id() != AppApplication.getMyUserIdWithdefault())
                        reviewName = groupOrFriendReviewBean.getShenhe_user_data().getName();
                    holder.getTextView(R.id.tv_state).setText(reviewName+(groupOrFriendReviewBean.getStatus() == 1 ? "已同意" : "已拒绝"));
                }else {
                    holder.getTextView(R.id.tv_state).setText("等待验证");
                }

                //自己提交的群组申请
                if(groupOrFriendReviewBean.getUser_data().getUser_id().equals(String.valueOf(AppApplication.getMyUserIdWithdefault()))){
                    holder.getTextView(R.id.tv_reason).setText("已发送验证消息");
                    holder.getTextView(R.id.tv_agree).setVisibility(View.GONE);
                    //holder.getTextView(R.id.tv_reject).setVisibility(View.GONE);
                    holder.getTextView(R.id.tv_state).setVisibility(View.VISIBLE);
                }else {//自己收到的群组申请
                    holder.getTextView(R.id.tv_state).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.GONE:View.VISIBLE);
                    holder.getTextView(R.id.tv_agree).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                    //holder.getTextView(R.id.tv_reject).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                }


                holder.getView(R.id.tv_agree).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,true));
                //holder.getView(R.id.tv_reject).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,false));

            }
        };

        return mAdapter;
    }

    /**
     * 好友申请adapter
     * @return
     */
    private CommonAdapter getFriendReviewAdapter(){

        CommonAdapter<GroupOrFriendReviewBean> mAdapter = new CommonAdapter<GroupOrFriendReviewBean>(mActivity,
                R.layout.item_notification_review,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, GroupOrFriendReviewBean groupOrFriendReviewBean, int position) {

                // 添加点击事件
                RxView.clicks(holder.getView(R.id.iv_headpic))
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                        .subscribe(aVoid -> toUserCenter(getContext(), groupOrFriendReviewBean.getFriend_data()));

                ImageUtils.loadCircleUserHeadPic(groupOrFriendReviewBean.getFriend_data(), holder.getView(R.id.iv_headpic));
                holder.getTextView(R.id.tv_name).setText(groupOrFriendReviewBean.getFriend_data().getName());
                holder.getTextView(R.id.tv_reason).setText( (TextUtils.isEmpty(groupOrFriendReviewBean.getInformation())?
                        "请求加你为好友":"理由："+groupOrFriendReviewBean.getInformation()) );
                try {
                    holder.getTextView(R.id.tv_time).setText(TimeUtils.getTimeFriendlyNormal(
                            TimeUtils.string2MillisDefaultLocal(groupOrFriendReviewBean.getCreated_at())));
                }catch (Exception e){}

                if(groupOrFriendReviewBean.getStatus() != 0){
                    holder.getTextView(R.id.tv_state).setText(groupOrFriendReviewBean.getStatus() == 1 ? "已同意" : "已拒绝");
                }else {
                    holder.getTextView(R.id.tv_state).setText("等待验证");
                }

                //自己提交的好友申请
                if(groupOrFriendReviewBean.getUser_id().equals(String.valueOf(AppApplication.getMyUserIdWithdefault()))){
                    holder.getTextView(R.id.tv_reason).setText("已发送验证消息");
                    holder.getTextView(R.id.tv_agree).setVisibility(View.GONE);
                    //holder.getTextView(R.id.tv_reject).setVisibility(View.GONE);
                    holder.getTextView(R.id.tv_state).setVisibility(View.VISIBLE);
                }else {//自己收到的好友申请
                    holder.getTextView(R.id.tv_state).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.GONE:View.VISIBLE);
                    holder.getTextView(R.id.tv_agree).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                    //holder.getTextView(R.id.tv_reject).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                }


                holder.getView(R.id.tv_agree).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,true));
                //holder.getView(R.id.tv_reject).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,false));

            }
        };

        return mAdapter;
    }


}
