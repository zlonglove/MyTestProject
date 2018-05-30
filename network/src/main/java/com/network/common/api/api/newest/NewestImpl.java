package com.network.common.api.api.newest;

import com.network.common.api.base.BaseImpl;
import com.network.common.api.callback.BaseCallback;
import com.network.common.api.event.newest.GetNewestBannerEvent;
import com.network.common.api.event.newest.GetNewestEvent;


public class NewestImpl extends BaseImpl<NewestService> implements NewestAPI {
    public NewestImpl(String baseUrl, int parsePattern) {
        super(baseUrl, parsePattern);
    }

    @Override
    public void getNewestList(String url) {
        mService.getNewestList(url).enqueue(new BaseCallback<>(new GetNewestEvent()));
    }

    @Override
    public void getNewestBannerList(String url) {
        mService.getNewestBannerList(url).enqueue(new BaseCallback<>(new GetNewestBannerEvent()));
    }
}
