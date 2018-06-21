package com.zhiyicx.thinksnsplus.modules.markdown_editor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.beans.PostDraftBean;
import com.zhiyicx.thinksnsplus.modules.circle.publish.PublishPostActivity;
import com.zhiyicx.thinksnsplus.modules.circle.publish.PublishPostFragment;

public abstract class BaseMarkdownActivity<P extends MarkdownPresenter,F extends MarkdownFragment> extends TSActivity<P, F> {

    @Override
    protected F getFragment() {
        return getYourFragment();
    }

    protected abstract F getYourFragment();

    @Override
    protected void componentInject() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }

    /**
     * 草稿箱发帖
     * @param context
     * @param draftBean
     */
    public static void startActivityForPublishPostInDraft(Context context, PostDraftBean draftBean) {
        Intent intent = new Intent(context, PublishPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PublishPostFragment.BUNDLE_DRAFT_DATA, draftBean);
        bundle.putParcelable(MarkdownFragment.BUNDLE_SOURCE_DATA, draftBean.getCircleInfo());
        bundle.putBoolean(PublishPostFragment.BUNDLE_ISOUT_BOOLEAN, draftBean.getIsOutCircle());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 圈内发帖
     *
     * @param context
     * @param circleInfo 圈子信息
     */
    public static void startActivityForPublishPostInCircle(Context context, CircleInfo circleInfo) {
        Intent intent = new Intent(context, PublishPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(MarkdownFragment.BUNDLE_SOURCE_DATA, circleInfo);
        bundle.putBoolean(PublishPostFragment.BUNDLE_ISOUT_BOOLEAN, false);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * 圈外发帖
     *
     * @param context
     */
    public static void startActivityForPublishPostOutCircle(Context context) {
        Intent intent = new Intent(context, PublishPostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(PublishPostFragment.BUNDLE_ISOUT_BOOLEAN, true);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
