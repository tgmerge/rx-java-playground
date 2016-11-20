package me.tgmerge.rxjavaplayground._backbone.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Created by tgmerge on 11/20.
 */

public abstract class BaseActivity<T extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected T mPresenter;

    private View mEmptyView;
    private View mLoadingView;

    @Override
    @CallSuper
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    public abstract T createPresenter();

    public T getPresenter() {
        return mPresenter;
    }

    @Override
    public void showEmptyView(int imgResId, @Nullable String text) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void hideEmptyView() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void showLoadingView() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void stopLoadingView() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void showErrorMessage(String message) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "[==> showToast]" + message);
    }

    @Override
    public void showAlert(String title, String message, DialogInterface.OnClickListener onClick) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void showChoice(String title, String message, String positiveText, DialogInterface.OnClickListener positiveClick, String negativeText, DialogInterface.OnClickListener negativeClick) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
