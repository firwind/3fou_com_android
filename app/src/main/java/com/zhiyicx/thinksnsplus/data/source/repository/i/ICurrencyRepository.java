package com.zhiyicx.thinksnsplus.data.source.repository.i;
/*
 * 文件名: 货币公用的Repository
 * 创建者：zhangl
 * 时  间：2018/7/19
 * 描  述：
 * 版  权: 九曲互动
 */

import android.content.Context;

import com.zhiyicx.thinksnsplus.data.beans.CurrencyTypeBean;
import com.zhiyicx.thinksnsplus.data.beans.TeamBean;
import com.zhiyicx.thinksnsplus.data.beans.UserInfoBean;

import java.util.List;

import rx.Observable;

public interface ICurrencyRepository {
    /**
     * 获取币种
     * @return
     */
    Observable<List<CurrencyTypeBean>> getCurrencyType(Context context);

}
