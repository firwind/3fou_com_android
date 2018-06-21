package com.zhiyicx.thinksnsplus.modules.collect;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.common.utils.ActivityUtils;
import com.zhiyicx.thinksnsplus.modules.home.HomeActivity;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;

/**
 * @author LiuChao
 * @describe 收藏列表
 * @date 2017/4/13
 * @contact email:450127106@qq.com
 */

public class CollectListActivity extends TSActivity<CollectListPresenter, CollectListFragment> {
    @Override
    protected CollectListFragment getFragment() {
        return CollectListFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        // 如果CollectListFragment需要Presenter逻辑，就创建dagger
    }

    @Override
    public void onPause() {
        super.onPause();
        JZVideoPlayer jzVideoPlayer = JZVideoPlayerManager.getCurrentJzvd();
        if (jzVideoPlayer != null) {
            if (JZUtils.scanForActivity(jzVideoPlayer.getContext()) instanceof CollectListActivity) {
                jzVideoPlayer.onStateNormal();
            }
        }
    }


    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

}
