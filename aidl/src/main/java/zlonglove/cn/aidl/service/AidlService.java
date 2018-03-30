package zlonglove.cn.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import zlonglove.cn.aidl.aidlInterface.IMyAidlInterface;
import zlonglove.cn.aidl.bean.User;

public class AidlService extends Service {
    private final String TAG = AidlService.class.getSimpleName();

    public AidlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AidlBinder();
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "--->onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "--->onStartCommand()");
        return super.onStartCommand(intent, flags, startId);
    }

    class AidlBinder extends IMyAidlInterface.Stub {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getName() throws RemoteException {
            return "AidlBinder getName String";
        }

        @Override
        public User getUser() throws RemoteException {
            User user = new User();
            user.setCMPPWD("cmpword");
            user.setCredential("credential".getBytes());
            user.setEmail("zhanglongit@126.com");
            user.setICBCloginUserName("zhanglong");
            user.setICBCUserID("1002323");
            user.setName("张龙");
            user.setToken("token".getBytes());
            user.setPassword("password".getBytes());
            user.setUserType("2");
            return user;
        }
    }
}
