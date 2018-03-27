package com.ISHello.UI;

import com.example.ishelloword.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class UIActivity extends Activity {
    private final String TAG = "UIActivity";
    private ImageView RoundImage;
    private BitmapDisplayer bitmapDisplayer;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_activity_layout);
        findViews();
        init();
    }

    private void findViews() {
        RoundImage = (ImageView) this.findViewById(R.id.RoundImage);
    }

    private void init() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            Log.i(TAG, "--->Uri==" + uri.toString());
        }
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        bitmapDisplayer = new RoundedBitmapDisplayer(250);
        ImageViewAware imageViewAware = new ImageViewAware(RoundImage);
        bitmapDisplayer.display(bitmap, imageViewAware, BitmapDisplayer.LoadedFrom.DISC_CACHE);
        Log.i(TAG, "--->width==" + imageViewAware.getWidth());
    }
}
