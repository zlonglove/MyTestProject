package com.ISHello.wifi;


import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiInfomation {
    private final static String TAG = "WifiInfomation";

    public WifiInfomation() {

    }

    /**
     * 获取IP地址
     *
     * @param context
     * @return 将整数值进行右移位操作（>>>），右移24位，再进行与操作符（&）0xFF，得到的数字即为第一段IP。
     * 将整数值进行右移位操作（>>>），右移16位，再进行与操作符（&）0xFF，得到的数字即为第二段IP。
     * 将整数值进行右移位操作（>>>），右移8位，再进行与操作符（&）0xFF，得到的数字即为第三段IP。
     * 将整数值进行与操作符（&）0xFF，得到的数字即为第四段IP。
     */
    public static String getIpAddress(Context context) {

        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        /*
		return ((ipAddress >> 24) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)
				+ "." + ((ipAddress >> 8) & 0xFF) + "." + (ipAddress & 0xFF);
				*/
        String Address = (ipAddress & 0xFF) + "." + ((ipAddress >> 8) & 0xFF) + "." + ((ipAddress >> 16) & 0xFF)
                + "." + ((ipAddress >> 24) & 0xFF);
        Log.i(TAG, "--->The TP Address==" + Address);
        return Address;
    }
}
