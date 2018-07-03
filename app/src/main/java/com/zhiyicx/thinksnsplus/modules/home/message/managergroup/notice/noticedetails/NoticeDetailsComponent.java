package com.zhiyicx.thinksnsplus.modules.home.message.managergroup.notice.noticedetails;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import javax.inject.Inject;

import dagger.Component;

/*
 * 文件名:
 * 创建者：zhangl
 * 时  间：2018/7/3
 * 描  述：
 * 版  权: 九曲互动
 */
@FragmentScoped
@Component(dependencies = AppComponent.class, modules = NoticeDetailsPresenterModule.class)
public interface NoticeDetailsComponent extends InjectComponent<NoticeDetailsActivity> {
}
