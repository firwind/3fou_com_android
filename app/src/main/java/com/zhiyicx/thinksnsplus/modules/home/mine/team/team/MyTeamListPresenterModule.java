package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;

import dagger.Module;
import dagger.Provides;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */
@Module
public class MyTeamListPresenterModule {
    private MyTeamListContract.View mView;

    public MyTeamListPresenterModule(MyTeamListContract.View view) {
        this.mView = view;
    }

    @Provides
    public MyTeamListContract.View provideMyTeamContractView() {
        return mView;
    }
}
