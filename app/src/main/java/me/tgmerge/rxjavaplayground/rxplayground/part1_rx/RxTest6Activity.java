package me.tgmerge.rxjavaplayground.rxplayground.part1_rx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import me.tgmerge.rxjavaplayground.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RxTest6Activity extends Activity {

    private static final String TAG = RxTest6Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest6Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test6);

        // Code 16 - doOnSubscribe() 的线程
        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        Log.d(TAG, "Code 16 - Observable.onSubscribe thread: " + Thread.currentThread().getName());
                        subscriber.onNext("1");
                        subscriber.onNext("2");
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())  // <-- 这个 subscribeOn() 让之后的操作全部回到了主线程
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Code 16 - doOnSubscribe thread: " + Thread.currentThread().getName());
                    }
                })
                .subscribeOn(Schedulers.io())  // <-- 这个 subscribeOn() 会影响上面的 doOnSubscribe 的线程
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "Code 16 - onNext: " + s + " thread: " + Thread.currentThread().getName());
                    }
                });
    }
}
