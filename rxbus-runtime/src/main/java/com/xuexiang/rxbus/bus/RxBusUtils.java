/*
 * Copyright (C) 2018 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xuexiang.rxbus.bus;

import android.support.annotation.NonNull;

import com.xuexiang.rxbus.subsciber.BaseSubscriber;
import com.xuexiang.rxbus.subsciber.SimpleThrowableAction;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * RxBus辅助工具类
 *
 * @author xuexiang
 * @date 2018/3/1 上午10:41
 */
public class RxBusUtils {
    private final static String TAG = "RxBusUtils";

    private static RxBusUtils sInstance;
    /**
     * 管理Subscribers订阅，防止内存泄漏
     * 事件订阅的订阅池，key：事件名， value：事件的订阅反馈信息
     */
    private ConcurrentHashMap<Object, CompositeSubscription> maps = new ConcurrentHashMap<Object, CompositeSubscription>();

    private RxBusUtils() {

    }

    /**
     * 获取RxBus辅助工具类实例
     *
     * @return
     */
    public static RxBusUtils get() {
        if (sInstance == null) {
            synchronized (RxBusUtils.class) {
                if (sInstance == null) {
                    sInstance = new RxBusUtils();
                }
            }
        }
        return sInstance;
    }
    //===============================RxBus==================================//

    /**
     * RxBus注入监听（订阅发生在主线程）
     *
     * @param eventName 事件名
     * @param action1   订阅动作
     */
    public <T> SubscribeInfo<T> onMainThread(@NonNull Object eventName, Action1<T> action1) {
        return onMainThread(eventName, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * RxBus注入监听（订阅发生在主线程）
     *
     * @param eventName  事件名
     * @param subscriber 订阅者 [只走onNext和onError]
     */
    public <T> SubscribeInfo<T> onMainThread(@NonNull Object eventName, BaseSubscriber<T> subscriber) {
        Observable<T> Observable = register(eventName); //注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber)));
        return info;
    }

    /**
     * RxBus注入监听（订阅发生在主线程）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     * @param errorAction 错误订阅
     */
    public <T> SubscribeInfo<T> onMainThread(@NonNull Object eventName, Action1<T> action1, Action1<Throwable> errorAction) {
        Observable<T> Observable = register(eventName); //注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.observeOn(AndroidSchedulers.mainThread()).subscribe(action1, errorAction)));
        return info;
    }

    /**
     * RxBus注入监听（订阅发生在IO线程）
     *
     * @param eventName 事件名
     * @param action1   订阅动作
     */
    public <T> SubscribeInfo<T> onIO(@NonNull Object eventName, Action1<T> action1) {
        return on(eventName, Schedulers.io(), action1);
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName 事件名
     * @param action1   订阅动作
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, Action1<T> action1) {
        return on(eventName, action1, new SimpleThrowableAction(TAG));
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName  事件名
     * @param subscriber 订阅者 [只走onNext和onError]
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, BaseSubscriber<T> subscriber) {
        Observable<T> Observable = register(eventName);//注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.subscribe(subscriber)));
        return info;
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName   事件名
     * @param action1     订阅动作
     * @param errorAction 错误订阅
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, Action1<T> action1, Action1<Throwable> errorAction) {
        Observable<T> Observable = register(eventName);//注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.subscribe(action1, errorAction)));
        return info;
    }


    /**
     * RxBus注入监听（可指定订阅的线程）
     *
     * @param eventName 事件名
     * @param scheduler 指定订阅的线程
     * @param action1   订阅动作
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, Scheduler scheduler, Action1<T> action1) {
        return on(eventName, scheduler, action1, new SimpleThrowableAction(TAG));
    }


    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventName  事件名
     * @param scheduler  响应线程
     * @param subscriber 订阅者 [只走onNext和onError]
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, Scheduler scheduler, BaseSubscriber<T> subscriber) {
        Observable<T> Observable = register(eventName);//注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.observeOn(scheduler).subscribe(subscriber)));
        return info;
    }

    /**
     * RxBus注入监听（可指定订阅的线程）
     *
     * @param eventName   事件名
     * @param scheduler   指定订阅的线程
     * @param action1     订阅动作
     * @param errorAction 错误订阅
     */
    public <T> SubscribeInfo<T> on(@NonNull Object eventName, Scheduler scheduler, Action1<T> action1, Action1<Throwable> errorAction) {
        Observable<T> Observable = register(eventName);//注册后，返回订阅者
        /* 订阅管理 */
        SubscribeInfo<T> info = new SubscribeInfo<>(Observable);
        info.setSubscription(add(eventName, Observable.observeOn(scheduler).subscribe(action1, errorAction)));
        return info;
    }

    /**
     * 单纯的Observables 和Subscribers管理
     *
     * @param eventName 事件名
     * @param m         订阅信息
     */
    public Subscription add(@NonNull Object eventName, Subscription m) {
        /* 订阅管理 */
        CompositeSubscription subscription = maps.get(eventName);
        if (subscription == null) {
            subscription = new CompositeSubscription();
            maps.put(eventName, subscription);
        }
        subscription.add(m);
        return m;
    }

    /**
     * 取消事件的所有订阅并注销事件
     *
     * @param eventName 事件名
     */
    public void unregisterAll(@NonNull Object eventName) {
        CompositeSubscription subscription = maps.get(eventName);
        if (subscription != null) {
            subscription.unsubscribe(); //取消订阅
            maps.remove(eventName);
        }
        RxBus.get().unregisterAll(eventName);
    }

    /**
     * 取消事件的指定订阅
     *
     * @param eventName  事件名
     * @param m          订阅信息
     * @param observable 订阅者
     */
    public void unregister(@NonNull Object eventName, Subscription m, Observable observable) {
        CompositeSubscription subscription = maps.get(eventName);
        if (subscription != null) {
            subscription.remove(m); //先取消特定的事件订阅
            if (!subscription.hasSubscriptions()) {
                maps.remove(eventName);
                RxBus.get().unregisterAll(eventName); //没有订阅信息了，直接注销事件
            }
        }
        RxBus.get().unregister(eventName, observable); //取消事件的订阅者
    }

    /**
     * 取消事件的指定订阅
     *
     * @param eventName     事件名
     * @param subscribeInfo 订阅信息
     */
    public void unregister(@NonNull Object eventName, SubscribeInfo subscribeInfo) {
        if (subscribeInfo != null) {
            unregister(eventName, subscribeInfo.getSubscription(), subscribeInfo.getObservable());
        }
    }

    /**
     * 注册事件
     *
     * @param eventName 事件名
     * @param <T>
     * @return 订阅者
     */
    public <T> Observable<T> register(@NonNull Object eventName) {
        return RxBus.get().register(eventName);
    }

    /**
     * 发送指定的事件(不携带数据)
     *
     * @param eventName 事件名
     */
    public void post(@NonNull Object eventName) {
        RxBus.get().post(eventName);
    }

    /**
     * 发送指定的事件（携带数据）
     *
     * @param eventName 注册标识
     * @param content   发送的内容
     */
    public void post(@NonNull Object eventName, Object content) {
        RxBus.get().post(eventName, content);
    }

}
