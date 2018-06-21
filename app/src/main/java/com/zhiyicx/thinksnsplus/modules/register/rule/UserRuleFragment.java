package com.zhiyicx.thinksnsplus.modules.register.rule;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;

import br.tiagohm.markdownview.MarkdownView;
import butterknife.BindView;

/**
 * @Author Jliuer
 * @Date 2017/10/27/13:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class UserRuleFragment extends TSFragment {

    public static final String RULE = "markdown_rule";

    @BindView(R.id.md_user_rule)
    MarkdownView mMarkdownView;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.scroll_view)
    View mScrollContainer;

    String mMarkDownRule;

    @Override
    protected String setCenterTitle() {
        return getString(R.string.user_rule_register);
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    public static UserRuleFragment newInstance(Bundle bundle) {
        UserRuleFragment fragment = new UserRuleFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {
        mMarkDownRule = getArguments().getString(RULE, RULE);
        if (!TextUtils.isEmpty(mMarkDownRule) && mMarkDownRule.contains("#")) {
            // 使用markdonw
            mMarkdownView.setVisibility(View.VISIBLE);
            mMarkdownView.loadMarkdown(mMarkDownRule);
        } else {
            // 普通文本
            mScrollContainer.setVisibility(View.VISIBLE);
            mTvContent.setText(mMarkDownRule);
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_user_rule;
    }
}
