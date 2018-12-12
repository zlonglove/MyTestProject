package com.ISHello.Glide;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * Created by zhanglong on 2017/8/3.
 * placeholder 占位图
 */

public class GlideImageView extends ImageView {
    public GlideImageView(Context context) {
        super(context);
    }

    public GlideImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GlideImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /***
     * 通过url加载图片
     *
     * @param url
     */
    public void setImageUrl(String url, @DrawableRes int placeholder, @DrawableRes int error) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .crossFade()
                    .placeholder(placeholder).error(error)
                    .into(this);
        } catch (Exception e) {
            this.setImageResource(placeholder);
        }
    }

    /**
     * 广告banner的加载图片方法
     *
     * @param url
     */
    public void setAdBannerImageUrl(String url, @DrawableRes int placeholder, @DrawableRes int error) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .crossFade()
                    .placeholder(placeholder).error(error).centerCrop()
                    .into(this);
        } catch (Exception e) {
            this.setImageResource(placeholder);
        }
    }

    /***
     * 加载本地图片
     *
     * @param path
     */
    public void loadLocalImage(String path, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(getContext())
                .load("file://" + path)
                .placeholder(placeholder).error(error)
                .into(this);
    }

    /***
     * 加载drawable,mipmap下面图片
     *
     * @param id
     */
    public void setLoadSrcDrawable(int id) {
        Glide.with(getContext())
                .load(id)
                .into(this);
    }

    /**
     * 读取sd图片，可以设置宽高
     *
     * @param path
     * @param width
     * @param height
     */
    public void loadLocalImageWidthResize(String path, int width, int height, @DrawableRes int placeholder, @DrawableRes int error) {
        Glide.with(getContext())
                .load("file://" + path)
                .placeholder(placeholder).error(error).override(width, height)
                .into(this);
    }

    /***
     * 根据 FILE 读取图片
     *
     * @param file
     * @param width
     * @param height
     */
    public void loadLocalFile(File file, int width, int height, @DrawableRes int placeholder, @DrawableRes int error) {
        try {
            Glide.with(getContext())
                    .load(file)
                    .placeholder(placeholder).error(error).override(width, height)
                    .into(this);
        } catch (Exception e) {
            this.setImageResource(placeholder);
        }
    }

    /***
     * 圆型头像
     *
     * @param url
     */
    public void setRoundRoundAngle(String url, @DrawableRes int placeholder, @DrawableRes int error) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .placeholder(placeholder).error(error).fitCenter()
                    .into(this);
        } catch (Exception e) {
            this.setImageResource(placeholder);
        }
    }

    /***
     * 圆型头像
     *
     * @param url
     */
    public void setRoundRoundAngle(String url, int dp, @DrawableRes int placeholder, @DrawableRes int error) {
        try {
            Glide.with(getContext())
                    .load(url)
                    .load(url)
                    .placeholder(placeholder).error(error).fitCenter()
                    .into(this);
        } catch (Exception e) {
            this.setImageResource(placeholder);
        }
    }
}
