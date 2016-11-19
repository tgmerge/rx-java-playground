package me.tgmerge.rxjavaplayground;

import android.app.Application;

import me.tgmerge.rxjavaplayground._api.JsonAPI;
import me.tgmerge.rxjavaplayground._api.MockyAPI;
import me.tgmerge.rxjavaplayground._backbone.network.APIUtils;
import me.tgmerge.rxjavaplayground._backbone.network.ToStringConverterFactory;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by tgmerge on 11/17.
 * me.tgmerge.rxjavaplayground
 */

public class PlaygroundApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 Retrofit (仅为 part2 使用）
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(new ToStringConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        JsonAPI.init(retrofit);

        // 初始化 Retrofit 和 API
        APIUtils.RetrofitHolder.init();
        MockyAPI.init();
    }
}
