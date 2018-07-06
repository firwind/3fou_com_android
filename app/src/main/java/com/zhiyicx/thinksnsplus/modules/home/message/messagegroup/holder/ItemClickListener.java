package com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.holder;

import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;


/*
 * 文件名:父布局Item点击监听接口
 * 创建者：zhangl
 * 时  间：2018/7/5
 * 描  述：
 * 版  权: 九曲互动
 */
public interface ItemClickListener {
    /**
     * 展开子Item
     *
     * @param bean
     */
    void onExpandChildren(ChatGroupBean bean);

    /**
     * 隐藏子Item
     *
     * @param bean
     */
    void onHideChildren(ChatGroupBean bean);
}
