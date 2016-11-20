package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;

import java.util.List;

import me.tgmerge.rxjavaplayground._backbone.view.BaseContract;

/**
 * Created by tgmerge on 11/20.
 */
interface ListContract extends BaseContract {

    interface View extends BaseContract.View {
        void showItems(List<String> msgs);
    }

    interface Presenter extends BaseContract.Presenter {
        void requestItems();
        void sendListItem(String item);
    }
}
