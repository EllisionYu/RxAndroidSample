package com.ys.rxandroid.sample;

import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;
    public static final String TAG = "RxAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView1);
//        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doPrePart();
//                doPart1_1();
//                doPart1_2();
//                doPart1_3();
//            }
//        });

//                doPrePart();
//                doPart1_1();
//                doPart1_2();
//                doPart1_3();
//                doPart2();
        findViewById(R.id.button1).setOnClickListener(v -> doPart5_1());
    }

    private void doPrePart() {
        //step1 : create an Observable emitting something.
//        Observable.OnSubscribe<String> observableAction = new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello, world!");
//                subscriber.onCompleted();
//            }
//        };

        Observable.OnSubscribe<String> observableAction = subscriber -> {
            subscriber.onNext("Hello, RxAndroid!");
            subscriber.onCompleted();
        };


        Observable<String> mObservable = Observable.create(observableAction);

        //step2 : create a couple of Subscribers
        Subscriber<String> mTextViewSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "onCompleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                mTextView.setText(s);
            }
        };

        Subscriber<String> mToastSubscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        };

        //step3 :  emitting string.
        mObservable.observeOn(AndroidSchedulers.mainThread()); //on the mainThread
        mObservable.subscribe(mTextViewSubscriber);
        mObservable.subscribe(mToastSubscriber);
    }

    //map operator
    private void doPart1_1(){
//        Action1<String> mTextViewAction = new Action1<String>() {
//            @Override
//            public void call(String s) {
//                mTextView.setText(s);
//            }
//        };

        Action1<String> mTextViewAction = s -> mTextView.setText(s);


//        Func1<String, String> toUpperCaseMap = new Func1<String, String>() {
//            @Override
//            public String call(String s) {
//                return s.toUpperCase();
//            }
//        };

        Func1<String, String> toUpperCaseMap = s -> s.toUpperCase();

        //emit the single String
        Observable<String> singleStringObservable = Observable.just("Hello, World!");
        singleStringObservable.observeOn(AndroidSchedulers.mainThread())
                .map(toUpperCaseMap)
                .subscribe(mTextViewAction);

/*        Observable.just("Hello, world!")
                .observeOn(AndroidSchedulers.mainThread())
                .map((s -> s.toUpperCase()))
                .subscribe(s1 -> mTextView.setText(s1));*/
    }

    //working with arrays and lists
    private void doPart1_2(){
//        Action1<String> mToastAction = new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//            }
//        };

        Action1<String> mToastAction = s -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        
        //emit each and every entry of the array, one by one.
        Observable<String> oneByOneObservable = Observable.from(new String[]{"android", "java", "python"});
        oneByOneObservable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(mToastAction);
    }

    //operator : flatMap
    private void doPart1_3(){
//        Func1<String[], Observable<String>> getStringsFunc1 = new Func1<String[], Observable<String>>() {
//            @Override
//            public Observable<String> call(String[] strings) {
//                return Observable.from(strings);
//            }
//        };

        Func1<String[], Observable<String>> getStringsFunc1 = strings -> Observable.from(strings);

//        Func2<String, String, String> mMergeRoutine = new Func2<String, String, String>() {
//            @Override
//            public String call(String s, String s2) {
//                return String.format("%s %s", s, s2);
//            }
//        };

        Func2<String, String, String> mMergeRoutine = (s, s2) -> String.format("%s %s", s, s2);

//        Action1<String> mToastAction = new Action1<String>() {
//            @Override
//            public void call(String s) {
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
//            }
//        };

        Action1<String> mToastAction = s -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();

        //emit the full list on a single shot
        Observable<String[]> observable = Observable.just(new String[]{"android", "java", "python"});

        observable.observeOn(AndroidSchedulers.mainThread())
                .flatMap(getStringsFunc1)
                .reduce(mMergeRoutine)
                .subscribe(mToastAction);
    }

    
    private List<PackageData> packageDatas = new ArrayList<>();
    public void doPart3(){
        List<String> softwarePackages = new ArrayList<>();
        softwarePackages.add("com.r2.r2sdk");
        softwarePackages.add("com.ys.rxandroid.sample");
        softwarePackages.add("testPackageName");
        softwarePackages.add("com.ys.popupmessage.demo");

        List<String> softwareNames = new ArrayList<>();
        softwareNames.add("r2sdktest");
        softwareNames.add("rxAndroidSample");
        softwareNames.add("testName");
        softwareNames.add("popupmessageDemo");

        Observable<String> softwareNamesObservable = Observable.from(softwareNames);
        Observable<String> softwarePackagesObservable = Observable.from(softwarePackages);

        Observable<PackageData> packageDataObservable = softwarePackagesObservable.zipWith(softwareNamesObservable, (s, s2) -> new PackageData(s, s2));

        //at this point, we have an Observable emitting PackageDatas;
        packageDataObservable.filter(packageData -> isAppInstalled(packageData.getPackageName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Subscribers.create(packageData1 -> packageDatas.add(packageData1), throwable -> throwable.printStackTrace(), () -> mTextView.setText(packageDatas.toString())));
    }

    private boolean isAppInstalled(String packageName){
        Log.d("RxAndroid", "ThreadId = " + Thread.currentThread().getId());
        boolean isAppInstalled = true;
        try {
            getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isAppInstalled = false;
        }
        return isAppInstalled;
    }

    private static class PackageData{
        private String packageName;
        private String name;

        public String getPackageName() {
            return packageName;
        }

        public PackageData(String packageName, String name){
            this.packageName = packageName;
            this.name = name;
        }

        @Override
        public String toString() {
            return "[packageName = " + packageName + ", name = " + name + "]";
        }
    }

    private void doPart5_1(){
        Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> Log.d(TAG, "OnNext"), throwable -> Log.d(TAG, throwable.getLocalizedMessage()), () -> Log.d(TAG, "onCompleted"));
    }
}
