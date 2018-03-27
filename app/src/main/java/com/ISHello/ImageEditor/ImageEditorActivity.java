package com.ISHello.ImageEditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ishelloword.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import im.icbc.com.imageeditor.CustomSurfaceView;

public class ImageEditorActivity extends AppCompatActivity implements OnClickListener, CustomSurfaceView.SurfaceListener, TextWatcher {


    private ImageView bianjia_0;
    private ImageView bianjia_1;
    private ImageView bianjia_2;
    private ImageView bianjia_3;
    private ImageView bianjia_4;
    private ImageView bianjia_5;
    private LinearLayout bianjia_6;
    private EditText input_text;
    private CustomSurfaceView surfce;
    private LinearLayout ll;
    private ImageView showbitmap;
    private File myFile;
    private String pathUrl = Environment.getExternalStorageDirectory().getAbsolutePath() + "/surface";
    private String pathUrl2 = Environment.getDataDirectory().getAbsolutePath() + "/surface";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_editor);
        /*if (getSupportActionBar() != null)
            getSupportActionBar().hide();*/
        String url = "http://122.19.173.71:8011/icons/text.gif";
        //Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.jianpan);
        surfce = new CustomSurfaceView(this, url, false);
        //surfce=new CustomSurfaceView(this,null,bitmap,false);
        ll = (LinearLayout) findViewById(R.id.ll);
        showbitmap = (ImageView) findViewById(R.id.showbitmap);
        ll.addView(surfce);
        bianjia_0 = (ImageView) findViewById(R.id.bianjia_0);
        bianjia_1 = (ImageView) findViewById(R.id.bianjia_1);
        bianjia_2 = (ImageView) findViewById(R.id.bianjia_2);
        bianjia_3 = (ImageView) findViewById(R.id.bianjia_3);
        bianjia_4 = (ImageView) findViewById(R.id.bianjia_4);
        bianjia_5 = (ImageView) findViewById(R.id.bianjia_5);
        bianjia_6 = (LinearLayout) findViewById(R.id.bianjia_6);
        input_text = (EditText) findViewById(R.id.input_text);
        bianjia_0.setOnClickListener(this);
        bianjia_1.setOnClickListener(this);
        bianjia_2.setOnClickListener(this);
        bianjia_3.setOnClickListener(this);
        bianjia_4.setOnClickListener(this);
        bianjia_5.setOnClickListener(this);
        bianjia_6.setOnClickListener(this);
        surfce.setSurfaceListener(this);
        input_text.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bianjia_0:
                surfce.setState(0);
                break;
            case R.id.bianjia_1:
                surfce.setState(1);
                break;
            case R.id.bianjia_2:
                surfce.setState(2);
                break;
            case R.id.bianjia_3:
                surfce.setState(3);
                break;
            case R.id.bianjia_4:
                surfce.setState(4);
                break;
            case R.id.bianjia_5:
                surfce.revocation();
                break;
            case R.id.bianjia_6:
                Bitmap bm = surfce.getBitmap();
                SavaImage(bm);
                showbitmap.setImageBitmap(bm);
                showbitmap.setVisibility(View.VISIBLE);
                Log.e("msg", myFile.exists() + "    " + myFile.length() + "   " + bm.getByteCount());
                break;
        }
    }

    @Override
    public void startDrawText() {
        input_text.setText("");
        showKeyboard();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String text = input_text.getText().toString();
        surfce.inputText(text);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    /**
     * 显示键盘
     *
     * @param
     */
    protected void showKeyboard() {
        try {
            input_text.setFocusable(true);
            input_text.setFocusableInTouchMode(true);
            input_text.requestFocus();
            InputMethodManager inputMethodManage = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManage.showSoftInput(input_text, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SavaImage(Bitmap bitmap) {
        if (ExistSDCard()) {
            myFile = new File(pathUrl + "/icon.png");
        } else {
            myFile = new File(pathUrl2 + "/icon.png");
        }
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!myFile.exists()) {
            try {
                myFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("msg", myFile.getName() + "文件长度 " + myFile.getAbsolutePath());
        try {
            fileOutputStream = new FileOutputStream(myFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.close();
            this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + myFile.getAbsolutePath())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
}
