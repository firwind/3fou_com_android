package com.tym.shortvideo.interfaces;

import java.nio.ByteBuffer;

/**
 * @author Jliuer
 * @Date 18/04/28 9:31
 * @Email Jliuer@aliyun.com
 * @Description 拍照回调
 */
public interface CaptureFrameCallback {
    void onFrameCallback(ByteBuffer buffer, int width, int height);
}
