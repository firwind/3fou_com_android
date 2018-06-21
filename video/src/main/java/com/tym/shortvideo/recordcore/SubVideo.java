package com.tym.shortvideo.recordcore;

/**
 * Created by cain.huang on 2017/12/29.
 */


import com.tym.shortvideo.utils.FileUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Jliuer
 * @Date 18/04/28 9:36
 * @Email Jliuer@aliyun.com
 * @Description 录制分段视频信息
 */
public class SubVideo implements Serializable {
    // 视频路径
    public String mediaPath;
    // 视频长度
    public int duration;

    public SubVideo() {
    }

    // 删除视频
    public void delete() {
        FileUtils.deleteFile(mediaPath);
        duration = 0;
        mediaPath = null;
    }

    /**
     * 获取时长
     *
     * @return
     */
    public int getDuration() {
        return duration;
    }

    /**
     * 获取媒体路径
     *
     * @return
     */
    public String getMediaPath() {
        return mediaPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubVideo subVideo = (SubVideo) o;
        return duration == subVideo.duration &&
                mediaPath.equals(subVideo.mediaPath);
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + (mediaPath == null ? 0 : mediaPath.hashCode());
        result = 31 * result;
        return result;
    }
}
