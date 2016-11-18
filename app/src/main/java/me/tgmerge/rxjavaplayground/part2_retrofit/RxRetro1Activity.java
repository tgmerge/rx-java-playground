package me.tgmerge.rxjavaplayground.part2_retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._api.JsonAPI;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxRetro1Activity extends Activity {

    private static final String TAG = RxRetro1Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxRetro1Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retro1);

        // Code 17 - RxJava 与 Retrofit 的结合
        JsonAPI.getPost(1)
                .subscribeOn(Schedulers.io())  // <-- 上面这个网络请求会在 IO 线程
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Code 17 - doOnSubscribe: loading... thread=" + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())  // <-- 上面这个 doOnSubscribe 会在主线程
                .observeOn(Schedulers.computation()) // <-- 下面这个 map 会在 computation 线程
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "Code 17 - map: processing response... thread=" + Thread.currentThread().getName());
                        return s + "已经处理过叻";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  // <-- 下面这个 subscriber 会在主线程
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Code 17 - onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Code 17 - onError error: " + e);
                    }

                    @Override
                    public void onNext(String response) {
                        Log.d(TAG, "Code 17 - onNext thread=" + Thread.currentThread().getName());
                        Log.d(TAG, "Code 17 - onNext response=" + response);
                    }
                });
    }
}
