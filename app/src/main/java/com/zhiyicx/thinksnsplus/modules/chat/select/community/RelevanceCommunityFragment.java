package com.zhiyicx.thinksnsplus.modules.chat.select.community;
/*
 * 文件名：
 * 创建者：zl
 * 时  间：2018/8/21 0021
 * 描  述：
 * 版  权：九曲互动
 * 
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.edittext.DeleteEditText;
import com.zhiyicx.common.utils.ColorPhrase;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.modules.circle.create.CreateCircleActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.zhiyicx.common.config.ConstantConfig.JITTER_SPACING_TIME;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_COMMUNITY;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_DELETE_QUIT;
import static com.zhiyicx.thinksnsplus.i.IntentKey.GROUP_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.select.community.RelevanceCommunityActivity.COMMUNITY_ID;

public class RelevanceCommunityFragment extends TSListFragment<RelevanceCommunityContract.Presenter, CircleInfo> implements RelevanceCommunityContract.View {
    @BindView(R.id.et_search_community)
    DeleteEditText mSearchCommunity;

    public static RelevanceCommunityFragment getInstance(Bundle bundle) {
        RelevanceCommunityFragment fragment = new RelevanceCommunityFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private String mGroupId;
    private String mCommunityId;

    @Override
    protected boolean isNeedRefreshAnimation() {
        return true;
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return true;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    protected float getItemDecorationSpacing() {
        return DEFAULT_LIST_ITEM_SPACING;
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        CommonAdapter adapter = new CommonAdapter<CircleInfo>(getActivity(), R.layout.item_community_list, mListDatas) {
            @Override
            protected void convert(ViewHolder holder, CircleInfo circleInfo, int position) {
                // 封面
                ImageView circleCover = holder.getView(R.id.iv_circle_cover);
                holder.setText(R.id.tv_circle_name, circleInfo.getName());
                // 帖子数量
                TextView circleFeedCount = holder.getView(R.id.tv_circle_feed_count);
                // 成员数量
                TextView circleMemberCount = holder.getView(R.id.tv_circle_follow_count);
                Context context = holder.getConvertView().getContext();
                // 设置封面
                Glide.with(context)
                        .load(circleInfo.getAvatar())
                        .error(R.drawable.shape_default_image)
                        .placeholder(R.drawable.shape_default_image)
                        .into(circleCover);
                String feedCountNumber = ConvertUtils.numberConvert(circleInfo.getPosts_count());
                String feedContent = context.getString(R.string.circle_post) + " " + "<" +
                        feedCountNumber + ">";
                CharSequence feedString = ColorPhrase.from(feedContent).withSeparator("<>")
                        .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                        .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                        .format();
                circleFeedCount.setText(feedString);
                // 设置订阅人数
                String followCountNumber = ConvertUtils.numberConvert(circleInfo.getUsers_count());
                String followContent = context.getString(R.string.circle_member) + " " + "<" +
                        followCountNumber + ">";
                CharSequence followString = ColorPhrase.from(followContent).withSeparator("<>")
                        .innerColor(ContextCompat.getColor(context, R.color.themeColor))
                        .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
                        .format();
                circleMemberCount.setText(followString);

                TextView mRelevance = holder.getView(R.id.tv_community_relevance);
                if (getCommunityId().length() > 0 && circleInfo.getId().equals(Long.valueOf(getCommunityId()))) {
                    mRelevance.setText("已关联");
                }
                RxView.clicks(mRelevance)
                        .throttleFirst(JITTER_SPACING_TIME, TimeUnit.SECONDS)
                        .subscribe(aVoid -> {
                            if (getCommunityId().length() > 0 && circleInfo.getId().equals(Long.valueOf(getCommunityId()))) {
                                showSnackErrorMessage("不能重复关联同一个社区");
                                return;
                            }
                            mPresenter.relevanceCommunity(circleInfo.getId());
                        });
            }
        };
        return adapter;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_relevance_community;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.group);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean useEventBus() {
        return super.useEventBus();
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        if (prompt == Prompt.SUCCESS) {
            getActivity().finish();
        }
        EventBus.getDefault().post(true, EventBusTagConfig.EVENT_IM_GROUP_UPDATE_INFO);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSearchCommunity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getCommunityListData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        if (getArguments() != null) {
            mGroupId = getArguments().getString(GROUP_ID);
            mCommunityId = getArguments().getString(COMMUNITY_ID);
        }
        getCommunityListData();
    }

    @Override
    public String getSearchKeyWord() {
        return mSearchCommunity.getText().toString();
    }

    @Override
    public String getGroupId() {
        return mGroupId;
    }

    @Override
    public String getCommunityId() {
        return mCommunityId;
    }

    /**
     * 获取社区列表
     */
    private void getCommunityListData() {
        if (mPresenter != null) {
//            mRefreshlayout.autoRefresh(0)/0p;
            mPresenter.requestNetData(DEFAULT_PAGE_MAX_ID, false);
        }
    }


    @OnClick(R.id.iv_community)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_community:
                CreateCircleActivity.startCreateActivity(getContext());
                break;
        }
    }
}
