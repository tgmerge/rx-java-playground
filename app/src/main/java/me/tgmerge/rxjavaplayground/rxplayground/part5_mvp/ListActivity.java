package me.tgmerge.rxjavaplayground.rxplayground.part5_mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import me.tgmerge.rxjavaplayground.R;
import me.tgmerge.rxjavaplayground._backbone.view.BaseActivity;

/**
 * Code 26
 * 使用 _backbone 的 MVP 结构
 * 编写的一个 Activity 和 Fragment 通过 RxJava + Retrofit 加载网络请求，
 * Activity 通过 RxBus 发送总线事件，并在 Fragment 中监听事件的实例。
 */
public class ListActivity extends BaseActivity<ListContract.Presenter> implements ListContract.View {

    private static final String TAG = ListActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, ListActivity.class);
        context.startActivity(starter);
    }

    ListView lvList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        lvList = (ListView) findViewById(R.id.lv_list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                getPresenter().sendListItem(adapter.getItem(position));
            }
        });

        if (savedInstanceState == null) {
            ClickedFragment fragment = ClickedFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_clicked, fragment)
                    .commit();
        }

        getPresenter().requestItems();
    }

    @Override
    public ListContract.Presenter createPresenter() {
        return new ListPresenter();
    }

    @Override
    public void showItems(List<String> msgs) {
        adapter.clear();
        adapter.addAll(msgs);
        adapter.notifyDataSetChanged();
    }
}
