package com.network.common.api;

import com.network.common.api.api.hotgoods.HotGoodsApi;
import com.network.common.api.api.hotgoods.HotGoodsImpl;
import com.network.common.api.api.newest.NewestAPI;
import com.network.common.api.api.newest.NewestImpl;
import com.network.common.api.base.Constant;


public class CommonApi implements NewestAPI, HotGoodsApi {

    private static NewestImpl sNewestImpl;
    private static HotGoodsImpl sHotGoodsImpl;

    //--- 单例 -----------------------------------------------------------------------------------
    private volatile static CommonApi mCommonApi;

    private CommonApi() {
    }

    public static CommonApi getSingleInstance() {
        if (null == mCommonApi) {
            synchronized (CommonApi.class) {
                if (null == mCommonApi) {
                    mCommonApi = new CommonApi();
                    init();
                }
            }
        }
        return mCommonApi;
    }

    //--- 初始化 ---------------------------------------------------------------------------------
    public static void init() {
        initImplement();
    }


    private static void initImplement() {
        try {
            sNewestImpl = new NewestImpl(Constant.BASE_URL_NEWS, Constant.PARSE_XML);
            sHotGoodsImpl = new HotGoodsImpl(Constant.BASE_URL_LAPIN, Constant.PARSE_GSON);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getNewestList(String url) {
        sNewestImpl.getNewestList(url);
    }

    @Override
    public void getNewestBannerList(String url) {
        sNewestImpl.getNewestBannerList(url);
    }

    @Override
    public void getHotGoodsBannerList(String url) {
        sHotGoodsImpl.getHotGoodsBannerList(url);
    }
}
