package com.ISHello.Mvc.module;

import com.ISHello.Mvc.bean.Weather;

/**
 * WeatherModule实现类
 */
public class WeatherModuleImpl implements WeatherModule {
    @Override
    public void getWeather(String name, OnWeatherListener onWeatherListener) {
        Weather weather = new Weather();
        weather.setName("sun");
        onWeatherListener.onSuccess(weather);
    }
}
