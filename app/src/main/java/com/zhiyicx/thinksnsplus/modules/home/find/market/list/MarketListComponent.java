package com.zhiyicx.thinksnsplus.modules.home.find.market.list;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.find.market.MarketActivity;
import com.zhiyicx.thinksnsplus.modules.home.find.market.list.MarketListPresenterMoudle;

import dagger.Component;

/**
 * <pre>
 *     @author : huwenyong
 *     @date : 2018/6/24 9:39
 *     desc :
 *     version : 1.0
 * <pre>
 */
@FragmentScoped
@Component(dependencies = AppComponent.class,modules = {MarketListPresenterMoudle.class})
public interface MarketListComponent extends InjectComponent<MarketListFragment>{
}
