package com.zhiyicx.thinksnsplus.modules.circle.mine.joined;

import dagger.Module;
import dagger.Provides;

/**
 * @Describe
 * @Author zl
 * @Date 2017/12/6
 * @Contact master.jungle68@gmail.com
 */
@Module
public class BaseCircleListPresenterModule {
    BaseCircleListContract.View mView;

    public BaseCircleListPresenterModule(BaseCircleListContract.View view) {
        mView = view;
    }

    @Provides
    BaseCircleListContract.View provideCircleListContractView() {
        return mView;
    }

}
