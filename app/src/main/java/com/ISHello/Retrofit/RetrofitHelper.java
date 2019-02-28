package com.ISHello.Retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ISHello.Retrofit.Interceptor.JsDownloadInterceptor;
import com.ISHello.Retrofit.Interceptor.JsDownloadListener;
import com.ISHello.Retrofit.bean.Book;
import com.google.gson.GsonBuilder;

import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private final String TAG = RetrofitHelper.class.getSimpleName();
    private OkHttpClient client;
    GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    private RetrofitService retrofitService = null;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    private RetrofitHelper() {
        init();
    }

    private void init() {
        resetRetrofit();
    }

    private void resetRetrofit() {
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {
                Log.e(TAG, "--->onStartDownload()" + length);
            }

            @Override
            public void onProgress(int progress) {
                Log.e(TAG, "--->onProgress()" + progress);
            }

            @Override
            public void onFail(String errorInfo) {
                Log.e(TAG, "--->onFail()" + errorInfo);
            }
        });
        client = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.douban.com/v2/")
                .client(client)
                .addConverterFactory(factory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //支持RxJava
                .build();
        retrofitService = mRetrofit.create(RetrofitService.class);
    }

    public RetrofitService getServer() {
        if (retrofitService == null) {
            resetRetrofit();
        }
        return retrofitService;
    }

    //https://api.douban.com/v2/book/search?q=金瓶梅&tag=&start=0&count=1
    public void getBookSearch(final CallBack call) {
        Observable<Book> bookObservable = getServer().getSearchBooks("金瓶梅", "", 0, 1);
        bookObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Book book) {
                        //可以在里面进行操作
                        if (null == book) {
                            call.onError("");
                        } else {
                            call.onSuccess(book);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        call.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getBookSearchMapParams(final CallBack call) {
        //q=金瓶梅&tag=&start=0&count=1
        HashMap<String, String> map = new HashMap<>();
        map.put("q", "金瓶梅");
        map.put("tag", "");
        map.put("start", "0");
        map.put("count", "1");
        Observable<Book> bookObservable = getServer().getSearchBookMapParams(map);
        bookObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Book>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Book book) {
                        //可以在里面进行操作
                        if (null == book) {
                            call.onError("");
                        } else {
                            call.onSuccess(book);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        call.onError(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 开始下载
     *
     * @param url  String url = "http://download.sdk.mob.com/apkbus.apk";
     * @param filePath
     * @param observe
     */
    public void download(@NonNull String url, final String filePath, Observer observe) {
        Observable<ResponseBody> dowanload = getServer().download(url);

        dowanload.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, InputStream>() {

                    @Override
                    public InputStream apply(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .observeOn(Schedulers.computation()) // 用于计算任务
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observe);

    }
}
