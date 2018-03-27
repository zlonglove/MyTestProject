package com.ISHello.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ISHello.Cache.DiskLrcCache.ISDiskLruCacheUtils;
import com.ISHello.ImageLoader.implement.ImageLoader;
import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.ArrayList;
import java.util.List;

public class ImageLoaderActivity extends BaseActivity {
    private final String TAG = "ImageLoaderActivity";
    private GridView displayImage;
    private List<String> imageData;
    private ImageShowAdapter imageShowAdapter;
    private String url = "http://img3.duitang.com/uploads/item/201604/30/20160430090033_HrznW.jpeg";//错误url
    //private String url = "http://img3.duitang.com/uploads/item/201604/30/20160430090033_HrznW.jpeg";
    private ISDiskLruCacheUtils diskLruCacheUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loader);
        findViews();
        init();
    }

    private void findViews() {
        displayImage = (GridView) findViewById(R.id.display_image_area);
    }

    private void init() {
        diskLruCacheUtils = ISDiskLruCacheUtils.getInstance(this);
        if (!diskLruCacheUtils.open("bitmap")) {
            Log.i(TAG, "--->init DiskLruCache fail");
        }
        diskLruCacheUtils.setErrorResource(R.drawable.ic_launcher);

        displayImage.setSelector(new ColorDrawable(Color.TRANSPARENT));
        imageData = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            imageData.add(url);
        }
        //imageData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491887946202&di=9999ef21dbfb453261ac6c65548d4688&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201405%2F29%2F20140529223850_wWs3H.thumb.600_0.jpeg");
        //imageData.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1491887946202&di=cf2f5b78e6b8b634b7ac7f0f85c5a082&imgtype=0&src=http%3A%2F%2Fwww.1tong.com%2Fuploads%2Fwallpaper%2Fillustration%2F116-5-1366x768.jpg");
        imageData.add("http://122.19.125.27/userfiles/menuPic/19.png?VersionNo=20180131142510");
        imageData.add("http://122.19.125.27/userfiles/menuPic/B9.png?VersionNo=20240822174073");
        imageData.add("http://122.19.125.27/userfiles/menuPic/E6.png");
        imageData.add("http://122.19.125.27/userfiles/menuPic/C15.png");
        imageData.add("http://122.19.125.27/userfiles/menuPic/B8.png");
        imageData.add("http://122.19.125.27/userfiles/menuPic/B3.png");
        imageData.add("http://122.19.125.27/userfiles/menuPic/64.png");
        imageShowAdapter = new ImageShowAdapter(this, imageData);
        displayImage.setAdapter(imageShowAdapter);
    }

    public void downloadImage(View view) {
        imageShowAdapter.setData(imageData);

    }

    private class ImageShowAdapter extends BaseAdapter {
        private Context context;
        private List<String> data;

        public ImageShowAdapter(Context context, List<String> data) {
            this.context = context;
            this.data = data;
        }

        public void setData(List<String> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (data != null) {
                return data.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (data != null) {
                data.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "--->getView position==" + position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.layout_imageloader_display, null);
                holder = new ViewHolder();
                holder.image = (ImageView) convertView.findViewById(R.id.imageloader_display_imageview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            /*if (diskLruCacheUtils != null) {
                diskLruCacheUtils.bindBitmap(data.get(position), holder.image);
            }*/
            ImageLoader.getInstance().displayImage(data.get(position), holder.image);
            return convertView;
        }

        private class ViewHolder {
            ImageView image;
        }
    }
}
