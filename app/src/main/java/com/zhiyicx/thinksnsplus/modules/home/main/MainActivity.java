package com.zhiyicx.thinksnsplus.modules.home.main;
/*
 * 文件名：
 * 创建者：zhangl
 * 描  述：
 * 时  间：2018/6/13 18:14
 * 修改者：
 * 修改备注：
 * 修改时间：
 * 版  权：互动科技
 */

import android.support.v4.app.Fragment;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.modules.dynamic.list.DynamicFragment;

public class MainActivity extends TSActivity implements DynamicFragment.OnCommentClickListener {
    @Override
    protected Fragment getFragment() {
        return MainFragment.newInstance(this);
    }

    @Override
    protected void componentInject() {

    }

    @Override
    public void onButtonMenuShow(boolean isShow) {

    }
}
