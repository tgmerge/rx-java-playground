package me.tgmerge.rxjavaplayground.rxplayground.part3_rxbus;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.tgmerge.rxjavaplayground.R;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;

public class SubscriberFragment extends Fragment {

    private static final String TAG = SubscriberFragment.class.getSimpleName();

    // 这个 CompositeSubscription 应该由 Presenter 持有
    private CompositeSubscription mSubscriptions;

    public SubscriberFragment() {
        // 按说应该在 Presenter 里初始化这个 CompositeSubscription
        mSubscriptions = new CompositeSubscription();
    }

    public static SubscriberFragment newInstance() {
        return new SubscriberFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscriber, container, false);

        subscribeEvent();

        return view;
    }

    private void subscribeEvent() {
        Subscription subs = RxBus.getDefault().toObservable(Event.class)
                .map(new Func1<Event, String>() {
                    @Override
                    public String call(Event event) {
                        Log.d(TAG, "map1: event value=" + event.value);
                        if (event.value == -1) {
                            Log.d(TAG, "map1: throwing an error value=" + event.value);
                            throw new IllegalStateException("故意抛出的异常");
                        }
                        return "已经处理过叻" + event.value;
                    }
                })
                .subscribe(new RxBusSubscriber<String>() {
                    @Override
                    protected void onEvent(String s) {
                        Log.d(TAG, "onEvent: 接收事件: " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // Code 21 - RxBus 异常时重新订阅
                        subscribeEvent();  // 由于一旦处理过程中抛出异常 就会跳到onError来
                                           // 事件流就中断了，需要重新订阅
                                           // 但 sticky 事件不能重新订阅（会反复发送最后一个事件导致死循环）
                                           // todo sticky 事件或许可以在出错时清除同类型事件，然后重新订阅？
                    }
                });
        mSubscriptions.add(subs);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // 在 Presenter 脱离 View 的时候，执行 clear()
        mSubscriptions.clear();
    }
}
