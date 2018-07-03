package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.album;

import com.zhiyicx.baseproject.base.ITSListPresenter;
import com.zhiyicx.baseproject.base.ITSListView;
import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.mvp.i.IBasePresenter;
import com.zhiyicx.common.mvp.i.IBaseView;
import com.zhiyicx.imsdk.entity.Message;
import com.zhiyicx.thinksnsplus.data.beans.MessageGroupAlbumBean;

import java.util.List;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/22 15:47
 *     desc :
 *     version : 1.0
 * <pre>
 */

public interface MessageGroupAlbumContract {

    interface View extends ITSListView<MessageGroupAlbumBean,Presenter> {
        String getGroupId();
        void uploadOk();
        void deleteAlbumOk(MessageGroupAlbumBean messageGroupAlbumBean);
    }

    interface Presenter extends ITSListPresenter<MessageGroupAlbumBean> {
        void requestUploadToAlbum(List<ImageBean> imgList);
        void requestDeleteAlbum(MessageGroupAlbumBean messageGroupAlbumBean);
    }

}
