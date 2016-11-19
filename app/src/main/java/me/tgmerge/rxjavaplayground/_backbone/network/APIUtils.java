package me.tgmerge.rxjavaplayground._backbone.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import me.tgmerge.rxjavaplayground._backbone.util.Utils;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tgmerge on 11/20.
 */

public final class APIUtils {

    private static final String TAG = APIUtils.class.getSimpleName();

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String CLIENT_KEY = "SOME CLIENT KEY";
    private static final String CLIENT_SECRET = "SOME CLIENT SECRET";
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static final class RetrofitHolder {

        private static Retrofit retrofit;

        public static void init() {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(JsonObject.class, new JsonElementTypeAdapter())
                    .create();
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(createClient())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
        }

        private static OkHttpClient createClient() {

            // 可以在这里设置信任的 SSL 证书
            // See: http://stackoverflow.com/a/31436459/2996355

            // 仅在 Debug 时显示网络请求日志
            HttpLoggingInterceptor.Level httpLoggingLevel = Utils.isDebug() ?
                    HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE;

            return new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(httpLoggingLevel))
                    .readTimeout(15, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .build();
        }
    }

    /**
     * 请求返回的 Response 对象用这个方法 flatMap 一下，取出其中的 result
     *
     * 用 flatMap 不用 map，是为了让 stack trace 清楚一点
     * See: http://stackoverflow.com/a/24273027
     * @param <T> Model 类
     */
    public static class ValidateResponse<T> implements Func1<Response<T>, Observable<T>> {

        @Override
        public Observable<T> call(final Response<T> response) {
            if (response.getStatus() != 200) {
                return Observable.error(new ApiException(response.getStatus(), response.getMsg()));
            }
            return Observable.just(response.getResult());
        }
    }

    /**
     * API 类中的每个请求都需要进行的相同变换操作
     *
     * 由于 API 类中每个请求都需要 Observable 进行相同的变换：
     *      service.getSomeData(time, key, hash)
     *              .subscribeOn(io)
     *              .observeOn(comp)
     *              .flatMap(new ValidateResponse())
     *              .observeOn(main)
     * 可以用一个 Transformer 和 compose() 的方法提供方便的 Observable 变换
     * @param <T> Model 类
     */
    public static class RequestTransformer<T> implements Observable.Transformer<Response<T>, T> {

        @Override
        public Observable<T> call(Observable<Response<T>> responseObservable) {
            return responseObservable
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .flatMap(new ValidateResponse<T>())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * 使用 Retrofit 所需的 interface 创建网络请求服务
     */
    public static <T> T createEndpoint(Class<T> clazz) {
        return RetrofitHolder.retrofit.create(clazz);
    }

    // --- 请求签名相关

    private static final Gson hashGson = new GsonBuilder().disableHtmlEscaping().create();

    @NonNull
    public static synchronized String getTime() {
        return String.valueOf(new Date().getTime());
    }

    @NonNull
    public static synchronized String getKey() {
        return CLIENT_KEY;
    }

    @NonNull
    public static synchronized <T> String getHash(String time, T body) {
        String msg = body == null ? "" : hashGson.toJson(body);
        return sha1(CLIENT_SECRET + sha1(CLIENT_SECRET) + time + msg);
    }

    /**
     * 返回参数 String 的 sha1，以十六进制 String 表示
     */
    private static String sha1(String str) {
        return Hashing.sha1().hashString(str, UTF_8).toString();
    }
}
