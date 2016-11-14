package me.tgmerge.rxjavaplayground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;

public class RxTest1Activity extends Activity {

    private static final String TAG = RxTest1Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest1Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test1);

        // Code 1 - 创建 Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "Completed!");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "Error!");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: item: " + s);
            }
        };
        // ---

        // Code 2 - 创建 Subscriber
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted - subscriber");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError - subscriber");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext: item: " + s);
            }
        };
        // ---

        // Code 3 - 创建 Observable
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("Hi");
                subscriber.onCompleted();
            }
        });

        Observable observable2 = Observable.just("Hello2", "Hi2", "Hi2 - again");

        String[] words = {"Hello3", "Hi3", "Hi3 - again"};
        Observable observable3 = Observable.from(words);
        // ---

        // Code 4 - 订阅
        observable.subscribe(subscriber);

    }
}
