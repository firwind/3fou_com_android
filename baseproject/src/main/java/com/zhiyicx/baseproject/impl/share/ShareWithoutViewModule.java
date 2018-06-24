package com.zhiyicx.baseproject.impl.share;

import android.app.Activity;

import com.zhiyicx.common.thridmanager.share.SharePolicy;

import dagger.Module;
import dagger.Provides;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 12:58
 *     desc :
 *     version : 1.0
 * <pre>
 */
@Module
public class ShareWithoutViewModule {

    private Activity mActivity;

    public ShareWithoutViewModule(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Provides
    SharePolicy provideSharePolicy(){
        return new UmengSharePolicyWithoutViewImpl(mActivity);
    }

}
