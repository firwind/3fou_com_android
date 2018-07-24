package com.zhiyicx.thinksnsplus.modules.home.mine.team.team;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.mine.team.MyTeamActivity;

import dagger.Component;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/18
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = MyTeamListPresenterModule.class)
public interface MyTeamListComponent extends InjectComponent<MyTeamListFragment>{

}
