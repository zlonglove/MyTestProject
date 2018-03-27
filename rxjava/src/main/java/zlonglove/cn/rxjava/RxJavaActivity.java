package zlonglove.cn.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import zlonglove.cn.rxjava.bean.Student;

public class RxJavaActivity extends AppCompatActivity {
    private final String TAG = RxJavaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);

        //创建一个观察者
        Observer<Student> observer = new Observer<Student>() {
            //事件队列异常。在事件处理过程中出异常时，onError() 会被触发,同时队列自动终止，不允许再有事件发出
            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "--->Error");
            }

            //事件队列完结时调用该方法。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列
            @Override
            public void onComplete() {
                Log.i(TAG, "--->onComplete");
                Log.i(TAG, "--->" + Thread.currentThread().getName());
            }

            //RxJava2.0中新增的,传递参数为Disposable,Disposable相当于RxJava1.x中的Subscription,用于解除订阅
            @Override
            public void onSubscribe(Disposable d) {
                Log.i(TAG, "--->Disposable");
            }

            @Override
            public void onNext(Student student) {
                //进行UI刷新动作
                Log.i(TAG, "--->" + student.toString());
                Log.i(TAG, "--->" + Thread.currentThread().getName());
            }

        };

        getStudentInfo(observer);
        /*justTest();
        mapTest();
        fromIterable();
        defer();
        interval();
        repeat();
        flatMap();*/
        doOnNext();
    }

    private void getStudentInfo(Observer<Student> observer) {
        //使用Observable.create()创建被观察者
        Observable.create(new ObservableOnSubscribe<Student>() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                Log.i(TAG, "--->" + Thread.currentThread().getName());
                Student student = new Student();
                student.setName("zhanglong");
                student.setAge("27");
                student.setSex("male");

                e.onNext(student);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())//指定subscribe()发生在IO线程
                .observeOn(AndroidSchedulers.mainThread())//指定Subscriber的回调发生在主线程
                .subscribe(observer);
    }

    /**
     * just()方式
     * Observable<String> observable = Observable.just("Hello");
     * 使用just( )，将为你创建一个Observable并自动为你调用onNext( )发射数据。通过just( )方式 直接触发onNext()
     * just中传递的参数将直接在Observer的onNext()方法中接收到。
     */
    private void justTest() {
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "--->" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * fromIterable()方式
     * 使用fromIterable()，遍历集合，发送每个item。相当于多次回调onNext()方法，每次传入一个item。
     * 注意：Collection接口是Iterable接口的子接口，所以所有Collection接口的实现类都可以作为Iterable对象直接传入fromIterable()方法。
     */
    private void fromIterable() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }
        Observable<String> observable = Observable.fromIterable((Iterable<String>) list);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "--->fromIterable " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--->fromIterable onComplete()");
                    }
                });
    }

    /**
     * defer
     * 当观察者订阅时，才创建Observable，并且针对每个观察者创建都是一个新的Observable。
     * 以何种方式创建这个Observable对象，当满足回调条件后，就会进行相应的回调
     */
    private void defer() {
        Observable<String> observable = Observable.defer(new Callable<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> call() throws Exception {
                return Observable.just("hello");
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.i(TAG, "--->defer " + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--->defer onComplete()");
                    }
                });
    }

    /**
     * interval
     * 创建一个按固定时间间隔发射整数序列的Observable，可用作定时器。即按照固定2秒一次调用onNext()方法
     * 每隔period时间发射一个整数[0,1,2,3,4....]
     */
    private void interval() {
        Observable<Long> observable = Observable.interval(2, TimeUnit.SECONDS);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.i(TAG, "--->interval " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--->interval onComplete()");
                    }
                });
    }

    /**
     * range
     * 创建一个发射特定整数序列的Observable，第一个参数为起始值，第二个为发送的个数，
     * 如果为0则不发送，负数则抛异常。上述表示发射1到20的数。即调用20次nNext()方法，依次传入1-20数字
     */
    private void range() {
        Observable<Integer> observable = Observable.range(1, 20);
    }

    /**
     * timer
     * 创建一个Observable，它在一个给定的延迟后发射一个特殊的值，即表示延迟2秒后，调用onNext()方法
     */
    private void timer() {
        Observable<Long> observable = Observable.timer(2, TimeUnit.SECONDS);
    }

    /**
     * repeat
     * 创建一个Observable，该Observable的事件可以重复调用
     */
    private void repeat() {
        Observable<Integer> observable = Observable.just(123).repeat();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "--->repeat " + integer);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "--->repeat onComplete()");
                    }
                });
    }

    /**
     * map()操作符，就是把原来的Observable对象转换成另一个Observable对象
     * 同时将传输的数据进行一些灵活的操作，方便Observer获得想要的数据形式
     */
    private void mapTest() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(100);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<Integer, String>() {
                    @Override
                    public String apply(Integer integer) throws Exception {
                        return integer + "test";
                    }
                }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.i(TAG, "--->" + s);
            }
        });
    }

    /**
     * flatMap()
     */
    private void flatMap() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }

        Observable<String> observable = Observable.just(list).flatMap(new Function<List<String>, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, "--->flatMap " + s);
                    }
                });
    }

    /**
     * doOnNext
     * doOnNext()允许我们在每次输出一个元素之前做一些额外的事情
     */
    private void doOnNext(){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("Hello" + i);
        }
        Observable.just(list).flatMap(new Function<List<String>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(List<String> strings) throws Exception {
                return Observable.fromIterable(strings);
            }
        }).take(5).doOnNext(new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.i(TAG,"--->doOnNext 准备工作");
            }
        }).subscribe(new Consumer<Object>() {
            @Override
            public void accept(Object s) throws Exception {
                Log.i(TAG,"--->doOnNext "+s);
            }
        });

    }

}
