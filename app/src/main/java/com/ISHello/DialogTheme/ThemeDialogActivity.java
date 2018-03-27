/**
 *
 */
package com.ISHello.DialogTheme;

import com.ISHello.CustomToast.CustomToast;
import com.example.ishelloword.MainActivity;
import com.example.ishelloword.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

/**
 * @author zhanglong
 *         互动对话框的种类
 *         1)PopuWindow
 *         2)Dialog
 *         3)AlterDialog
 *         4)ProgressDialog
 *         5)Toast
 *         6)DatePickerDialog
 *         7)TimePickerDialog
 */
public class ThemeDialogActivity extends Activity {
    private final String TAG = "ThemeDialogActivity";
    private ListView listView;
    Button button;
    Button button1;
    private PopupWindow popupWindow;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private Dialog alertDialog;
    private CharSequence[] list =
            {
                    "PopuWindow",
                    "Dialog",
                    "AlertDialog",
                    "ProgressDialog",
                    "Toast",
            };

    /**
     *
     */
    public ThemeDialogActivity() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        setContentView(R.layout.themedialog_activity);
        listView = (ListView) this.findViewById(R.id.dialog_listview);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        button = new Button(this);
        button1 = new Button(this);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int arg2,
                                    long arg3) {
                switch (arg2) {
                    //PopuWindow
                    case 0:
                        button.setText("单击就可以关闭PopuWindow");
                        if (popupWindow == null) {
                            popupWindow = new PopupWindow(ThemeDialogActivity.this);
                            popupWindow.setContentView(button);
                            popupWindow.setFocusable(true);
                            popupWindow.setWidth(300);
                            popupWindow.setHeight(200);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    popupWindow.dismiss();
                                }
                            });
                        }
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                        break;

                    case 1:
                        button1.setText("关闭Dialog");
                        if (dialog == null) {
                            dialog = new Dialog(ThemeDialogActivity.this);
                            dialog.setTitle("提示");
                            dialog.setContentView(button1);
                            button1.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        dialog.show();

                        break;

                    case 2:
                        if (alertDialog == null) {
                            alertDialog = new AlertDialog.Builder(ThemeDialogActivity.this)
                                    .setTitle("AlertDialog")
                                    .setMessage("显示Message区域")
                                    .setIcon(R.drawable.ic_launcher)
                                    .setNegativeButton("negative", new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            showText("NegativeButton Click");
                                        }
                                    })
                                    .setPositiveButton("positive", new OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO Auto-generated method stub
                                            showText("PositiveButton Click");
                                        }
                                    })
                                    .setCancelable(true)
                                    .setOnCancelListener(new OnCancelListener() {

                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            // TODO Auto-generated method stub
                                            showText("Cancel Key Click");
                                        }
                                    }).create();
                        }
                        alertDialog.show();
                    /*
					Builder builder=new Builder(ThemeDialogActivity.this);
					builder.setTitle("AlertDialog");
					builder.setMessage("显示Message区域");
					builder.setIcon(R.drawable.ic_launcher);
					builder.show();
					*/
                        break;

                    case 3:
                        if (progressDialog == null) {
                            progressDialog = new ProgressDialog(ThemeDialogActivity.this);
                            progressDialog.setIcon(R.drawable.ic_launcher);
                            progressDialog.setTitle("下载提示");
                            progressDialog.setMessage("显示ProgressDialog Message区域");
                            progressDialog.setButton("取消", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    showText("Cancel Click");
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        progressDialog.show();
                        break;

                    case 4:
                        showText("Toast Message");
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showText(String text) {
        CustomToast.makeText(this, text, Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }


}
