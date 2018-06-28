package com.zhiyicx.thinksnsplus.modules.chat.record;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.common.utils.TimeUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.CustomLinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatRecord;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.widget.TSSearchView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/6/28 9:33
 * description:
 * version:
 */

public class ChatRecordFragment extends TSListFragment<ChatRecordContract.Presenter,ChatRecord> implements ChatRecordContract.View{

    @BindView(R.id.searchView)
    TSSearchView mSearchView;

    public static ChatRecordFragment newInstance(String conversationId){
        ChatRecordFragment fragment = new ChatRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IntentKey.CONVERSATION_ID,conversationId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.search_chat_record);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_tslist_with_search;
    }

    @Override
    public String getConversationId() {
        return getArguments().getString(IntentKey.CONVERSATION_ID);
    }

    @Override
    protected void initView(View rootView) {
        super.initView(rootView);
        mSearchView.setVisibility(View.VISIBLE);
        mSearchView.setOnSearchClickListener(new TSSearchView.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPresenter == null) {
                    return;
                }
                // 显示搜索结果
                onRefresh(null);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        onRefresh(null);
    }

    @Override
    protected boolean isLoadingMoreEnable() {
        return false;
    }

    @Override
    protected boolean isRefreshEnable() {
        return false;
    }

    @Override
    protected boolean isNeedRefreshDataWhenComeIn() {
        return true;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new CustomLinearDecoration(0, getResources().getDimensionPixelSize(R.dimen
                .divider_line), 0, 0, ContextCompat.getDrawable(getContext(), R.drawable
                .shape_recyclerview_grey_divider));
    }

    @Override
    public String getSearchText() {
        return mSearchView.getText().toString();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary));
        CommonAdapter<ChatRecord> mAdapter = new CommonAdapter<ChatRecord>(mActivity,
                R.layout.item_message_list,mListDatas) {
            @Override
            protected void convert(ViewHolder holder, ChatRecord chatRecord, int position) {
                ImageUtils.loadUserHead(chatRecord.getUserInfo(), (UserAvatarView)holder.getView(R.id.iv_headpic), false);
                holder.setText(R.id.tv_name, chatRecord.getUserInfo().getName());
                holder.setText(R.id.tv_time, TimeUtils.getTimeFriendlyNormal(chatRecord.getEmMessage().getMsgTime()));
                //使用spannableString来实现字体部分变色
                String searchText = getSearchText();
                String content = ((EMTextMessageBody)chatRecord.getEmMessage().getBody()).getMessage();
                int index = content.indexOf(searchText);
                SpannableString spannableString = new SpannableString(content);
                spannableString.setSpan(colorSpan,index,index+searchText.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                holder.getTextView(R.id.tv_content).setText(spannableString);
            }
        };
        return mAdapter;
    }
}
