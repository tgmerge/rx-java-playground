package me.tgmerge.rxjavaplayground;

import android.app.Application;

import me.tgmerge.rxjavaplayground._api.JsonAPI;
import me.tgmerge.rxjavaplayground._api.ToStringConverterFactory;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com")
                .addConverterFactory(new ToStringConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        JsonAPI.init(retrofit);
    }
}
