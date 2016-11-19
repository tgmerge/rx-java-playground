package me.tgmerge.rxjavaplayground.part3_rxbus;

import rx.Subscriber;

/**
 * from: https://github.com/YoKeyword/RxBus/blob/c739257c7fe637d820c98280e410dc36982affc6/rxbus/src/main/java/me/yokeyword/rxbus/RxBusSubscriber.java
 *
 * RxBus 使用的 Subscriber。
 */
public abstract class RxBusSubscriber<T> extends Subscriber<T> {
    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        try {
            onEvent(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onEvent(T t);
}
