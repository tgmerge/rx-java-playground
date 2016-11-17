package me.tgmerge.rxjavaplayground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.tgmerge.rxjavaplayground.part1_rx.RxTest1Activity;
import me.tgmerge.rxjavaplayground.part1_rx.RxTest2Activity;
import me.tgmerge.rxjavaplayground.part1_rx.RxTest3Activity;
import me.tgmerge.rxjavaplayground.part1_rx.RxTest4Activity;
import me.tgmerge.rxjavaplayground.part1_rx.RxTest5Activity;
import me.tgmerge.rxjavaplayground.part1_rx.RxTest6Activity;

public class TestActivity extends Activity {

    public static void start(Context context) {
        Intent starter = new Intent(context, TestActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void openRxTest1Activity(View view) {
        RxTest1Activity.start(this);
    }

    public void openRxTest2Activity(View view) {
        RxTest2Activity.start(this);
    }

    public void openRxTest3Activity(View view) {
        RxTest3Activity.start(this);
    }

    public void openRxTest4Activity(View view) {
        RxTest4Activity.start(this);
    }

    public void openRxTest5Activity(View view) {
        RxTest5Activity.start(this);
    }

    public void openRxTest6Activity(View view) {
        RxTest6Activity.start(this);
    }
}
