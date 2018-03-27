package zlonglove.cn.network.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import zlonglove.cn.network.Constants.Constants;
import zlonglove.cn.network.R;
import zlonglove.cn.network.base.InfoCallback;
import zlonglove.cn.network.bean.AllCity;
import zlonglove.cn.network.bean.StoryList;
import zlonglove.cn.network.http.ApiService;
import zlonglove.cn.network.http.DataType;
import zlonglove.cn.network.http.HttpClient;
import zlonglove.cn.network.http.LoggerInterceptor;
import zlonglove.cn.network.http.OnResultListener;

public class OkHttpTestActivity extends AppCompatActivity {
    private final String TAG = OkHttpTestActivity.class.getSimpleName();
    private TextView infoText;
    private final String picUrl = "http://img3.duitang.com/uploads/item/201604/30/20160430090033_HrznW.jpeg";
    private ImageView get_pic_show_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ok_http_test);
        infoText = (TextView) findViewById(R.id.info);
        get_pic_show_image = (ImageView) findViewById(R.id.get_pic_show_image);
        getNewsList(getWeekDate().get(0), new InfoCallback<StoryList>() {
            @Override
            public void onSuccess(StoryList info) {
                if (info != null) {
                    Log.i(TAG, "--->" + info.toString());
                    infoText.setText(info.toString());
                }
            }

            @Override
            public void onError(int code, String message) {
                Log.i(TAG, "--->" + code + "/" + message);
            }
        });

        getPicFromNet(picUrl);
        getCitys("http://v.juhe.cn/weather/");
    }

    public void getNewsList(String date, final InfoCallback<StoryList> callback) {
        HttpClient client = new HttpClient.Builder()
                .baseUrl(Constants.ZHIHU_DAILY_BEFORE_MESSAGE)
                .url(date)
                .bodyType(DataType.JSON_OBJECT, StoryList.class)
                .build();
        client.get(new OnResultListener<StoryList>() {

            @Override
            public void onSuccess(StoryList result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(int code, String message) {
                callback.onError(code, message);
            }

            @Override
            public void onFailure(String message) {
                callback.onError(0, message);
            }
        });
    }

    private void getPicFromNet(final String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .addInterceptor(new LoggerInterceptor(null, true))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://img3.duitang.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        ApiService serviceApi = retrofit.create(ApiService.class);

        serviceApi.getImg(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new Function<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap apply(ResponseBody responseBody) {
                        if (responseBody == null) {
                            return null;
                        }
                        try {
                            Log.i(TAG, "--->" + Thread.currentThread().getName() + "/url==" + url);
                            InputStream inputStream = responseBody.byteStream();
                            if (inputStream != null) {
                                return BitmapFactory.decodeStream(responseBody.byteStream());
                            } else {
                                return null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            return null;
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap bitmap) throws Exception {
                        if (bitmap != null) {
                            get_pic_show_image.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(getApplicationContext(), "网络错误,请检查你的网络", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void getCitys(String url) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ApiService api = retrofit.create(ApiService.class);
        Observable<AllCity> observable = api.getAllCity("citys?key=");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllCity>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllCity allCity) {
                        Log.i(TAG, "--->" + allCity.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "--->" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 获取过去7天的时期，格式为yymmdd
     **/
    public static List<String> getWeekDate() {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_YEAR, 1 - i);
            dates.add(simpleDateFormat.format(calendar.getTime()));
        }
        return dates;
    }

}
