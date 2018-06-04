package com.ISHello.Manager;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiConfiguration.AuthAlgorithm;
import android.net.wifi.WifiConfiguration.KeyMgmt;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * @author kfzx-zhanglong
 * @category IcbcWifiManager icbcWifiManager = new IcbcWifiManager(this);
 * icbcWifiManager.connect("AndroidAP", "zlonglove1988",
 * WifiCipherType.WIFICIPHER_WPA);
 */
public class IcbcWifiManager {
    private WifiManager wifiManager;
    private List<ScanResult> listResult;
    private final String TAG = "IcbcWifiManager";

    /**
     * 定义几种加密方式，一种是WEP，一种是WPA，还有没有密码的情况
     *
     * @author kfzx-zhanglong
     */
    public enum WifiCipherType {
        WIFICIPHER_WEP, WIFICIPHER_WPA, WIFICIPHER_NOPASS, WIFICIPHER_INVALID
    }

    public IcbcWifiManager(Context context) {
        this.wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 提供一个外部接口，传入要连接的无线网
     *
     * @param ssid
     * @param password
     * @param type
     */
    public void connect(String ssid, String password, WifiCipherType type) {
        Thread thread = new Thread(new ConnectRunnable(ssid, password, type));
        thread.start();
    }

    /**
     * 查看以前是否也配置过这个网络
     *
     * @param SSID
     * @return
     */
    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration existingConfig : existingConfigs) {
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }
        return null;
    }

    /**
     * 判断wifi不是不打开
     *
     * @return true-打开，false-没有打开
     */
    public boolean isWifiEnabled() {
        return wifiManager.isWifiEnabled();
    }

    /**
     * 判断当前连接的wifi是不是给定SSID的wifi
     * <p>
     * true-是，false-不是
     * </p>
     *
     * @param SSID
     * @return
     */
    public boolean isConnected(String SSID) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo == null) {
            return false;
        } else {
            String ssid = wifiInfo.getSSID();
            if (ssid.startsWith("\"")) {
                ssid = ssid.substring(1);
            }
            if (ssid.endsWith("\"")) {
                ssid = ssid.substring(0, ssid.length() - 1);
            }
            if (ssid.equals(SSID)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * @param SSID
     * @param Password
     * @param Type
     * @return
     */
    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        config.status = WifiConfiguration.Status.ENABLED;
        // NoPassword
        if (Type == WifiCipherType.WIFICIPHER_NOPASS) {
            config.wepKeys[0] = "\"" + "\"";
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // WEP
        else if (Type == WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }
            config.allowedAuthAlgorithms.set(AuthAlgorithm.OPEN);
            config.allowedAuthAlgorithms.set(AuthAlgorithm.SHARED);
            config.allowedKeyManagement.set(KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        }
        // WPA-PSK
        else if (Type == WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            // 此处需要修改否则不能自动重联
            // config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        } else {
            config = null;
        }
        return config;
    }

    /**
     * @return
     */
    private boolean openWifi() {
        boolean bRet = true;
        if (!wifiManager.isWifiEnabled()) {
            bRet = wifiManager.setWifiEnabled(true);
        }
        return bRet;
    }

    class ConnectRunnable implements Runnable {
        private String ssid;

        private String password;

        private WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        @Override
        public void run() {
            openWifi();
            //开启wifi功能需要一段时间(3-5秒左右)，所以要等到wifi
            while (!(wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                }
            }
            /**
             * 扫描wifi
             */
            wifiApScan();

            WifiConfiguration wifiConfig = createWifiInfo(ssid, password, type);
            //
            if (wifiConfig == null) {
                Log.i(TAG, "--->create wifiConfig fail!");
                return;
            }

            WifiConfiguration tempConfig = isExsits(ssid);

            if (tempConfig != null) {
                boolean removeStatus = wifiManager.removeNetwork(tempConfig.networkId);
                Log.i(TAG, "--->removeNetwork status=" + removeStatus);
            }

            int netID = wifiManager.addNetwork(wifiConfig);
            Log.i(TAG, "--->addNetwork netID==" + netID);
            boolean enabled = wifiManager.enableNetwork(netID, true);
            Log.i(TAG, "--->enableNetwork status enable=" + enabled);
            boolean connected = wifiManager.reconnect();
            Log.i(TAG, "--->reconnect connected=" + connected);
        }
    }

    private boolean isHexWepKey(String wepKey) {
        final int len = wepKey.length();

        // WEP-40, WEP-104, and some vendors using 256-bit WEP (WEP-232?)
        if (len != 10 && len != 26 && len != 58) {
            return false;
        }
        return isHex(wepKey);
    }

    private boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; i--) {
            final char c = key.charAt(i);
            if (!(c >= '0' && c <= '9' || c >= 'A' && c <= 'F' || c >= 'a' && c <= 'f')) {
                return false;
            }
        }
        return true;
    }

    /**
     * wifi扫描
     */
    public void wifiApScan() {
        if (listResult != null && listResult.size() > 0) {
            this.listResult.clear();
        }
        wifiManager.startScan();
        this.listResult = wifiManager.getScanResults();
        while (this.listResult == null || this.listResult.size() <= 0) {
            try {
                Thread.sleep(500);
                this.listResult = wifiManager.getScanResults();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断指定SSID的wifi是不知存在
     *
     * @param ssid
     * @return true-存在，false-不存在
     */
    public boolean ifWifiExists(String ssid) {
        if (listResult == null || listResult.size() <= 0) {
            wifiApScan();
        }
        for (int i = 0; i < this.listResult.size(); i++) {
            ScanResult mScanResult = ((ScanResult) this.listResult.get(i));
            if (mScanResult.SSID.equals(ssid)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 自动获取加密方式
     *
     * @param ssid
     * @return 返回加密类型
     */
    public WifiCipherType getCipherType(String ssid) {
        if (TextUtils.isEmpty(ssid)) {
            return WifiCipherType.WIFICIPHER_INVALID;
        }
        if (listResult == null || listResult.size() <= 0) {
            wifiApScan();
        }
        for (ScanResult result : listResult) {
            if (result.SSID.equals(ssid)) {
                String capabilities = result.capabilities;
                capabilities = capabilities.toUpperCase();
                if (!TextUtils.isEmpty(capabilities)) {
                    if (capabilities.contains("WPA")) {
                        return WifiCipherType.WIFICIPHER_WPA;
                    } else if (capabilities.contains("WEP")) {
                        return WifiCipherType.WIFICIPHER_WEP;
                    } else {
                        return WifiCipherType.WIFICIPHER_NOPASS;
                    }
                }
            }
        }
        return WifiCipherType.WIFICIPHER_INVALID;
    }

}
