package com.zhiyicx.thinksnsplus.data.source.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.SparseArray;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.zhiyicx.baseproject.base.SystemConfigBean;
import com.zhiyicx.baseproject.base.TSListFragment;
import com.zhiyicx.baseproject.em.manager.util.TSEMConstants;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.utils.log.LogUtils;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppApplication;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;
import com.zhiyicx.thinksnsplus.data.source.local.ChatGroupBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.local.UserInfoBeanGreenDaoImpl;
import com.zhiyicx.thinksnsplus.data.source.remote.EasemobClient;
import com.zhiyicx.thinksnsplus.data.source.remote.ServiceManager;
import com.zhiyicx.thinksnsplus.modules.home.message.messagelist.EmTimeSortClass;
import com.zhiyicx.thinksnsplus.utils.TSImHelperUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.zhiyicx.baseproject.base.TSListFragment.DEFAULT_ONE_PAGE_SHOW_MAX_SIZE;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/29
 * @contact email:648129313@qq.com
 */

public class BaseMessageRepository implements IBaseMessageRepository {

    @Inject
    Application mContext;
    @Inject
    UserInfoRepository mUserInfoRepository;
    @Inject
    UserInfoBeanGreenDaoImpl mUserInfoBeanGreenDao;
    @Inject
    SystemRepository mSystemRepository;
    @Inject
    ChatGroupBeanGreenDaoImpl mChatGroupBeanGreenDao;
    protected EasemobClient mClient;

    @Inject
    public BaseMessageRepository(ServiceManager manager) {
        mClient = manager.getEasemobClient();
    }

    @Override
    public Observable<List<MessageItemBeanV2>> getConversationList(int userId) {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return Observable.just(conversations)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(stringEMConversationMap -> {
                    // 先将环信返回的数据转换为model 然后根据type来处理是否需要获取用户信息，如果不需要则直接返回数据
                    List<MessageItemBeanV2> list = new ArrayList<>();
                    for (Map.Entry<String, EMConversation> entry : stringEMConversationMap.entrySet()) {
                        MessageItemBeanV2 itemBeanV2 = new MessageItemBeanV2();
                        itemBeanV2.setConversation(entry.getValue());
                        itemBeanV2.setEmKey(entry.getKey());
                        list.add(itemBeanV2);
                    }
                    LogUtils.d("getConversationList::", list.size());
                    // 再在第一条插入ts助手，前提是当前消息列表中没有小助手的消息
                    List<SystemConfigBean.ImHelperBean> tsHlepers = mSystemRepository.getBootstrappersInfoFromLocal().getIm_helper();
                    // 需要手动插入的小助手，本地查找不到会话才插入聊天信息
                    List<SystemConfigBean.ImHelperBean> needAddedHelpers = new ArrayList<>();
                    if (tsHlepers != null && !tsHlepers.isEmpty()) {
                        for (SystemConfigBean.ImHelperBean imHelperBean : tsHlepers) {
                            if (AppApplication.getMyUserIdWithdefault() != Long.valueOf(imHelperBean.getUid()) && EMClient.getInstance().chatManager()
                                    .getConversation(imHelperBean.getUid()) == null) {
                                needAddedHelpers.add(imHelperBean);
                            }
                        }
                        List<MessageItemBeanV2> messageItemBeanList = new ArrayList<>();
                        for (SystemConfigBean.ImHelperBean imHelperBean : needAddedHelpers) {
                            MessageItemBeanV2 tsHelper = new MessageItemBeanV2();
                            tsHelper.setEmKey(imHelperBean.getUid());
                            tsHelper.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(imHelperBean.getUid())));
                            // 创建会话的 conversation 要传入用户名 ts+采用用户Id作为用户名，聊天类型 单聊
                            EMConversation conversation =
                                    EMClient.getInstance().chatManager().getConversation(tsHelper.getEmKey(), EMConversation
                                            .EMConversationType
                                            .Chat, true);
                            // 没有被清空历史消息的才加入消息提示；
                            if (!TSImHelperUtils.getMessageHelperIsDeletedHistory(mContext, imHelperBean.getUid(), String.valueOf(AppApplication
                                    .getMyUserIdWithdefault()))) {
                                // 给这个会话插入一条自定义的消息 文本类型的
                                EMMessage welcomeMsg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
                                welcomeMsg.setMsgId(tsHelper.getEmKey());
                                // 消息体
                                EMTextMessageBody textBody = new EMTextMessageBody(mContext.getString(R.string.ts_helper_default_tip));
                                welcomeMsg.addBody(textBody);
                                // 来自 用户名
                                welcomeMsg.setFrom(tsHelper.getEmKey());
                                // 当前时间
                                welcomeMsg.setMsgTime(System.currentTimeMillis());
                                conversation.insertMessage(welcomeMsg);
                            }

                            tsHelper.setConversation(conversation);
                            messageItemBeanList.add(tsHelper);
                        }
                        list.addAll(0, messageItemBeanList);
                    }
                    return completeEmConversation(list)
                            .map(list1 -> {
                                /*List<MessageItemBeanV2> tmps = new ArrayList<>();
                                HashSet<String> emKeys = new HashSet<>();

                                for (MessageItemBeanV2 messageItemBeanV2 : list1) {
                                    if (emKeys.contains(messageItemBeanV2.getEmKey())) {
                                        continue;
                                    }
                                    emKeys.add(messageItemBeanV2.getEmKey());
                                    boolean ischatAndImHelper = EMConversation.EMConversationType.Chat == messageItemBeanV2.getConversation()
                                            .getType() && messageItemBeanV2.getUserInfo() != null && mSystemRepository.checkUserIsImHelper
                                            (messageItemBeanV2.getUserInfo().getUser_id());
                                    boolean isHasMessage = messageItemBeanV2.getConversation() != null && messageItemBeanV2.getConversation()
                                            .getLastMessage()
                                            != null;
                                    if (ischatAndImHelper || isHasMessage) {
                                        tmps.add(messageItemBeanV2);
                                    }

                                }
                                if (tmps.size() > 1) {
                                    // 数据大于一个才排序
                                    Collections.sort(tmps, new EmTimeSortClass());
                                }*/
                                if (list1.size() > 1) {
                                    // 数据大于一个才排序
                                    Collections.sort(list1, new EmTimeSortClass());
                                }
                                return /*tmps*/list1;
                            });
                });
    }

    /**
     * 完善回话信息
     *
     * @param list 回话列表，包涵单聊、群聊
     * @return
     */
    @Override
    public Observable<List<MessageItemBeanV2>> completeEmConversation(List<MessageItemBeanV2> list) {

        return Observable.just(list)
                .subscribeOn(Schedulers.io())
                .flatMap(list1 -> {
                    // 保存数据库没有的用户 id
                    List<Object> users = new ArrayList<>();
                    // 数据库里面没有的群信息
                    final StringBuilder groupIds = new StringBuilder();
                    for (MessageItemBeanV2 itemBeanV2 : list1) {

                        if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.Chat) {
                            // 单聊处理用户信息，首先过滤掉环信后台的管理员有用户 admin
                            if (!itemBeanV2.getEmKey().equals("admin")) {
                                try {
                                    itemBeanV2.setUserInfo(mUserInfoBeanGreenDao.getSingleDataFromCache(Long.parseLong(itemBeanV2.getEmKey())));
                                } catch (NumberFormatException ignored) {
                                }
                                if (itemBeanV2.getUserInfo() == null) {
                                    users.add(itemBeanV2.getEmKey());
                                }
                            }
                        } else if (itemBeanV2.getConversation().getType() == EMConversation.EMConversationType.GroupChat) {
                            // 群聊
                            String chatGroupId = itemBeanV2.getConversation().conversationId();

                            //拿到群聊信息最后一条记录，看发消息的人本地数据库有没有
                            EMMessage message = itemBeanV2.getConversation().getLastMessage();

                            //如果是 admin ,消息会是：xxx修改了群信息，xxx进入了聊天群之类的通知
                            if (null != message && "admin".equals(message.getFrom()) &&  null != message.ext()) {
                                boolean isUserJoin = TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
                                boolean isUserExit = TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"));
                                //这个userId格式可能不合法，例如邀请多个人聊天，这里的userId 会是  [3,4,5]
                                //这里只拿单个的用户信息
                                Long userId = null;
                                try {
                                    userId = Long.parseLong((String) message.ext().get("uid"));
                                }catch (Exception e){
                                    //
                                }
                                if (null != userId && isUserJoin) {
                                    UserInfoBean userInfoBean = mUserInfoBeanGreenDao.getSingleDataFromCache(userId);
                                    if (userInfoBean == null) {
                                        users.add(userId);
                                    }
                                }
                                /*这里重复创建了会话 fix by huwenyong on 2018/06/29

                                if (groupIds.indexOf(chatGroupId) == -1 && (isUserJoin || isUserExit)) {
                                    groupIds.append(chatGroupId);
                                    groupIds.append(",");
                                }*/

                            } else {
                                Long userId = null;
                                try {
                                    userId = Long.parseLong(message.getFrom());
                                }catch (Exception e){
                                    //
                                }
                                if (null != userId && mUserInfoBeanGreenDao.getSingleDataFromCache(userId) == null) {
                                    users.add(itemBeanV2.getConversation().getLastMessage().getFrom());
                                }
                            }


                            ChatGroupBean chatGroupBean = mChatGroupBeanGreenDao.getChatGroupBeanById(chatGroupId);

                            if (chatGroupBean == null) {
                                // 之前在这里也许重复创建了会话 ，fix by tym on 2018-5-4 16:09:22
                                if (groupIds.indexOf(chatGroupId) == -1) {
                                    groupIds.append(chatGroupId);
                                    groupIds.append(",");
                                }
                            } else {
                                itemBeanV2.setEmKey(chatGroupBean.getId());
                                itemBeanV2.setList(chatGroupBean.getAffiliations());
                                itemBeanV2.setConversation(EMClient.getInstance().chatManager().getConversation(chatGroupBean.getId()));
                                itemBeanV2.setChatGroupBean(chatGroupBean);
                            }

                        } else {
                            // TODO: 2018/2/3   chatRoom 等
                        }
                    }
                    return Observable.just(users)
                            .flatMap(objects -> {
                                if (users.isEmpty()) {
                                    return Observable.just(list1);
                                }
                                return mUserInfoRepository.getUserInfo(users)
                                        .map(userInfoBeans -> {
                                            SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                            for (UserInfoBean userInfoBean : userInfoBeans) {
                                                userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                            }
                                            for (int i = 0; i < list1.size(); i++) {
                                                // 只有单聊才给用户信息
                                                if (list1.get(i).getConversation().getType() == EMConversation.EMConversationType.Chat) {
                                                    try {
                                                        int key = Integer.parseInt(list1.get(i).getEmKey());
                                                        if (list1.get(i).getUserInfo() == null) {
                                                            list1.get(i).setUserInfo(userInfoBeanSparseArray.get(key));
                                                        }
                                                        // 因为去重，所以 userInfoBeanSparseArray.size < list1.size
                                                        if (list1.get(i).getUserInfo() == null) {
                                                            list1.get(i).setUserInfo(mUserInfoBeanGreenDao.getUserInfoById("" + key));
                                                        }
                                                    } catch (NumberFormatException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else {
                                                    // 退出群组
                                                    EMMessage message = list1.get(i).getConversation().getLastMessage();

                                                    if(null != message && null != message.ext()){
                                                        boolean isUserJoin = TSEMConstants.TS_ATTR_JOIN.equals(message.ext().get("type"));
                                                        boolean isUserExit = TSEMConstants.TS_ATTR_EIXT.equals(message.ext().get("type"));

                                                        if (isUserExit || isUserJoin && EMMessage.Type.TXT.equals(message.getType())) {
                                                            String id = ((EMTextMessageBody) message.getBody()).getMessage();
                                                            try {
                                                                int key = Integer.parseInt(id);
                                                                UserInfoBean userInfoBean = userInfoBeanSparseArray.get(key);
                                                                EMTextMessageBody textBody = new EMTextMessageBody(mContext.getResources().getString(R
                                                                        .string.userup_exit_group, userInfoBean.getName()));
                                                                message.addBody(textBody);
                                                            } catch (Exception ignore) {
                                                            }
                                                        }
                                                    }

                                                }
                                            }
                                            return list1;
                                        });
                            }).flatMap(messages -> {
                                if (TextUtils.isEmpty(groupIds.toString())) {
                                    //todo:此处去重 感觉没有必要，暂时干掉这段代码
                                    /*String lastConversationId = "";
                                    List<MessageItemBeanV2> repeatItemBeanList = new ArrayList<>();
                                    for (MessageItemBeanV2 exitItem : messages) {
                                        String currentConversationId = exitItem.getConversation().conversationId();

                                        if (!lastConversationId.equals(currentConversationId)) {
                                            repeatItemBeanList.add(exitItem);
                                        }
                                        lastConversationId = exitItem.getConversation().conversationId();
                                    }*/
                                    return Observable.just(messages/*repeatItemBeanList*/);
                                }
                                return getGroupInfo(groupIds.deleteCharAt(groupIds.length() - 1).toString())
                                        .flatMap(data -> {
                                            //data是从服务器获取的 群聊信息
                                            //保存信息到数据库
                                            mChatGroupBeanGreenDao.saveMultiData(data);
                                            //遍历data和list1，将list1中未获取到群信息的MessageItemBeanV2塞进去ChatGroupBean
                                            for (ChatGroupBean chatGroupBean:
                                                 data) {

                                                for (MessageItemBeanV2 message:
                                                     list1) {

                                                    if(chatGroupBean.getId().equals(message.getConversation().conversationId())){

                                                        message.setEmKey(chatGroupBean.getId());
                                                        message.setList(chatGroupBean.getAffiliations());
                                                        message.setChatGroupBean(chatGroupBean);

                                                        //如果匹配，结束掉当前循环
                                                        break;
                                                    }

                                                }

                                            }

                                            /*List<MessageItemBeanV2> repeatItemBeanList = new ArrayList<>();
                                            String lastConversationId = "";
                                            mChatGroupBeanGreenDao.saveMultiData(data);
                                            for (ChatGroupBean chatGroupBean : data) {
                                                mUserInfoBeanGreenDao.saveMultiData(chatGroupBean.getAffiliations());
                                                for (MessageItemBeanV2 exitItem : list1) {
                                                    String currentConversationId = exitItem.getConversation().conversationId();

                                                    if (currentConversationId.equals(chatGroupBean.getId())) {
                                                        exitItem.setEmKey(chatGroupBean.getId());
                                                        exitItem.setList(chatGroupBean.getAffiliations());
                                                        exitItem.setConversation(EMClient.getInstance().chatManager().getConversation
                                                                (chatGroupBean
                                                                        .getId()));
                                                        exitItem.setChatGroupBean(chatGroupBean);
                                                    }

                                                    if (!lastConversationId.equals(currentConversationId)) {
                                                        repeatItemBeanList.add(exitItem);
                                                    }
                                                    lastConversationId = exitItem.getConversation().conversationId();
                                                }

                                            }*/

                                            return Observable.just(list1/*repeatItemBeanList*/);
                                        });
                            });
                });
    }

    /**
     * 获取群组信息
     *
     * @param ids 群 id , 以 ， 分割
     * @return
     */
    @Override
    public Observable<List<ChatGroupBean>> getGroupInfo(String ids) {
        return mClient.getGroupInfo(ids)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取群组信息，只需要群头像
     *
     * @param ids 群 id , 以 ， 分割
     * @return
     */
    @Override
    public Observable<List<ChatGroupBean>> getGroupInfoOnlyGroupFace(String ids) {
        return mClient.getGroupInfoOnlyGroupFace(ids)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<ChatGroupServerBean>> getSearchGroupInfoFace(String groupName) {
        if (null != groupName && groupName.length() > 0) {
            return mClient.getSearchGroupInfo(groupName)
                    .subscribeOn(Schedulers.io());
        } else {
            return mClient.getSearchGroupInfoFace()
                    .subscribeOn(Schedulers.io());
        }
    }

    @Override
    public void deleteLocalChatGoup(String id) {
        mChatGroupBeanGreenDao.deleteChatGroupBeanById(id);
    }

    @Override
    public void saveChatGoup(List<ChatGroupBean> groupBeans) {
        mChatGroupBeanGreenDao.saveMultiData(groupBeans);
    }

    @Override
    public Observable<UserInfoBean> getUserInfo(String id) {
        return null;
    }

    @Override
    public Observable<List<ChatItemBean>> completeUserInfo(List<ChatItemBean> list) {
        return Observable.just(list)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(list1 -> {
                    List<Object> users = new ArrayList<>();
                    for (ChatItemBean chatItemBean : list1) {
                        if ("admin".equals(chatItemBean.getMessage().getFrom())) {
                            users.add(1L);
                        } else {
                            users.add(chatItemBean.getMessage().getFrom());
                        }
                    }
                    return mUserInfoRepository.getUserInfo(users)
                            .map(userInfoBeans -> {
                                SparseArray<UserInfoBean> userInfoBeanSparseArray = new SparseArray<>();
                                for (UserInfoBean userInfoBean : userInfoBeans) {
                                    userInfoBeanSparseArray.put(userInfoBean.getUser_id().intValue(), userInfoBean);
                                }
                                for (int i = 0; i < list1.size(); i++) {
                                    int key;
                                    if ("admin".equals(list1.get(i).getMessage().getFrom())) {
                                        key = 1;
                                    } else {
                                        key = Integer.parseInt(list1.get(i).getMessage().getFrom());
                                    }
                                    list1.get(i).setUserInfo(userInfoBeanSparseArray.get(key));
                                    if (list1.get(i).getUserInfo() == null && !"admin".equals(list1.get(i).getMessage().getFrom())) {
                                        list1.get(i).setUserInfo(mUserInfoBeanGreenDao.getUserInfoById(key + ""));
                                    }
                                }
                                return list1;
                            });
                });
    }

    @Override
    public Observable<List<NoticeItemBean>> noticeList(String group_id) {
        return mClient.getGruopNoticeList(group_id)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> releaseNotice(String group_id, String title, String content, String author) {
        return mClient.releaseNotice(group_id, title, content, author)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<String> addGroupAlbum(String images, String group_id) {
        return mClient.addGroupAlbum(images, group_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<String> deleteGroupAlbum(String image_id,String im_group_id) {
        return mClient.deleteGroupAlbum(image_id,im_group_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<BaseJsonV2<List<MessageGroupAlbumBean>>> getGroupAlbumList(String group_id, int page) {
        return mClient.getGroupAlbumList(group_id, TSListFragment.DEFAULT_PAGE_SIZE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ChatGroupBean>> getSimpleGroupList(String group_id, int group_level) {
        return mClient.getSimpleGroupInfo(group_id,group_level)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
