package com.zhiyicx.thinksnsplus.modules.findsomeone.contacts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.widget.recycleview.stickygridheaders.StickyHeaderGridLayoutManager;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.LinearDecoration;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ContactsBean;
import com.zhiyicx.thinksnsplus.data.beans.ContactsContainerBean;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.modules.eventbus.StickyContactEvent;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupListFragment;
import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupPresenter;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_LIST_ITEM_SPACING;
import static com.zhiyicx.thinksnsplus.modules.findsomeone.contacts.ContactsAdapter.DEFAULT_MAX_ADD_SHOW_NUMS;

/**
 * @Describe 通讯录
 * @Author zl
 * @Date 2017/1/9
 * @Contact master.jungle68@gmail.com
 */
public class ContactsFragment extends TSFragment<ContactsContract.Presenter> implements ContactsContract.View, ContactsAdapter.OnMoreClickLitener {
    private static final String BUNDLE_DATA = "data";
    private static final String BUNDLE_TITLE = "title";

    private static final int SPAN_SIZE = 1;

    @BindView(R.id.rv_contacts)
    RecyclerView mRvTagClass;

    private StickyHeaderGridLayoutManager mTagClassLayoutManager;

    private ArrayList<ContactsContainerBean> mListData = new ArrayList<>();

    private ContactsAdapter mTagClassAdapter;

    private ArrayList<ContactsContainerBean> mBundleData;

    /**
     * 仅用于构造
     */
    @Inject
    ContactsPresenter mContactsPresenter;

    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 通讯录
     * 需要避免该异常
     * 传递的数据不能超过 1MB
     * 5000多个通讯录数据
     * Caused by: android.os.TransactionTooLargeException: data parcel size 2317736 bytes
     *
     */
    public static void startToEditTagActivity(Context context, List<ContactsContainerBean> listData) {
        //发送粘性事件
        if(null != listData)
            EventBus.getDefault().postSticky(new StickyContactEvent(listData));

        Intent intent = new Intent(context, ContactsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IntentKey.IS_SELECT,null!=listData);
        intent.putExtras(bundle);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, 100);
        } else {
            throw new IllegalAccessError("context must instance of Activity");
        }
    }


    public static ContactsFragment newInstance(Bundle bundle) {
        ContactsFragment editUserTagFragment = new ContactsFragment();
        editUserTagFragment.setArguments(bundle);
        return editUserTagFragment;
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected int setToolBarBackgroud() {
        return R.color.white;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }
    //设置展示状态栏
    @Override
    protected boolean setUseSatusbar() {
        return false;
    }

    @Override
    protected String setCenterTitle() {
        return null == getParentFragment() ? "联系人列表":"";
    }

    @Override
    protected boolean showToolbar() {
        return null == getParentFragment();
    }

    @Override
    protected boolean useEventBusSticky() {
        return true;
    }

    //粘性事件接收
    @Subscriber
    public void onStickyContactEvent(StickyContactEvent event){
        if(null == getParentFragment()){
            EventBus.getDefault().removeStickyEvent(StickyContactEvent.class);
            this.mListData.addAll(event.getList());
            mTagClassAdapter.notifyAllSectionsDataSetChanged();
            hideLoading();
        }
    }

    @Override
    protected void initView(View rootView) {
//        mToolbarCenter.setVisibility(View.VISIBLE);
//        mToolbarCenter.setText(mTitle);
        DaggerContactsFComponent.builder()
                .appComponent(AppApplication.AppComponentHolder.getAppComponent())
                .contactsPresenterModule(new ContactsPresenterModule(this))
                .build()
                .inject(this);
        initRvTagClass();
        initListener();


    }

    @Override
    public void showLoading() {
        showLoadingView();
    }

    @Override
    public void hideLoading() {
        closeLoadingView();
    }


    @Override
    public void onResume() {
        super.onResume();
        layzLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        layzLoad();
    }

    private boolean isFirst = true;
    /**
     * 数据懒加载
     */
    private void layzLoad() {

        if (getUserVisibleHint() && isFirst) {
            isFirst = false;

            PackageManager packageManager = getActivity().getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission("android.Manifest.permission.READ_CONTACTS", "packageName"));
            boolean permission1 = (PackageManager.PERMISSION_GRANTED == packageManager.checkPermission("android.Manifest.permission.SEND_SMS", "packageName"));

            if (!permission) {
                mRxPermissions.request(android.Manifest.permission.READ_CONTACTS)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
//                            ContactsFragment.startToEditTagActivity(getActivity(), null, null);
                                mPresenter.getInviteCode();
                                if (!getArguments().getBoolean(IntentKey.IS_SELECT,false)) {
                                    mPresenter.getContacts();
                                } /*else {
                                mListData.addAll(mBundleData);
                                mTagClassAdapter.notifyAllSectionsDataSetChanged();
                                hideLoading();
                            }*/

                            } else {
                                closeLoadingView();
                                showSnackErrorMessage(getString(R.string.contacts_permission_tip));
                            }
                        });
            }
            if (!permission1) {
                mRxPermissions.request(android.Manifest.permission.SEND_SMS)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
//                            ContactsFragment.startToEditTagActivity(getActivity(), null, null);
                            } else {
                                showSnackErrorMessage(getString(R.string.SMS_permission_tip));
                            }
                        });
            }
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    private void initRvTagClass() {
        mTagClassLayoutManager = new StickyHeaderGridLayoutManager(SPAN_SIZE);
//        mTagClassLayoutManager.setHeaderBottomOverlapMargin(getResources().getDimensionPixelSize(R.dimen.spacing_small));
        mTagClassLayoutManager.setSpanSizeLookup(new StickyHeaderGridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int section, int position) {
                return 1;
            }
        });

        // Workaround RecyclerView limitation when playing remove animations. RecyclerView always
        // puts the removed item on the top of other views and it will be drawn above sticky header.
        // The only way to fix this, abandon remove animations :(
        mRvTagClass.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateRemove(RecyclerView.ViewHolder holder) {
                dispatchRemoveFinished(holder);
                return false;
            }
        });
        mRvTagClass.setLayoutManager(mTagClassLayoutManager);
        mRvTagClass.addItemDecoration(new LinearDecoration(0, ConvertUtils.dp2px(getContext(), DEFAULT_LIST_ITEM_SPACING), 0, 0));
        mTagClassAdapter = new ContactsAdapter(mListData, mPresenter);
        mTagClassAdapter.setOnMoreClickLitener(this);
        mRvTagClass.setAdapter(mTagClassAdapter);

    }

    private void initListener() {


    }


    @Override
    public void onMoreClick(int categoryPosition) {
        ArrayList<ContactsContainerBean> data = new ArrayList<>();
        ContactsContainerBean contactsContainerBean = mListData.get(categoryPosition);
        for (ContactsContainerBean containerBean : mBundleData) {
            if (containerBean.getTitle().equals(contactsContainerBean.getTitle())) {
                data.add(containerBean);
                break;
            }
        }
        startToEditTagActivity(getActivity(),  data);
    }

    @Override
    public void updateContacts(ArrayList<ContactsContainerBean> data) {

        mBundleData = new ArrayList<>();
        if (data.size() > 0) {
            ContactsContainerBean contactsContainerBean = new ContactsContainerBean();
            contactsContainerBean.setTitle(data.get(0).getTitle());
            contactsContainerBean.setContacts(new ArrayList<>());
            contactsContainerBean.getContacts().addAll(data.get(0).getContacts());
            mBundleData.add(contactsContainerBean);
        }
        if (data.size() > 1) {
            ContactsContainerBean contactsContainerBean2 = new ContactsContainerBean();
            contactsContainerBean2.setTitle(data.get(1).getTitle());
            contactsContainerBean2.setContacts(new ArrayList<>());
            contactsContainerBean2.getContacts().addAll(data.get(1).getContacts());
            mBundleData.add(contactsContainerBean2);
        }
        mListData.clear();
        mListData.addAll(data);
        for (int i = 0; i < mListData.size(); i++) {
            if (mListData.get(i).getContacts().size() > DEFAULT_MAX_ADD_SHOW_NUMS) {
                mListData.get(i).setContacts(new ArrayList<>(mListData.get(i).getContacts().subList(0, ContactsAdapter.DEFAULT_MAX_ADD_SHOW_NUMS)));
            }
        }
        mTagClassAdapter.notifyAllSectionsDataSetChanged();
    }
}
