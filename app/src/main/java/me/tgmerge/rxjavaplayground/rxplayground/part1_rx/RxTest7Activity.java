package me.tgmerge.rxjavaplayground.rxplayground.part1_rx;

import android.app.Activity;
import android.os.Bundle;

import me.tgmerge.rxjavaplayground.R;
import rx.Observable;
import rx.Observer;

public class RxTest7Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test7);

        Observable.from()
    }
}
