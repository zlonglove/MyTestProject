package com.ISHello.GetPictureFromInternet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.ISHello.Cache.DiskLrcCache.ISDiskLruCacheUtils;
import com.ISHello.CustomToast.CustomToast;
import com.ISHello.NetWork.ISAndroidHandler;
import com.ISHello.NetWork.ISNetWork;
import com.ISHello.base.base.BaseActivity;
import com.androidquery.AQuery;
import com.example.ishelloword.R;
import com.example.ishelloword.wxapi.WeChatShare;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import im.icbc.cn.keyboard.safeEditText;
import im.icbc.cn.keyboard.utils.NewTransferInterface;
import im.icbc.cn.keyboard.utils.transferInterface;

public class ISGetPictureFromInternet extends BaseActivity {
    private final String TAG = ISGetPictureFromInternet.class.getSimpleName();
    private final String MOBILE_NUMBER_RE = "1[0-9]{10}";
    boolean isUseSafeEdit;
    private safeEditText mLoginPassword;
    public AQuery aQuery;
    private ImageView image;
    private ISNetWork isNetWork;
    private ISAndroidHandler androidHandler;
    static ISGetPictureFromInternet activity;
    private static final int LOAD_IMAGE_REQUEST_CODE = 1;
    private ISNetWorkClient client;
    private String filePath = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.getpicture);
        aQuery = new AQuery(this);
        image = (ImageView) findViewById(R.id.image);
        androidHandler = new ISAndroidHandler(this);
        isNetWork = new ISNetWork(androidHandler);
        client = ISNetWorkClient.getInstance();
        /**
         * 软键盘使用
         */
        mLoginPassword = (safeEditText) findViewById(R.id.login_password);
        //第一个参数 是否调用系统输入法
        //第二个 是否是加密的控件
        //第三个是保留参数，暂时没用
        //mLoginPassword.initMe(true, true, "abcdef");
        mLoginPassword.initMe(false, true, "abcdef");
        mLoginPassword.setUIType(1);
        mLoginPassword.setIsOrdered(new boolean[]{false, true, true});
        mLoginPassword.setTextMaxLen(30);
        mLoginPassword.setExitShowText("登  录", "完  成");
        mLoginPassword.setAnimStat(true);
        mLoginPassword.setCallParent(this);
        mLoginPassword.setInterface(new transferInterface() {
            /**
             * 第一次点击密码框会执行(会获得focus)
             */
            public void upLayout() {
                Log.i(TAG, "--->upLayout()");
                mLoginPassword.setText("");
                mLoginPassword.setHint(R.string.login_password_notip);
            }

            /**
             * 切换系统输入法(arg==2)
             */
            public void downLayout(int arg) {
                Log.i(TAG, "--->downLayout()" + arg);
                if (arg == 1) {
                    isUseSafeEdit = true;
                    mLoginPassword.setHint(R.string.login_password_tip);

                } else {
                    mLoginPassword.setText("");
                    isUseSafeEdit = false;
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        public void run() {
                            InputMethodManager inputMethodManager = (InputMethodManager) ISGetPictureFromInternet.this
                                    .getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        }
                    }, 320);
                }
            }
        });

        mLoginPassword.setNewInterface(new NewTransferInterface() {

            @Override
            public void finishDown() {
                Log.i(TAG, "--->finishDown()");
            }

            @Override
            public void ExitDown() {
                Log.i(TAG, "--->ExitDown()");
                // 点击登录按钮
                loading();
            }

            /**
             * 大小写按键按下的时候
             */
            @Override
            public void CapsLockDown(int style) {
                Log.i(TAG, "--->CapsLockDown()");
            }

            @Override
            public void AnimSetDown(int style) {
                Log.i(TAG, "--->AnimSetDown()");
            }

            @Override
            public void ChangeModeDown(int style) {
                Log.i(TAG, "--->ChangeModeDown()");
            }
        });
        mLoginPassword.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Log.i(TAG, "--->onFocus()");
                    mLoginPassword.openKeyBoard();
                }
            }
        });
    }

    private void loading() {
        mLoginPassword.closeKeyBoard();
        String tmpPassword;
        String tmpUserName;
        if (!isUseSafeEdit) {
            tmpPassword = mLoginPassword.getText().toString();
        } else {
            tmpPassword = mLoginPassword.getEncryptStr();
        }

        tmpUserName = aQuery.id(R.id.login_id).getText().toString().trim();
        Log.i(TAG, "---userName==" + tmpUserName);
        Log.i(TAG, "--->password==" + tmpPassword);

        if (TextUtils.isEmpty(tmpUserName)) {
            CustomToast.makeText(ISGetPictureFromInternet.this, "请输入手机号/卡（账）号/用户名",
                    Toast.LENGTH_SHORT);
            aQuery.id(R.id.login_id).getTextView().requestFocus();
            return;
        }

        if ("".equals(tmpPassword) || null == tmpPassword) {
            aQuery.id(R.id.login_password).getTextView().requestFocus();
            CustomToast.makeText(ISGetPictureFromInternet.this, "请输入登录密码!",
                    Toast.LENGTH_SHORT);
            return;
        }
        // 校验是否纯数字
        if (isNumericString(tmpUserName) && tmpUserName.length() < 12) {
            if (!isMobileNum(tmpUserName)) {
                CustomToast.makeText(ISGetPictureFromInternet.this, "请输入正确的手机号",
                        Toast.LENGTH_SHORT);
                return;
            }
        }
        Log.i(TAG, "--->loading start");
    }

    public boolean isMobileNum(String phoneNum) {
        Pattern pattern = Pattern.compile(MOBILE_NUMBER_RE);
        Matcher macher = pattern.matcher(phoneNum);
        return macher.find();
    }

    /**
     * 方法名称： 输入项
     * 输入项说明： 待校验字符串 +表示至少有1个 这里没有用*号 支持出现0个
     * 返回项说明：如果是纯数字字符组成的字符串则返回true 否则返回false
     *
     * @param inString
     * @return boolean
     */
    public boolean isNumericString(String inString) {
        if (inString == null) return false;
        Pattern p = Pattern.compile("^[0-9]+$");
        Matcher m = p.matcher(inString);
        return m.matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //mLoginPassword.cleanOutHandle();
    }

    public static ISGetPictureFromInternet getInstance() {
        return activity;
    }

    public void UpdateImage(Bitmap bitmap) {
        if (image != null && bitmap != null) {
            image.setImageBitmap(bitmap);
        }
    }

    public void showImage(View view) {
        /*if (isNetWork != null) {
            isNetWork.sendNetWorkEvent(ISNetWorkCmd.EVENT_GET_PICTURE,
					urlPathEditText.getText().toString());
		}*/
        //ImageLoaderActivity.getInstance().loadImage("http://122.19.157.215/userfiles/menuPic/B3.png",image,true);

        String url = "http://img3.duitang.com/uploads/item/201604/30/20160430090033_HrznW.jpeg";
        ISDiskLruCacheUtils diskLruCacheUtils = ISDiskLruCacheUtils.getInstance(this);
        if (!diskLruCacheUtils.open("bitmap")) {
            Log.i(TAG, "--->init DiskLruCache fail");
        }
        for (int i = 0; i < 20; i++) {
            diskLruCacheUtils.bindBitmap(url, image);
        }
    }

    /**
     * 上传图片
     *
     * @param view
     */
    public void uploadPicture(View view) {
        /**
         * 从图片库中选择一张图片,得到物理路径
         */
        /*Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, LOAD_IMAGE_REQUEST_CODE);*/
        WeChatShare weChatShare = new WeChatShare(this);
        weChatShare.setTimeline(false);
        weChatShare.shareUrl("www.icbc.com");
    }

    public boolean[] getKeyboardOrdered() {
        boolean[] keyboardOrdered = new boolean[3];
        // 临时设置
        String data = "";
        // String data =
        // configDao.getStringConfig(ConfigDao.SAFE_KEYBOARD_TYPE);
        if ("".equals(data)) {
            keyboardOrdered = new boolean[]{false, true, true};
        } else {
            String[] temp = data.split(":");
            keyboardOrdered[0] = (temp[0].equals("1") ? true : false);
            keyboardOrdered[1] = (temp[1].equals("1") ? true : false);
            keyboardOrdered[2] = (temp[2].equals("1") ? true : false);
        }
        return keyboardOrdered;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOAD_IMAGE_REQUEST_CODE) {
            if (data == null) {
                CustomToast.makeText(this, "no picture seleced", Toast.LENGTH_LONG);
            } else {
                Uri uri = data.getData();
                if (uri == null) {
                    CustomToast.makeText(this, "no picture path", Toast.LENGTH_LONG);
                } else {
                    String path = null;
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columIndex = cursor
                                .getColumnIndex(filePathColumn[0]);
                        path = cursor.getString(columIndex);
                        cursor.close();
                    }
                    if (path != null) {
                        CustomToast.makeText(this, "picture path==" + path,
                                Toast.LENGTH_LONG);
                        filePath = path;
                        new AlertDialog.Builder(this)
                                .setTitle("提示")
                                .setMessage("你要上传选择的图片吗?")
                                .setPositiveButton("确定", new OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        upload();
                                    }
                                })
                                .setNegativeButton("取消", new OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    }
                }
            }
        }
    }

    private void upload() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Name", "zhanglong");
                    params.put("Gender", "male");
                    String url = "http://192.168.46.44:8080/First_web_server/upload.do";
                    final String result = client.uploadImage(url, filePath,
                            params);
                    Log.i(TAG, "--->upload image result==" + result);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CustomToast.makeText(ISGetPictureFromInternet.this,
                                    "upload result==" + result,
                                    Toast.LENGTH_LONG);
                        }
                    });

                } catch (Exception e) {
                    CustomToast.makeText(ISGetPictureFromInternet.this,
                            "upload　error==" + e.getMessage(),
                            Toast.LENGTH_LONG);
                } finally {

                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if (isNetWork != null) {
            isNetWork.stopNetWork();
        }
        androidHandler = null;
        super.onDestroy();
    }

}
