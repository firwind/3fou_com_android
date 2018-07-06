package com.zhiyicx.thinksnsplus.data.source.repository;

import com.hyphenate.chat.EMGroup;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupServerBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.NoticeItemBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/29
 * @contact email:648129313@qq.com
 */

public interface IBaseMessageRepository {
    /**
     * 获取对话列表信息
     *
     * @param user_id 用户 id
     */
    Observable<List<MessageItemBeanV2>> getConversationList(int user_id);

    Observable<List<MessageItemBeanV2>> completeEmConversation(List<MessageItemBeanV2> list);

    /**
     * 获取多组群信息
     *
     * @param ids 群 id , 以 ， 分割
     * @return
     */
    Observable<List<ChatGroupBean>> getGroupInfo(String ids);

    /**
     * 获取多组群信息，只有群头像
     *
     * @param ids 群 id , 以 ， 分割
     * @return
     */
    Observable<List<ChatGroupBean>> getGroupInfoOnlyGroupFace(String ids);
    /**
     * 获取所有官方群
     *
     * @param
     * @return
     */
    Observable<List<ChatGroupBean>> getOfficialGroupInfo();

    /**
     * 获取多组群信息，只有群头像
     *
     * @param
     * @return
     */
    Observable<ChatGroupBean> getGroupInfoOnlyGroupFaceV2();

    /**
     * 获取搜索群列表
     *
     * @param ids 群 id , 以 ， 分割
     * @return
     */
    Observable<List<ChatGroupServerBean>> getSearchGroupInfoFace(String ids);

    /**
     * 完善用户信息 ，在 聊天详情界面
     *
     * @param id
     * @return
     */
    Observable<UserInfoBean> getUserInfo(String id);

    /**
     * 删除本地群聊信息
     *
     * @param id
     */
    void deleteLocalChatGoup(String id);

    void saveChatGoup(List<ChatGroupBean> groupBeans);

    Observable<List<ChatItemBean>> completeUserInfo(List<ChatItemBean> list);

    /**
     * 获取群公告列表
     *
     * @param group_id 群ID
     * @return
     */
    Observable<List<NoticeItemBean>> noticeList(String group_id);

    /**
     * 发布公告
     *
     * @param
     * @return
     */
    Observable<String> releaseNotice(String group_id, String title, String content, String author,int state);

    /**
     * 删除公告
     * @param notice_id
     * @return
     */
    Observable<String> delNotice(String notice_id);

    /**
     * 添加群相册
     * @param images
     * @param group_id
     * @return
     */
    Observable<String> addGroupAlbum(String images, String group_id);

    /**
     * 删除群相册
     * @param image_id
     * @return
     */
    Observable<String> deleteGroupAlbum(String image_id,String im_group_id);

    /**
     * 获取群相册图片
     * @param group_id
     * @param page
     * @return
     */
    Observable<BaseJsonV2<List<MessageGroupAlbumBean>>> getGroupAlbumList(String group_id, int page);


    /**
     * 简单获取群信息
     * @param group_id
     * @param group_level
     * @return
     */
    Observable<List<ChatGroupBean>> getSimpleGroupList(String group_id,int group_level);

    /**
     * 检测是否加入该群
     * @param group_id
     * @param
     * @return
     */
    Observable<ChatGroupBean> getChickIsAddGroup(String group_id);

}
