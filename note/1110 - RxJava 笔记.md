# RxJava 笔记

阅读内容： <http://gank.io/post/560e15be2dca930e00da1083>

## 概念：扩展的观察者模式

RxJava 是一个观察者模式的工具库。

RxJava 中的基本概念：

1. Observable - 被观察者
2. Observer - 观察者
3. subscribe - 订阅
4. 事件

RxJava 的事件回调，最基本的事件是`onNext()`，它相当于`onClick()`和`onEvent()`之类。

特殊的事件：表示事件队列完结的`onCompleted()`和表示事件队列异常的`onError()`，正确运行的事件队列中，他们有且仅有一个（不是各有一个），且是事件序列中的最后一个。

## 基本实现

创建`Observer`（观察者） - Code 1

创建`Subscriber` - Code 2

`Subscriber`扩展了`Observer`，实际使用 subscribe 时，`Observer`会被转换成`Subscriber`。但`Subscriber`增加了

1. `onStart()`方法：可选，总在 subscribe 发生的线程被调用，如果需要特定线程可以用`doOnSubscribe()`；
2. `unsubscribe()`方法：取消订阅，不再接收事件。subscribe 之后，`Observable`会持有`Subscriber`的引用，不需要时应当尽快 unsubscribe 防止内存泄漏。

创建`Observable`（被观察者） - Code 3

`Observable.OnSubscribe` - 当 `Observable` 被订阅的时候将被调用的方法（这个对象的`call(...)`方法）。

订阅 - Code 4

订阅是使用 `observable.subscribe(subscriber)`，看起来是“给被观察者设置了一个订阅者”。

`subscribe()`大致上会执行这几个操作：

```java
    public Subscription subscribe(Subscriber subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);    // 这个 onSubscribe 是 observable 的
        return subscriber;
    }
```

使用回调方法创建 Subscriber - Code 5

使用`observable.subscribe(Action1 onNext, Action1 onError, Action0 onCompleted)`方法，可以无需显示地创建 Subscriber，而让 Observable 根据相应的动作隐式地自行创建 Subscriber。

## 场景示例

依次打印字符串数组中的字符串 - Code 6

使用`Observable.from(T[] array)`。

`Observable.from()`可以从一个数组创建 Observable，依次发送它们。

从 ID 取得图片并显示出来 - Code 7

使用`Observable.create( new OnSubscribe<T> ).subscribe( new Observer<T> )`。

## 线程控制

Code 8, Code 9 - 线程控制

不指定线程的时候，会在调用`subscribe()`的线程中生产事件和消费事件。

如果需要切换线程，需要使用`Scheduler`。RxJava 内置的 Scheduler：

* `Schedulers.immediate()`：在当前线程运行，相当于不指定线程（默认值）。
* `Schedulers.newThread()`：总是在新的线程中执行操作。
* `Schedulers.io()`：使用一个无上限的线程池进行操作，适用于读写文件/数据库和网络请求。
* `Schedulers.computation()`：使用一个根据 CPU 核心数调节容量的线程池操作，进行 CPU 密集型计算。
* `AndroidSchedulers.mainThread()`：在 Android 的主线程（UI线程）进行操作。

可以用`subscribeOn()`和`observeOn()`两个方法对线程进行控制。

* `subscribeOn()`：指定`subscribe()`发生的线程，也就是`Observable.OnSubscribe`被激活时所在的线程（事件产生的线程）。
* `observeOn()`：指定`Subscriber`所处的线程（事件消费的线程）。

`subscribeOn(Scheduler.io())`和`observeOn(AndroidSchedulers.mainThread())`的组合非常适用于从 **后台线程获取数据，在主线程显示数据** 的情景。

## 变换

