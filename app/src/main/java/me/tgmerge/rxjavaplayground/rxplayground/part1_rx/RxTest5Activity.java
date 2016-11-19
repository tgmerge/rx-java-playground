package me.tgmerge.rxjavaplayground.rxplayground.part1_rx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.tgmerge.rxjavaplayground.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxTest5Activity extends Activity {

    private static final String TAG = RxTest5Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest5Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test5);

        // Code 14 - 多次切换线程
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation())  // <-- 下面这个 map 会发生在 computation 线程
                .map(new Func1<Integer, String>() {
                    @Override
                    public String call(Integer integer) {
                        Log.d(TAG, "Code 14 - map1: thread " + Thread.currentThread().getName());
                        return "map1 " + integer;
                    }
                }).observeOn(Schedulers.io())  // <-- 下面这个 map 会发生在 io 线程
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "Code 14 - map2: thread " + Thread.currentThread().getName());
                        return "map2 " + s;
                    }
                }).observeOn(AndroidSchedulers.mainThread())  // <-- 下面这个 Subscriber 的 onNext 会发生在 Android 主线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "Code 14 - onNext: thread " + Thread.currentThread().getName());
                        Log.d(TAG, "Code 14 - onNext: " + s);
                    }
                });

        // Code 15 - 混合的 subscribeOn() 和 observeOn()
        Observable.just("a", "b", "c")
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "Code 15 - map1: thread " + Thread.currentThread().getName());
                        return "map1 " + s;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "Code 15 - map2: thread " + Thread.currentThread().getName());
                        return "map2 " + s;
                    }
                })
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(String s) {
                        Log.d(TAG, "Code 15 - flatMap: thread " + Thread.currentThread().getName());
                        return Observable.just(s + ".1 flat!", s + ".2 flat!", s + ".3 flat!");
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "Code 15 - onNext: thread " + Thread.currentThread().getName());
                        Log.d(TAG, "Code 15 - onNext: " + s);
                    }
                });
    }
}
