package com.zhiyicx.thinksnsplus.modules.shortvideo.clipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.tym.shortvideo.media.VideoInfo;
import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.shortvideo.cover.CoverActivity;

/**
 * @author Jliuer
 * @Date 18/03/28 11:25
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TrimmerActivity extends TSActivity {

    private static void startTrimmerActivity(Context context, String videoPath) {
        if (!TextUtils.isEmpty(videoPath)) {
            Bundle bundle = new Bundle();
            bundle.putString(TrimmerFragment.PATH, videoPath);
            Intent intent = new Intent(context, TrimmerActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        }
    }

    public static void startTrimmerActivity(Context context, VideoInfo video) {
        Bundle bundle = new Bundle();
        bundle.putString(TrimmerFragment.PATH, video.getPath());
        bundle.putParcelable(TrimmerFragment.VIDEO, video);
        Intent intent = new Intent(context, TrimmerActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected Fragment getFragment() {
        return TrimmerFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {

    }
}
