package com.ISHello.IOSDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ISHello.DefineDialog.AddressDialog;
import com.ISHello.DefineDialog.MenuBottomDialog;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.ArrayList;
import java.util.List;

import im.icbc.com.iosdialog.HintDialog;
import im.icbc.com.iosdialog.IOSLoadingDialog;
import im.icbc.com.iosdialog.LoadingDialog;
import im.icbc.com.iosdialog.PhotoDialog;
import zlonglove.cn.base.BaseDialog;

public class iosDialogActivity extends BaseActivity {
    private HintDialog hintDialog = new HintDialog(); // 提示框
    private HintDialog singleHintDialog = new HintDialog(); // 单个提示框
    private PhotoDialog photoDialog = new PhotoDialog(); // 拍照 选择相册

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ios_dialog);
        setTitle("自定义Dialog");
        initView();
    }

    private void initView() {
        // AlertDialog
        findViewById(R.id.btn_alert_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(iosDialogActivity.this).setTitle("测试标题").setMessage("Hello Word~!").setPositiveButton("哈喽", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
        });
        // Android 加载框
        findViewById(R.id.btn_android_loading_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadingDialog loadingDialog = new LoadingDialog().setOnTouchOutside(true);
                loadingDialog.show(iosDialogActivity.this.getFragmentManager(), "loadingDialog");
            }
        });
        // 高仿IOS加载框
        findViewById(R.id.btn_ios_loading_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IOSLoadingDialog iosLoadingDialog = new IOSLoadingDialog().setOnTouchOutside(true).setHintMsg("正在加载");
                iosLoadingDialog.show(iosDialogActivity.this.getFragmentManager(), "iosLoadingDialog");
            }
        });
        // 提示框
        findViewById(R.id.btn_ios_hint_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintDialog.setContent("确定要离开吗？");
                hintDialog.setOnConfirmClickListener(new HintDialog.HintConfirmCallback() {
                    @Override
                    public void onClick() {
                        hintDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击确定", Toast.LENGTH_SHORT).show();
                    }
                });
                hintDialog.setOnCancelClickListener(new HintDialog.HintCancelCallback() {
                    @Override
                    public void onClick() {
                        hintDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
                    }
                });
                hintDialog.show(iosDialogActivity.this.getFragmentManager(), "hintDialog");
            }
        });
        // 单个提示框
        findViewById(R.id.btn_ios_single_hint_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleHintDialog.setContent("请认真填写相关信息，谢谢合作~").setIsSingleButton(true);
                singleHintDialog.setOnSingleClickListener(new HintDialog.HintSingleCallback() {
                    @Override
                    public void onClick() {
                        singleHintDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击确认~", Toast.LENGTH_SHORT).show();
                    }
                });
                singleHintDialog.show(iosDialogActivity.this.getFragmentManager(), "singleHintDialog");
            }
        });
        // 拍照 选取相册
        findViewById(R.id.btn_ios_camera_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialog.setOnCameraClickListener(new PhotoDialog.PhotoCameraCallback() {
                    @Override
                    public void onClick() {
                        photoDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击拍照", Toast.LENGTH_SHORT).show();
                    }
                });
                photoDialog.setOnChoosePhotoClickListener(new PhotoDialog.ChoosePhotoCallback() {
                    @Override
                    public void onClick() {
                        photoDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击选取相册", Toast.LENGTH_SHORT).show();
                    }
                });
                photoDialog.setOnCancleClickListener(new PhotoDialog.PhoneCancelCallback() {
                    @Override
                    public void onClick() {
                        photoDialog.dismiss();
                        Toast.makeText(iosDialogActivity.this, "点击取消", Toast.LENGTH_SHORT).show();
                    }
                });
                photoDialog.show(iosDialogActivity.this.getFragmentManager(), "");
            }
        });

        findViewById(R.id.btn_area_select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddressDialog.Builder(iosDialogActivity.this)
                        .setTitle("选择地区")
                        //.setIgnoreArea() // 不选择县级区域
                        .setListener(new AddressDialog.OnListener() {

                            @Override
                            public void onSelected(Dialog dialog, String province, String city, String area) {
                                showToast(province + city + area, Toast.LENGTH_LONG);
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                showToast("取消了", Toast.LENGTH_LONG);
                            }
                        }).show();
            }
        });

        findViewById(R.id.btn_bottom_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    data.add("我是数据" + i);
                }
                new MenuBottomDialog.Builder(iosDialogActivity.this)
                        .setCancel("取消") // 设置 null 表示不显示取消按钮
                        //.setAutoDismiss(false) // 设置点击按钮后不关闭对话框
                        .setList(data)
                        .setListener(new MenuBottomDialog.OnListener() {

                            @Override
                            public void onSelected(Dialog dialog, int position, String text) {
                                showToast("位置：" + position + "，文本：" + text, Toast.LENGTH_SHORT);
                            }

                            @Override
                            public void onCancel(Dialog dialog) {
                                showToast("取消了", Toast.LENGTH_SHORT);
                            }
                        })
                        .setGravity(Gravity.BOTTOM)
                        .setAnimStyle(BaseDialog.AnimStyle.BOTTOM)
                        .show();
            }
        });
    }
}
