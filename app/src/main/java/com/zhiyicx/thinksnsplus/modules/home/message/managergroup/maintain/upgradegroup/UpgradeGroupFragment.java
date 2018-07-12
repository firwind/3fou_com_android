package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradegroup;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.trycatch.mysnackbar.Prompt;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.impl.imageloader.glide.transformation.GlideCircleTransform;
import com.zhiyicx.common.widget.NoPullRecycleView;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.UpgradeTypeBean;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.maintain.upgradepay.UpgradePayActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.zhiyicx.thinksnsplus.i.IntentKey.GROUP_ID;

/**
 * Created by zhang on 2018/6/28.
 */

public class UpgradeGroupFragment extends TSFragment<UpgradeGroupContract.Presenter> implements UpgradeGroupContract.View {
    @BindView(R.id.vp_upgrade_group_viewpager)
    ViewPager mViewPager;
    Unbinder unbinder;
    @BindView(R.id.ll_upgrade_viewpager)
    LinearLayout mUpgradeViewpager;
    String mGroupId;
    public static final UpgradeGroupFragment newInstance(Bundle bundle) {
        UpgradeGroupFragment fragment = new UpgradeGroupFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }


    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_info_upgrade);
    }

    @Override
    protected void initData() {
        mGroupId = getArguments().getString(GROUP_ID);
        mPresenter.getUpgradeType();//从获取升级群Bean
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_upgrade_group;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private LayoutInflater inflater;
    private List<UpgradeTypeBean> data;
    ArrayList<View> views = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void getUpgradeTypes(List<UpgradeTypeBean> data) {
        inflater = getActivity().getLayoutInflater();
        for (int i = 0; i < data.size(); i++) {
            View view = inflater.inflate(R.layout.item_upgrade_group, null);
            views.add(view);
        }

        this.data = data;
        // 1.设置幕后item的缓存数目
        mViewPager.setOffscreenPageLimit(3);
        // 2.设置页与页之间的间距
        mViewPager.setPageMargin(50);
        // 3.将父类的touch事件分发至viewPgaer，否则只能滑动中间的一个view对象
        mUpgradeViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        mViewPager.setAdapter(new UpgradeAdapter());
        mViewPager.setCurrentItem(0);
    }

    @Override
    protected void snackViewDismissWhenTimeOut(Prompt prompt) {
        super.snackViewDismissWhenTimeOut(prompt);
        getActivity().finish();
    }

    /**
     * ViewPager 适配器
     */
    private class UpgradeAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object); // 将view 类型 的object熊容器中移除,根据key
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = views.get(position);
            UpgradeTypeBean upgradeTypeBean = data.get(position);
            TextView mTitle = (TextView) view.findViewById(R.id.tv_upgrade_item_title);
            TextView mPrice = (TextView) view.findViewById(R.id.tv_upgrade_price);
            NoPullRecycleView recycleView = (NoPullRecycleView) view.findViewById(R.id.upgrade_group_rv);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            recycleView.setLayoutManager(linearLayoutManager);
            CommonAdapter adapter = new CommonAdapter<UpgradeTypeBean.TaocanDataBean>(getContext(),R.layout.item_combo,upgradeTypeBean.getTaocan_data()) {
                @Override
                protected void convert(ViewHolder holder, UpgradeTypeBean.TaocanDataBean comboBean, int position) {
                    Glide.with(mContext)
                            .load(TextUtils.isEmpty(comboBean.getIcon()) ? R.mipmap.album_icon : comboBean
                                    .getIcon())
                            .error(R.mipmap.album_icon)
                            .placeholder(R.mipmap.album_icon)
                            .transform(new GlideCircleTransform(mContext))
                            .into(holder.getImageViwe(R.id.iv_combo_icon));
                    holder.setText(R.id.tv_combo_str,comboBean.getDec());
                }
            };
            recycleView.setAdapter(adapter);
            Button button = (Button) view.findViewById(R.id.bt_upgrade_immediately);
            button.setOnClickListener(view1 -> {
//                    mPresenter.upgradegroup(mGroupId,upgradeTypeBean.getId());
                UpgradePayActivity.startUpgradePayActivity(getContext(),upgradeTypeBean,mGroupId);
            });
            mTitle.setText(upgradeTypeBean.getName());
            mPrice.setText("￥" + upgradeTypeBean.getPrice());
//            if (upgradeTypeBean.getId() == 2) {
//                mVip.setVisibility(View.GONE);
//                mLocation.setVisibility(View.GONE);
//            }
            container.addView(view);
            return views.get(position);
        }

    }
}
