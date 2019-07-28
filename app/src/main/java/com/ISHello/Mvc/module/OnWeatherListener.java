package com.ISHello.Mvc.module;

import com.ISHello.Mvc.bean.Weather;

/**
 * 得到网络请求结果接口
 */
public interface OnWeatherListener {
    void onSuccess(Weather weather);

    void onError();
}
