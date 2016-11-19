
## RxJava 的适用场景

### 与 Retrofit 的结合

[Code 17 - RxJava + Retrofit 进行网络请求](https://github.com/tgmerge/rx-java-playground/blob/8f8f79766f7ad731529fe2bb15348b76250a4632/app/src/main/java/me/tgmerge/rxjavaplayground/part2_retrofit/RxRetro1Activity.java#L31)

![](/assets/0222-05.png)

这个示例模拟了“在网络请求发起时更新UI -> 处理请求结果 -> 根据请求结果再次更新UI”的过程。

使用了[JSONPlaceholder](https://jsonplaceholder.typicode.com) API。

* **在发起请求时更新UI** 在`subscribeOn()`中完成；
* **处理请求结果** 在`map()`中完成；
* **根据处理后的结果更新UI** 在`subscribe()`中完成。

期间根据需求调度线程。

[Code 18 - RxJava + Retrofit 按顺序依次发起两个请求](https://github.com/tgmerge/rx-java-playground/blob/8f8f79766f7ad731529fe2bb15348b76250a4632/app/src/main/java/me/tgmerge/rxjavaplayground/part2_retrofit/RxRetro2Activity.java#L36)

![](/assets/0222-06.png)

这个示例模拟了“更新 UI -> 发起请求 A -> 利用请求 A 的结果发起请求 B -> 处理请求 B 的结果 -> 根据请求 B 的结果再次更新 UI”的过程。

具体来说，它会发起一个 JSONPlaceholder 的`/posts`请求，用这个请求`返回字符串的长度 % 10 + 1`作为`id`，发起`/post/{id}`请求。

实际应用中类似的情景是，先发起一个请求获取 token，然后使用这个 token 发起其他请求。

RxJava 有一点好，就是在链式声明的任意位置抛出的异常都会被 Subscriber 的`onError()`接到，可以统一处理。

### RxBus 作为事件总线

