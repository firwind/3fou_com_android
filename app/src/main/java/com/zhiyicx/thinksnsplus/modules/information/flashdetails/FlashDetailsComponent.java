package com.zhiyicx.thinksnsplus.modules.information.flashdetails;

import com.zhiyicx.baseproject.impl.share.ShareWithoutViewModule;
import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;

import dagger.Component;

/*
 * 文件名：
 * 创建者：Administrator
 * 时  间：2018/8/9 0009
 * 描  述：
 * 版  权：九曲互动
 * 
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = {FlashDetailsPresenterModule.class, ShareWithoutViewModule.class})
public interface FlashDetailsComponent extends InjectComponent<FlashDetailsActivity> {
}
