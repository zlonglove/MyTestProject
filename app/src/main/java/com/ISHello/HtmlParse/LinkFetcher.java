package com.ISHello.HtmlParse;

import com.ISHello.utils.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LinkFetcher {

    private final String TAG = LinkFetcher.class.getSimpleName();

    public interface OnLinkListener {
        void onLinkDataReady(String title, String image);
    }

    /**
     * 获取url中的title和pic
     *
     * @param url
     * @param listener
     */
    public void loadUrl(final String url, OnLinkListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL linkUrl = new URL(url);
                    Document document = Jsoup.parse(linkUrl, 5000);
                    URL imageUrl = null;
                    if (url.contains("mp.weixin.qq.com")) {
                        Elements imgElements = document.getElementsByTag("img");
                        for (int i = 0; i < imgElements.size(); i++) {
                            String src = imgElements.get(i).attr("src");
                            if (src != null && src.indexOf("mmbiz.qpic.cn") > 0) {
                                imageUrl = new URL(linkUrl, src);
                                break;
                            }
                        }
                    }
                    if (imageUrl == null) {
                        Element img = document.getElementsByTag("img").first();
                        if (img != null) {
                            imageUrl = new URL(linkUrl, img.attr("src"));
                        }
                    }
                    if (listener != null) {
                        listener.onLinkDataReady(document.title(), imageUrl == null ? "" : imageUrl.toString());
                    }
                } catch (IOException e) {
                    listener.onLinkDataReady("", "");
                    LogUtil.log(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 获取url中的title和pic
     *
     * @param url
     * @param consumer
     */
    public void loadUrl(final String url, Consumer<Map> consumer) {
        //以下是用了Rxjava如果不懂的可以使用thread+handler进行处理
        Observable.create(new ObservableOnSubscribe<Map>() {
            @Override
            public void subscribe(ObservableEmitter<Map> emitter) {
                Map map;
                try {
                    //这里开始是做一个解析，需要在非UI线程进行
                    String imgStr = "";
                    Document document = Jsoup.parse(new URL(url.trim()), 5000);
                    String title = document.head().getElementsByTag("title").text();
                    Elements imgs = document.getElementsByTag("img");//取得所有Img标签的值
                    if (imgs.size() > 0) {
                        imgStr = imgs.get(0).attr("abs:src");//默认取第一个为图片
                    }
                    map = new HashMap();
                    map.put("code", "1");
                    map.put("title", title);
                    map.put("url", url);
                    map.put("img", imgStr);
                    emitter.onNext(map);
                } catch (IOException e) {
                    map = new HashMap();
                    map.put("code", "0");
                    emitter.onNext(map);
                    e.printStackTrace();
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}
