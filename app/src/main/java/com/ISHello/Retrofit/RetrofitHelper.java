package com.ISHello.Retrofit;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ISHello.ResideMenu.utils.FileUtil;
import com.ISHello.Retrofit.Interceptor.JsDownloadInterceptor;
import com.ISHello.Retrofit.Interceptor.JsDownloadListener;
import com.ISHello.Retrofit.bean.Book;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private final String TAG = RetrofitHelper.class.getSimpleName();
    private OkHttpClient client;
    private GsonConverterFactory factory = GsonConverterFactory.create(new GsonBuilder().create());
    private static RetrofitHelper instance = null;
    private Retrofit mRetrofit = null;
    private RetrofitService retrofitService = null;
    private CallBack downloadCallBack;
    private HashMap<String, FileObserver> hashMap;

    private long downSize = 0;
    private long totalSize = 0;

    public static RetrofitHelper getInstance() {
        if (instance == null) {
            instance = new RetrofitHelper();
        }
        return instance;
    }

    private RetrofitHelper() {
        resetRetrofit();
    }

    private void resetRetrofit() {
        hashMap = new HashMap<>();
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(new JsDownloadListener() {
            @Override
            public void onStartDownload(long length) {
                Log.e(TAG, "--->onStartDownload()" + length);
            }

            @Override
            public void onProgress(long mTotalSize, long mDownSize) {
                Log.e(TAG, "--->onProgress()" + downSize + "/" + mTotalSize);
                downSize = mDownSize;
                totalSize = mTotalSize;
                if (downloadCallBack != null) {
                    downloadCallBack.onProgress(totalSize, downSize);
                }
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
     * @param call
     */
    public void download(@NonNull String url, final CallBack call) {
        if (isDownLoad(url)) {
            pause(url);
            Log.e(TAG, "pause url=" + url);
            return;
        }
        downloadCallBack = call;
        String currentPath = getTemporaryName(url);
        FileObserver dowanload = getServer().download(url)
                .map(new Function<ResponseBody, String>() {

                    @Override
                    public String apply(ResponseBody responseBody) {
                        if (downSize != 0 && totalSize != 0) {
                            return FileUtil.saveFile(currentPath, downSize, responseBody).getPath();
                        }
                        File file = FileUtil.saveFile(currentPath, responseBody);
                        return file.getPath();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new FileObserver<String>() {

                    @Override
                    public void onNext(String s) {
                        call.onSuccess(s);
                        hashMap.remove(url);
                    }

                    @Override
                    public void onError(Throwable e) {
                        call.onError(e.getMessage());
                        hashMap.remove(url);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        //保存
        hashMap.put(url, dowanload);
    }


    /**
     * 暂停/取消任务
     *
     * @param url 完整url
     */
    public void pause(String url) {
        if (hashMap.containsKey(url)) {
            FileObserver observer = hashMap.get(url);
            if (observer != null) {
                observer.dispose();
                hashMap.remove(url);
            }
        }
    }

    /**
     * 是否在下载
     *
     * @param url
     * @return
     */
    public boolean isDownLoad(String url) {
        return hashMap.containsKey(url);
    }

    /**
     * 获取临时文件名
     *
     * @param url
     * @return
     */
    public String getTemporaryName(String url) {
        String type = "";
        if (url.contains(".")) {
            type = url.substring(url.lastIndexOf("."));
        }
        String dirName = Environment.getExternalStorageDirectory() + "/firstProject/img/";

        File f = new File(dirName);
        //不存在创建
        if (!f.exists()) {
            f.mkdirs();
        }
        return dirName + System.currentTimeMillis() + type;
    }

    public abstract class FileObserver<T> extends DisposableObserver<T> {

    }
}
