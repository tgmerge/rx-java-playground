package me.tgmerge.rxjavaplayground._backbone.view;

import android.app.Activity;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by tgmerge on 11/20.
 */

public class BasePresenter<V extends BaseContract.View> implements BaseContract.Presenter {

    private static final String TAG = BasePresenter.class.getSimpleName();

    private WeakReference<V> mViewRef;

    private CompositeSubscription mSubs;

    @Override
    @CallSuper
    public void attachView(BaseContract.View view) {
        mViewRef = new WeakReference<>((V) view);
        mSubs = new CompositeSubscription();
        Log.d(TAG, "[attachView] P:" + this.getClass().getSimpleName() + " --> V:" + view.getClass().getSimpleName());
    }

    @Override
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (mSubs != null) {
            mSubs.unsubscribe();
        }
        Log.d(TAG, "[detachView] P:" + this.getClass().getSimpleName() + " detached.");
    }

    @Override
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public Activity getContext() {
        if (getView() instanceof BaseActivity) {
            return (Activity) getView();
        }
        if (getView() instanceof BaseFragment) {
            return ((BaseFragment) getView()).getActivity();
        }
        // else:
        throw new IllegalStateException("Contract.View must either be BaseActivity or BaseFragment");
    }

    @Nullable
    @Override
    public V getView() {
        return mViewRef.get();
    }

    @Override
    public void addSubscription(Subscription subs) {
        if (mSubs != null) {
            mSubs.add(subs);
        }
    }
}
