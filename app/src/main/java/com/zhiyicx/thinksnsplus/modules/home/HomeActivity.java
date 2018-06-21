package com.zhiyicx.thinksnsplus.modules.home;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.config.JpushMessageTypeConfig;
import com.zhiyicx.thinksnsplus.data.beans.JpushMessageBean;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.NetChangeReceiver;
import com.zhiyicx.thinksnsplus.modules.shortvideo.helper.ZhiyiVideoView;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

import static com.zhiyicx.thinksnsplus.modules.dynamic.detail.DynamicDetailFragment.DYNAMIC_VIDEO_STATE;


/**
 * @Describe
 * @Author zl
 * @Date 2016/12/22
 * @Contact master.jungle68@gmail.com
 */

public class HomeActivity extends TSActivity {
    public static final String BUNDLE_JPUSH_MESSAGE = "jpush_message";
    private NetChangeReceiver mNetChangeReceiver;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            JpushMessageBean jpushMessageBean = bundle.getParcelable(BUNDLE_JPUSH_MESSAGE);
            if (jpushMessageBean != null && jpushMessageBean.getType() != null) {
                switch (jpushMessageBean.getType()) {
                    case JpushMessageTypeConfig.JPUSH_MESSAGE_TYPE_SYSTEM:
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MINE);
                        break;
                    default:
                        ((HomeContract.View) mContanierFragment).checkBottomItem(HomeFragment.PAGE_MESSAGE);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNetChangeReceiver == null) {
            mNetChangeReceiver = new NetChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(mNetChangeReceiver, filter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer jzVideoPlayer = JZVideoPlayerManager.getCurrentJzvd();
        if (jzVideoPlayer != null) {
            if (JZUtils.scanForActivity(jzVideoPlayer.getContext()) instanceof HomeActivity) {
                jzVideoPlayer.onStateNormal();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
        if(mNetChangeReceiver!=null){
            unregisterReceiver(mNetChangeReceiver);
        }
    }

    @Override
    protected void componentInject() {
    }

    @Override
    protected Fragment getFragment() {
        return HomeFragment.newInstance(getIntent().getExtras());
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        ActivityUtils.goHome(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode, resultCode, data);
    }



}
