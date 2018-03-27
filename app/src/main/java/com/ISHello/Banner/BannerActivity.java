package com.ISHello.Banner;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ISHello.base.base.BaseActivity;
import com.example.ishelloword.R;

import java.util.ArrayList;
import java.util.List;

public class BannerActivity extends BaseActivity {
    public final int[] RES = new int[]{R.drawable.image5, R.drawable.image2, R.drawable.image3,
            R.drawable.image4, R.drawable.image6, R.drawable.image7, R.drawable.image8};

    private MZBannerView mMZBanner;
    private MZBannerView mNormalBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);
        initView();
    }

    private void initView() {

        mMZBanner = (MZBannerView) findViewById(R.id.banner);
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                showToast("click page:" + position, Toast.LENGTH_SHORT);
            }
        });
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < RES.length; i++) {
            list.add(RES[i]);
        }
        mMZBanner.setIndicatorVisible(true);
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });


        mNormalBanner = (MZBannerView) findViewById(R.id.banner_normal);
        mNormalBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                showToast("click page:" + position,Toast.LENGTH_SHORT);
            }
        });
        mNormalBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }

    public class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMZBanner.start();
        mNormalBanner.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMZBanner.pause();
        mNormalBanner.pause();
    }
}
