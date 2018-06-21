package com.zhiyicx.thinksnsplus.modules.circle.search.onlypost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.data.beans.CircleInfo;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseCircleRepository;
import com.zhiyicx.thinksnsplus.modules.circle.detailv2.BaseCircleDetailFragment;
import com.zhiyicx.thinksnsplus.modules.circle.search.SearchCirclePostFragment;
import com.zhiyicx.thinksnsplus.modules.circle.search.container.CircleSearchContainerPresenter;

/**
 * @Describe 圈子搜索容器
 * @Author zl
 * @Date 2017/12/7
 * @Contact master.jungle68@gmail.com
 */
public class CirclePostSearchActivity extends TSActivity<CircleSearchContainerPresenter, SearchCirclePostFragment> {

    @Override
    protected void componentInject() {

    }

    @Override
    protected SearchCirclePostFragment getFragment() {
        return SearchOnlyCirclePostFragment.newInstance(BaseCircleRepository.CircleMinePostType.SEARCH,
                (CircleInfo)getIntent().getExtras().getSerializable(BaseCircleDetailFragment.CIRCLE));
    }

    /**
     * @param context not application context
     * @param circleInfo 圈子的信息
     */
    public static void startCircelPostSearchActivity(Context context, CircleInfo circleInfo) {

        Intent intent = new Intent(context, CirclePostSearchActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(BaseCircleDetailFragment.CIRCLE, circleInfo);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }

}
