package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._backbone.view.BaseFragment;

public class ClickedFragment extends BaseFragment<ClickedContract.Presenter> implements ClickedContract.View {

    public ClickedFragment() {
    }

    public static ClickedFragment newInstance() {
        ClickedFragment fragment = new ClickedFragment();
        return fragment;
    }

    TextView tvClicked;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_clicked, container, false);
        tvClicked = (TextView) rootView.findViewById(R.id.tv_clicked_item);
        return rootView;
    }

    @Override
    public ClickedContract.Presenter createPresenter() {
        return new ClickedPresenter();
    }

    @Override
    public ClickedContract.Presenter getPresenter() {
        return new ClickedPresenter();
    }

    @Override
    public void showClickedItem(String item) {
        tvClicked.setText(item);
    }
}
