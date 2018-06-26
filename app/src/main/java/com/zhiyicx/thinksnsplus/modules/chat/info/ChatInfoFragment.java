package com.zhiyicx.thinksnsplus.modules.chat.info;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.baseproject.config.ImageZipConfig;
import com.zhiyicx.baseproject.impl.photoselector.DaggerPhotoSelectorImplComponent;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSelectorImpl;
import com.zhiyicx.baseproject.impl.photoselector.PhotoSeletorImplModule;
import com.zhiyicx.baseproject.widget.EmptyView;
import com.zhiyicx.baseproject.widget.UserAvatarView;
import com.zhiyicx.baseproject.widget.popwindow.ActionPopupWindow;
import com.zhiyicx.common.utils.ToastUtils;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.common.utils.recycleviewdecoration.TGridDecoration;
import com.zhiyicx.common.widget.popwindow.CustomPopupWindow;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.config.EventBusTagConfig;
import com.zhiyicx.thinksnsplus.data.beans.AnimationRectBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupNewBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.modules.chat.ChatActivity;
import com.zhiyicx.thinksnsplus.modules.chat.adapter.ChatMemberAdapter;
import com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameActivity;
import com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerActivity;
import com.zhiyicx.thinksnsplus.modules.chat.item.ChatConfig;
import com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListActivity;
import com.zhiyicx.thinksnsplus.modules.chat.select.SelectFriendsActivity;
import com.zhiyicx.thinksnsplus.modules.gallery.GalleryActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album.MessageGroupAlbumActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.jurisdiction.JurisdictionActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.NoticeManagerActivity;
import com.zhiyicx.thinksnsplus.modules.home.message.managergroup.settingadmin.SettingAdminActivity;
import com.zhiyicx.thinksnsplus.modules.personal_center.PersonalCenterFragment;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;
import com.zhiyicx.thinksnsplus.utils.TSImHelperUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.iwf.photopicker.PhotoPagerActivity;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.hyphenate.easeui.EaseConstant.CHATTYPE_GROUP;
import static com.hyphenate.easeui.EaseConstant.EXTRA_BANNED_POST;
import static com.hyphenate.easeui.EaseConstant.EXTRA_IS_ADD_GROUP;
import static com.hyphenate.easeui.EaseConstant.EXTRA_TO_USER_ID;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_MUTE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_NOTICE;
import static com.zhiyicx.thinksnsplus.config.EventBusTagConfig.EVENT_IM_GROUP_UPDATE_GROUP_USER_INFO;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_ID;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.GROUP_ORIGINAL_NAME;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.name.EditGroupNameFragment.IS_GROUP_OWNER;
import static com.zhiyicx.thinksnsplus.modules.chat.edit.owner.EditGroupOwnerFragment.BUNDLE_GROUP_DATA;
import static com.zhiyicx.thinksnsplus.modules.chat.member.GroupMemberListFragment.BUNDLE_GROUP_MEMBER;

/**
 * @author Catherine
 * @describe
 * @date 2018/1/22
 * @contact email:648129313@qq.com
 */

public class ChatInfoFragment extends TSFragment<ChatInfoContract.Presenter> implements ChatInfoContract.View, PhotoSelectorImpl.IPhotoBackListener {

    @BindView(R.id.iv_user_portrait)
    UserAvatarView mIvUserPortrait;
    @BindView(R.id.tv_user_name)
    TextView mTvUserName;
    @BindView(R.id.tv_group_header)
    TextView mTvGroupHeader;
    @BindView(R.id.iv_add_user)
    ImageView mIvAddUser;
    @BindView(R.id.ll_single)
    LinearLayout mLlSingle;
    @BindView(R.id.rv_member_list)
    RecyclerView mRvMemberList;
    @BindView(R.id.tv_to_all_members)
    TextView mTvToAllMembers;
    @BindView(R.id.ll_group)
    LinearLayout mLlGroup;
    @BindView(R.id.ll_manager)
    LinearLayout mLlManager;
    @BindView(R.id.tv_clear_message)
    TextView mTvClearMessage;
    @BindView(R.id.tv_delete_group)
    TextView mTvDeleteGroup;
    @BindView(R.id.iv_group_portrait)
    ImageView mIvGroupPortrait;
    @BindView(R.id.tv_group_name)
    TextView mTvGroupName;
    @BindView(R.id.sc_block_message)
    SwitchCompat mScBlockMessage;
    @BindView(R.id.sc_banned_post)
    SwitchCompat mScBannedPost;
    @BindView(R.id.sc_stick_message)
    SwitchCompat mScStickMessage;
    @BindView(R.id.ll_container)
    View mLlContainer;
    @BindView(R.id.emptyView)
    EmptyView mEmptyView;
    @BindView(R.id.rl_block_message)
    RelativeLayout mRlBlockMessage;
    @BindView(R.id.v_line_find_member)
    View mVLineFindMember;

    @BindView(R.id.iv_grop_icon_arrow)
    View mIvGropIconArrow;
    @BindView(R.id.iv_grop_name_arrow)
    View mIvGropNameArrow;
    @BindView(R.id.ll_announcement)
    LinearLayout mLlAnnouncement;
    @BindView(R.id.ll_photo)
    LinearLayout mLlPhoto;
    @BindView(R.id.rv_album)
    RecyclerView mRvAlbum;
    @BindView(R.id.ll_card)
    LinearLayout mLlCard;
    @BindView(R.id.ll_set_stick)
    LinearLayout mLlSetStick;
    @BindView(R.id.ll_banned_post)
    RelativeLayout mLlBannedPost;
    @BindView(R.id.tv_jurisdiction)
    TextView mTvJurisdiction;
    @BindView(R.id.tv_upgrade)
    TextView mTvUpgrade;

    Unbinder unbinder;
    @BindView(R.id.tv_set_admin)
    TextView mTvSetAdmin;
    @BindView(R.id.tv_group_card_name)
    TextView mTvGroupCardName;
    @BindView(R.id.ll_group_manager)
    LinearLayout llGroupManager;
    @BindView(R.id.vw_set_admin)
    View vwSetAdmin;
    @BindView(R.id.vw_jurisdiction)
    View vwJurisdiction;
    @BindView(R.id.vw_upgrade)
    View vwUpgrade;
    @BindView(R.id.vw_set_stick)
    View vwSetStick;

    @BindView(R.id.tv_announcement)
    TextView mNoticeText;

    @BindView(R.id.tv_album_desc)
    TextView mTvAlbumDesc;

    private int mChatType;
    private int mIsStick;
    private String mChatId;

    // 删除群聊
    private ActionPopupWindow mDeleteGroupPopupWindow;
    private ActionPopupWindow mPhotoPopupWindow;// 图片选择弹框

    /**
     * 清楚消息记录
     */
    private ActionPopupWindow mClearAllMessagePop;
    private PhotoSelectorImpl mPhotoSelector;
    private ChatGroupBean mChatGroupBean;

    private ChatMemberAdapter mChatMemberAdapter;
    private List<UserInfoBean> mChatMembers = new ArrayList<>();
    private UserInfoBean user;
    private boolean mIsAddGroup = false;//默认是已加入群组

    public ChatInfoFragment instance(Bundle bundle) {
        ChatInfoFragment fragment = new ChatInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected void initView(View rootView) {
        // 初始化图片选择器
        mPhotoSelector = DaggerPhotoSelectorImplComponent
                .builder()
                .photoSeletorImplModule(new PhotoSeletorImplModule(this, this, PhotoSelectorImpl
                        .SHAPE_SQUARE))
                .build().photoSelectorImpl();
        mChatType = getArguments().getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        mChatId = getArguments().getString(EXTRA_TO_USER_ID);

        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            mIsStick = getArguments().getInt(EaseConstant.EXTRA_IS_STICK, 0);
            // 屏蔽群聊的布局
            mLlGroup.setVisibility(View.GONE);
            mLlManager.setVisibility(View.GONE);
            mTvDeleteGroup.setVisibility(View.GONE);
            llGroupManager.setVisibility(View.GONE);
            isShowEmptyView(false, true);
            setGroupData();
            setCenterText(getString(R.string.chat_info_title_single));
            // 单聊没有屏蔽消息
            mRlBlockMessage.setVisibility(View.GONE);
//            mScStickMessage.setVisibility(View.GONE);
        } else {
            mPresenter.getGroupChatInfo(mChatId);
            mEmptyView.setNeedTextTip(false);
            mEmptyView.setNeedClickLoadState(false);
            mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoadingView();
                    mPresenter.getGroupChatInfo(mChatId);
                }
            });
            // 屏蔽单聊的布局
            mLlSingle.setVisibility(View.GONE);
            mIsAddGroup = getArguments().getBoolean(EXTRA_IS_ADD_GROUP);
            getIsAddGroup();
        }

        initPhotoPopupWindow();
    }

    /**
     * 设置置顶开关是否打开
     *
     * @param
     */
    private void setIsStick() {
        if (mChatType == CHATTYPE_GROUP) {
            if (mChatGroupBean.getIs_stick() == EaseConstant.CLOESS_MUTS) {//关闭置顶
                mScStickMessage.setChecked(false);
            } else {
                mScStickMessage.setChecked(true);
            }
        } else {
            if (mIsStick == EaseConstant.CLOESS_MUTS) {//关闭置顶
                mScStickMessage.setChecked(false);
            } else {
                mScStickMessage.setChecked(true);
            }
        }
    }

    @Override
    protected void initData() {
        // 成员列表
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 5);
        mRvMemberList.setLayoutManager(manager);
        mRvMemberList.addItemDecoration(new TGridDecoration(0, getResources().getDimensionPixelOffset(R.dimen.spacing_large), true));
        dealAddOrDeleteButton();
        mChatMemberAdapter = new ChatMemberAdapter(getContext(), mChatMembers, -1, false);
        mRvMemberList.setAdapter(mChatMemberAdapter);
        mChatMemberAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (mChatMembers.get(position).getUser_id() == -1L) {
                    // 添加
                    SelectFriendsActivity.startSelectFriendActivity(mActivity, mChatGroupBean, false);
                } else if (mChatMembers.get(position).getUser_id() == -2L) {
                    // 移除
                    SelectFriendsActivity.startSelectFriendActivity(mActivity, mChatGroupBean, true);
                } else {
                    PersonalCenterFragment.startToPersonalCenter(getContext(), mChatMembers.get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    @Override
    protected String setCenterTitle() {
        return getString(R.string.chat_info_title);
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat_info;
    }

    @Override
    public void closeCurrentActivity() {
        mActivity.finish();
    }

    @OnClick({R.id.iv_add_user, R.id.tv_to_all_members, R.id.ll_manager, R.id.tv_clear_message, R.id.tv_delete_group,
            R.id.ll_group_portrait, R.id.ll_group_name, R.id.iv_user_portrait, R.id.ll_announcement, R.id.ll_photo,
            R.id.ll_card, R.id.tv_find_message, R.id.tv_set_admin, R.id.ll_banned_post, R.id.tv_jurisdiction, R.id.tv_upgrade})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_user:
                // 添加成员
                if (mChatGroupBean == null) {
                    mChatGroupBean = new ChatGroupBean();
                }
                List<UserInfoBean> userInfoBeanList = new ArrayList<>();
                userInfoBeanList.add(mPresenter.getUserInfoFromLocal(mChatId));
                mChatGroupBean.setAffiliations(userInfoBeanList);
                SelectFriendsActivity.startSelectFriendActivity(mActivity, mChatGroupBean, false);

                break;
            case R.id.tv_to_all_members:
                // 查看所有成员
                Intent intentAllMember = new Intent(getContext(), GroupMemberListActivity.class);
                Bundle bundleAllMember = new Bundle();
                bundleAllMember.putParcelable(BUNDLE_GROUP_MEMBER, mChatGroupBean);
                intentAllMember.putExtras(bundleAllMember);
                startActivity(intentAllMember);
                break;
            case R.id.ll_manager:
                // 跳转群管理
//                Intent intent = new Intent(getContext(), GroupManagerActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putParcelable(BUNDLE_GROUP_DATA, mChatGroupBean);
//                intent.putExtras(bundle);
//                startActivity(intent);
                // 转让群主
                Intent intent = new Intent(getContext(), EditGroupOwnerActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(BUNDLE_GROUP_DATA, mChatGroupBean);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tv_clear_message:
                // 清空消息记录
                showClearAllMsgPopupWindow(getString(R.string.ts_message_history_deleted_tip));
                break;
                /*
                 群主：删除群聊
                 普通用户：退出群
                 非本群用户：加入群聊
                 */
            case R.id.tv_delete_group:
                initDeletePopupWindow(mPresenter.isGroupOwner() ? getString(R.string.chat_delete) : !mIsAddGroup ? getString(R.string.chat_quit) : getString(R.string.tv_add_group_chat)
                        , mPresenter.isGroupOwner() ? getString(R.string.chat_delete_group_alert) : !mIsAddGroup ? getString(R.string.chat_quit_group_alert) : getString(R.string.chat_quit_group_add));
                break;
            case R.id.ll_group_portrait:
                // 修改群头像
                if (mChatType == CHATTYPE_GROUP && mPresenter.isGroupOwner()) {
                    mPhotoPopupWindow.show();
                }
                break;
            case R.id.ll_group_name:
                // 修改群名称
                if (mChatType == CHATTYPE_GROUP && mPresenter.isGroupOwner()) {
                    Intent intentName = new Intent(getContext(), EditGroupNameActivity.class);
                    Bundle bundleName = new Bundle();
                    bundleName.putString(GROUP_ORIGINAL_NAME, mChatGroupBean.getName());
                    intentName.putExtras(bundleName);
                    startActivity(intentName);
                }
                break;
            case R.id.iv_user_portrait:
                try {
                    UserInfoBean userInfoBean = new UserInfoBean();
                    userInfoBean.setUser_id(Long.parseLong(mChatId));
                    PersonalCenterFragment.startToPersonalCenter(getContext(), userInfoBean);
                } catch (NumberFormatException ignore) {
                }
                break;
            case R.id.ll_announcement:
                // 群公告
                if (mChatType == CHATTYPE_GROUP) {
                    Intent intentName = new Intent(getContext(), NoticeManagerActivity.class);
                    intentName.putExtra(GROUP_ORIGINAL_ID, mChatGroupBean.getId());
                    intentName.putExtra(IS_GROUP_OWNER, mPresenter.isGroupOwner());
                    startActivity(intentName);
                }
                break;
            case R.id.tv_jurisdiction:
                if (mChatType == CHATTYPE_GROUP) {
                    JurisdictionActivity.startSelectFriendActivity(getContext(), mChatGroupBean);
                }
                break;
            case R.id.tv_set_admin:
                if (mChatType == CHATTYPE_GROUP) {
                    SettingAdminActivity.startSettingAdminActivty(getContext(), mChatGroupBean);
                }
                break;
            case R.id.ll_photo:
                startActivity(MessageGroupAlbumActivity.newIntent(mActivity, mChatGroupBean.getId()));
                break;
//            case R.id.ll_card:
            case R.id.tv_find_message:

            case R.id.tv_upgrade:
                ToastUtils.showLongToast(R.string.normal_dispark);
                break;
            default:
        }
    }

    private void initDeletePopupWindow(String item2, String dec) {
        mDeleteGroupPopupWindow = ActionPopupWindow.builder()
                .item1Str(getString(R.string.prompt))
                .item2Str(item2)
                .desStr(dec)
                .bottomStr(getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                .with(getActivity())
                .item2ClickListener(() -> {
                    mPresenter.destoryOrLeaveGroup(mChatId);
                    mDeleteGroupPopupWindow.hide();
                })
                .bottomClickListener(() -> mDeleteGroupPopupWindow.hide())
                .build();
        mDeleteGroupPopupWindow.show();
    }

    /**
     * 初始化图片选择弹框
     */
    private void initPhotoPopupWindow() {
        if (mPhotoPopupWindow != null) {
            return;
        }
        mPhotoPopupWindow = ActionPopupWindow.builder()
                .item1Str(mActivity.getString(R.string.choose_from_photo))
                .item2Str(mActivity.getString(R.string.choose_from_camera))
                .bottomStr(mActivity.getString(R.string.cancel))
                .isOutsideTouch(true)
                .isFocus(true)
                .backgroundAlpha(0.8f)
                .with(mActivity)
                .item1ClickListener(() -> {
                    // 选择相册，单张
                    mPhotoSelector.getPhotoListFromSelector(1, null);
                    mPhotoPopupWindow.hide();
                })
                .item2ClickListener(() -> {
                    // 选择相机，拍照
                    mPhotoSelector.getPhotoFromCamera(null);
                    mPhotoPopupWindow.hide();
                })
                .bottomClickListener(() -> mPhotoPopupWindow.hide()).build();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected boolean usePermisson() {
        return true;
    }

    @Override
    public String getChatId() {
        return mChatId;
    }

    @Override
    public void updateGroup(ChatGroupBean chatGroupBean) {
        // emm 由于没有完全返回所有信息 再加上字段也不同 所以手动改一下
        mChatGroupBean.setGroup_face(chatGroupBean.getGroup_face());
        mChatGroupBean.setOwner(chatGroupBean.getOwner());
        mChatGroupBean.setPublic(chatGroupBean.isPublic());
        mChatGroupBean.setName(chatGroupBean.getName());
        mChatGroupBean.setDescription(chatGroupBean.getDescription());
        mChatGroupBean.setMembersonly(chatGroupBean.isMembersonly());
        mChatGroupBean.setAllowinvites(chatGroupBean.isAllowinvites());
        mPresenter.saveGroupInfo(mChatGroupBean);
        setGroupData();
    }

    @Override
    public void updateGroupOwner(ChatGroupBean chatGroupBean) {
        // emm 由于没有完全返回所有信息 再加上字段也不同 所以手动改一下
        Observable.just(chatGroupBean)
                .subscribeOn(Schedulers.io())
                .map(chatGroupBean1 -> {
                    System.out.println("Thread.currentThread().getName() = " + Thread.currentThread().getName());
                    mChatGroupBean.setGroup_face(chatGroupBean.getGroup_face());
                    mChatGroupBean.setOwner(chatGroupBean.getOwner());
                    mChatGroupBean.setPublic(chatGroupBean.isPublic());
                    mChatGroupBean.setName(chatGroupBean.getName());
                    mChatGroupBean.setDescription(chatGroupBean.getDescription());
                    mChatGroupBean.setMembersonly(chatGroupBean.isMembersonly());
                    mChatGroupBean.setAllowinvites(chatGroupBean.isAllowinvites());
                    if (mChatGroupBean.getAffiliations() != null) {
                        for (UserInfoBean userInfoBean : mChatGroupBean.getAffiliations()) {
                            if (mChatGroupBean.getOwner() == userInfoBean.getUser_id()) {
                                mChatGroupBean.getAffiliations().remove(userInfoBean);
                                mChatGroupBean.getAffiliations().add(0, userInfoBean);
                                break;
                            }
                        }
                    }
                    return mChatGroupBean;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(chatGroupBean12 -> {
                    if (getActivity() != null) {
                        setGroupData();
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public void setSticksSuccess() {
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            EventBus.getDefault().post(mIsStick == 0 ? 1 : 0, EventBusTagConfig.EVENT_GROUP_UPLOAD_SET_STICK);
        }
    }

    /**
     * 禁言成功
     */
    @Override
    public void setBannedPostSuccess() {
        onPublishGroupSuccess(true);
    }

    @Override
    public boolean getIsAddGroup() {
        return mIsAddGroup;
    }

    @Override
    public void goChatActivity() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(mChatId, EMConversation.EMConversationType.GroupChat, true);
        ChatActivity.startChatActivity(mActivity, conversation.conversationId(), CHATTYPE_GROUP);
        getActivity().finish();
    }

    @Override
    public void getGroupInfoSuccess(ChatGroupNewBean chatGroupBean) {
        mChatGroupBean = chatGroupBean;
        mChatGroupBean.setId(mChatId);
        setGroupData();
        // 切换是否屏蔽消息
        mScBlockMessage.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
                if (isChecked) {
                }
            } else {
                mPresenter.openOrCloseGroupMessage(isChecked, mChatId);
            }
        });
        //禁言
        mScBannedPost.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (mChatType == CHATTYPE_GROUP) {
                if (mScBannedPost.isChecked()) {
                    mPresenter.openBannedPost(mChatGroupBean.getId(), "0", "", EXTRA_BANNED_POST);//开启禁言
                } else {
                    mPresenter.removeBannedPost(mChatGroupBean.getId(), "0", EXTRA_BANNED_POST);//关闭禁言
                }
            }
        });
        //设置置顶
        mScStickMessage.setOnClickListener((buttonView) -> {//不知道什么原因  一进来页面就触发 setOnCheckedChangeListener事件
            if (mScStickMessage.isChecked()) {
                mPresenter.setSticks(String.valueOf(mChatGroupBean.getId()), String.valueOf(AppApplication.getMyUserIdWithdefault()), 0);
            } else {
                mPresenter.setSticks(String.valueOf(mChatGroupBean.getId()), String.valueOf(AppApplication.getMyUserIdWithdefault()), 1);
            }
        });

        mNoticeText.setText(TextUtils.isEmpty(chatGroupBean.getNoticeItemBean().getOriginal().getContent()) ? "暂无公告" : chatGroupBean.getNoticeItemBean().getOriginal().getContent());
        if (mChatType == CHATTYPE_GROUP) {
            if (chatGroupBean.getmIsMute() == EaseConstant.CLOESS_MUTS) {//关闭禁言状态
                mScBannedPost.setChecked(false);
            } else if (chatGroupBean.getmIsMute() == EaseConstant.OPEN_MUTS) {//开启禁言状态
                mScBannedPost.setChecked(true);
            } else {
                mScBannedPost.setChecked(false);
            }
        }

        setIsStick();

        setGroupAlbumData(chatGroupBean.group_images_data);

    }

    @Subscriber(tag = EventBusTagConfig.EVENT_GROUP_UPLOAD_ALBUM_SUCCESS)
    public void uploadAlbumSuccess(List<MessageGroupAlbumBean> newData) {
        setGroupAlbumData(newData);
    }

    /**
     * 设置相册数据
     *
     * @param newData
     */
    private void setGroupAlbumData(List<MessageGroupAlbumBean> newData) {

        mRvAlbum.setVisibility((null == newData || newData.size() == 0) ? View.GONE : View.VISIBLE);
        mTvAlbumDesc.setVisibility((null == newData || newData.size() == 0) ? View.VISIBLE : View.GONE);

        if (null == mRvAlbum.getAdapter())
            initAlbumAdapter();

        List<MessageGroupAlbumBean> fullData = newData.size() > 4 ? newData.subList(0, 4) : newData;
        if (fullData.size() == 4) {
            MessageGroupAlbumBean bean = new MessageGroupAlbumBean();
            bean.file_id = -1;
            fullData.add(bean);
        }
        ((CommonAdapter) mRvAlbum.getAdapter()).dataChange(fullData);

    }

    @Override
    protected boolean setUseCenterLoading() {
        return true;
    }

    @Override
    protected boolean setUseCenterLoadingAnimation() {
        return super.setUseCenterLoadingAnimation();
    }

    @Override
    public ChatGroupBean getGroupBean() {
        return mChatGroupBean;
    }

    @Override
    public void isShowEmptyView(boolean isShow, boolean isSuccess) {

        mLlContainer.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
        if (!isSuccess) {
            mEmptyView.setVisibility(View.VISIBLE);
            mEmptyView.setErrorType(EmptyView.STATE_NETWORK_ERROR);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        if (!isShow) {
            closeLoadingView();
        }

    }


    @Override
    public String getToUserId() {
        return mChatType == EaseConstant.CHATTYPE_SINGLE ? mChatId : "";
    }

    @Override
    public void createGroupSuccess(ChatGroupBean chatGroupBean) {
        String id = chatGroupBean.getId();
        if (EMClient.getInstance().groupManager().getGroup(id) == null) {
            // 不知道为啥 有时候获取不到群组对象
            showSnackErrorMessage(getString(R.string.create_fail));
        } else {
            // 点击跳转聊天
            ChatActivity.startChatActivity(mActivity, id, CHATTYPE_GROUP);
            getActivity().finish();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelector.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        dismissPop(mClearAllMessagePop);
        dismissPop(mPhotoPopupWindow);
        dismissPop(mDeleteGroupPopupWindow);
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getPhotoSuccess(List<ImageBean> photoList) {
        mChatGroupBean.setGroup_face(photoList.get(0).getImgUrl());
        mPresenter.updateGroup(mChatGroupBean, true);
    }

    @Override
    public void getPhotoFailure(String errorMsg) {
        showSnackErrorMessage(errorMsg);
    }

    /**
     * 初始化群相册
     */
    private void initAlbumAdapter() {

        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 5);
        mRvAlbum.setLayoutManager(manager);
        mRvAlbum.addItemDecoration(new TGridDecoration(getResources().getDimensionPixelOffset(R.dimen.spacing_normal_18dp)
                , 0,
                true));
        CommonAdapter<MessageGroupAlbumBean> mAdapter = new CommonAdapter<MessageGroupAlbumBean>(getContext(),
                R.layout.item_square_height_imageview, new ArrayList<>()) {
            @Override
            protected void convert(ViewHolder holder, MessageGroupAlbumBean messageGroupAlbumBean, int position) {
                if (-1 != messageGroupAlbumBean.file_id)
                    ImageUtils.loadCircleImageDefault(holder.getImageViwe(R.id.iv),
                            ImageUtils.imagePathConvertV2(messageGroupAlbumBean.file_id, 0, 0, ImageZipConfig.IMAGE_80_ZIP));
                else {
                    holder.getImageViwe(R.id.iv).setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    holder.getImageViwe(R.id.iv).setImageResource(R.mipmap.icon_more_deep_gray);
                }
                    /*ImageUtils.loadCircleImageDefault(holder.getImageViwe(R.id.iv),R.mipmap.icon_more_deep_gray,
                            R.mipmap.icon_more_deep_gray,R.mipmap.icon_more_deep_gray);*/
            }
        };
        mRvAlbum.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {


                MessageGroupAlbumBean photo = ((CommonAdapter<MessageGroupAlbumBean>) mRvAlbum.getAdapter())
                        .getDatas().get(position);
                if (-1 == photo.file_id) {
                    startActivity(MessageGroupAlbumActivity.newIntent(mActivity, mChatGroupBean.getId()));
                } else {
                    ImageBean imageBean = new ImageBean();
                    imageBean.setImgUrl(ImageUtils.imagePathConvertV2(photo.file_id, 0, 0,
                            ImageZipConfig.IMAGE_80_ZIP));
                    imageBean.setWidth(holder.itemView.findViewById(R.id.iv).getMeasuredWidth());
                    imageBean.setHeight(holder.itemView.findViewById(R.id.iv).getMeasuredHeight());
                    imageBean.setPosition(0);

                    AnimationRectBean rect = AnimationRectBean.buildFromImageView((ImageView) view);

                    GalleryActivity.startToSingleGallery(mActivity, 0, imageBean, rect);
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

    }

    private void dealAddOrDeleteButton() {

        if (mChatGroupBean == null) {
            return;
        }
        mChatMembers.clear();
        mChatMembers.addAll(mChatGroupBean.getAffiliations());
        if (mPresenter.isGroupOwner()) {
            if (mChatMembers.size() > 18) {
                // 是群主 18 + 2
                mChatMembers = mChatMembers.subList(0, 18);
                mVLineFindMember.setVisibility(View.VISIBLE);
                mTvToAllMembers.setVisibility(View.VISIBLE);
            } else {
                mVLineFindMember.setVisibility(View.GONE);
                mTvToAllMembers.setVisibility(View.GONE);
            }
        } else {
            // 不是群主
            if (mChatMembers.size() > 19) {
                // 19 +1
                mChatMembers = mChatMembers.subList(0, 19);
                mVLineFindMember.setVisibility(View.VISIBLE);
                mTvToAllMembers.setVisibility(View.VISIBLE);
            } else {
                mVLineFindMember.setVisibility(View.GONE);
                mTvToAllMembers.setVisibility(View.GONE);
            }
        }
        // 添加按钮，都可以拉人
        UserInfoBean chatUserInfoBean = new UserInfoBean();
        chatUserInfoBean.setUser_id(-1L);
        mChatMembers.add(chatUserInfoBean);
        if (mPresenter.isGroupOwner()) {
            // 删除按钮，仅群主
            UserInfoBean chatUserInfoBean1 = new UserInfoBean();
            chatUserInfoBean1.setUser_id(-2L);
            mChatMembers.add(chatUserInfoBean1);
        }

    }

    private void showClearAllMsgPopupWindow(String tipStr) {
        if (mClearAllMessagePop == null) {
            mClearAllMessagePop = ActionPopupWindow.builder()
                    .item1Str(getString(R.string.info_publish_hint))
                    .desStr(tipStr)
                    .item2Str(getString(R.string.chat_info_clear_message))
                    .bottomStr(getString(R.string.cancel))
                    .isOutsideTouch(true)
                    .isFocus(true)
                    .backgroundAlpha(CustomPopupWindow.POPUPWINDOW_ALPHA)
                    .with(getActivity())
                    .item2ClickListener(() -> {
                        EMClient.getInstance().chatManager().getConversation(mChatId).clearAllMessages();
                        if (mPresenter != null && mPresenter.checkImhelper(mChatId)) {
                            TSImHelperUtils.saveDeletedHistoryMessageHelper(
                                    getContext().getApplicationContext()
                                    , mChatId
                                    , String.valueOf(AppApplication.getMyUserIdWithdefault())
                            );
                        }
                        mClearAllMessagePop.hide();
                    })
                    .bottomClickListener(() -> mClearAllMessagePop.hide()).build();
        }
        mClearAllMessagePop.show();
    }

    private void setGroupData() {
        if (mChatType == EaseConstant.CHATTYPE_SINGLE) {
            // 单聊处理布局
            user = mPresenter.getUserInfoFromLocal(mChatId);
            setIsStick();
            ImageUtils.loadUserHead(user, mIvUserPortrait, false);
            mTvUserName.setText(user.getName());
            mIvUserPortrait.setOnClickListener(v -> PersonalCenterFragment.startToPersonalCenter(getContext(), user));
            //设置置顶
            mScStickMessage.setOnClickListener((buttonView) -> {
                if (mScStickMessage.isChecked()) {
                    mPresenter.setSticks(String.valueOf(user.getUser_id()), String.valueOf(AppApplication.getMyUserIdWithdefault()), 0);
                } else {
                    mPresenter.setSticks(String.valueOf(user.getUser_id()), String.valueOf(AppApplication.getMyUserIdWithdefault()), 1);
                }
            });
        } else {
            // 非群主屏蔽群管理
            if (!mPresenter.isGroupOwner()) {
                mLlManager.setVisibility(View.GONE);
                mRlBlockMessage.setVisibility(View.VISIBLE);
                mTvGroupHeader.setText(R.string.chat_group_portrait);
                mTvDeleteGroup.setText(getString(R.string.chat_quit_group));
                mScBlockMessage.setEnabled(true);
                mIvGropIconArrow.setVisibility(View.GONE);
                mIvGropNameArrow.setVisibility(View.GONE);
                mTvSetAdmin.setVisibility(View.GONE);
                mLlBannedPost.setVisibility(View.GONE);
                mTvJurisdiction.setVisibility(View.GONE);
                mTvUpgrade.setVisibility(View.GONE);
                vwSetAdmin.setVisibility(View.GONE);
                vwJurisdiction.setVisibility(View.GONE);
                vwUpgrade.setVisibility(View.GONE);
                vwSetStick.setVisibility(View.GONE);

                if (mIsAddGroup) {
                    mLlSetStick.setVisibility(View.GONE);
                    mTvClearMessage.setVisibility(View.GONE);
                    mTvDeleteGroup.setText(getString(R.string.tv_add_group_chat));
                    mRlBlockMessage.setVisibility(View.GONE);
                }

            } else {
                // 群主无法屏蔽消息
                mTvGroupHeader.setText(R.string.chat_set_group_portrait);
                mTvDeleteGroup.setText(getString(R.string.chat_delete_group));
                mRlBlockMessage.setVisibility(View.GONE);
                mScBlockMessage.setEnabled(false);
                mIvGropIconArrow.setVisibility(View.VISIBLE);
                mIvGropNameArrow.setVisibility(View.VISIBLE);
            }
            if (!mIsAddGroup) {
                // 群聊的信息展示
                EMGroup group = EMClient.getInstance().groupManager().getGroup(mChatId);
                // 屏蔽按钮
                mScBlockMessage.setChecked(group.isMsgBlocked());
            }
            // 群名称
            String groupName = mChatGroupBean.getName();
            // + "(" + mChatGroupBean.getAffiliations_count() + ")";
            mTvGroupName.setText(groupName);
            mTvGroupCardName.setText(groupName);
            // 群头像
            ImageUtils.loadCircleImageDefault(mIvGroupPortrait, mChatGroupBean.getGroup_face(), R.mipmap.ico_ts_assistant, R.mipmap.ico_ts_assistant);

            if (mChatMemberAdapter != null && mChatGroupBean != null) {
                mChatMemberAdapter.setOwnerId(mChatGroupBean.getOwner());
                dealAddOrDeleteButton();
                mChatMemberAdapter.refreshData(mChatMembers);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Subscriber(tag = EVENT_IM_GROUP_UPDATE_GROUP_MUTE)
    public void onPublishGroupSuccess(boolean isRefresh) {
        if (isRefresh) {
            mPresenter.getGroupChatInfo(mChatId);//获取群信息
        }
    }

    @Subscriber(tag = EVENT_IM_GROUP_UPDATE_GROUP_NOTICE)
    public void onPublishNoticeSuccess(String noticeStr) {

        mNoticeText.setText(noticeStr);

    }
}
