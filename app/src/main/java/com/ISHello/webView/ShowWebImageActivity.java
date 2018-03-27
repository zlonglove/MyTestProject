package com.ISHello.webView;

import android.os.Bundle;
import android.widget.ImageView;

import com.ISHello.base.base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.ishelloword.R;

public class ShowWebImageActivity extends BaseActivity {

    private String imagePath = null;
    private ImageView imageView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_web_image);
        this.imagePath = getIntent().getStringExtra("image");

        imageView = (ImageView) findViewById(R.id.show_webimage_imageview);
        Glide.with(this).load(imagePath)
                .transition(new DrawableTransitionOptions().crossFade())
                .apply(new RequestOptions().centerCrop())
                .into(imageView);
    }
}
