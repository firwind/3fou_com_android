package com.zhiyicx.thinksnsplus.modules.home.mine.mycode;


import dagger.Module;
import dagger.Provides;

/**
 * @author Catherine
 * @describe
 * @date 2017/12/8
 * @contact email:648129313@qq.com
 */
@Module
public class MyCodePresenterModule {

    private MyCodeContract.View mView;

    public MyCodePresenterModule(MyCodeContract.View mView) {
        this.mView = mView;
    }

    @Provides
    public MyCodeContract.View provideMyCodeContractView(){
        return mView;
    }

}
