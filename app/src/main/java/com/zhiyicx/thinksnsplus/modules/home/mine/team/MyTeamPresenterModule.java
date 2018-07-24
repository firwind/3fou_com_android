package com.zhiyicx.thinksnsplus.modules.home.mine.team;

import com.zhiyicx.thinksnsplus.modules.home.mine.team.team.MyTeamListContract;

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
public class MyTeamPresenterModule {
    private MyTeamContract.View mView;

    public MyTeamPresenterModule(MyTeamContract.View view) {
        this.mView = view;
    }

    @Provides
    public MyTeamContract.View provideMyTeamContractView() {
        return mView;
    }
}
