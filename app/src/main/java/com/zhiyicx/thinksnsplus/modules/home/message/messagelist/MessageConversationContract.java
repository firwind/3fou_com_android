package com.zhiyicx.thinksnsplus.modules.home.message.messagelist;

import com.hyphenate.easeui.bean.ChatUserInfoBean;
import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.common.base.BaseFragment;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBean;
import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;
import com.zhiyicx.thinksnsplus.data.beans.StickBean;
import com.zhiyicx.thinksnsplus.data.source.repository.IBaseMessageRepository;

import java.util.List;


/**
 * @author Catherine
 * @describe
 * @date 2017/12/28
 * @contact email:648129313@qq.com
 */

public interface MessageConversationContract {

    /**
     * 对于经常使用的关于 UI 的方法可以定义到 BaseView 中,如显示隐藏进度条,和显示文字消息
     */
    interface View extends ITSListView<MessageItemBeanV2, Presenter> {
        void showNewMessageTip(boolean isShow);
    }

    /**
     * Model 层定义接口,外部只需关心 model 返回的数据,无需关心内部细节,及是否使用缓存
     */
    interface Repository extends IBaseMessageRepository {
    }

    interface Presenter extends ITSListPresenter<MessageItemBeanV2> {
        /**
         * 群/单聊ID
         *
         * @param author
         */
        /*void refreshSticks(String author);*/

        /**
         * 上传置顶群/单聊ID
         * @param message
         * @param author
         */
        void setSticks(MessageItemBeanV2 message, String author);

        /**
         * 刷新是否显示底部红点
         * 刷新当条item 的未读数
         */
        void refreshConversationReadMessage();

        /**
         * 删除本地对话
         *
         * @param position 位置
         */
        void deleteConversation(int position);

        /**
         * 获取聊天用户列表
         *
         * @param position 点击位置
         * @return List<ChatUserInfoBean>
         */
        List<ChatUserInfoBean> getChatUserList(int position);

        void deleteGroup(String id);

        /**
         * @param userId 用户 id
         * @return true 是 IM 小助手
         */
        boolean checkUserIsImHelper(long userId);

        void updateGroup(String id);
    }
}
