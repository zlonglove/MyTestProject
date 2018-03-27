package im.icbc.cn.keyboard.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class safeUtils {
    private static boolean isDebugState = true;
    private static int m_iPid = -1;
    private static int m_iWeightValide = 60;

    public static boolean IsDebug(Context context, String packageName) {
        // set a weight score to this check method.
        int value_debug = 0;

        if (1 == isApkDebugableNew(context, packageName)) {
            value_debug += 60;
        }

        if (isJavaDebug()) {
            value_debug += 60;
        }

        if (isDebug1(context)) {
            value_debug += 60;
        }

        if (1 == getDebugState(context)) {
            value_debug += 60;
        }

        if (60 < value_debug) {
            return true;
        }

        return false;
    }

    public static int isApkDebugable(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                if (0 == (info.flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                    int a = 1;
                }
                return info.flags;
            }
        } catch (Exception e) {

        }

        return -1;
    }

    public static int isApkDebugableNew(Context context, String packageName) {
        try {
            PackageInfo pkginfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            if (pkginfo != null) {
                ApplicationInfo info = pkginfo.applicationInfo;
                if (0 == (info.flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
                    return 0;
                }
                return 1;
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public static boolean isJavaDebug() {
        return android.os.Debug.isDebuggerConnected();
    }

    /*check whether be attached by a third-party to debug. */
    public static boolean isDebug1(Context ctx) {
        if (m_iPid == -1) {
            ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null)
                return false;

            List<RunningAppProcessInfo> rInfo = am.getRunningAppProcesses();
            if (rInfo == null)
                return false;

            String pName = ctx.getPackageName();
            if (pName == null)
                return false;

            int count = rInfo.size();
            boolean find = false;
            for (int i = 0; i < count; i++) {
                RunningAppProcessInfo info = rInfo.get(i);
                if (info == null)
                    continue;

                if (pName.equals(info.processName)) {
                    m_iPid = info.pid;
                    find = true;
                    break;
                }
            }

            if (!find)
                return false;
        }

        String str1 = "/proc/" + m_iPid + "/stat";
        String str2 = null;
        String str3 = "/proc/" + m_iPid + "/status";

        try {
            FileReader r = new FileReader(str1);
            BufferedReader bufferedRead = new BufferedReader(r);

            str2 = bufferedRead.readLine();
            String[] values = str2.split(" ");

            bufferedRead.close();

            FileReader r1 = new FileReader(str3);
            BufferedReader br = new BufferedReader(r1);
            str2 = br.readLine();

            while (str2 != null) {
                if (str2.contains("TracerPid")) {
                    String[] values1 = str2.split("\t");
                    int tracer = Integer.parseInt(values1[1]);
                    if (0 != tracer) {
                        br.close();
                        return true;
                    }
                }
                str2 = br.readLine();
            }

            br.close();
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    /**
     * 但是当我们没在AndroidManifest.xml中设置其debug属性时:
     * 使用Eclipse运行这种方式打包时其debug属性为true,使用Eclipse导出这种方式打包时其debug属性为法false.
     * 在使用ant打包时，其值就取决于ant的打包参数是release还是debug.
     * 因此在AndroidMainifest.xml中最好不设置android:debuggable属性置，而是由打包方式来决定其值.
     **/
    public static int getDebugState(Context ctx) {
        if (ctx == null)
            return -1;

        if ((ctx.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) != 0)
            return 1;

        return 0;
    }

    public static Vector<String> getLoadLibrarys(Context ctx) {
        if (m_iPid == -1) {
            ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            if (am == null)
                return null;

            List<RunningAppProcessInfo> rInfo = am.getRunningAppProcesses();
            if (rInfo == null)
                return null;

            String pName = ctx.getPackageName();
            if (pName == null)
                return null;

            int count = rInfo.size();
            boolean find = false;
            for (int i = 0; i < count; i++) {
                RunningAppProcessInfo info = rInfo.get(i);
                if (info == null)
                    continue;

                if (pName.equals(info.processName)) {
                    m_iPid = info.pid;
                    find = true;
                    break;
                }
            }

            if (!find)
                return null;
        }

        String str1 = "/proc/" + m_iPid + "/maps";
        String str2 = null;

        Vector<String> ret = new Vector<String>();
        try {
            FileReader r = new FileReader(str1);
            BufferedReader bufferedRead = new BufferedReader(r);

            str2 = bufferedRead.readLine();
            while (str2 != null) {
                if (str2.endsWith(".so")) {
                    int pos = str2.indexOf("/");
                    String str3 = str2.substring(pos);
                    ret.add(str3);
                }
                str2 = bufferedRead.readLine();
            }

            bufferedRead.close();

        } catch (Exception e) {
            return null;
        }

        return deleteDuplication(ret);
    }

    private static Vector<String> deleteDuplication(Vector<String> source) {
        if (source == null)
            return null;

        if (source.isEmpty())
            return source;

        Vector<String> newVector = new Vector<String>();
        int size = source.size();
        for (int i = 0; i < size; i++) {
            String str = source.get(i);
            if (!newVector.contains(str))
                newVector.add(str);
        }

        source.removeAllElements();
        source = null;

        return newVector;
    }

    public static boolean isRoot() {
        File file = new File("/system/app/Superuser.apk");
        boolean find1 = file.exists();

        File file1 = new File("/system/bin/sh");
        boolean find2 = file1.exists();

        if (find1 || find2)
            return true;
        else
            return false;
        /*
		Process process = null;
        DataOutputStream os = null;
        boolean root = true;
        
        try
        {
	        process = Runtime.getRuntime().exec("su");
	    	os = new DataOutputStream(process.getOutputStream());
	    	String command = "ls /data/data";
	    	os.writeBytes(command + "\n");  
	    	os.writeBytes("exit\n");  
	    	os.flush();  
	    	process.waitFor();
	    	
        }
        catch( Exception e )
        {
        	e.getMessage();
        	root = false;
        }
        finally  
        {
        	try
        	{
        		if( os != null )
        			os.close();
        		
        		process.destroy();
        	}
        	catch( Exception e )
        	{
        		
        	}
        }
        
		return root;*/
    }

    public static boolean IsRootNew() {
        // set a weight score to this check method.
        int value = 0;
        if (isRootEnv()) {
            value += 10;
        }

        if (isRoot2()) {
            value += 10;
        }
        if (isRoot3()) {
            value += 60;
        }

        if (m_iWeightValide < value) {
            return true;
        }
        return false;
    }

    private static boolean isRootEnv() {
        // the Superuser.apk file dosen't exist on root or unroot phone.
        File file = new File("/system/app/Superuser.apk");
        boolean find1 = file.exists();

        File file1 = new File("/system/bin/sh");
        boolean find2 = file1.exists();

        //find"system/xbin/su", "/system/bin/su",unroot return false; root will return true;
        File file3 = new File("/system/xbin/su");
        boolean find3 = file3.exists();
        File file4 = new File("/system/bin/su");
        boolean find4 = file4.exists();

        if (find1 || find2 || find3 || find4) {
            // root.
            return true;
        }

        return false;
    }

    // begin to add check root method 2016-06-14.
    private static boolean isRoot2() {
        // unroot find release-keys.
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            // root;
            return true;
        }

        return false;
    }

    private static boolean isRoot3() {
        // the most useful method.
        String binPath = "/system/bin/su";
        String xBinPath = "/system/xbin/su";
        if (new File(binPath).exists() && isExecutable(binPath)) {
            // root;
            return true;
        }

        if (new File(xBinPath).exists() && isExecutable(xBinPath)) {
            // root.
            return true;
        }

        return false;
    }

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // get the return value
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = in.readLine();

            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (p != null) {
                p.destroy();
            }
        }

        return false;
    }
    // end to add.

    public static int getDexCount(Context ctx) {
        if (ctx == null)
            return -1;

        int count = 0;

        String path = ctx.getApplicationContext().getPackageCodePath();
        try {
            ZipInputStream localZipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(new File(path))));
            while (true) {
                ZipEntry localZipEntry = localZipInputStream.getNextEntry();
                if (localZipEntry == null) {
                    localZipInputStream.close();
                    break;
                }

                String name = localZipEntry.getName();
                if (name.endsWith(".dex")) {
                    count++;
                }
            }

        } catch (Exception e) {
            return -1;
        }

        return count;
    }

    // check emunlator. add on 2016-06-29.
    public static boolean IsEmulatorNew(Context ctx) {
        int emulator_value = 0;

        if (1 == isEmulator(ctx)) {
            emulator_value += 30;
        }

        if (CheckEmulator.checkPipes()) {
            emulator_value += 10;
        }
        if (CheckEmulator.checkQEmuDriverFile()) {
            emulator_value += 10;
        }
        if (CheckEmulator.CheckPhoneNumber(ctx)) {
            emulator_value += 5;
        }
        if (CheckEmulator.CheckDeviceIDS(ctx)) {
            emulator_value += 10;
        }
        if (CheckEmulator.CheckImsiIDS(ctx)) {
            emulator_value += 10;
        }
        if (CheckEmulator.CheckEmulatorBuild(ctx)) {
            emulator_value += 10;
        }
        if (CheckEmulator.CheckOperatorNameAndroid(ctx)) {
            emulator_value += 10;
        }

        if (60 < emulator_value) {
            //emulator.
            return true;
        }

        return false;
    }

    public static int isEmulator(Context ctx) {
        if (ctx == null)
            return -1;

        String prop1 = getProp(ctx, "ro.secure");
        String prop2 = getProp(ctx, "ro.debuggable");

        try {
            if (Integer.parseInt(prop1) == 0 && Integer.parseInt(prop2) == 1)
                return 1;
        } catch (Exception e) {
            return -1;
        }

        return 0;
    }

    private static String getProp(Context ctx, String property) {
        try {
            ClassLoader cl = ctx.getClassLoader();
            Class<?> SystemProperties = cl.loadClass("android.os.SystemProperties");

            Method get = SystemProperties.getMethod("get", String.class);
            Object[] param = new Object[1];

            param[0] = new String(property);

            return (String) get.invoke(SystemProperties, param);

        } catch (Exception e) {
            return null;
        }
    }

    public static long getCRC(Context ctx) {
        if (ctx == null)
            return -1;

        try {
            ZipFile zf = new ZipFile(ctx.getApplicationContext().getPackageCodePath());
            ZipEntry ze = zf.getEntry("classes.dex");
            return ze.getCrc();
        } catch (Exception e) {
            return -1;
        }
    }

}
