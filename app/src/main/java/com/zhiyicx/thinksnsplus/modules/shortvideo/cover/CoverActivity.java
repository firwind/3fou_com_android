package com.zhiyicx.thinksnsplus.modules.shortvideo.cover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.modules.shortvideo.record.RecordFragment;

import java.util.ArrayList;

import static com.zhiyicx.thinksnsplus.modules.shortvideo.cover.CoverFragment.REQUEST_COVER_CODE;

/**
 * @Author Jliuer
 * @Date 2018/04/07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class CoverActivity extends TSActivity<AppBasePresenter, CoverFragment> {
    @Override
    protected CoverFragment getFragment() {
        return CoverFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }

    /**
     *
     * @param context
     * @param path 视频路径，支持多个
     * @param pre 是否是动态发布页的预览
     * @param hasFilter 是否有滤镜界面
     */
    public static void startCoverActivity(Context context, ArrayList<String> path,boolean pre,boolean hasFilter,boolean back2record) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(CoverFragment.PATH, path);
        bundle.putBoolean(CoverFragment.PREVIEW,pre);
        bundle.putBoolean(CoverFragment.FILTER,hasFilter);
        bundle.putBoolean(CoverFragment.BACKTORECORD,back2record);
        Intent intent = new Intent(context, CoverActivity.class);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intent, REQUEST_COVER_CODE);
            return;
        }
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        mContanierFragment.onBackPressed();
    }
}
