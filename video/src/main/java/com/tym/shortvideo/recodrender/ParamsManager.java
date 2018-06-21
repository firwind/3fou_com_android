package com.tym.shortvideo.recodrender;

import android.content.Context;
import android.os.Environment;

import com.tym.shortvideo.filter.helper.type.GLType;


/**
 * @author Jliuer
 * @Date 18/04/28 9:32
 * @Email Jliuer@aliyun.com
 * @Description 管理全局参数和上下文
 */
public class ParamsManager {

    private ParamsManager() {
    }

    // 上下文，方便滤镜使用
    public static Context context;

    // 默认相册位置
    public static final String AlbumPath = "/DCIM/Camera/";

    // 图片存放地址
    public static final String ImagePath = "/Zhiyi/Image/";

    // 视频存放地址
    public static final String VideoPath = "/Zhiyi/Video/";

    // 录制时分段视频
    public static final String RecordPath = "/Zhiyi/Record/";

    // 合并后的视频
    public static final String CombineVideo = "zhiyi_combine.mp4";

    // 处理完准备发送的视频
    public static final String ClipVideo = "zhiyi_done.mp4";

    // 压缩剪辑的视频存放
    public static final String CompressVideo = "zhiyi_compress.mp4";

    // 处理完准备发送的视频存放目录
    public static final String SaveVideo = "Zhiyi/shortvideo/";

    // 视频封面图
    public static final String VideoCover = "video_cover.jpg";

    // 存放预览类型，GIF表情包、PICTURE拍照、VIDEO视频等
    public static GLType sMGLType = GLType.PICTURE;

    // 是否允许录音(用户自行设置，默认开启)
    public static boolean canRecordingAudio = true;

    // 是否允许位置
    public static boolean canRecordingLocation = false;

    // 是否倒置(Nexus 5X与其他手机不一样，后置摄像头图像会倒置)
    public static boolean mBackReverse = false;
}
