package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.DynamicDetailBeanV2;
import com.zhiyicx.thinksnsplus.i.IntentKey;

import java.util.ArrayList;
import java.util.List;

/**
 * author: huwenyong
 * date: 2018/8/27 18:58
 * description:
 * version:
 */

public class SmallVideoActivity extends TSActivity<SmallVideoPresenter,SmallVideoFragment> {

    @Override
    protected SmallVideoFragment getFragment() {
        return SmallVideoFragment.newInstance(getIntent().getParcelableArrayListExtra(IntentKey.DATA),
                getIntent().getIntExtra(IntentKey.POSITION,0));
    }

    @Override
    protected void componentInject() {
        DaggerSmallVideoComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .smallVideoPresenterModule(new SmallVideoPresenterModule(mContanierFragment))
                .build()
                .inject(this);
    }


    public static void startSmallVideoActivity(Context mContext, List<DynamicDetailBeanV2> list,int position){
        Intent intent = new Intent(mContext,SmallVideoActivity.class);
        intent.putParcelableArrayListExtra(IntentKey.DATA, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(IntentKey.POSITION,position);
        mContext.startActivity(intent);
    }

}
