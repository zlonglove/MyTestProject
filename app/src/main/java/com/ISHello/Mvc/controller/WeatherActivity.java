package com.ISHello.Mvc.controller;

import android.os.Bundle;
import android.widget.TextView;

import com.ISHello.Mvc.bean.Weather;
import com.ISHello.Mvc.module.OnWeatherListener;
import com.ISHello.Mvc.module.WeatherModule;
import com.ISHello.Mvc.module.WeatherModuleImpl;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class WeatherActivity extends BaseActivity implements OnWeatherListener {
    private WeatherModule weatherModule;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("MVC");
        setContentView(R.layout.activity_weather);
        findViews();
        init();
    }

    private void findViews() {
        textView = findViewById(R.id.weather_tv);
    }

    private void init() {
        weatherModule = new WeatherModuleImpl();
        weatherModule.getWeather("", this);
    }

    @Override
    public void onSuccess(Weather weather) {
        textView.setText(weather.getName());
    }

    @Override
    public void onError() {
        textView.setText("Error");
    }
}
