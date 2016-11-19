package me.tgmerge.rxjavaplayground.rxplayground.part2_retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._api.JsonAPI;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxRetro2Activity extends Activity {

    private static final String TAG = RxRetro2Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxRetro2Activity.class);
        context.startActivity(starter);
    }

    private static String currentThread() {
        return " | Thread: " + Thread.currentThread().getName();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retro2);

        // Code 18 - 按顺序依次发起两个请求
        /**
         * 首先发起一个 /posts 请求，用 "请求返回字符串长度 % 10 + 1" 作为 id，
         * 发起一个 /posts/{id} 请求。
         */
        JsonAPI.getPosts()
                .subscribeOn(Schedulers.io())  // <-- 上面的请求会在 IO 线程处理
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Code 18 - doOnSubscribe - 发起请求，初始化 UI" + currentThread());
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())  // <-- 上面的 doOnSubscribe 会在主线程处理
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        int id = s.length() % 10 + 1;
                        Log.d(TAG, "Code 18 - flatMap - 第一个请求的返回长度: " + s.length());
                        Log.d(TAG, "Code 18 - flatMap - 第二个请求的 id: " + id + currentThread());
                        return JsonAPI.getPost(id);
                                // .subscribeOn(AndroidSchedulers.mainThread());  // <-- 这里也可以调度第二个请求发起的线程
                                                                                  //     比如，这样就会让第二个请求在主线程发起
                                                                                  //     从而出错（android.os.NetworkOnMainThreadException）
                    }
                })
                .observeOn(Schedulers.computation())  // <-- 下面这个 map 会在 computation 线程处理
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "Code 18 - map" + currentThread());
                        return s + "请求已经处理过叻";
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())  // <-- 下面这个 subscribe 会在主线程处理事件
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Code 18 - onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "Code 18 - onError: " + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "Code 18 - onNext: " + s + currentThread());
                        // throw new NullPointerException("aha");  // <-- 在整个链式调用的任意位置抛出异常都会导致
                                                                   //     跳转到 onError，可以统一处理
                    }
                });

    }
}
