package com.ISHello.Mvc.module;

/**
 * 天气module接口
 */
public interface WeatherModule {
    void getWeather(String name, OnWeatherListener onWeatherListener);
}
