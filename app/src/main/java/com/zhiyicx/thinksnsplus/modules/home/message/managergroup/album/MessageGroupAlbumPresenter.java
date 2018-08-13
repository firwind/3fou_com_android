package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.base.BaseJson;
import com.zhiyicx.common.base.BaseJsonV2;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.base.AppBasePresenter;
import com.zhiyicx.thinksnsplus.base.BaseSubscribeForV2;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;
import com.zhiyicx.thinksnsplus.data.source.repository.BaseMessageRepository;
import com.zhiyicx.thinksnsplus.data.source.repository.UpLoadRepository;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 17:01
 *     desc :
 *     version : 1.0
 * <pre>
 */
@FragmentScoped
public class MessageGroupAlbumPresenter extends AppBasePresenter<MessageGroupAlbumContract.View> implements MessageGroupAlbumContract.Presenter {
    @Inject
    public MessageGroupAlbumPresenter(MessageGroupAlbumContract.View rootView) {
        super(rootView);
    }

    @Inject
    public UpLoadRepository mUpLoadRepository;
    @Inject
    public BaseMessageRepository mMessageRepository;

    @Override
    public void requestNetData(Long maxId, boolean isLoadMore) {

        addSubscrebe(mMessageRepository.getGroupAlbumList(mRootView.getGroupId(), mRootView.getPage())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscribeForV2<BaseJsonV2<List<MessageGroupAlbumBean>>>() {
                    @Override
                    protected void onSuccess(BaseJsonV2<List<MessageGroupAlbumBean>> data) {
                        mRootView.onNetResponseSuccess(data.getData(), isLoadMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mRootView.onResponseError(e, isLoadMore);
                    }
                }));

    }

    @Override
    public void requestCacheData(Long maxId, boolean isLoadMore) {
        mRootView.onCacheResponseSuccess(null, isLoadMore);
    }

    @Override
    public boolean insertOrUpdateData(@NotNull List<MessageGroupAlbumBean> data, boolean isLoadMore) {
        return false;
    }

    @Override
    public void requestUploadToAlbum(List<ImageBean> imgList) {
        // 先处理图片上传，图片上传成功后，在调接口
        List<Observable<BaseJson<Integer>>> upLoadPics = new ArrayList<>();
        for (int i = 0; i < imgList.size(); i++) {
            String filePath = imgList.get(i).getImgUrl();
            upLoadPics.add(mUpLoadRepository.upLoadSingleImageV2(filePath, imgList.get(i).getImgMimeType(),
                    (int) (imgList.get(i).getWidth()),
                    (int) (imgList.get(i).getHeight())));
        }
        Observable<String> observable = // 组合多个图片上传任务
                Observable.combineLatest(upLoadPics, new FuncN<List<Integer>>() {
                    @Override
                    public List<Integer> call(Object... args) {
                        // 得到图片上传的结果
                        List<Integer> integers = new ArrayList<Integer>();
                        for (Object obj : args) {
                            BaseJson<Integer> baseJson = (BaseJson<Integer>) obj;
                            if (baseJson.isStatus()) {
                                integers.add(baseJson.getData());// 将返回的图片上传任务id封装好
                            } else {
                                throw new NullPointerException();// 某一次失败就抛出异常，重传，因为有秒传功能所以不会浪费多少流量
                            }
                        }
                        return integers;
                    }
                }).flatMap(new Func1<List<Integer>, Observable<String>>() {
                    @Override
                    public Observable<String> call(List<Integer> integers) {
                        StringBuffer images = new StringBuffer();
                        for (int i = 0; i < integers.size(); i++) {
                            images.append(integers.get(i));
                            if (i != integers.size() - 1)
                                images.append(",");
                        }
                        return mMessageRepository.addGroupAlbum(images.toString(), mRootView.getGroupId());// 进行添加群相册
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        addSubscrebe(observable.subscribe(new BaseSubscribeForV2<String>() {
            @Override
            protected void onSuccess(String data) {
                mRootView.showMessage("上传成功");
                mRootView.startRefrsh();
                mRootView.uploadOk();
            }

            @Override
            protected void onFailure(String message, int code) {
                super.onFailure(message, code);
                mRootView.showSnackErrorMessage(message);
            }

            @Override
            protected void onException(Throwable throwable) {
                super.onException(throwable);
                mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
            }

            @Override
            public void onCompleted() {
                super.onCompleted();
                mRootView.hideCenterLoading();
            }
        }));
    }

    @Override
    public void requestDeleteAlbum(MessageGroupAlbumBean messageGroupAlbumBean) {
        addSubscrebe(mMessageRepository.deleteGroupAlbum(String.valueOf(messageGroupAlbumBean.file_id), mRootView.getGroupId())
                .subscribe(new BaseSubscribeForV2<String>() {
                    @Override
                    protected void onSuccess(String data) {
                        mRootView.deleteAlbumOk(messageGroupAlbumBean);
                        mRootView.showSnackSuccessMessage("删除成功!");
                    }

                    @Override
                    protected void onFailure(String message, int code) {
                        super.onFailure(message, code);
                        mRootView.showSnackErrorMessage(message);
                    }

                    @Override
                    protected void onException(Throwable throwable) {
                        super.onException(throwable);
                        mRootView.showSnackErrorMessage(mContext.getString(R.string.network_anomalies));
                    }

                }));
    }
}
