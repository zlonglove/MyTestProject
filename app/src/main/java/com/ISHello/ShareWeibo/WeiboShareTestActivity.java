package com.ISHello.ShareWeibo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.example.ishelloword.R;

public class WeiboShareTestActivity extends BaseWeiboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weibo_share_test);
    }

    public void shareWeiboText(View view) {
        sendTextObj("测试");
    }

    public void shareWeiboImage(View view) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        sendImageObj(bitmap);
    }
}
