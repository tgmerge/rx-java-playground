package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;

import me.tgmerge.rxjavaplayground._backbone.view.BaseContract;

/**
 * Created by tgmerge on 11/20.
 */

interface ClickedContract extends BaseContract {

    interface View extends BaseContract.View {
        void showClickedItem(String item);
    }

    interface Presenter extends BaseContract.Presenter {

    }
}
