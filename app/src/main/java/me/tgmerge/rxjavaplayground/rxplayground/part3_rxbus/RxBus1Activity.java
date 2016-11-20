package me.tgmerge.rxjavaplayground.rxplayground.part3_rxbus;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._backbone.bus.RxBus;

public class RxBus1Activity extends Activity {

    public static void start(Context context) {
        Intent starter = new Intent(context, RxBus1Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_bus1);

        if (savedInstanceState == null) {
            SubscriberFragment subFragment = SubscriberFragment.newInstance();

            getFragmentManager().beginTransaction()
                    .add(R.id.fl_subscriber, subFragment)
                    .commit();
        }
    }

    private int clickCount = 0;

    public void sendEventClicked(View view) {
        switch (clickCount) {
            case 0: RxBus.getDefault().post(new Event(0)); clickCount = 1; break;
            case 1: RxBus.getDefault().post(new Event(1)); clickCount = 2; break;
            case 2: RxBus.getDefault().post(new Event(-1)); clickCount = 0; break;
        }
    }
}
