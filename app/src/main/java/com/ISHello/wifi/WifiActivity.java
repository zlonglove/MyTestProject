package com.ISHello.wifi;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.ISHello.CustomToast.CustomToast;
import com.example.ishelloword.R;

/**
 * @author zhanglong
 */
public class WifiActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private Button startButton = null;
    private Button stopButton = null;
    private Button checkButton = null;
    private WifiManager wifiManager = null;

    /**
     * wifi状态
     */
    final int WIFI_STATE_ENABLED = WifiManager.WIFI_STATE_ENABLED;//网卡可用3
    final int WIFI_STATE_ENABLING = WifiManager.WIFI_STATE_ENABLING;//网卡正在打开2
    final int WIFI_STATE_DISABLED = WifiManager.WIFI_STATE_DISABLED;//网卡不可用1
    final int WIFI_STATE_DISABLING = WifiManager.WIFI_STATE_DISABLING;//网卡正在关闭0
    final int WIFI_STATE_UNKNOWN = WifiManager.WIFI_STATE_UNKNOWN;//未知网卡状态4


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifiactivity);
        startButton = (Button) findViewById(R.id.startWifi);
        stopButton = (Button) findViewById(R.id.stopWifi);
        checkButton = (Button) findViewById(R.id.checkWifi);
        startButton.setOnClickListener(new StartWifiListener());
        stopButton.setOnClickListener(new StopWifiListener());
        checkButton.setOnClickListener(new CheckWifiListener());
    }

    private void showInfo(int status) {
        switch (status) {
            case WIFI_STATE_ENABLED:
                showToast("网卡可用");
                CustomToast.makeText(this, getIPInfo(), Toast.LENGTH_LONG);
                break;
            case WIFI_STATE_ENABLING:
                showToast("网卡正在打开");
                break;
            case WIFI_STATE_DISABLED:
                showToast("网卡不可用");
                break;
            case WIFI_STATE_DISABLING:
                showToast("网卡正在关闭");
                break;
            case WIFI_STATE_UNKNOWN:
                showToast("未知网卡状态");
                break;
            default:
                break;
        }
    }

    private void showToast(String title) {
        Toast.makeText(WifiActivity.this, title, Toast.LENGTH_SHORT).show();
    }

    class StartWifiListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            wifiManager = (WifiManager) WifiActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            showInfo(wifiManager.getWifiState());

        }
    }

    class StopWifiListener implements OnClickListener {

        @Override
        public void onClick(View arg0) {
            // TODO Auto-generated method stub
            wifiManager = (WifiManager) WifiActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(false);
            showInfo(wifiManager.getWifiState());
        }

    }

    class CheckWifiListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            wifiManager = (WifiManager) WifiActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            showInfo(wifiManager.getWifiState());
        }

    }

    public String getIPInfo() {
        WifiManager wifiManager = (WifiManager)getApplicationContext(). getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return ((ipAddress >> 24) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                (ipAddress & 0xFF);
    }
}