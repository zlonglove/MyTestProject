package com.example.ishelloword;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.DialogActivity.DialogActivity;
import com.Hello.Contacts.CursorsActivity;
import com.Hello.Tabactivity.ISTabActivity;
import com.ISHello.AIDL.PlayerProxy;
import com.ISHello.AIDL.mp3RemoteService;
import com.ISHello.AndroidThread.AndroidThread;
import com.ISHello.AppManager.AppManager;
import com.ISHello.Banner2.BannerActivity2;
import com.ISHello.BroadcastReceiver.MediaControlReceiver;
import com.ISHello.Builder.BuildTools;
import com.ISHello.Builder.User;
import com.ISHello.CustomToast.CustomToast;
import com.ISHello.DAO.WealthBarInfoDAO;
import com.ISHello.DateAndTime.DateTimeActivity;
import com.ISHello.DefineDialog.DefineDialog;
import com.ISHello.DefineDialog.ShareDialog;
import com.ISHello.DesignMode.proxy.NotifyProxy;
import com.ISHello.DialogTheme.ThemeDialogActivity;
import com.ISHello.DropDownMenu.ui.DropDownMenuActivity;
import com.ISHello.Encryption.Des;
import com.ISHello.Entity.BuryPointEntity;
import com.ISHello.EventBus.EventBusA;
import com.ISHello.File.ISFile;
import com.ISHello.GesturePassword.UnlockGesturePasswordActivity;
import com.ISHello.GetPictureFromInternet.ISGetPictureFromInternet;
import com.ISHello.GsonModule.ProductsList;
import com.ISHello.GsonModule.UserInfo;
import com.ISHello.Handler.LooperThread;
import com.ISHello.Handler.ThreadLocalTest;
import com.ISHello.HtmlParse.LinkFetcher;
import com.ISHello.IOSDialog.iosDialogActivity;
import com.ISHello.ISNotification.ISNotification;
import com.ISHello.ImageEditor.ImageEditorActivity;
import com.ISHello.Manager.IcbcWifiManager;
import com.ISHello.Manager.ThreadPoolManager;
import com.ISHello.Map.CheckPermissionsActivity;
import com.ISHello.Map.Location_Activity;
import com.ISHello.Module.BarInfoModel;
import com.ISHello.Module.CityModule;
import com.ISHello.NetWork.NetWorkActivity;
import com.ISHello.PickView.PickViewActivity;
import com.ISHello.Process.UserManager;
import com.ISHello.RecyclerView.ui.StickHeaderActivity;
import com.ISHello.RemoteCalls.RemoteCallsClient;
import com.ISHello.ResideMenu.ResideMenuActivity;
import com.ISHello.Retrofit.RetrofitDownLoadActivity;
import com.ISHello.ScreenInfo.ISScreenInfo;
import com.ISHello.Serializable.Parcelable.ObjectTranDemo;
import com.ISHello.Sort.sort;
import com.ISHello.Tools.ISTools;
import com.ISHello.TouchEvent.TouchEvent1Activity;
import com.ISHello.Update.ISUpdateActivity;
import com.ISHello.UserInfo.ISUserInfo;
import com.ISHello.ViewPage.ViewPagerActivity;
import com.ISHello.Voice.VoiceActivity;
import com.ISHello.XmlManager.xmlManager;
import com.ISHello.baseModule.HomeActivity;
import com.ISHello.databaseNew.BuryPointDao;
import com.ISHello.getPackageInfo.ISPackageInfo;
import com.ISHello.logger.Logger;
import com.ISHello.preference.FragmentPreferences;
import com.ISHello.utils.DipPixUtil;
import com.ISHello.utils.FileLogUtil;
import com.ISHello.utils.LogUtil;
import com.ISHello.utils.NetworkUtils;
import com.ISHello.utils.SdcardUtils;
import com.ISHello.webView.WebViewActivity;
import com.app.guide.library.FunctionGuideActivity;
import com.example.updateversion.LaunchAppServices;
import com.in.zlonglove.commonutil.AppApplicationMgr;
import com.in.zlonglove.commonutil.StringUtils;
import com.in.zlonglove.commonutil.ToastUtils;
import com.in.zlonglove.commonutil.ui.dialog.CommonDialogFragment;
import com.in.zlonglove.commonutil.ui.dialog.DialogFragmentHelper;
import com.in.zlonglove.commonutil.ui.dialog.IDialogResultListener;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;
import com.leon.lfilepickerlibrary.utils.FileUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.lsp.RulerTestActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ui.marqueeview.MarqueeViewActivity;
import com.ws.mediaprojectionmediamuxer.RecodeActivity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.icbc.cn.keyboard.safePay.PayKeyboardActivity;
import im.icbc.com.downloadfile.DownloadActivity;
import im.icbc.com.golddrop.GoldAnimationActivity;
import im.icbc.com.indexbarlayout.activity.barIndexLayoutActivity;
import im.icbc.com.linclibrary.PhoneInfo;
import im.icbc.com.popmenu.PopMenu;
import im.icbc.com.popmenu.PopMenuItem;
import im.icbc.com.popmenu.PopMenuItemListener;
import io.reactivex.functions.Consumer;
import zlonglove.cn.adrecyclerview.activity.AdRecyclerViewActivity;
import zlonglove.cn.aidl.activity.AidlActivity;
import zlonglove.cn.network.activity.OkHttpTestActivity;
import zlonglove.cn.recyclerview.activity.RecyclerActivity;
import zlonglove.cn.rxjava.RxJavaActivity;
import zlonglove.cn.systemkeyboard.SystemKeyboardActivity;
import zlonglove.cn.tabswitch.ui.BottomNavigationActivity;

/**
 * @author zhanglong
 */
public class MainActivity extends CheckPermissionsActivity {
    private final String TAG = "MainActivity";
    private AsyncHttpClient asyncHttpClient;

    enum Orientation {
        LANDSCAPE, PORTRAIT
    }

    AudioManager audioManager;
    ComponentName mediaComponentName;
    private final int EXIT_DIALOG = 1;
    private PlayerProxy playerProxy = null;
    private ServiceConnection connection;

    private Dialog NoLinkDialog;
    private PopMenu mPopMenu;
    private SmartRefreshLayout mPullToRefreshView;
    private List<String> listData;
    private TextView main_toolbar_title;
    private int REQUESTCODE_FROM_ACTIVITY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setFullScreen(true);
        LogUtil.log(TAG, "onCreate");
        if (getIntent() != null && getIntent().getData() != null) {
            LogUtil.log(TAG, "getScheme==" + getIntent().getScheme());
            getIntent().getDataString();//获取全部参数
            getIntent().getData().getQuery();//startType=SHAREINJECT&data=123
            getIntent().getData().getQueryParameter("startType");//SHAREINJECT
            getIntent().getData().getQueryParameter("data");//123
            LogUtil.log(TAG, "getQueryParameter==" + getIntent().getData().getQueryParameter("startType"));
            LogUtil.log(TAG, "getQueryParameter==" + getIntent().getData().getQueryParameter("data"));
        }
        LogUtil.log(TAG, "The sdk version==" + ISTools.Instance(this).getVersion());
        LogUtil.log(TAG, "The javaheap==" + ISTools.Instance(this).getJavaHeap());
        LogUtil.log(TAG, "The GLES Version==" + ISTools.Instance(this).getGlEsVersion());
        LogUtil.log(TAG, "Is Support OpenGLES 2.0==" + ISTools.Instance(this).isGlEs2Supported());
        LogUtil.log(TAG, "MaxMemory==" + ISTools.Instance(this).getMaxMemoryM() + "M");

        setContentView(R.layout.activity_list_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        initUI();

        LooperThread looperThread = new LooperThread();
        looperThread.start();

        audioManager = (AudioManager) getSystemService(android.content.Context.AUDIO_SERVICE);
        mediaComponentName = new ComponentName(this, MediaControlReceiver.class);
        audioManager.registerMediaButtonEventReceiver(mediaComponentName);
        ISPackageInfo packageInfo = ISPackageInfo.getInstance(this);
        int type = packageInfo.checkAppType(packageInfo.getPackageName());
        switch (type) {
            case ISPackageInfo.USER_APP:
                //showToast(R.string.user_app, Toast.LENGTH_SHORT);
                break;

            case ISPackageInfo.SYSTEM_APP:
                //showToast(R.string.system_app, Toast.LENGTH_SHORT);
                break;

            case ISPackageInfo.SYSTEM_UPDATE_APP:
                //showToast("SYSTEM_UPDATE_APP", Toast.LENGTH_SHORT);
                break;

            case ISPackageInfo.SYSTEM_REF_APP:
                //showToast("SYSTEM_REF_APP", Toast.LENGTH_SHORT);
                break;

            case ISPackageInfo.UNKNOW_APP:
                //showToast("UNKNOW_APP", Toast.LENGTH_SHORT);
                break;

            default:
                break;
        }
        String arrayString[] = getResources().getStringArray(R.array.sample_string);
        for (int i = 0; i < arrayString.length; i++) {
            LogUtil.log(TAG, "-->arrayString[" + i + "]" + arrayString[i]);
        }

        int[] ids = getResources().getIntArray(R.array.sample_int);
        for (int i = 0; i < ids.length; i++) {
            LogUtil.log(TAG, "-->ids[" + i + "]" + ids[i]);
        }

        xmlManager xmlmanager = new xmlManager(this);
        xmlmanager.read("userinfo.xml", xmlManager.PULL);

        List<ISUserInfo> users = new ArrayList<ISUserInfo>();
        users.add(new ISUserInfo(1, "zhanglong", (short) 24));
        users.add(new ISUserInfo(2, "zhangjun", (short) 25));
        users.add(new ISUserInfo(3, "zhangsan", (short) 26));
        xmlmanager.write("userinfo.xml", xmlManager.PULL, users);

        /********************************************************************************************************************/

        /********************************************************************************************************************/

        /********************************************************************************************************************/
        ISScreenInfo screenInfo = new ISScreenInfo(this);
        LogUtil.log(TAG, "--->Screen info==" + screenInfo.toString());
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        ISFile file = new ISFile();
        file.writeFileData(this, "write.txt", "helloAndroid");
        LogUtil.log(TAG, "--->the file===" + file.readFileData(this, "write.txt"));
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        LogUtil.log(TAG, "--->screen width==" + ISTools.Instance(this).getWidth());
        LogUtil.log(TAG, "--->screen height==" + ISTools.Instance(this).getHeight());
        LogUtil.log(TAG, "--->screen density==" + ISTools.Instance(this).getDensity());
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        // WifiFunction wifiFunction=WifiFunction.Instance(this);
        // wifiFunction.startScan();
        // LogUtil.log(TAG, "--->ScanResult=="+wifiFunction.lookUpScan().toString());
        // wifiFunction.set_static("HAILI","192.168.10.110", "192.168.10.254",
        // "8.8.8.8");
        /********************************************************************************************************************/


        /**
         * AsyncHttp用法
         */
        /********************************************************************************************************************/
        asyncHttpClient = new AsyncHttpClient();
        executeSample(getAsyncHttpClient(), getDefaultURL(), null, null, getResponseHandler());
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        // LooperHandlerTest looperHandlerTest = new LooperHandlerTest();
        // looperHandlerTest.sendMessage(100, 23);
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        connection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {

            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                playerProxy = new PlayerProxy(service);
                playerProxy.play();
                LogUtil.log(TAG, "--->" + playerProxy.getStatus());

            }
        };
        bindService(new Intent(this, mp3RemoteService.class), connection, android.content.Context.BIND_AUTO_CREATE);
        /********************************************************************************************************************/

        LogUtil.log(TAG, "--->the sdcard large is==" + SdcardUtils.getAvailableStore(SdcardUtils.getSDCardPathWithFileSeparators()) / 1024 / 1024 / 1024
                + "G");
        /********************************************************************************************************************/

        LogUtil.log(TAG, "--->density/densityDPI==" + DipPixUtil.getWindowDensity(this) + "/" + DipPixUtil.getWindowDensityDPI(this));

        /********************************************************************************************************************/
        BarInfoModel wealth_Bar_Info = new BarInfoModel();
        wealth_Bar_Info.setBarId("123");
        wealth_Bar_Info.setFansCount("100");
        wealth_Bar_Info.setTopicCount("23");
        wealth_Bar_Info.setGroupCount("12");
        wealth_Bar_Info.setArticleId("2015122");
        wealth_Bar_Info.setArticleTitle("第一场雪");
        wealth_Bar_Info.setIsAttention("1");
        wealth_Bar_Info.setFilesServerUrl("www.baidu.com");
        wealth_Bar_Info.setIsComment("1");
        WealthBarInfoDAO.getInstance().updateWealthBarInfo(wealth_Bar_Info);

        // WealthBarInfoDAO.getInstance(getApplicationContext()).deteleTableForWealthBarInfoById("123");
        // List<BarInfoModel> list1 =
        // WealthBarInfoDAO.getInstance(getApplicationContext()).queryWealthBarInfo("123");
        /********************************************************************************************************************/
        for (int i = 0; i < 2; i++) {
            ThreadPoolManager.getInstance().addTask(new Runnable() {

                @Override
                public void run() {
                    final List<BarInfoModel> list = WealthBarInfoDAO.getInstance().queryWealthBarInfo("123");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LogUtil.log(TAG, "--->" + list.toString());
                        }
                    });
                }
            });
        }
        // LogUtil.log(TAG, "--->all table name" +
        // WealthBarInfoDAO.getInstance().queryAllTableName());
        //LogUtil.log(TAG, "--->table column " + WealthBarInfoDAO.getInstance().checkColumnExists(Wealth_Bar_Info.TABLE_WEALTH_BAR_INFO_OPERATE, ""));

        /********************************************************************************************************************/

        // IcbcWifiManager icbcWifiManager = new IcbcWifiManager(this);
        // icbcWifiManager.connect("zhuangzhuang", "",WifiCipherType.WIFICIPHER_NOPASS);

        /********************************************************************************************************************/

        //createNoLinkDialog();

        /********************************************************************************************************************/
        String jssonNameString = "{\"name\": \"王五\",\"gender\": \"man\",\"age\": 15,\"height\": \"140cm\"}";
        UserInfo info = UserInfo.objectFromData(jssonNameString);
        //CustomToast.makeText(MainActivity.this, info.getName(), Toast.LENGTH_SHORT);

        String productString = "{\n" +
                "    \"name\": \"王五\",\n" +
                "    \"gender\": \"man\",\n" +
                "    \"age\": 15,\n" +
                "    \"height\": \"140cm\",\n" +
                "    \"addr\": {\n" +
                "        \"province\": \"fujian\",\n" +
                "        \"city\": \"quanzhou\",\n" +
                "        \"code\": \"300000\"\n" +
                "    },\n" +
                "    \"hobby\": [\n" +
                "        {\n" +
                "            \"name\": \"billiards\",\n" +
                "            \"code\": \"1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"name\": \"computerGame\",\n" +
                "            \"code\": \"2\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        ProductsList productsList = ProductsList.objectFromData(productString);
        LogUtil.log(TAG, "--->" + productsList.getAddr().toString());
        /********************************************************************************************************************/
        ThreadLocalTest threadLocalTest = new ThreadLocalTest();
        threadLocalTest.test();
        /********************************************************************************************************************/
        AndroidThread androidThread = new AndroidThread();
        androidThread.callAsyncTask();
        androidThread.callIntentService(this);
        /********************************************************************************************************************/

        /**
         * 线程池
         */
        /********************************************************************************************************************/
        //FixedThreadPool.getInstance().excute();
        //CachedThreadPool.getInstance().excute();
        //ScheduleThreadPool.getInstance().excuteDelay(2000);
        //ScheduleThreadPool.getInstance().excuteAtFixedRate(2000, 5000);
        //SingleThreadExecutor.getInstance().excute();
        /********************************************************************************************************************/

        LogUtil.log(TAG, "--->getExternalStorageDirectory==" + Environment.getExternalStorageDirectory().getPath() + File.separator);
        LogUtil.log(TAG, "--->getDataDirectory==" + Environment.getDataDirectory().getPath());


        /********************************************************************************************************************/
        //String processName = ProcessUtil.getProcessName(this);
        //boolean isRunning = ProcessUtil.isRunning(this, this.getPackageName());
        //boolean isProcessInFront = ProcessUtil.isProcessInFront(this, ProcessUtil.getProcessName(this));
        /********************************************************************************************************************/

        /**
         * 以追加的方式讲将符串写入文件
         */
        /********************************************************************************************************************/
        //long milliseconds = System.currentTimeMillis();
        //for (int i = 0; i < 1000; i++) {
        FileLogUtil.write("fileLogUtil write log file test");
        //}
        //LogUtil.log("write file total time==" + Long.toString(System.currentTimeMillis() - milliseconds));
        FileLogUtil.write("fileLogUtil write log file test two");
        /********************************************************************************************************************/


        /********************************************************************************************************************/
        Logger.init(TAG);
        Logger.d("debug");
        Logger.i("info");
        Logger.e("error");
        /********************************************************************************************************************/

        /**
         * UI线程修改静态变量的值
         */
        /********************************************************************************************************************/
        UserManager.sUserId = 2;
        Log.d(TAG, "--->Static value ==" + UserManager.sUserId);
        /********************************************************************************************************************/

        /**
         * Crash日志测试
         */
        /********************************************************************************************************************/
        /*String test = null;
        test.contains("12");*/
        /********************************************************************************************************************/

        /**
         * module调用
         */
        /********************************************************************************************************************/
        PhoneInfo phoneInfo = new PhoneInfo(getApplicationContext());
        Logger.e(phoneInfo.getCpuInfo()[0]);
        /********************************************************************************************************************/

        /**
         * GsonFormat使用
         */
        /********************************************************************************************************************/
        String citySrc = "{\n" +
                "    \"name\": \"中国\",\n" +
                "    \"province\": [{\n" +
                "        \"name\": \"黑龙江\",\n" +
                "        \"cities\": {\n" +
                "            \"city\": [\"哈尔滨\", \"大庆\"]\n" +
                "        }\n" +
                "    }, {\n" +
                "        \"name\": \"广东\",\n" +
                "        \"cities\": {\n" +
                "            \"city\": [\"广州\", \"深圳\", \"珠海\"]\n" +
                "        }\n" +
                "    }, {\n" +
                "        \"name\": \"台湾\",\n" +
                "        \"cities\": {\n" +
                "            \"city\": [\"台北\", \"高雄\"]\n" +
                "        }\n" +
                "    }, {\n" +
                "        \"name\": \"新疆\",\n" +
                "        \"cities\": {\n" +
                "            \"city\": [\"乌鲁木齐\"]\n" +
                "        }\n" +
                "    }]\n" +
                "}";
        CityModule cityModule = CityModule.objectFromData(citySrc);
        List<CityModule.ProvinceBean> provinceBeenList = cityModule.getProvince();
        for (CityModule.ProvinceBean provinceBean : provinceBeenList) {
            LogUtil.log(TAG, provinceBean.getName());
            LogUtil.log(TAG, provinceBean.getCities().getCity().toString());
        }
        /********************************************************************************************************************/


        /**
         * 网络工具类的使用
         */
        /********************************************************************************************************************/
        Logger.i(NetworkUtils.getNetWorkTypeName(getApplicationContext()));
        /********************************************************************************************************************/

        /**
         * Dialog测试
         */
        /********************************************************************************************************************/
        /********************************************************************************************************************/
        /*final AlertDialog logDialog = ICBCDialog.getCrashDialog(this, null);
        logDialog.show();*/
        /********************************************************************************************************************/

        /**
         * Builder模式测试
         */
        /********************************************************************************************************************/
        User user = new User.Builder().mAge("12").mFirstName("zhang").mLastName("long").mGender("male").mPhoneNo("12323234545").build();
        LogUtil.log(TAG, "--->User info==" + user.toString());
        BuildTools buildTools = new BuildTools.Builder().mToolsName("Tools").mDesc("desc").mDevelop("zhanglong").mDownTime("2018/03/22").build();
        LogUtil.log(TAG, "--->BuildTools info==" + buildTools.toString());
        /********************************************************************************************************************/


        /********************************************************************************************************************/
        mPopMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                .addMenuItem(new PopMenuItem("文字", getResources().getDrawable(R.drawable.tabbar_compose_idea)))
                .addMenuItem(new PopMenuItem("照片/视频", getResources().getDrawable(R.drawable.tabbar_compose_photo)))
                .addMenuItem(new PopMenuItem("头条文章", getResources().getDrawable(R.drawable.tabbar_compose_headlines)))
                .addMenuItem(new PopMenuItem("签到", getResources().getDrawable(R.drawable.tabbar_compose_lbs)))
                .addMenuItem(new PopMenuItem("点评", getResources().getDrawable(R.drawable.tabbar_compose_review)))
                .addMenuItem(new PopMenuItem("更多", getResources().getDrawable(R.drawable.tabbar_compose_more)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        CustomToast.makeText(MainActivity.this, "你点击了第" + position + "个位置", Toast.LENGTH_SHORT);
                    }
                })
                .build();
        /********************************************************************************************************************/


        /**
         * 3desc加密
         */
        /********************************************************************************************************************/
       /* String src = "1001037799";
        String encodeString = DESUtils.encryptFixed(src);
        LogUtil.log(encodeString);
        String decodeString = DESUtils.decodeFixed(encodeString);
        LogUtil.log(decodeString);*/
        String src = "1001037799";
        Des dtDes = Des.getInstance();
        String encodeString = dtDes.getEncString(src);
        LogUtil.log(encodeString);
        LogUtil.log(dtDes.getDesString(encodeString));
        /********************************************************************************************************************/

        /**
         *
         /********************************************************************************************************************/
        new sort().test();
        /********************************************************************************************************************/
        boolean isAvailableByPing = com.in.zlonglove.commonutil.NetworkUtils.isAvailableByPing();
        LogUtil.log(isAvailableByPing);
        com.in.zlonglove.commonutil.NetworkUtils.NetworkType networkType = com.in.zlonglove.commonutil.NetworkUtils.getNetworkType();
        LogUtil.log(networkType);
        int core = AppApplicationMgr.getNumCores();
        LogUtil.log(core);
        List<String> permissionList = AppApplicationMgr.getPermissions(getApplicationContext());
        LogUtil.log(permissionList);
        LogUtil.log(StringUtils.buffer("a", "b"));
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        //gotoShareDialog();
        getBookSearch();
        /********************************************************************************************************************/

        /********************************************************************************************************************/
        BuryPointDao buryPointDao = new BuryPointDao();
        HashMap<String, String> map = buryPointDao.getBuryPointInfo("SubMenuActivity_tv_titlemenu_tofavor");
        LogUtil.log(map);
        BuryPointEntity buryPointEntity=buryPointDao.getBuryPointEntity("SubMenuActivity_tv_titlemenu_tofavor");
        LogUtil.log(buryPointEntity.toString());

    }

    private void getBookSearch() {
        /*RetrofitHelper.getInstance().getBookSearch(new CallBack<Book>() {
            @Override
            public void onSuccess(Book callBack) {
                //Toast.makeText(MainActivity.this, callBack.getTotal(), Toast.LENGTH_SHORT).show();
                CustomToast.makeText(MainActivity.this, callBack.getTotal() + "", Toast.LENGTH_SHORT);
                LogUtil.log("--->getBookSearch() Total==" + callBack.getTotal());
            }

            @Override
            public void onError() {
                Log.e(TAG, "--->getBookSearch Fail");
            }
        });

        RetrofitHelper.getInstance().getBookSearchMapParams(new CallBack<Book>() {
            @Override
            public void onSuccess(Book callBack) {
                LogUtil.log("--->getBookSearchMapParams() Total==" + callBack.getTotal());
            }

            @Override
            public void onError() {
                Log.e(TAG, "--->getBookSearchMapParams() Fail");
            }
        });*/

        /*Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);*/
    }

    private void initUI() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        listData = new ArrayList<>();
        listData = Arrays.asList(getResources().getStringArray(R.array.main_item_table));
        ListView listView = (ListView) findViewById(R.id.list_view_main);
        listView.setAdapter(new SampleAdapter(this, listData));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        gotoLocation();
                        break;
                    case 1:
                        //gotoJNIActivity();
                        new NotifyProxy(MainActivity.this).send();
                        break;
                    case 2:
                        GesturePassword();
                        break;
                    case 3:
                        gotoGetPic();
                        break;
                    case 4:
                        openWebView();
                        break;
                    case 5:
                        gotoNetWork();
                        break;
                    case 6:
                        gotoFileDownload();
                        break;
                    case 7:
                        gotoIosDialogActivity();
                        break;
                    case 8:
                        gotoDateAndTimeActivity();
                        break;
                    case 9:
                        gotoIndexBarLayout();
                        break;
                    case 10:
                        gotoPayKeyboard();
                        break;
                    case 11:
                        gotoMarqueeView();
                        break;
                    case 12:
                        gotoFuncGuide();
                        break;
                    case 13:
                        gotoImageEditor();
                        break;
                    case 14:
                        gotoMoneyAnimation();
                        break;
                    case 15:
                        gotoRulerActivity();
                        break;
                    case 16:
                        gotoOkHttpActivity();
                        break;
                    case 17:
                        gotoBanner();
                        break;
                    case 18:
                        gotoEventBus();
                        break;
                    case 19:
                        gotoRxJava();
                        break;
                    case 20:
                        gotoTabSwitch();
                        break;
                    case 21:
                        gotoDialogActivity();
                        break;
                    case 22:
                        gotoResideMenuActivity();
                        break;
                    case 23:
                        gotoAidlActivity();
                        break;
                    case 24:
                        gotoFilePicker();
                        break;
                    case 25:
                        gotoRecycler();
                        break;
                    case 26:
                        gotoAdRecycler();
                        break;
                    case 27:
                        wifiAutoConnect();
                        break;
                    case 28:
                        gotoHtmlParse("https://www.baidu.com");
                        //gotoHtmlParse("https://mp.weixin.qq.com/");
                        break;
                    case 29:
                        gotoSystemKeyboard();
                        break;
                    case 30:
                        gotoTouchEvent();
                        break;
                    case 31:
                        gotoRecord();
                        break;
                    case 32:
                        gotoIndexablerecyclerView();
                        break;
                    case 33:
                        gotoPickView();
                        break;
                    case 34:
                        gotoDropDownMenu();
                        break;
                    case 35:
                        gotoStickHeader();
                        break;
                    case 36:
                        gotoRecode();
                        break;
                    case 37:
                        gotoRetrofitDownLoad();
                        break;
                    case 38:
                        gotoHomeActivity();
                        break;
                    default:
                        break;
                }
            }
        });

        mPullToRefreshView = (SmartRefreshLayout) findViewById(R.id.pull_to_refresh_main);
        mPullToRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                Log.i(TAG, "--->onRefresh()==" + Thread.currentThread().getName());
                refreshlayout.finishRefresh(2000, true);//传入false表示刷新失败
                main_toolbar_title.setText("刷新中...");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        main_toolbar_title.setText("首页");
                    }
                }, 2000);
            }
        });

        mPullToRefreshView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                Log.i(TAG, "--->onLoadMore()==" + Thread.currentThread().getName());
                refreshLayout.finishLoadMore(2000, true, true);
                if (!mPopMenu.isShowing()) {
                    mPopMenu.show();
                }
            }
        });
    }

    class SampleAdapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        private final List<String> mData;

        public SampleAdapter(Context context, List<String> data) {
            mData = data;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int i) {
            return mData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.main_list_refresh_item, parent, false);
                viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view_text);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textView.setText(mData.get(position));

            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {// 得到被点击的item的itemId
            case R.id.showConfirmDialog:
                showConfirmDialog();
                break;

            case R.id.showDateDialog:
                showDateDialog();
                break;

            case R.id.showInsertDialog:
                showInsertDialog();
                break;

            case R.id.showIntervalInsertDialog:
                showIntervalInsertDialog();
                break;

            case R.id.showListDialog:
                showListDialog();
                break;

            case R.id.showPasswordInsertDialog:
                showPasswordInsertDialog();
                break;

            case R.id.showProgress:
                DialogFragmentHelper.showProgress(getSupportFragmentManager(), "正在加载中");
                break;

            case R.id.showTimeDialog:
                showTimeDialog();
                break;

            case R.id.showTips:
                DialogFragmentHelper.showTips(getSupportFragmentManager(), "你进入了无网的异次元中");
                break;

            default:
                break;
        }
        return true;
    }

    /**
     * 选择时间的弹出窗
     */
    private void showTimeDialog() {
        String titleTime = "请选择时间";
        Calendar calendarTime = Calendar.getInstance();
        DialogFragmentHelper.showTimeDialog(getSupportFragmentManager(), titleTime, calendarTime, new IDialogResultListener<Calendar>() {
            @Override
            public void onDataResult(Calendar result) {
                showToast(String.valueOf(result.getTime().getDate()), Toast.LENGTH_SHORT);
            }
        }, true);
    }

    /**
     * 输入密码的弹出窗
     */
    private void showPasswordInsertDialog() {
        String titlePassword = "请输入密码";
        DialogFragmentHelper.showPasswordInsertDialog(getSupportFragmentManager(), titlePassword, new IDialogResultListener<String>() {
            @Override
            public void onDataResult(String result) {
                showToast("密码为：" + result, Toast.LENGTH_SHORT);
            }
        }, true);
    }

    /**
     * 显示列表的弹出窗
     */
    private void showListDialog() {
        String titleList = "选择哪种方向？";
        final String[] languanges = new String[]{"Android", "iOS", "web 前端", "Web 后端", "老子不打码了"};

        DialogFragmentHelper.showListDialog(getSupportFragmentManager(), titleList, languanges, new IDialogResultListener<Integer>() {
            @Override
            public void onDataResult(Integer result) {
                showToast(languanges[result], Toast.LENGTH_SHORT);
            }
        }, true);
    }

    /**
     * 两个输入框的弹出窗
     */
    private void showIntervalInsertDialog() {
        String title = "请输入想输入的内容";
        DialogFragmentHelper.showIntervalInsertDialog(getSupportFragmentManager(), title, new IDialogResultListener<String[]>() {
            @Override
            public void onDataResult(String[] result) {
                showToast(result[0] + result[1], Toast.LENGTH_SHORT);
            }
        }, true);
    }

    private void showInsertDialog() {
        String titleInsert = "请输入想输入的内容";
        DialogFragmentHelper.showInsertDialog(getSupportFragmentManager(), titleInsert, new IDialogResultListener<String>() {
            @Override
            public void onDataResult(String result) {
                showToast(result, Toast.LENGTH_SHORT);
            }
        }, true);
    }

    /**
     * 选择日期的弹出窗
     */
    private void showDateDialog() {
        String titleDate = "请选择日期";
        Calendar calendar = Calendar.getInstance();
        DialogFragmentHelper.showDateDialog(getSupportFragmentManager(), titleDate, calendar, new IDialogResultListener<Calendar>() {
            @Override
            public void onDataResult(Calendar result) {
                showToast(String.valueOf(result.getTime().getDate()), Toast.LENGTH_SHORT);
            }
        }, true);
    }

    /**
     * 确认和取消的弹出窗
     */
    private void showConfirmDialog() {
        DialogFragmentHelper.showConfirmDialog(getSupportFragmentManager(), "是否选择 Android？", new IDialogResultListener<Integer>() {
            @Override
            public void onDataResult(Integer result) {
                showToast("You Click Ok", Toast.LENGTH_SHORT);
            }
        }, true, new CommonDialogFragment.OnDialogCancelListener() {
            @Override
            public void onCancel() {
                showToast("You Click Cancel", Toast.LENGTH_SHORT);
            }
        });
    }

    public void openWebView() {
        LogUtil.log(TAG, "--->openWebView Button Click");
        Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoNetWork() {
        Intent intent = new Intent(MainActivity.this, NetWorkActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoDialogActivity() {
        Intent intent = new Intent(MainActivity.this, DialogActivity.class);
        startActivity(intent);
    }

    public void gotoResideMenuActivity() {
        Intent intent = new Intent(MainActivity.this, ResideMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoAidlActivity() {
        Intent intent = new Intent(MainActivity.this, AidlActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoFilePicker() {
        String size = FileUtils.getReadableFileSize(1024);
        Log.i(TAG, "--->size==" + size);
        //返回图标风格
        final int BACKICON_STYLEONE = 0;
        final int BACKICON_STYLETWO = 1;
        final int BACKICON_STYLETHREE = 2;
        //图标风格
        final int ICON_STYLE_YELLOW = 0;
        final int ICON_STYLE_BLUE = 1;
        final int ICON_STYLE_GREEN = 2;
        String startPath = Environment.getExternalStorageDirectory().getPath();
        new LFilePicker()
                .withActivity(this)
                .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                .withTitle("文件选择")
                .withIconStyle(ICON_STYLE_YELLOW)
                .withBackIcon(BACKICON_STYLETHREE)
                .withMutilyMode(true)
                .withMaxNum(2)
                .withStartPath(startPath + "/Download")//指定初始显示路径
                .withNotFoundBooks("至少选择一个文件")
                .withIsGreater(false)//过滤文件大小 小于指定大小的文件
                .withFileSize(500 * 1024)//指定文件大小为500K
                .withChooseMode(true)//文件夹选择模式
                //.withFileFilter(new String[]{"txt", "png", "docx"})
                .start();
    }

    public void gotoRecycler() {
        Intent intent = new Intent(MainActivity.this, RecyclerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoAdRecycler() {
        Intent intent = new Intent(MainActivity.this, AdRecyclerViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void wifiAutoConnect() {
        /*WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiConnect wifiConnect = new WifiConnect(wifiManager);
        boolean connectStatus = wifiConnect.Connect("TAIYUE_5G", "123456789", WIFICIPHER_WPA);*/
        IcbcWifiManager icbcWifiManager = new IcbcWifiManager(this);
        icbcWifiManager.connect("ICBC_WJB", "", IcbcWifiManager.WifiCipherType.WIFICIPHER_NOPASS);
    }

    public void gotoHtmlParse(String url) {
        LinkFetcher linkFetcher = new LinkFetcher();
        linkFetcher.loadUrl(url, new LinkFetcher.OnLinkListener() {
            @Override
            public void onLinkDataReady(String title, String image) {
                LogUtil.log(TAG, "title==" + title + " image==" + image);
            }
        });

        linkFetcher.loadUrl(url, new Consumer<Map>() {
            @Override
            public void accept(Map map) throws Exception {
                if (map.get("code").equals("1")) {
                    LogUtil.log(TAG, "title==" + map.get("title").toString()
                            + " img==" + map.get("img").toString()
                            + " url==" + map.get("url").toString());
                } else {
                    Toast.makeText(getApplicationContext(), "解析网址失败,请检查是否包含http://", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void gotoSystemKeyboard() {
        Intent intent = new Intent(MainActivity.this, SystemKeyboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoTouchEvent() {
        Intent intent = new Intent(MainActivity.this, TouchEvent1Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoRecord() {
        Intent intent = new Intent(MainActivity.this, RecodeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoIndexablerecyclerView() {
        Intent intent = new Intent(MainActivity.this, com.ISHello.IndexablerecyclerView.MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoPickView() {
        Intent intent = new Intent(MainActivity.this, PickViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoDropDownMenu() {
        Intent intent = new Intent(MainActivity.this, DropDownMenuActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoStickHeader() {
        Intent intent = new Intent(MainActivity.this, StickHeaderActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoRecode() {
        Intent intent = new Intent(MainActivity.this, VoiceActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void gotoRetrofitDownLoad() {
        Intent intent = new Intent(MainActivity.this, RetrofitDownLoadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    private void gotoShareDialog() {
        //显示分享底部区域
        ShareDialog shareDialog = ShareDialog.getInstance();
        shareDialog.setOnShareClickLitener(new ShareDialog.OnShareClickLitener() {
            @Override
            public void onShareToQQ() {
                openShare("QQ");
            }

            @Override
            public void onShareToQZone() {
                openShare("QZone");
            }

            @Override
            public void onShareToWX() {
                openShare("WX");
            }

            @Override
            public void onShareToWXCircle() {
                openShare("WXCircle");
            }

            @Override
            public void onShareToSina() {
                openShare("Sina");
            }
        });
        shareDialog.show(getSupportFragmentManager(), "Share");
    }

    /**
     * 在这里可以配合友盟分享，通过switch语句，根据type判断平台。执行分享代码
     *
     * @param type
     */
    private void openShare(String type) {
        ToastUtils.showShortToast(type);
    }

    public void gotoFileDownload() {
        Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoIosDialogActivity() {
        Intent intent = new Intent(MainActivity.this, iosDialogActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoDateAndTimeActivity() {
        Intent intent = new Intent(MainActivity.this, DateTimeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoIndexBarLayout() {
        Intent intent = new Intent(MainActivity.this, barIndexLayoutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoPayKeyboard() {
        Intent intent = new Intent(MainActivity.this, PayKeyboardActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoMarqueeView() {
        Intent intent = new Intent(MainActivity.this, MarqueeViewActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoFuncGuide() {
        Intent intent = new Intent(MainActivity.this, FunctionGuideActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoImageEditor() {
        Intent intent = new Intent(MainActivity.this, ImageEditorActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoMoneyAnimation() {
        Intent intent = new Intent(MainActivity.this, GoldAnimationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoRulerActivity() {
        Intent intent = new Intent(MainActivity.this, RulerTestActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoOkHttpActivity() {
        Intent intent = new Intent(MainActivity.this, OkHttpTestActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoBanner() {
        Intent intent = new Intent(MainActivity.this, BannerActivity2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoEventBus() {
        Intent intent = new Intent(MainActivity.this, EventBusA.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoRxJava() {
        Intent intent = new Intent(MainActivity.this, RxJavaActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoTabSwitch() {
        Intent intent = new Intent(MainActivity.this, BottomNavigationActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void updateApk(View view) {
        LogUtil.log(TAG, "--->updateApk Button Click");
        Intent intent = new Intent(MainActivity.this, ISUpdateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoLocation() {
        LogUtil.log(TAG, "--->mapTest click");
        Intent intent = new Intent(MainActivity.this, Location_Activity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void gotoJNIActivity() {
        /*Intent intent = new Intent(MainActivity.this, JNIActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);*/
    }

    public void gotoGetPic() {
        Intent intent = new Intent(MainActivity.this, ISGetPictureFromInternet.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void RemoteCalls(View view) {
        LogUtil.log(TAG, "--->RemoteCalls Button Click");
        Intent intent = new Intent(MainActivity.this, RemoteCallsClient.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void viewPager(View view) {
        LogUtil.log(TAG, "--->viewPager Button Click");
        Intent intent = new Intent(MainActivity.this, ViewPagerActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void fragmentPreference(View view) {
        LogUtil.log(TAG, "--->fragmentPreference Button Click");
        Intent intent = new Intent(MainActivity.this, FragmentPreferences.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void DialogTheme(View view) {
        LogUtil.log(TAG, "--->DialogTheme Button Click");
        Intent intent = new Intent(this, ThemeDialogActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        this.finish();
    }

    /**
     * 启动另一个APK
     *
     * @param view
     */
    public void toApk(View view) {
        LogUtil.log(TAG, "--->toApk Button Click");
        String downLoadPath = "http://122.19.173.71:8011/1705%b2%e2%ca%d4%b0%fc/0406/IMAPP_ANDROID_TEST_0501.apk";
        LaunchAppServices launchAppServices = new LaunchAppServices(this, "融e联", "com.icbc.im", downLoadPath, "temp.apk");
        launchAppServices.launchApp();
    }

    /**
     * UI控件测试activity
     *
     * @param view
     */
    public void UI(View view) {
        // LogUtil.log(TAG, "--->UI Button Click");
        // Intent intent = new Intent(DropActivity.this, UIActivity.class);
        // intent.setAction("android.intent.action.VIEW");
        // Uri uri =
        // Uri.parse("com.zhy.sample://params?startType=SHAREINJECT&data=123");
        // intent.setData(uri);
        // startActivity(intent);
        // overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        String downLoadPath = "http://122.19.173.71:8011/1705%b2%e2%ca%d4%b0%fc/0406/IMAPP_ANDROID_TEST_0501.apk";
        LaunchAppServices launchAppServices = new LaunchAppServices(this, "融e联", "com.icbc.im", downLoadPath, "temp.apk");
        launchAppServices.launchAppAndFinishActivity();
    }

    public void GesturePassword() {
        Intent intent = new Intent(MainActivity.this, UnlockGesturePasswordActivity.class);
        intent.putExtra("cwp.md", "md");
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void sparcelable(View view) {
        LogUtil.log(TAG, "--->sparcelable Button Click");
        Intent intent = new Intent(MainActivity.this, ObjectTranDemo.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        this.finish();
    }

    public void dateAndTime(View view) {
        LogUtil.log(TAG, "--->date and time Button Click");
        Intent intent = new Intent(this, DateTimeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        //this.finish();
    }

    public void showNotification(View view) {
        LogUtil.log(TAG, "--->fileExplorer Button Click");
        ISNotification isNotification = new ISNotification();
        //isNotification.showNotification(this, R.drawable.ic_launcher, "通知测试", true, true, true, ISGetPictureFromInternet.class);
        isNotification.showNotificationNew(this, R.drawable.ic_launcher, "title", "contentText", MainActivity.class);
        Intent intent = new Intent(this, ISTabActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
    }

    public void showContacts(View view) {
        LogUtil.log(TAG, "--->show Contacts Button Click");
        Intent intent = new Intent(this, CursorsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        this.finish();
    }

    @SuppressWarnings("deprecation")
    public void exitDialog(View view) {
        LogUtil.log(TAG, "--->exitDialog Button Click");
        showDialog(EXIT_DIALOG);
    }

    /**
     * 設置屏幕的方向
     *
     * @param orientation LANDSCAPE--橫屏 PORTRAIT---竖屏
     */
    public void setOrientation(Orientation orientation) {
        if (orientation == Orientation.LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (orientation == Orientation.PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtil.log(TAG, "onConfigurationChanged()");
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LogUtil.log(TAG, "----->这是横屏");
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            LogUtil.log(TAG, "----->这是竖屏");
        }
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 設置是否全屏
     *
     * @param flag true--全屏 false--非全屏
     */
    public void setFullScreen(boolean flag) {
        if (flag) {
            // 设置没有标题
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            // 设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.log(TAG, "onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtil.log(TAG, "onRestart()");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtil.log(TAG, "onNewIntent(Intent intent)");
        if (intent != null && intent.getData() != null) {
            LogUtil.log(TAG, "Scheme==" + intent.getScheme());//com.example.ishelloword
            LogUtil.log(TAG, intent.getDataString());//获取全部参数 com.example.ishelloword://params?startType=SHAREINJECT&data=123
            LogUtil.log(TAG, intent.getData().getQuery());//startType=SHAREINJECT&data=123
            LogUtil.log(TAG, intent.getData().getQueryParameter("startType"));//SHAREINJECT
            LogUtil.log(TAG, intent.getData().getQueryParameter("data"));//123
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.log(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.log(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.log(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audioManager.unregisterMediaButtonEventReceiver(mediaComponentName);
        /**
         * 断开远程连接
         */
        if (connection != null) {
            unbindService(connection);
        }
        dismisDialog();
        LogUtil.log(TAG, "onDestroy()");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.log(TAG, "onRestoreInstanceState()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.log(TAG, "onSaveInstanceState()");
    }

    // 屏蔽Home键
    @Override
    public void onAttachedToWindow() {
        LogUtil.log(TAG, "onAttachedToWindow()");
        super.onAttachedToWindow();
    }

    private static Boolean isExit = false;
    private static final int EXIT = 100;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT: {
                    isExit = false;
                    break;
                }
                default:
                    break;
            }
        }

    };

    // 捕获home键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.isLongPress()) {
            LogUtil.log(TAG, "onKeyDown() longPress keycode==" + keyCode);
            return true;
        }
        LogUtil.log(TAG, "onKeyDown() keycode==" + keyCode);
        switch (keyCode) {
            case KeyEvent.KEYCODE_HOME: {
                LogUtil.log(TAG, "HOME Click");
                //finish();
                //Process.killProcess(Process.myPid());
                //Process.killProcess(Process.myPid());
                break;
            }

            case KeyEvent.KEYCODE_BACK: {
                LogUtil.log(TAG, "KEY_BACK");
                if (isExit == false) {
                    isExit = true;
                    showToast(R.string.exit, Toast.LENGTH_SHORT);
                    handler.sendEmptyMessageDelayed(EXIT, 2000);
                } else {
                    AppManager.getAppManager().appExit(getBaseContext());
                }
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        LogUtil.log(TAG, "onKeyLongPress keyCode==" + keyCode);
        // TODO Auto-generated method stub
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    protected Dialog onCreateDialog(int paramType) {
        LogUtil.log(TAG, "start onCreateDialog() id : " + paramType);
        DefineDialog localMessageDialog = new DefineDialog(this);
        localMessageDialog.setContentView(getLayoutInflater().inflate(R.layout.message_dialog, null));
        switch (paramType) {
            case EXIT_DIALOG: {
                localMessageDialog.setTitle(this.getString(R.string.dlg_prompt));
                localMessageDialog.setMessage(this.getString(R.string.exit_confim));
                localMessageDialog.setPriorityListener(new DefineDialog.PriorityListener() {
                    @Override
                    public void dismissDialog(Dialog dialog) {
                        LogUtil.log(TAG, "MessageDialog Click Back");
                        dialog.dismiss();
                    }
                });
                DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.log(TAG, "MessageDialog Ok Click");
                        dialog.cancel();
                        AppManager.getAppManager().appExit(getBaseContext());
                    }
                };
                DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LogUtil.log(TAG, "MessageDialog Cancel Click");
                        dialog.dismiss();
                    }
                };
                localMessageDialog.setButton(this.getString(R.string.dlg_quit), local1);
                localMessageDialog.setButton2(this.getString(R.string.dlg_cancel), local2);

            }
            break;

            default:
                break;
        }
        return localMessageDialog;
    }

    private void dismisDialog() {
        if (NoLinkDialog != null && NoLinkDialog.isShowing()) {
            NoLinkDialog.dismiss();
        }
    }

    private void createNoLinkDialog() {
        NoLinkDialog = new Dialog(this, R.style.NewsTranslucent2);
        NoLinkDialog.getWindow().setBackgroundDrawable(null);
        NoLinkDialog.setContentView(R.layout.dialog_quick_share_layout);
        TextView dialogTitle = (TextView) NoLinkDialog.findViewById(R.id.dialog_warning_title);
        TextView dialogContent = (TextView) NoLinkDialog.findViewById(R.id.dialog_warning_content);
        LinearLayout bottomLayout = (LinearLayout) NoLinkDialog.findViewById(R.id.ll_dialog_bottom_button);
        Button btnCancel = (Button) NoLinkDialog.findViewById(R.id.btn_cancel);
        Button btnOk = (Button) NoLinkDialog.findViewById(R.id.btn_ok);
        Button btnKnow = (Button) NoLinkDialog.findViewById(R.id.btn_I_know);
        CheckBox doNotRemind = (CheckBox) NoLinkDialog.findViewById(R.id.dialog_do_not_remind);
        dialogTitle.setText("提示");
        dialogContent.setText("用户提示信息");
        dialogContent.setGravity(Gravity.CENTER_HORIZONTAL);
        btnKnow.setText("我知道了");
        bottomLayout.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
        btnOk.setVisibility(View.GONE);
        btnKnow.setVisibility(View.VISIBLE);
        doNotRemind.setVisibility(View.GONE);
        btnOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NoLinkDialog.dismiss();

            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                NoLinkDialog.dismiss();
            }
        });
        btnKnow.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                NoLinkDialog.dismiss();
            }
        });
        NoLinkDialog.show();
    }

    protected AsyncHttpClient getAsyncHttpClient() {
        return this.asyncHttpClient;
    }

    protected void executeSample(AsyncHttpClient client, String URL, Header[] headers, HttpEntity entity, AsyncHttpResponseHandler responseHandler) {
        client.get(this, URL, headers, null, responseHandler);
    }

    protected String getDefaultURL() {
        return "http://210.75.225.164:8080/msaggregation_site/external?method_name=getm3u8&video_page_url=http://www.funshion.com/subject/play/103568/41";
    }

    protected AsyncHttpResponseHandler getResponseHandler() {
        return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                LogUtil.log(TAG, "---->Http onStart()");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                LogUtil.log(TAG, "--->onSuccess==" + statusCode + " response==" + new String(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e(TAG, "--->onFailure==" + statusCode + " message==" + e.getMessage());
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUESTCODE_FROM_ACTIVITY) {
                List<String> list = data.getStringArrayListExtra(Constant.RESULT_INFO);
                //for (String s : list) {
                //    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //}
//                Toast.makeText(getApplicationContext(), "选中了" + list.size() + "个文件", Toast.LENGTH_SHORT).show();
                String path = data.getStringExtra("path");
                Toast.makeText(getApplicationContext(), "选中的路径为" + path, Toast.LENGTH_SHORT).show();
                Log.i("LeonFilePicker", path);
            }
        }
    }
}
