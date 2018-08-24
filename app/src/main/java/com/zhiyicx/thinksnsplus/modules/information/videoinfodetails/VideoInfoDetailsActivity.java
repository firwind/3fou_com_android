package com.zhiyicx.thinksnsplus.modules.information.videoinfodetails;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.InfoListDataBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.information.infodetails.DaggerInfoDetailsComponent;

import cn.jzvd.JZVideoPlayer;

import static com.zhiyicx.thinksnsplus.modules.information.infodetails.InfoDetailsFragment.BUNDLE_INFO;

/**
 * @Author Jliuer
 * @Date 2017/03/07
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class VideoInfoDetailsActivity extends TSActivity<VideoInfoDetailsPresenter, VideoInfoDetailsFragment> {

    @Override
    protected VideoInfoDetailsFragment getFragment() {
        return VideoInfoDetailsFragment.newInstance(getIntent().getParcelableExtra(IntentKey.DATA),
                getIntent().getIntExtra(IntentKey.VIDEO_STATE,-1));
    }

    @Override
    protected void componentInject() {
        DaggerVideoInfoDetailsComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .videoInfoDetailsPresenterMudule(new VideoInfoDetailsPresenterMudule(mContanierFragment))
                .shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
        mContanierFragment.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (JZVideoPlayer.backPress()) {
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.goOnPlayOnPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }


    public static void startVideoInfoDetailsActivity(Context mContext,InfoListDataBean data, int videoState){
        Intent intent = new Intent(mContext,VideoInfoDetailsActivity.class);
        intent.putExtra(IntentKey.DATA, (Parcelable) data);
        intent.putExtra(IntentKey.VIDEO_STATE,videoState);
        mContext.startActivity(intent);
    }

}
