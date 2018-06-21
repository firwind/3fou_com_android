package com.zhiyicx.thinksnsplus.modules.shortvideo.videostore;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;


/**
 * @author Jliuer
 * @Date 18/03/28 11:25
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoSelectActivity extends TSActivity {

    @Override
    protected Fragment getFragment() {
        return new VideoSelectFragment();
    }

    @Override
    protected void componentInject() {

    }

    public static void startVideoSelectActivity(Context context,boolean isRelaod){
        Intent intent=new Intent(context,VideoSelectActivity.class);
        intent.putExtra(VideoSelectFragment.IS_RELOAD,isRelaod);
        context.startActivity(intent);
    }
}
