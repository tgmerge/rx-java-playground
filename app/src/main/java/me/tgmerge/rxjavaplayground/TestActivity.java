package me.tgmerge.rxjavaplayground;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
}
