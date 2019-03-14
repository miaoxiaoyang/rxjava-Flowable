package scut.carson_ho.rxjava_flowable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Rxjava";
    private Button btn;
    private Subscription mSubscription;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        /**
//         * 基本操作构建
//         */
//        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR);
//        // 需要传入背压参数BackpressureStrategy，下面会详细讲解
//
//        /**
//         * 步骤2：创建观察者 =  Subscriber
//         */
//        Subscriber<Integer> downstream = new Subscriber<Integer>() {
//
//            @Override
//            public void onSubscribe(Subscription s) {
//                // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
//                // 相同点：Subscription具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
//                // 不同点：Subscription增加了void request(long n)
//                Log.d(TAG, "onSubscribe");
//                s.request(Long.MAX_VALUE);
//                // 关于request()下面会继续详细说明
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG, "onNext: " + integer);
//                Log.i("Rxjava","onNext "+integer);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.w(TAG, "onError: ", t);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        };
//
//        /**
//         * 步骤3：建立订阅关系
//         */
//        upstream.subscribe(downstream);

//        /**
//         * 应用2：自例
//         */
//        Flowable<Integer> object=Flowable.create(new FlowableOnSubscribe<Integer>() {
//
//            @Override
//            public void subscribe(FlowableEmitter<Integer> e) throws Exception {
//                //获取当前接收者需要接收的事件数量
//                long n=e.requested();
//                Log.i("number","可接受的总事件"+n);
//                for (int i = 0; i < n; i++) {
//                    Log.i("number","发送的事件"+i);
//                    e.onNext(i);
//                }
//            }
//        },BackpressureStrategy.ERROR);
//
//        Subscriber<Integer> integerSubscriber=new Subscriber<Integer>() {
//            @Override
//            public void onSubscribe(Subscription s) {
//                s.request(10);
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.i("number","可接受到事件"+integer);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.i("number","onError"+t);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.i("number","onComplete");
//            }
//        };
//        object.subscribe(integerSubscriber);

        /**
         * 应用2.设置观察者可接收的事件,超过观察者接受的事件会报错
         */
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//
//                // 调用emitter.requested()获取当前观察者需要接收的事件数量
//                long n = emitter.requested();
//
//                Log.d("TWO", "观察者可接收事件" + n);
//                Log.i("emitter", "观察者可接收事件" + n);
//
//                // 根据emitter.requested()的值，即当前观察者需要接收的事件数量来发送事件
//                for (int i = 0; i < n; i++) {
//                    Log.d("TWO", "发送了事件" + i);
//                    Log.i("emitter", "发送了事件" + i);
//                    emitter.onNext(i);
//                }
//            }
//        }, BackpressureStrategy.ERROR)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
////                        Log.d("TWO", "onSubscribe");
//                        Log.i("emitter", "onSubscribe");
//                        // 设置观察者每次能接受10个事件
//                        s.request(10);
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
////                        Log.d("TWO", "接收到了事件" + integer);
//                        Log.i("emitter", "接收到了事件"+integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
////                        Log.w("TWO", "onError: ", t);
//                        Log.i("emitter", "onError",t);
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        Log.d("TWO", "onComplete");
//                        Log.i("emitter", "onComplete");
//                    }
//                });

        /**
         *应用3：sleep 发射方为 10ms 发送一次，接受方为 2s 接受处理一次
         * 背压操作，缓存中发送的事件会依次处理，可以看出下方 发送与接受方 相差近 9000 支持背压
         * 03-13 11:28:20.371 2096-2127/scut.carson_ho.rxjava_flowable I/sleep: 发送了事件9999
         * 03-13 11:28:21.451 2096-2096/scut.carson_ho.rxjava_flowable I/sleep: 接收到了事件51
         */
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            // 1. 创建被观察者 & 生产事件
//            @Override
//            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
//
//                for (int i = 0; i<1000; i++) {
//                    Log.i("sleep", "发送了事件"+ i );
//                    Thread.sleep(10);
//                    // 发送事件速度：10ms / 个
//                    emitter.onNext(i);
//
//                }
//
//            }
//        }).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
//                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
//             .subscribe(new Observer<Integer>() {
//            // 2. 通过通过订阅（subscribe）连接观察者和被观察者
//
//            @Override
//            public void onSubscribe(Disposable d) {
//                Log.d(TAG, "开始采用subscribe连接");
//                Log.i("sleep", "开始采用subscribe连接");
//            }
//
//            @Override
//            public void onNext(Integer value) {
//
//                try {
//                    // 接收事件速度：5s / 个
//                    Thread.sleep(200);
//                    Log.d(TAG, "接收到了事件"+ value  );
//                    Log.i("sleep", "接收到了事件"+ value );
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Log.d(TAG, "对Error事件作出响应");
//                Log.i("sleep", "对Error事件作出响应");
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "对Complete事件作出响应");
//                Log.i("sleep", "对Complete事件作出响应");
//            }
//
//        });

        /**
         * 应用4：onBackpressureBuffer 操作符就是解决背压问题的，背压策略 继续传给 FlowableOnBackpressureBuffer() 参数传递
         */
//        Flowable.interval(1, TimeUnit.MILLISECONDS)
//                .onBackpressureBuffer() // 添加背压策略封装好的方法，此处选择Buffer模式，即缓存区大小无限制
//                //RxJavaPlugins() 配置
//                .observeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        Log.i("interval","onSubscribe");
//                        mSubscription = s;
//                        //设置可观察的最大值
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "onNext: " + aLong);
//                        Log.i("interval","onNext: " + aLong);
//                        try {
//                            Thread.sleep(2000);
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable t) {
////                        Log.w(TAG, "onError: ", t);
//                        Log.i("interval","onError: " +t);
//                    }
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                        Log.i("interval","onComplete: ");
//                    }
//                });


//        /**
//         * BackpressureStrategy.LATEST 设置背压，发射器发射的多余没观察到的数据，暂时缓存到背压中
//         */
//        // 创建被观察者Flowable
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//
//                // 发送 150个事件
//                for (int i = 1;i< 150; i++) {
////                    Log.d(TAG, "发送了事件" + i);
//                    Log.i("backpress","发送了事件"+ i);
//                    emitter.onNext(i);
//                }
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.LATEST) // 设置背压模式 = BackpressureStrategy.LATEST
//                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
//                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
////                        Log.d(TAG, "onSubscribe");
//                        Log.i("backpress","onSubscribe");
//                        s.request(127);
//                        // 设置观察者可接受的事件条目数
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
////                        Log.d(TAG, "接收到了事件" + integer);
//                        Log.i("backpress","接收到了事件"+integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
////                        Log.w(TAG, "onError: ", t);
//                        Log.i("backpress","onError"+t);
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        Log.d(TAG, "onComplete");
//                        Log.i("backpress","onComplete");
//                    }
//                });

//        btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSubscription.request(2);
//                // 点击按钮 则 读取1个文字
//            }
//        });

//        /**
//         * 应用5：控制观察者事件数量,可以获得观察者事件的数量
//         * （异步）控制上游方法： FlowableEmitter 缓冲池的上限是 128，所有这里就可以设置为最多发送128即可不在发送，随后就是去按钮解压发送
//         * 应用场景：控制了最上层缓冲池上限128，然后当点击的时候，再去出现弹出来
//         */
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//
////                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
//                Log.i("FlowableEmitter","观察者可接收事件数量 ="+ emitter.requested());
//                boolean flag;
//
//                // 被观察者一共需要发送500个事件
//                for (int i = 1; i < 500; i++) {
//                    flag = false;
//
//                    // 若requested() == 0则不发送
//                    while (emitter.requested() == 0) {
//                        if (!flag) {
//                            Log.d(TAG, "不再发送");
//                            Log.i("FlowableEmitter","不再发送");
//                            flag = true;
//                        }
//                    }
////                    Log.d(TAG, "发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
//                    Log.i("FlowableEmitter","发送了事件"+i+"，观察者可接收事件数量 ="+emitter.requested());
//                    emitter.onNext(i);
//                }
//            }
//        }, BackpressureStrategy.ERROR)
//                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
//                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
////                        Log.d(TAG, "onSubscribe");
//                        Log.i("FlowableEmitter","onSubscribe");
//                        mSubscription = s;
//                        // 初始状态 = 不接收事件；通过点击按钮接收事件
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
////                        Log.d(TAG, "接收到了事件" + integer);
//                        Log.i("FlowableEmitter","接收到了事件"+ integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
////                        Log.w(TAG, "onError: ", t);
//                        Log.i("FlowableEmitter","onError: "+ t);
//                    }
//
//                    @Override
//                    public void onComplete() {
////                        Log.d(TAG, "onComplete");
//                        Log.i("FlowableEmitter","onComplete ");
//                    }
//                });

        /**
         * （异步）控制上游方法： FlowableEmitter
         */

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSubscription.request(48);
                // 点击按钮 则 接收48个事件
            }

        });

        /**
         * 应用：FlowableEmitter 缓冲背压
         */
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
                    boolean flag;

                    // 被观察者一共需要发送500个事件
                    for (int i = 0; i < 500; i++) {
                        flag = false;

                        // 若requested() == 0则不发送
                        while (emitter.requested() == 0) {
                            if (!flag) {
                                Log.d(TAG, "不再发送");
                                flag = true;
                            }
                        }
//                        Log.d(TAG, "发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
                        Log.i("FlowableEmitter","发送了事件" + i + "，观察者可接收事件数量 = " +emitter.requested());
                        emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
                        Log.i("FlowableEmitter","onSubscribe");
                        mSubscription = s;
                       // 初始状态 = 不接收事件；通过点击按钮接收事件
                    }

                    @Override
                    public void onNext(Integer integer) {
//                        Log.d(TAG, "接收到了事件" + integer);
                        Log.i("FlowableEmitter","接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
                        Log.i("FlowableEmitter","接收到了事件" ,t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        Log.i("FlowableEmitter","onComplete" );
                    }
                });


        /**
         * （同步）控制上游方法： FlowableEmitter
         */
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//
//                // 1. 调用emitter.requested()获取当前观察者需要接收的事件数量
//                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
//
//                // 2. 每次发送事件后，emitter.requested()会实时更新观察者能接受的事件
//                // 即一开始观察者要接收10个事件，发送了1个后，会实时更新为9个
//                Log.d(TAG, "发送了事件 1");
//                emitter.onNext(1);
//                Log.d(TAG, "发送了事件1后, 还需要发送事件数量 = " + emitter.requested());
//
//                Log.d(TAG, "发送了事件 2");
//                emitter.onNext(2);
//                Log.d(TAG, "发送事件2后, 还需要发送事件数量 = " + emitter.requested());
//
////                Log.d(TAG, "发送了事件 3");
////                emitter.onNext(3);
////                Log.d(TAG, "发送事件3后, 还需要发送事件数量 = " + emitter.requested());
//
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        s.request(1); // 设置观察者每次能接受10个事件
////                        s.request(20); // 第2次设置观察者每次能接受20个事件
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "接收到了事件" + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });
        


        /**
         * 调用RxJava封装好的背压模式方法
         */

//        Flowable.interval(1, TimeUnit.MILLISECONDS)
//                .onBackpressureBuffer() // 添加背压策略封装好的方法，此处选择Buffer模式，即缓存区大小无限制
//                .observeOn(Schedulers.newThread())
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        mSubscription = s;
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "onNext: " + aLong);
//                        try {
//                            Thread.sleep(1000);
//
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });

//
//        // 通过interval自动创建被观察者Flowable
//        // 每隔1ms将当前数字（从0开始）加1，并发送出去
//        // interval操作符会默认新开1个新的工作线程
//        Flowable.interval(1, TimeUnit.MILLISECONDS)
//                .onBackpressureBuffer() // 添加背压策略封装好的方法，此处选择Buffer模式，即缓存区大小无限制
//                .observeOn(Schedulers.newThread()) // 观察者同样工作在一个新开线程中
//                .subscribe(new Subscriber<Long>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        mSubscription = s;
//                        s.request(Long.MAX_VALUE); //默认可以接收Long.MAX_VALUE个事件
//                    }
//
//                    @Override
//                    public void onNext(Long aLong) {
//                        Log.d(TAG, "onNext: " + aLong);
//                        try {
//                            Thread.sleep(1000);
//                            // 每次延时1秒再接收事件
//                            // 因为发送事件 = 延时1ms，接收事件 = 延时1s，出现了发送速度 & 接收速度不匹配的问题
//                            // 缓存区很快就存满了128个事件，从而抛出MissingBackpressureException异常
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });




        /**
         * 测试各种背压模式
         */

//        btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSubscription.request(128);
//                // 每次接收128个事件
//            }
//
//        });
//
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0;i< 150; i++) {
//                    Log.d(TAG, "发送了事件" + i);
//                    emitter.onNext(i);
//                }
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.LATEST).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
//                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        mSubscription = s;
//                        // 通过按钮进行接收事件
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "接收到了事件" + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });



        /**
         * 异步调用
         */


//        btn = (Button) findViewById(R.id.btn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSubscription.request(2);
//            }
//
//        });
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                for (int i = 0;i< 129; i++) {
//                    Log.d(TAG, "发送了事件" + i);
//                    emitter.onNext(i);
//                }
//
////                Log.d(TAG, "发送事件 1");
////                emitter.onNext(1);
////                Log.d(TAG, "发送事件 2");
////                emitter.onNext(2);
////                Log.d(TAG, "发送事件 3");
////                emitter.onNext(3);
////                Log.d(TAG, "发送事件 4");
////                emitter.onNext(4);
////                Log.d(TAG, "发送完成");
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
//                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        mSubscription = s;
//                        // 保存Subscription对象，等待点击按钮时（调用request(2)）观察者再接收事件
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "接收到了事件" + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });


        /**
         * 同步情况
         */

//        /**
//         * 步骤1：创建被观察者 =  Flowable
//         */
//        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//
//                // 发送4个事件
//                Log.d(TAG, "发送了事件1");
//                emitter.onNext(1);
//                Log.d(TAG, "发送了事件2");
//                emitter.onNext(2);
//                Log.d(TAG, "发送了事件3");
//                emitter.onNext(3);
//                Log.d(TAG, "发送了事件4");
//                emitter.onNext(4);
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR);
//
//        /**
//         * 步骤2：创建观察者 =  Subscriber
//         */
//        Subscriber<Integer> downstream = new Subscriber<Integer>() {
//
//            @Override
//            public void onSubscribe(Subscription s) {
//                Log.d(TAG, "onSubscribe");
//                 s.request(3);
//                 // 每次可接收事件 = 3 ，即不匹配
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG, "接收到了事件 " + integer);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.w(TAG, "onError: ", t);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        };
//
//        /**
//         * 步骤3：建立订阅关系
//         */
//        upstream.subscribe(downstream);


        /**
         * 初步使用Demo
         */
//        /**
//         * 步骤1：创建被观察者 =  Flowable
//         */
//        Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR);
//        // 对比Observable，增加了1个参数BackpressureStrategy
//        // 作用：选择背压模式，即决定了当消费事件 与 生产事件速度不匹配时该如何处理的模式
//        // 模式说明：
//                  // BackpressureStrategy.ERROR：当出现消费事件 与 生产事件速度不匹配时，直接抛出异常
//
//        /**
//         * 步骤2：创建观察者 =  Subscriber
//         */
//        Subscriber<Integer> downstream = new Subscriber<Integer>() {
//
//            @Override
//            public void onSubscribe(Subscription s) {
//                // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
//                // 相同点：Subscription具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
//                // 不同点：Subscription增加了void request(long n)，下面详细讲解
//
//                Log.d(TAG, "onSubscribe");
//                s.request(Long.MAX_VALUE);
//                // 作用：决定观察者能够接收多少个事件，从而决定被观察者能够发送多少个事件，从而解决观察者 & 被观察者速度不匹配的问题
//                // 如设置了s.request(20)，这就说明观察者能够接收20个事件，然后被观察者只会发送20个事件给观察者处理，从而解决速度匹配问题
//            }
//
//            @Override
//            public void onNext(Integer integer) {
//                Log.d(TAG, "onNext: " + integer);
//            }
//
//            @Override
//            public void onError(Throwable t) {
//                Log.w(TAG, "onError: ", t);
//            }
//
//            @Override
//            public void onComplete() {
//                Log.d(TAG, "onComplete");
//            }
//        };
//
//        /**
//         * 步骤3：建立订阅关系
//         */
//        upstream.subscribe(downstream);



        /**
         * 链式调用
         */
//        Flowable.create(new FlowableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
//                Log.d(TAG, "发送事件 1");
//                emitter.onNext(1);
//                Log.d(TAG, "发送事件 2");
//                emitter.onNext(2);
//                Log.d(TAG, "发送事件 3");
//                emitter.onNext(3);
//                Log.d(TAG, "发送完成");
//                emitter.onComplete();
//            }
//        }, BackpressureStrategy.ERROR)
//                .subscribe(new Subscriber<Integer>() {
//
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        Log.d(TAG, "onSubscribe");
//                        s.request(30);
////                        mSubscription = s;  //把Subscription保存起来
//                    }
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        Log.d(TAG, "接收到了事件" + integer);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//                        Log.w(TAG, "onError: ", t);
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        Log.d(TAG, "onComplete");
//                    }
//                });

    }
}
