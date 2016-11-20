package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;

import android.util.Log;

import com.google.common.base.Joiner;

import java.util.List;

import me.tgmerge.rxjavaplayground._backbone.bus.RxBus;
import me.tgmerge.rxjavaplayground._backbone.bus.RxBusSubscriber;
import me.tgmerge.rxjavaplayground._backbone.view.BaseContract;
import me.tgmerge.rxjavaplayground._backbone.view.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by tgmerge on 11/20.
 */

class ClickedPresenter extends BasePresenter<ClickedContract.View> implements ClickedContract.Presenter {

    private static final String TAG = ClickedPresenter.class.getSimpleName();

    @Override
    public void attachView(BaseContract.View view) {
        super.attachView(view);
        subscribeClickedItemEvent();
    }

    private void subscribeClickedItemEvent() {

        // 将总线事件两个一组地显示出来
        Subscription subs = RxBus.getDefault().toObservable(ItemClickedEvent.class)
                .observeOn(Schedulers.computation())
                .map(new Func1<ItemClickedEvent, String>() {
                    @Override
                    public String call(ItemClickedEvent itemClickedEvent) {
                        return itemClickedEvent.clickedItem;
                    }
                })
                .buffer(2)
                .map(new Func1<List<String>, String>() {
                    @Override
                    public String call(List<String> strings) {
                        if (strings.size() == 2 && strings.get(0).equals(strings.get(1))) {
                            throw new IllegalStateException("故意抛出的异常: 两次点击相同");
                        }
                        return Joiner.on(", ").join(strings);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new RxBusSubscriber<String>() {
                    @Override
                    protected void onEvent(String s) {
                        getView().showClickedItem(s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        getView().showClickedItem("异常：" + e);
                        subscribeClickedItemEvent();
                    }
                });
        addSubscription(subs);
    }
}
