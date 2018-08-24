package com.zhiyicx.thinksnsplus.modules.information.videoinfodetails;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2017/03/24
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class VideoInfoDetailsPresenterMudule {

    VideoInfoDetailsConstract.View mView;

    public VideoInfoDetailsPresenterMudule(VideoInfoDetailsConstract.View view) {
        mView = view;
    }

    @Provides
    VideoInfoDetailsConstract.View provideInfoDetailsView() {
        return mView;
    }

}
