package com.zhiyicx.thinksnsplus.modules.settings.blacklist;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zl
 * @Date 2018/4/17
 * @Contact master.jungle68@gmail.com
 */
@Module
public class BlackListPresenterModule {
    private BlackListContract.View mView;

    public BlackListPresenterModule(BlackListContract.View view) {
        mView = view;
    }

    @Provides
    BlackListContract.View provideContractView() {
        return mView;
    }
}
