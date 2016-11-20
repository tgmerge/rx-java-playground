package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.tgmerge.rxjavaplayground._api.MockyAPI;
import me.tgmerge.rxjavaplayground._backbone.bus.RxBus;
import me.tgmerge.rxjavaplayground._backbone.network.RetroSubscriber;
import me.tgmerge.rxjavaplayground._backbone.view.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tgmerge on 11/20.
 */

class ListPresenter extends BasePresenter<ListContract.View> implements ListContract.Presenter {

    private static final String TAG = ListPresenter.class.getSimpleName();

    @Override
    public void requestItems() {
        Subscription helloSubs = MockyAPI.getOneString()
                .observeOn(Schedulers.io())
                .delay(5, TimeUnit.SECONDS)  // 请求延时 5 秒
                .observeOn(Schedulers.computation())
                .map(new Func1<String, List<String>>() {  // 将请求结果按空格分割成 List<String>
                    @Override
                    public List<String> call(String s) {
                        return Arrays.asList(s.split(" "));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RetroSubscriber<List<String>>() {  // 并显示出来
                    @Override
                    public void error(String msg, Throwable e) {
                        getView().showErrorMessage("Error: " + msg);
                    }

                    @Override
                    public void success(List<String> body) {
                        getView().showItems(body);
                    }
                });
        addSubscription(helloSubs); // 这里会让请求在 detachView 的时候解除订阅
                                    // 不用管 getView() 是否为空的问题
    }

    @Override
    public void sendListItem(String item) {
        RxBus.getDefault().post(new ItemClickedEvent(item));
        Log.i(TAG, "sendListItem: item=" + item);
    }
}
