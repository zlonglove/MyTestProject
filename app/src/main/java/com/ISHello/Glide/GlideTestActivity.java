package com.ISHello.Glide;

import android.os.Bundle;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

public class GlideTestActivity extends BaseActivity {
    private GlideImageView glideImageView;
    private GlideImageView glide_image_view_round;
    private final String url = "http://img3.duitang.com/uploads/item/201604/30/20160430090033_HrznW.jpeg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide_test);
        glideImageView = (GlideImageView) findViewById(R.id.glide_image_view);
        glideImageView.setImageUrl(url, R.drawable.load_0, R.drawable.error);

        glide_image_view_round = (GlideImageView) findViewById(R.id.glide_image_view_round);
        glide_image_view_round.setRoundRoundAngle(url, 50,R.drawable.load_0, R.drawable.error);
    }
}
