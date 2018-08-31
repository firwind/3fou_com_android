package com.zhiyicx.thinksnsplus.modules.information.smallvideo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.zhiyicx.baseproject.base.TSActivity;
import com.zhiyicx.baseproject.impl.share.ShareModule;
import com.zhiyicx.baseproject.impl.share.UmengSharePolicyImpl;
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
//        return SmallVideoFragment.newInstance(getIntent().getParcelableArrayListExtra(IntentKey.DATA),
//                getIntent().getIntExtra(IntentKey.POSITION,0));
        return SmallVideoFragment.newInstance(getIntent().getExtras());
    }

    @Override
    protected void componentInject() {
        DaggerSmallVideoComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .smallVideoPresenterModule(new SmallVideoPresenterModule(mContanierFragment))
                .shareModule(new ShareModule(this))
                .build()
                .inject(this);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UmengSharePolicyImpl.onActivityResult(requestCode, resultCode, data, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UmengSharePolicyImpl.onDestroy(this);
    }


    public static void startSmallVideoActivity(Context mContext, List<DynamicDetailBeanV2> list,int position){
        Intent intent = new Intent(mContext,SmallVideoActivity.class);
        intent.putParcelableArrayListExtra(IntentKey.DATA, (ArrayList<? extends Parcelable>) list);
        intent.putExtra(IntentKey.POSITION,position);
        mContext.startActivity(intent);
    }

    public static void startSmallVideoActivity(Context mContext, List<DynamicDetailBeanV2> list,int position,Long userId){
        Intent intent = new Intent(mContext,SmallVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(IntentKey.DATA, (ArrayList<? extends Parcelable>) list);
        bundle.putInt(IntentKey.POSITION,position);
        bundle.putLong(IntentKey.USER_ID,userId);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
