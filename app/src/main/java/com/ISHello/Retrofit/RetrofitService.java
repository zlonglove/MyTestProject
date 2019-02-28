package com.ISHello.Retrofit;

import com.ISHello.Retrofit.bean.Book;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {

    @GET("book/search")
    Observable<Book> getSearchBooks(@Query("q") String name,
                                    @Query("tag") String tag,
                                    @Query("start") int start,
                                    @Query("count") int count);

    @GET("book/search")
    Observable<Book> getSearchBookMapParams(@QueryMap Map<String, String> options);

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}
