package com.network.common.api.api.hotgoods;


import com.network.common.api.bean.hotgoods.HotGoodsTopNode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface HotGoodsService {
    @GET
    Call<HotGoodsTopNode> getHotGoodsBannerList(@Url String url);
}
