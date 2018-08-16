package com.zhiyicx.thinksnsplus.modules.home.message.notification.review;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.GroupOrFriendReviewBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

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

            }
        };

        return mAdapter;
    }
}
