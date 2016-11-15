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

    Code 1 - 创建 Observer（观察者）

<!-- -->

    Code 2 - 创建 Subscriber

`Subscriber`扩展了`Observer`，实际使用 subscribe 时，`Observer`会被转换成`Subscriber`。但`Subscriber`增加了

1. `onStart()`方法：可选，总在 subscribe 发生的线程被调用，如果需要特定线程可以用`doOnSubscribe()`；
2. `unsubscribe()`方法：取消订阅，不再接收事件。subscribe 之后，`Observable`会持有`Subscriber`的引用，不需要时应当尽快 unsubscribe 防止内存泄漏。

    Code 3 - 创建 Observable（被观察者）

`Observable.OnSubscribe` - 当 `Observable` 被订阅的时候将被调用的方法（这个对象的`call(...)`方法）。

    Code 4 - 订阅

订阅是使用 `observable.subscribe(subscriber)`，看起来是“给被观察者设置了一个订阅者”。

`subscribe()`大致上会执行这几个操作：

```java
    public Subscription subscribe(Subscriber subscriber) {
        subscriber.onStart();
        onSubscribe.call(subscriber);    // 这个 onSubscribe 是 observable 的
        return subscriber;
    }
```

    Code 5 - 使用回调方法创建 Subscriber

使用`observable.subscribe(Action1 onNext, Action1 onError, Action0 onCompleted)`方法，可以无需显示地创建 Subscriber，而让 Observable 根据相应的动作隐式地自行创建 Subscriber。

## 场景示例

    Code 6 - 依次打印字符串数组中的字符串

使用`Observable.from(T[] array)`。

`Observable.from()`可以从一个数组创建 Observable，依次发送它们。

    Code 7 - 从 ID 取得图片并显示出来

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

## map() 变换

RxJava 提供了 **将事件序列中的对象或者整个序列进行处理，转换成不同的事件或事件序列** 的特性，即变换。

    Code 10 - map() 变换

`map()`使用`Func1`作为参数，直接变换了事件对象。

## flatMap() 变换

    Code 11 - flatMap() 变换

![](img01.png)

```java
    // Code 11 - flatMap() 变换
    List<Student> students = Student.makeList();
    Observable.from(students)  // <-- 这个是“原始 Observable”
            .flatMap(new Func1<Student, Observable<String>>() {  // <-- 这个 Func1 接收每个发送出来的 Student，...
                @Override
                public Observable<String> call(Student student) {
                    return Observable.from(student.courses);  // <-- ...并用它创建一个新的 Observable，同时激活它。
                                                              //     创建出来的 Observable 发送的事件都被汇总，然后
                                                              //     交到原始 Observable 的 Subscriber 那里去。

                    // return Observable.just("Hi,A", "Hi,B");  // 为了加强理解，可以不用原始的student，
                                                                // 随便创建一个Observable
                }
            })
            .subscribe(new Action1<String>() {
                @Override
                public void call(String o) {
                    Log.d(TAG, "Code 11 - onNext: " + o);
                }
            });
```

将会输出：

```
D/RxTest4Activity: Code 11 - onNext: Student's name 0 - course 0
D/RxTest4Activity: Code 11 - onNext: Student's name 0 - course 1
D/RxTest4Activity: Code 11 - onNext: Student's name 0 - course 2
D/RxTest4Activity: Code 11 - onNext: Student's name 0 - course 3
D/RxTest4Activity: Code 11 - onNext: Student's name 0 - course 4
D/RxTest4Activity: Code 11 - onNext: Student's name 1 - course 0
D/RxTest4Activity: Code 11 - onNext: Student's name 1 - course 1
D/RxTest4Activity: Code 11 - onNext: Student's name 1 - course 2
D/RxTest4Activity: Code 11 - onNext: Student's name 1 - course 3
D/RxTest4Activity: Code 11 - onNext: Student's name 1 - course 4
D/RxTest4Activity: Code 11 - onNext: Student's name 2 - course 0
D/RxTest4Activity: Code 11 - onNext: Student's name 2 - course 1
D/RxTest4Activity: Code 11 - onNext: Student's name 2 - course 2
D/RxTest4Activity: Code 11 - onNext: Student's name 2 - course 3
D/RxTest4Activity: Code 11 - onNext: Student's name 2 - course 4
D/RxTest4Activity: Code 11 - onNext: Student's name 3 - course 0
D/RxTest4Activity: Code 11 - onNext: Student's name 3 - course 1
D/RxTest4Activity: Code 11 - onNext: Student's name 3 - course 2
D/RxTest4Activity: Code 11 - onNext: Student's name 3 - course 3
D/RxTest4Activity: Code 11 - onNext: Student's name 3 - course 4
D/RxTest4Activity: Code 11 - onNext: Student's name 4 - course 0
D/RxTest4Activity: Code 11 - onNext: Student's name 4 - course 1
D/RxTest4Activity: Code 11 - onNext: Student's name 4 - course 2
D/RxTest4Activity: Code 11 - onNext: Student's name 4 - course 3
D/RxTest4Activity: Code 11 - onNext: Student's name 4 - course 4
```

`flatMap()`相当于用`Func1`将 **每个** 事件参数对象转换成了一个能发射另一些参数对象的 Observable，并集中汇总它们发射出来的对象。作为结果，每个原来的事件参数对象都被“展平”成了一系列另一种类型的参数对象，传递给原本的 Subscriber 那里。

## throttleFirst() 变换

    Code 12 - throttleFirst() 变换

这个变换可以忽略 **每次成功触发事件后一定时间内的其他事件**，也就是类似事件去抖动的机制。

## 变换的原理：lift()

简单来说，`lift()`使用了类似代理模式的方法，使用`Operator`创建一个新的`Observable`包裹原来的`Observable`，负责接收原来的`Observable`发出的事件，并在事件处理后发送给`Subscriber`。

另外，RxJava 并不建议开发者自定义`Operator`直接使用`lift()`，而是尽量使用已有包装方法的组合来完成需求。

## 对 Observable 的变换：compose()

`compose()`针对`Observable`自身进行变换。

    Code 13 - compose() 变换

