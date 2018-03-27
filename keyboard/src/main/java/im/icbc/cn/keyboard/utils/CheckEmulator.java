package im.icbc.cn.keyboard.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class CheckEmulator {
    private static String[] known_pipes = {"/dev/socket/qemud",
            "/dev/qemu_pipe"};

    private static String[] known_qemu_drivers = {"goldfish"};

    private static String[] known_numbers = {"15555215554", "15555215556",
            "15555215558", "15555215560", "15555215562", "15555215564",
            "15555215566", "15555215568", "15555215570", "15555215572",
            "15555215574", "15555215576", "15555215578", "15555215580",
            "15555215582", "15555215584",};

    // emulator default ID.
    private static String[] known_device_ids = {"000000000000000"};

    // default imsi id.
    private static String[] known_imsi_ids = {"310260000000000"};

    // check “/dev/socket/qemud”，“/dev/qemu_pipe” pipes.
    public static boolean checkPipes() {
        for (int i = 0; i < known_pipes.length; i++) {
            String pipes = known_pipes[i];
            File qemu_socket = new File(pipes);
            if (qemu_socket.exists()) {
                LogUtils.v("Result:Find pipes!");
                return true;
            }
        }
        LogUtils.i("Result:Not Find pipes!");
        return false;
    }

    // 检测驱动文件内容
    // 读取文件内容，然后检查已知QEmu的驱动程序的列表
    public static Boolean checkQEmuDriverFile() {
        File driver_file = new File("/proc/tty/drivers");
        if (driver_file.exists() && driver_file.canRead()) {
            byte[] data = new byte[1024]; // (int)driver_file.length()
            try {
                InputStream inStream = new FileInputStream(driver_file);
                inStream.read(data);
                inStream.close();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            String driver_data = new String(data);
            for (String known_qemu_driver : CheckEmulator.known_qemu_drivers) {
                if (driver_data.indexOf(known_qemu_driver) != -1) {
                    LogUtils.i("Result:Find know_qemu_drivers!");
                    return true;
                }
            }
        }
        LogUtils.i("Result:Not Find known_qemu_drivers!");
        return false;
    }

    // 检测模拟器默认的电话号码
    public static Boolean CheckPhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String phonenumber = telephonyManager.getLine1Number();

        for (String number : known_numbers) {
            if (number.equalsIgnoreCase(phonenumber)) {
                LogUtils.v("Result:Find PhoneNumber!");
                return true;
            }
        }
        LogUtils.v("Result:Not Find PhoneNumber!");
        return false;
    }

    // 检测设备IDS 是不是 “000000000000000”
    public static Boolean CheckDeviceIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String device_ids = telephonyManager.getDeviceId();

        for (String know_deviceid : known_device_ids) {
            if (know_deviceid.equalsIgnoreCase(device_ids)) {
                LogUtils.v("Result:Find ids: 000000000000000!");
                return true;
            }
        }
        LogUtils.v("Result:Not Find ids: 000000000000000!");
        return false;
    }

    // 检测imsi id是不是“310260000000000”
    public static Boolean CheckImsiIDS(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        String imsi_ids = telephonyManager.getSubscriberId();

        for (String know_imsi : known_imsi_ids) {
            if (know_imsi.equalsIgnoreCase(imsi_ids)) {
                LogUtils.v("Result:Find imsi ids: 310260000000000!");
                return true;
            }
        }
        LogUtils.v("Result:Not Find imsi ids: 310260000000000!");
        return false;
    }

    // 检测手机上的一些硬件信息
    public static Boolean CheckEmulatorBuild(Context context) {
        String BOARD = android.os.Build.BOARD;
        String BOOTLOADER = android.os.Build.BOOTLOADER;
        String BRAND = android.os.Build.BRAND;
        String DEVICE = android.os.Build.DEVICE;
        String HARDWARE = android.os.Build.HARDWARE;
        String MODEL = android.os.Build.MODEL;
        String PRODUCT = android.os.Build.PRODUCT;
        if (BOARD == "unknown" || BOOTLOADER == "unknown" || BRAND == "generic"
                || DEVICE == "generic" || MODEL == "sdk" || PRODUCT == "sdk"
                || HARDWARE == "goldfish") {
            LogUtils.v("Result:Find Emulator by EmulatorBuild!");
            return true;
        }
        LogUtils.v("Result:Not Find Emulator by EmulatorBuild!");
        return false;
    }

    // 检测手机运营商家
    public static boolean CheckOperatorNameAndroid(Context context) {
        String szOperatorName = ((TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();

        if (szOperatorName.toLowerCase().equals("android") == true) {
            LogUtils.v("Result:Find Emulator by OperatorName!");
            return true;
        }
        LogUtils.v("Result:Not Find Emulator by OperatorName!");
        return false;
    }
}
