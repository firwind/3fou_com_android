package com.zhiyicx.thinksnsplus.modules.home.find.market;

import com.zhiyicx.common.dagger.scope.FragmentScoped;
import com.zhiyicx.thinksnsplus.base.AppComponent;
import com.zhiyicx.thinksnsplus.base.InjectComponent;
import com.zhiyicx.thinksnsplus.modules.home.find.market.list.MarketListFragment;
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
@Component(dependencies = AppComponent.class,modules = {MarketPresenterMoudle.class})
public interface MarketComponent extends InjectComponent<MarketActivity>{
}
