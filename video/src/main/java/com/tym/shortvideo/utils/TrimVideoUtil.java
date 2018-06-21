package com.tym.shortvideo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.tym.shortvideo.filter.helper.MagicFilterType;
import com.tym.shortvideo.interfaces.SingleCallback;
import com.tym.shortvideo.interfaces.TrimVideoListener;
import com.tym.shortvideo.media.VideoInfo;
import com.tym.shortvideo.mediacodec.VideoClipper;
import com.zhiyicx.common.utils.FileUtils;
import com.zhiyicx.common.utils.log.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Jliuer
 * @Date 18/04/28 9:46
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class TrimVideoUtil {

    private static final String TAG = TrimVideoUtil.class.getSimpleName();
    public static final int VIDEO_MAX_DURATION = 15;
    public static final int MIN_TIME_FRAME = 3;

    // 总宽度 15 s
    private static int thumb_Width = (DeviceUtils.getScreenWidth() - DeviceUtils.dipToPX(20)) / VIDEO_MAX_DURATION;
    private static final int thumb_Height = DeviceUtils.dipToPX(60);
    private static final long one_frame_time = 1000000;

    /**
     * 剪辑视频时长
     *
     * @param context
     * @param inputFile
     * @param outputFile
     * @param startMs    单位是微妙
     * @param endMs
     * @param callback
     */
    public static void trim(Context context, Uri inputFile, String outputFile, long startMs, long
            endMs, final TrimVideoListener callback) {
        VideoClipper clipper = new VideoClipper();
        clipper.setFilterType(MagicFilterType.NONE);
        clipper.setInputVideoPath(context, inputFile);
        clipper.setOutputVideoPath(outputFile);
        final String tempOutFile = outputFile;

        clipper.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
            @Override
            public void onFinish() {
                callback.onFinishTrim(tempOutFile);
            }

            @Override
            public void onFailed() {
                callback.onCancel();
            }
        });
        try {
            clipper.clipVideo(startMs, endMs - startMs);
            callback.onStartTrim();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Observable<Bitmap> getVideoOneFrame(final Context context, final Uri videoUri) {

        return Observable.just(1)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Integer, Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(Integer integer) {
                        MediaMetadataRetriever mediaMetadataRetriever =
                                new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(context,
                                videoUri);

                        Bitmap bitmap = mediaMetadataRetriever
                                .getFrameAtTime(1,
                                        MediaMetadataRetriever
                                                .OPTION_CLOSEST_SYNC);

                        mediaMetadataRetriever.release();
                        return Observable.just(bitmap);
                    }
                });

    }

    /**
     * 取视频图片
     *
     * @param context
     * @param videoUri
     * @param callback
     */
    public static void backgroundShootVideoThumb(final Context context, final Uri videoUri, final
    SingleCallback<ArrayList<Bitmap>, Integer> callback) {
        final ArrayList<Bitmap> thumbnailList = new ArrayList<>();
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
                                       @Override
                                       public void execute() {
                                           try {
                                               MediaMetadataRetriever mediaMetadataRetriever =
                                                       new MediaMetadataRetriever();
                                               mediaMetadataRetriever.setDataSource(context,
                                                       videoUri);
                                               // 微秒
                                               long videoLengthInMs = Long.parseLong
                                                       (mediaMetadataRetriever.extractMetadata
                                                               (MediaMetadataRetriever
                                                                       .METADATA_KEY_DURATION)) *
                                                       1000;
                                               long numThumbs = videoLengthInMs < one_frame_time
                                                       ? 1 : (videoLengthInMs / one_frame_time);
                                               final long interval = videoLengthInMs / numThumbs;
                                               float w, h;
                                               w = h = 0;
                                               w = thumb_Width;
                                               for (long i = 0; i < numThumbs; ++i) {
                                                   Bitmap bitmap = mediaMetadataRetriever
                                                           .getFrameAtTime(i * interval,
                                                                   MediaMetadataRetriever
                                                                           .OPTION_CLOSEST_SYNC);
                                                   if (bitmap == null) {
                                                       continue;
                                                   }
                                                   bitmap = BitmapUtils.zoomBitmap(bitmap, thumb_Width, thumb_Height, false);
                                                   thumbnailList.add(bitmap);
                                                   if (thumbnailList.size() == 3) {
                                                       callback.onSingleCallback(
                                                               (ArrayList<Bitmap>) thumbnailList
                                                                       .clone(), (int) interval);
                                                       thumbnailList.clear();
                                                   }
                                               }
                                               if (thumbnailList.size() > 0) {
                                                   callback.onSingleCallback((ArrayList<Bitmap>)
                                                           thumbnailList.clone(), (int) interval);
                                                   thumbnailList.clear();
                                               }
                                               mediaMetadataRetriever.release();
                                           } catch (final Throwable e) {
                                               Thread.getDefaultUncaughtExceptionHandler()
                                                       .uncaughtException(Thread.currentThread(),
                                                               e);
                                           }
                                       }
                                   }
        );

    }

    public static String getVideoFilePath(String url) {

        if (TextUtils.isEmpty(url) || url.length() < 5) {
            return "";
        }

        if (url.substring(0, 4).equalsIgnoreCase("http")) {
        } else {
            url = "file://" + url;
        }

        return url;
    }

    private static String convertSecondsToTime(long seconds) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (seconds <= 0) {
            return "00:00";
        } else {
            minute = (int) seconds / 60;
            if (minute < 60) {
                second = (int) seconds % 60;
                timeStr = "00:" + unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = (int) (seconds - hour * 3600 - minute * 60);
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    public static List<VideoInfo> getAllVideoFiles(final Context mContext) {
        VideoInfo video;
        ArrayList<VideoInfo> videos = new ArrayList<>();
        ContentResolver contentResolver = mContext
                .getContentResolver();
        try {

            Cursor cursor = contentResolver.query(MediaStore
                            .Video.Media.EXTERNAL_CONTENT_URI,
                    null,
                    null, null, MediaStore.Video.Media
                            .DEFAULT_SORT_ORDER);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    video = new VideoInfo();
                    video.setPath(cursor.getString(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.DATA)));
                    video.setCreateTime(cursor.getString
                            (cursor.getColumnIndex(MediaStore
                                    .Video.Media.DATE_ADDED)));
                    video.setName(cursor.getString(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.DISPLAY_NAME)));
                    video.setWidth(cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.WIDTH)));
                    video.setHeight(cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.HEIGHT)));

                    video.setDuration((int) cursor.getLong(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.DURATION)));
                    video.setStoreId(cursor.getInt(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media._ID)));
                    String mime = cursor.getString(cursor
                            .getColumnIndex(MediaStore.Video
                                    .Media.MIME_TYPE));

                    if (!"video/mp4".equals(mime)) {
                        continue;
                    }
                    if (!FileUtils
                            .isFileExists(video.getPath())) {
                        continue;
                    }

                    if (video.getDuration() < 500) {
                        continue;
                    }
                    videos.add(video);
                }
                cursor.close();
            }
        } catch (Exception ignore) {

        }
        return videos;
    }

    /**
     * 读取媒体库视频
     *
     * @param mContext
     * @param callback
     */
    public static void getAllVideoFiles(final Context mContext, final
    SingleCallback<ArrayList<VideoInfo>, Integer> callback) {
        BackgroundExecutor.execute(new BackgroundExecutor.Task("", 0L, "") {
                                       @Override
                                       public void execute() {
                                           VideoInfo video;
                                           ArrayList<VideoInfo> videos = new ArrayList<>();
                                           ContentResolver contentResolver = mContext
                                                   .getContentResolver();
                                           try {

                                               Cursor cursor = contentResolver.query(MediaStore
                                                               .Video.Media.EXTERNAL_CONTENT_URI,
                                                       null,
                                                       null, null, MediaStore.Video.Media
                                                               .DEFAULT_SORT_ORDER);
                                               if (cursor != null) {
                                                   while (cursor.moveToNext()) {
                                                       video = new VideoInfo();
                                                       video.setPath(cursor.getString(cursor
                                                               .getColumnIndex(MediaStore.Video
                                                                       .Media.DATA)));
                                                       video.setCreateTime(cursor.getString
                                                               (cursor.getColumnIndex(MediaStore
                                                                       .Video.Media.DATE_ADDED)));
                                                       video.setName(cursor.getString(cursor
                                                               .getColumnIndex(MediaStore.Video
                                                                       .Media.DISPLAY_NAME)));
                                                       video.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns.SIZE)));

                                                       int w, h;
                                                       MediaMetadataRetriever mediaMetadataRetriever =
                                                               new MediaMetadataRetriever();
                                                       mediaMetadataRetriever.setDataSource(mContext, Uri.parse(video.getPath()));


                                                       try {
                                                           int rotation = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
                                                           w = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                                                           h = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                                                           if (rotation == 90 || rotation == 270) {
                                                               video.setWidth(h);
                                                               video.setHeight(w);
                                                           } else {
                                                               video.setWidth(w);
                                                               video.setHeight(h);
                                                           }

                                                       } catch (Exception e) {
                                                           continue;
                                                       } finally {
                                                           mediaMetadataRetriever.release();
                                                       }

                                                       if (w * h == 0) {
                                                           continue;
                                                       }

                                                       video.setDuration((int) cursor.getLong(cursor
                                                               .getColumnIndex(MediaStore.Video
                                                                       .Media.DURATION)));
                                                       video.setStoreId(cursor.getInt(cursor
                                                               .getColumnIndex(MediaStore.Video
                                                                       .Media._ID)));
                                                       String mime = cursor.getString(cursor
                                                               .getColumnIndex(MediaStore.Video
                                                                       .Media.MIME_TYPE));

                                                       if (!"video/mp4".equals(mime)) {
                                                           continue;
                                                       }
                                                       if (!com.zhiyicx.common.utils.FileUtils
                                                               .isFileExists(video.getPath())) {
                                                           continue;
                                                       }

                                                       if (video.getDuration() < 3000) {
                                                           continue;
                                                       }
                                                       videos.add(video);
                                                       if (videos.size() >= 20) {
                                                           callback.onSingleCallback(new ArrayList<>(videos), 20);
                                                           videos.clear();
                                                       }

                                                   }
                                                   cursor.close();
                                                   callback.onSingleCallback(new ArrayList<>
                                                           (videos), -1);
                                               }
                                           } catch (Exception e) {
                                               callback.onSingleCallback(videos, 0);
                                               e.printStackTrace();
                                           }
                                       }
                                   }
        );
    }
}
