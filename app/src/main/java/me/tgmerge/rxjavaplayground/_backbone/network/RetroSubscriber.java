package me.tgmerge.rxjavaplayground._backbone.network;

import android.util.Log;

import me.tgmerge.rxjavaplayground._backbone.util.Utils;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * 订阅网络请求返回事件。处理请求成功/失败需要实现 error / success 两个方法。
 * @param <T> 请求结果事件对象的类型(Model, etc)
 */
public abstract class RetroSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ApiException) {
            Log.e("RetroSubscriber", "onError: status=" + ((ApiException) e).status
                    + " msg=" + e.getMessage());
            error(e.getMessage(), e);
            return;
        }
        if (e instanceof HttpException) {
            Log.e("RetroSubscriber", "onError: " + e.toString());
            int httpCode = ((HttpException) e).code();
            if (httpCode == 404) {
                error("找不到相关数据", e);
            } else if (400 <= httpCode && httpCode < 500) {
                error("无法连接网络……", e);
            } else if (500 <= httpCode && httpCode < 600) {
                error("服务器出现问题……", e);
            } else {
                error(e.toString(), e);
            }
            return;
        }
        // Else:
        if (Utils.isDebug()) {
            e.printStackTrace();
        }
        Log.e("RetroSubscriber", "onError: " + e.toString());
        error("无法访问网络，请检查网络连接", e);
    }

    @Override
    public void onNext(T response) {
        success(response);
    }

    public abstract void error(String msg, Throwable e);

    public abstract void success(T body);
}
