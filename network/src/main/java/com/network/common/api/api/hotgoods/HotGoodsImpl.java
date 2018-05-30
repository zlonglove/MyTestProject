package com.network.common.api.api.hotgoods;

import com.network.common.api.base.BaseImpl;
import com.network.common.api.callback.BaseCallback;
import com.network.common.api.event.hotgoods.GetHotGoodsBannerEvent;

public class HotGoodsImpl extends BaseImpl<HotGoodsService> implements HotGoodsApi {
    public HotGoodsImpl(String baseUrl, int currentParse) {
        super(baseUrl, currentParse);
    }

    @Override
    public void getHotGoodsBannerList(String url) {
        mService.getHotGoodsBannerList(url).enqueue(new BaseCallback<>(new GetHotGoodsBannerEvent()));
    }
}
