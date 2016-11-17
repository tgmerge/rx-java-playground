package me.tgmerge.rxjavaplayground.part1_rx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.tgmerge.rxjavaplayground.R;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class RxTest4Activity extends Activity {

    private static final String TAG = RxTest4Activity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, RxTest4Activity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_test4);

        // Code 10 - map() 变换
        Observable.just(1, 2)
                .map(new Func1<Integer, String>() {  // <-- 这个 map() 变换过后，事件的参数类型由 Integer 变成了 String
                    @Override
                    public String call(Integer integer) {
                        return "[Mapped Integer " + integer + "]";
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "Code 10 - onNext item: " + s);
                    }
                });

        // Code 11 - flatMap() 变换
        List<Student> students = Student.makeList();
        Observable.from(students)  // <-- 这个是“原始 Observable”
                .flatMap(new Func1<Student, Observable<String>>() {  // <-- 这个 Func1 接收每个发送出来的 Student，...
                    @Override
                    public Observable<String> call(Student student) {
                        return Observable.from(student.courses);  // <-- ...并用它创建一个新的 Observable，同时激活它。
                        //     创建出来的 Observable 发送的事件都被汇总，然后
                        //     交到原始 Observable 的 Subscriber 那里去。

                        // return Observable.just("Hi,A", "Hi,B");  // 为了加强理解，可以不用原始的student，
                        // 随便创建一个Observable
                    }
                })
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String o) {
                        Log.d(TAG, "Code 11 - onNext: " + o);
                    }
                });

        // Code 12 - throttleFirst() 变换

        Observable.just(1, 2, 3, 4)
                .throttleFirst(1, TimeUnit.SECONDS)  // <-- 在每次触发后的1秒内忽略其他事件，于是2,3,4都被丢弃了
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "Code 12 - onNext " + integer);
                    }
                });

        // Code 13 - compose() 变换
        ComposeTransformer trans = new ComposeTransformer();
        Observable.from(students)
                .compose(trans)
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, "Code 13 - length of name=" + integer);
                    }
                });

        
    }

    static class Student {
        String name;
        ArrayList<String> courses;

        public static Student make(int id) {
            Student obj = new Student();
            obj.name = String.format("Student's name %d", id);
            obj.courses = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                obj.courses.add(String.format("%s - course %d", obj.name, i));
            }
            return obj;
        }

        public static ArrayList<Student> makeList() {
            ArrayList<Student> students = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                students.add(make(i));
            }
            return students;
        }
    }

    // 这个 transformer 的作用： Student -[map]-> name -[map]-> len(name)
    static class ComposeTransformer implements Observable.Transformer<Student, Integer> {

        @Override
        public Observable<Integer> call(Observable<Student> studentObservable) {
            return studentObservable.map(new Func1<Student, String>() {  // <-- 第一个 lift() 变换
                @Override
                public String call(Student student) {
                    return student.name;
                }
            }).map(new Func1<String, Integer>() {  // <-- 第二个 lift() 变换
                @Override
                public Integer call(String s) {
                    return s.length();
                }
            });  // <-- 如果需要的话，可以添加更多变换（throttleFirst()之类都可以）
        }
    }
}
