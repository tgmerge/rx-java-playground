package me.tgmerge.rxjavaplayground.rxplayground.part4_retrofit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._api.MockyAPI;
import me.tgmerge.rxjavaplayground._backbone.network.RetroSubscriber;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

public class RxRetro3Activity extends Activity {

    private static final String TAG = RxRetro3Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxRetro3Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retro3);


        // TODO Code 23 试验请求结果为空的情况；试验请求错误的情况
        // TODO 多个请求连续发起的情况；多个请求同时发起均返回后执行的情况；
    }

    public void requestOneString(View view) {

        // Code 22 - 使用 Rx + Retrofit 进行封装好的网络请求
        MockyAPI.getOneString().subscribe(new RetroSubscriber<String>() {
            @Override
            public void error(String msg, Throwable e) {
                Log.d(TAG, "Code 22 - error: msg=" + msg + " e=" + e.toString());
            }

            @Override
            public void success(String result) {
                Log.d(TAG, "Code 22 - success: result=" + result);
            }
        });

    }

    public void requestStatus404(View view) {

        // Code 23 - 发起一个 status = 404 的请求
        MockyAPI.getStatus404().subscribe(new RetroSubscriber<String>() {
            @Override
            public void error(String msg, Throwable e) {
                Log.d(TAG, "Code 23 - error: msg=" + msg + " e=" + e.toString());
            }

            @Override
            public void success(String result) {
                Log.d(TAG, "Code 23 - success: result=" + result);
            }
        });

    }

    public void requestResultNull(View view) {

        // Code 24 - 发起一个 result = null 的请求
        MockyAPI.getNullResult().subscribe(new RetroSubscriber<String>() {
            @Override
            public void error(String msg, Throwable e) {
                Log.d(TAG, "Code 24 - error: msg=" + msg + " e=" + e.toString());
            }

            @Override
            public void success(String result) {
                Log.d(TAG, "Code 24 - success: result=" + result);
            }
        });
    }

    public void requestTwoWaitForAll(View view) {

        // Code 25 - 同时发起多个请求并等待它们完成
        // todo 注意：combineLatest Func2 的执行线程是 “它所合并的两个Observable中较晚发出事件的那个所在的线程”。
        // 也就是说，像下面这样写的话，
        // 如果 getOneString() 的返回较慢，Func2 就会执行在 NewThread 上
        // 如果 getNullResult() 的返回较慢，Func2 就会执行在 IoThread 上
        Observable
                .combineLatest(
                        MockyAPI.getOneString().observeOn(Schedulers.newThread()),
                        MockyAPI.getNullResult().observeOn(Schedulers.io()),
                        new Func2<String, String, String>() {
                    @Override
                    public String call(String s, String s2) {
                        Log.i(TAG, "call: combiner - thread=" + Thread.currentThread().getName());
                        return "resultA=" + s + ", resultB=" + s2;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetroSubscriber<String>() {
                    @Override
                    public void error(String msg, Throwable e) {
                        Log.d(TAG, "Code 25 - error: msg=" + msg + " e=" + e.toString());
                    }

                    @Override
                    public void success(String body) {
                        Log.d(TAG, "Code 24 - success: result=" + body + ", thread=" + Thread.currentThread().getName());
                    }
                });
    }
}
