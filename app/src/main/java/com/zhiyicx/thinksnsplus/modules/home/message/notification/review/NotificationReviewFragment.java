package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        return getArguments().getInt(IntentKey.NOTIFICATION_REVIEW_TYPE,IntentKey.NOTIFICATION_REVIEW_FRIEND)
                == IntentKey.NOTIFICATION_REVIEW_FRIEND?"好友申请":"加群申请";
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

        CommonAdapter<GroupOrFriendReviewBean> mAdapter = new CommonAdapter<GroupOrFriendReviewBean>(mActivity,
                R.layout.item_notification_review,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, GroupOrFriendReviewBean groupOrFriendReviewBean, int position) {
                ImageUtils.loadCircleUserHeadPic(groupOrFriendReviewBean.getFriend_data(), holder.getView(R.id.iv_headpic));
                holder.getTextView(R.id.tv_name).setText(groupOrFriendReviewBean.getFriend_data().getName());
                holder.getTextView(R.id.tv_reason).setText("理由："+groupOrFriendReviewBean.getInformation());
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
                    holder.getTextView(R.id.tv_reject).setVisibility(View.GONE);
                    holder.getTextView(R.id.tv_state).setVisibility(View.VISIBLE);
                }else {//自己收到的好友申请
                    holder.getTextView(R.id.tv_state).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.GONE:View.VISIBLE);
                    holder.getTextView(R.id.tv_agree).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                    holder.getTextView(R.id.tv_reject).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                }


                holder.getView(R.id.tv_agree).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,true));
                holder.getView(R.id.tv_reject).setOnClickListener(v -> mPresenter.requestAgreeOrInjectApply(groupOrFriendReviewBean,false));

            }
        };

        return mAdapter;
    }


    @Override
    protected Long getMaxId(@NotNull List<GroupOrFriendReviewBean> data) {
        return Long.valueOf(mListDatas.size());
    }


    /**
     * 清空验证信息弹窗
     */
    private void showClearNotificationReviewPop(){
        if(null == mClearReviewPop){
            mClearReviewPop = ActionPopupWindow
                    .builder()
                    .item2Str(getString(R.string.ts_delete))
                    .item2Color(ContextCompat.getColor(getContext(), R.color.important_for_note))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(POPUPWINDOW_ALPHA)
                    .with(mActivity)
                    .item1ClickListener(() -> {
                        mClearReviewPop.hide();
                    })
                    .bottomClickListener(() -> mClearReviewPop.hide())
                    .build();
        }
        mClearReviewPop.show();
    }



}
