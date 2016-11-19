package me.tgmerge.rxjavaplayground.rxplayground.part3_rxbus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * from: https://github.com/YoKeyword/RxBus/blob/c739257c7fe637d820c98280e410dc36982affc6/rxbus/src/main/java/me/yokeyword/rxbus/RxBus.java
 *
 * Code 19 - RxJava 作为事件总线
 */

public class RxBus {
    private static volatile RxBus mDefaultInstance;
    private final Subject<Object, Object> mBus;

    private final Map<Class<?>, Object> mStickyEventMap;

    public RxBus() {
        mBus = new SerializedSubject<>(PublishSubject.create());
        mStickyEventMap = new ConcurrentHashMap<>();
    }

    public static RxBus getDefault() {
        if (mDefaultInstance == null) {
            synchronized (RxBus.class) {
                if (mDefaultInstance == null) {
                    mDefaultInstance = new RxBus();
                }
            }
        }
        return mDefaultInstance;
    }

    /**
     * 发送事件
     */
    public void post(Object event) {
        mBus.onNext(event);
    }

    /**
     * 根据 eventType 的类型，返回特定类型的 Observable
     * [Observable<Object>] --ofType(XXX)--> [Observable<XXXType>]
     */
    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mBus.ofType(eventType);
    }

    /**
     * 是否有订阅者
     */
    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    public void reset() {
        mDefaultInstance = null;
    }

    // Sticky 相关
    // Code 20 - RxBus 对 sticky 事件的处理

    /**
     * 发送一个新 sticky 事件
     * 将最后一个事件存储在 mStickyEventMap 中
     */
    public void postSticky(Object event) {
        synchronized (mStickyEventMap) {
            mStickyEventMap.put(event.getClass(), event);
        }
        post(event);
    }

    /**
     * 根据传递的 eventType 类型，返回特定类型的 Observer
     * 并直接发送相同类型的最后一个 sticky 事件
     */
    public <T> Observable<T> toObservableSticky(final Class<T> eventType) {
        synchronized (mStickyEventMap) {
            Observable<T> observable = mBus.ofType(eventType);
            final Object event = mStickyEventMap.get(eventType);

            if (event != null) {
                return observable.mergeWith(Observable.create(new Observable.OnSubscribe<T>() {

                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        subscriber.onNext(eventType.cast(event));  // 直接发送最后一个 sticky 事件
                    }
                }));
            } else {
                return observable;  // 没有类型符合的 sticky 事件，直接返回和 toObservable 一样的 observable
            }
        }
    }

    /**
     * 根据 eventType 获取 sticky 事件
     */
    public <T> T getStickyEvent(Class<T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.get(eventType));
        }
    }

    /**
     * 移除指定 eventType 的 sticky 事件
     */
    public <T> T removeStickyEvent(Class <T> eventType) {
        synchronized (mStickyEventMap) {
            return eventType.cast(mStickyEventMap.remove(eventType));
        }
    }

    /**
     * 移除所有 sticky 事件
     */
    public void removeAllStickyEvents() {
        synchronized (mStickyEventMap) {
            mStickyEventMap.clear();
        }
    }
}
