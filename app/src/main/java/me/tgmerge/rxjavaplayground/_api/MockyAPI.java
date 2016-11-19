package me.tgmerge.rxjavaplayground._api;

import me.tgmerge.rxjavaplayground._backbone.network.APIUtils;
import me.tgmerge.rxjavaplayground._backbone.network.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by tgmerge on 11/20.
 *
 * 使用 Mocky 生成用于测试的在线 HTTP API，内容自定义
 * 但都遵循了 { status; msg; result } 的格式
 *
 * See: http://www.mocky.io/
 */

public class MockyAPI {

    private static MockyAPIService service;

    public static void init() {
        service = APIUtils.createEndpoint(MockyAPIService.class);
    }

    private interface MockyAPIService {
        /** 一个字符串
         {
             "status": 200,
             "msg": "OK :)",
             "result": "Hi there, I am some result"
         }
         */
        @GET("http://www.mocky.io/v2/5830b66c260000461cf2285d")
        Observable<Response<String>> getOneString();

        /** status = 404
         {
             "status": 404,
             "msg": "Not found :(",
             "result": ""
         }
         */
        @GET("http://www.mocky.io/v2/5830b6bc260000581cf2285e")
        Observable<Response<String>> getStatus404();

        /** result = null
         {
             "status": 200,
             "msg": "OK but result is null!",
             "result": null
         }
         */
        @GET("http://www.mocky.io/v2/5830b90a260000a31cf2285f")
        Observable<Response<String>> getNullResult();
    }

    public static Observable<String> getOneString() {
        return service.getOneString().compose(new APIUtils.RequestTransformer<String>());
    }

    public static Observable<String> getStatus404() {
        return service.getStatus404().compose(new APIUtils.RequestTransformer<String>());
    }

    public static Observable<String> getNullResult() {
        return service.getNullResult().compose(new APIUtils.RequestTransformer<String>());
    }

}
