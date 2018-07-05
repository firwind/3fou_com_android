package com.zhiyicx.thinksnsplus.utils;

import com.zhiyicx.thinksnsplus.data.beans.MessageItemBeanV2;

import java.util.Comparator;

/**
 * author: huwenyong
 * date: 2018/7/5 10:04
 * description: 根据消息的置顶和消息的时间进行排序
 * version:
 */

public class MessageTimeAndStickSort implements Comparator<MessageItemBeanV2>{

    @Override
    public int compare(MessageItemBeanV2 o1, MessageItemBeanV2 o2) {

        int result = 0;
        if(o1.getIsStick() == 1 && o2.getIsStick() != 1)
            //如果o1是置顶，o2不置顶，则o1比o2大
            result = 1;
        else if(o1.getIsStick() != 1 && o2.getIsStick() == 1){
            //如果o1不置顶，o2置顶，则o2比o1大
            result = -1;
        }else if( o1.getIsStick() == o2.getIsStick() ){
            //如果o1和o2都置顶或者都不置顶，则按照时间倒序排列
            if(null != o1.getConversation().getLastMessage() &&
                    null != o2.getConversation().getLastMessage())
                //如果o1和o2的会话都有最后一条消息，则按照时间倒序排列
                result = (o1.getConversation().getLastMessage().getMsgTime() -
                        o2.getConversation().getLastMessage().getMsgTime()) > 0 ? 1 : -1;
            else if(null != o1.getConversation().getLastMessage() &&
                    null == o2.getConversation().getLastMessage())
                //如果o1有最后一条消息，o2没有，则o1比o2大
                result = 1;
            else if(null == o1.getConversation().getLastMessage() &&
                    null != o2.getConversation().getLastMessage())
                //如果o1没有最后一条消息，o2有，则o2比o1大
                result = -1;
            else
                //如果o1和O2都没有最后一条消息，则相等
                result = 0;
        }

        //Comparaotr是按照 正序排的，从小到大 这里返回结果的负数就是  从大到小
        return -result;
    }
}
