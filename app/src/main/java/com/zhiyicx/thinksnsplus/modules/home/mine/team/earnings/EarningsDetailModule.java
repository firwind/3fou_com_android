package com.zhiyicx.thinksnsplus.modules.home.mine.team.earnings;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/21
 * 描  述：
 * 版  权: 九曲互动
 */
@Module
public class EarningsDetailModule {
    private EarningsDetailContract.View mView;

    public EarningsDetailModule(EarningsDetailContract.View view) {
        this.mView = view;
    }

    @Provides
    public EarningsDetailContract.View provideEarningsDetailContractView() {
        return mView;
    }
}
