package com.zhiyicx.thinksnsplus.modules.chat.card;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhiyicx.baseproject.base.TSFragment;
import com.zhiyicx.thinksnsplus.R;
import com.zhiyicx.thinksnsplus.data.beans.ChatGroupBean;
import com.zhiyicx.thinksnsplus.data.beans.QrCodeData;
import com.zhiyicx.thinksnsplus.i.IntentKey;
import com.zhiyicx.thinksnsplus.utils.ImageUtils;

import butterknife.BindView;

/**
 * author: huwenyong
 * date: 2018/8/10 9:17
 * description:
 * version:
 */

public class ChatGroupCardFragment extends TSFragment{

    @BindView(R.id.iv_group_avatar)
    ImageView mIvGroupAvatar;
    @BindView(R.id.tv_group_name)
    TextView mTvGroupName;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;

    public static ChatGroupCardFragment newInstance(ChatGroupBean mChatGroupBean){
        ChatGroupCardFragment fragment = new ChatGroupCardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(IntentKey.GROUP_INFO,mChatGroupBean);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected boolean showToolBarDivider() {
        return true;
    }

    @Override
    protected String setCenterTitle() {
        return "群二维码名片";
    }

    @Override
    public void setPresenter(Object presenter) {
        //这里不做网络请求
    }

    @Override
    protected void initView(View rootView) {

    }

    @Override
    protected void initData() {
        ChatGroupBean groupBean = getArguments().getParcelable(IntentKey.GROUP_INFO);
        if(null != groupBean){
            mTvGroupName.setText(groupBean.getName());
            if(TextUtils.isEmpty(groupBean.getGroup_face())){
                mIvGroupAvatar.setImageResource(R.mipmap.icon);
            }else {
                ImageUtils.loadImageDefault(mIvGroupAvatar,groupBean.getGroup_face());
            }
            QrCodeData data = new QrCodeData(1,groupBean.getId());
            mIvQrcode.setImageBitmap(ImageUtils.createQrcodeImage(new Gson().toJson(data),
                    getResources().getDimensionPixelSize(R.dimen.dp224),
                    null));
        }
    }

    @Override
    protected int getBodyLayoutId() {
        return R.layout.fragment_chat_group_card;
    }
}
