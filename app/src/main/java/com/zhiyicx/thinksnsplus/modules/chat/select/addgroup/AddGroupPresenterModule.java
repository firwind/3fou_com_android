package com.zhiyicx.thinksnsplus.modules.chat.select.addgroup;

import com.zhiyicx.thinksnsplus.modules.home.message.messagegroup.MessageGroupContract;

import dagger.Module;
import dagger.Provides;

/**
 * @Author Jliuer
 * @Date 2018/05/03/15:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
@Module
public class AddGroupPresenterModule {

    AddGroupContract.View mView;

    public AddGroupPresenterModule(AddGroupContract.View view) {
        mView = view;
    }

    @Provides
    public AddGroupContract.View providesMessageGroupContractView(){
        return mView;
    }
}
