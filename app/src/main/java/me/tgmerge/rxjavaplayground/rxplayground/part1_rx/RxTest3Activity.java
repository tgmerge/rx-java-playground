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

public class RxTest3Activity extends Activity {

    private static final String TAG = RxTest3Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest3Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test3);

        // Code 8 - 线程控制
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())  // <-- subscribe() 将发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())  // <-- onNext() 将发生在主线程
                .doOnSubscribe(new Action0() {  // <-- doOnSubscribe() 和 subscribe() 发生在同一个线程，所以下面这个 Log.d 将输出 main 线程
                    @Override
                    public void call() {
                        Log.d(TAG, "Code 8 - doOnSubscribe thread: " + Thread.currentThread().getName());
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        if (integer == 4) {
                            throw new RuntimeException("Hey, integer is 4 which is bad");
                        }
                        Log.d(TAG, "Code 8 - onNext: " + integer + " thread: " + Thread.currentThread().getName());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.d(TAG, "Code 8 - onError: " + throwable + " thread: " + Thread.currentThread().getName());
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Log.d(TAG, "Code 8 - onCompleted thread: " + Thread.currentThread().getName());
                    }
                });

        // Code 9 - 线程控制
        Observable
                .create(new Observable.OnSubscribe<Integer>() {  // <-- 和上面的 doOnSubscribe() 不同，这里的 OnSubscribe.call() 就会发生在 IO 线程了
                    @Override
                    public void call(Subscriber<? super Integer> subscriber) {
                        Log.d(TAG, "Code 9 - onSubscribe thread: " + Thread.currentThread().getName());
                        subscriber.onNext(1);
                        subscriber.onNext(2);
                        subscriber.onNext(3);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "Code 9 - onNext: " + integer + " thread: " + Thread.currentThread().getName());
                    }
                });
    }
}
