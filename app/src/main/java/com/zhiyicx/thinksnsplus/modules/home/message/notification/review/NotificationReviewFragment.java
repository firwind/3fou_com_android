package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/16 11:59
 * description:
 * version:
 */

public class NotificationReviewFragment extends TSListFragment<NotificationReviewContract.Presenter,GroupOrFriendReviewBean>
        implements NotificationReviewContract.View{

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
                }

                holder.getTextView(R.id.tv_state).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.GONE:View.VISIBLE);
                holder.getTextView(R.id.tv_agree).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);
                holder.getTextView(R.id.tv_reject).setVisibility(groupOrFriendReviewBean.getStatus() == 0 ? View.VISIBLE:View.GONE);

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
}
