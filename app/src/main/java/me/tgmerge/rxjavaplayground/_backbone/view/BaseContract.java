package me.tgmerge.rxjavaplayground._backbone.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;

import rx.Subscription;

/**
 * Created by tgmerge on 11/20.
 */

public interface BaseContract {

    interface View {

        /** View 应该返回一个新建的自己的 Presenter */
        Presenter createPresenter();

        /** 获取 Presenter */
        Presenter getPresenter();

        /** View 应该显示代表“没有内容”的图示和提示文字 */
        void showEmptyView(int imgResId, @Nullable String text);

        /** View 应该隐藏代表“没有内容”的提示 */
        void hideEmptyView();

        /** View 应该显示代表“加载中”的提示 */
        void showLoadingView();

        /** View 应该隐藏“加载中”的提示*/
        void stopLoadingView();

        /** View 应该显示一条错误信息 */
        void showErrorMessage(String message);

        /** View 应该显示一条普通的 Toast 提示 */
        void showToast(String message);

        /** View 应该显示一条弹出的警告 */
        void showAlert(String title, String message, DialogInterface.OnClickListener onClick);

        /** View 应该弹出警告让用户选择 */
        void showChoice(String title, String message,
                        String positiveText, DialogInterface.OnClickListener positiveClick,
                        String negativeText, DialogInterface.OnClickListener negativeClick);
    }

    interface Presenter<V extends View> {

        /** 给 Presenter 指定关联的 View */
        void attachView(View view);

        /** 解除 Presenter 和 View 的关联 */
        void detachView();

        /** 检查这个 Presenter 是否关联到 View */
        boolean isViewAttached();

        /**
         * 获取这个 Presenter 关联的 View 的 Activity context
         * 仅供临时使用，不应该持有 context
         *
         * 使用前需要先检查 isViewAttached()
         */
        Context getContext();

        /**
         * 获取 Presenter 关联的 View
         * 仅供临时使用
         *
         * 使用前需要检查 isViewAttached() 确定 View 还存在
         */
        @Nullable
        V getView();

        /** 添加一个订阅到 Presenter 的 CompositeSubscription 中 */
        void addSubscription(Subscription subs);
    }
}
