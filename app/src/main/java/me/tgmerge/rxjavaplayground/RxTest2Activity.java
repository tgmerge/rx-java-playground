package me.tgmerge.rxjavaplayground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action1;

public class RxTest2Activity extends Activity {

    private static final String TAG = RxTest2Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest2Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test2);

        // Code 6 - 依次打印字符串数组的字符串
        String[] names = {"Andy", "Brian", "Tsukimi"};
        Observable.from(names)
                .subscribe(new Action1<String>() {

                    @Override
                    public void call(String s) {
                        Log.d(TAG, "Code 6 - onNext: " + s);
                    }
                });

        // Code 7 - 从ID取得图片并显示
        final int drawableRes = R.mipmap.ic_launcher;
        // final int drawableRes = R.mipmap.ic_launcher + 1; // <-- 解除这一行的注释，触发onError
        final ImageView imageView = (ImageView) findViewById(R.id.activity_rx_test2_image_view);
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getTheme().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribe(new Observer<Drawable>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "Code 7 - onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(RxTest2Activity.this, "Code 7 - onError", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNext(Drawable drawable) {
                imageView.setImageDrawable(drawable);
            }
        });
    }
}
