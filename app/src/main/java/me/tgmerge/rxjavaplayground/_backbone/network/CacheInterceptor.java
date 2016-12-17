package me.tgmerge.rxjavaplayground._backbone.network;

import java.io.IOException;

import okhttp3.*;
import okhttp3.Response;

import static me.tgmerge.rxjavaplayground._backbone.util.Utils.strEqual;

/**
 * Created by tgmerge on 12/15.
 */

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        // 获取到 request
        Request request = chain.request();

        boolean canCached = false;
        request.headers()
        if (strEqual(request.header("Android-Cache-Within"), "true")) {
            // 这个请求允许被缓存
            canCached = true;
        }

        chain.request().tag()

        // --- 如果没有缓存，发起请求 ---
        Response response = chain.proceed(request);

        // response 是获得的结果。在这里可以将它存储到本地缓存
        // TODO save response to cache

        return null;
    }
}
