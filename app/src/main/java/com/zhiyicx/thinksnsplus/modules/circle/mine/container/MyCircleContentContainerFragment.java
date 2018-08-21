package com.zhiyicx.thinksnsplus.modules.circle.mine.container;

import android.support.v4.app.Fragment;
import android.view.View;

import com.zhiyicx.baseproject.base.TSViewPagerFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.MyJoinedCircleFragment;
import com.zhiyicx.thinksnsplus.modules.circle.mine.joined.MyWaitAuditCircleFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Catherine
 * @describe
 * @date 2017/8/28
 * @contact email:648129313@qq.com
 */

public class MyCircleContentContainerFragment extends TSViewPagerFragment {

    @Override
    protected boolean setUseSatusbar() {
        return true;
    }

    @Override
    protected boolean setUseStatusView() {
        return false;
    }

    @Override
    protected boolean isAdjustMode() {
        return false;
    }

    @Override
    protected List<String> initTitles() {
        return Arrays.asList(getResources().getStringArray(R.array.circle_mine_type));
    }

    @Override
    protected List<Fragment> initFragments() {
        if (mFragmentList == null) {
            mFragmentList = new ArrayList();
            mFragmentList.add(MyJoinedCircleFragment.newInstance(false));
            mFragmentList.add(MyWaitAuditCircleFragment.newInstance(false));
        }
        return mFragmentList;
    }


    @Override
    protected void initViewPager(View rootView) {
        super.initViewPager(rootView);

        mTsvToolbar.setLeftImg(0);
        mTsvToolbar.showDivider(false);
        mTsvToolbar.setPadding(getResources().getDimensionPixelOffset(R.dimen.spacing_mid), 0, getResources().getDimensionPixelOffset(R.dimen
                .spacing_mid), 0);
    }

    @Override
    protected void initData() {

    }
}
