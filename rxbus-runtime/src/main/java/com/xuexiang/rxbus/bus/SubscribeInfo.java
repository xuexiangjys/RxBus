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


import rx.Observable;
import rx.Subscription;

/**
 * 订阅信息
 * @author xuexiang
 * @date 2018/3/3 下午11:43
 */
public final class SubscribeInfo<T> {
    /**
     * 订阅者
     */
    private Observable<T> mObservable;
    /**
     * 订阅信息
     */
    private Subscription mSubscription;

    public SubscribeInfo(rx.Observable<T> observable) {
        mObservable = observable;
    }

    public rx.Observable<T> getObservable() {
        return mObservable;
    }

    public SubscribeInfo setObservable(rx.Observable<T> observable) {
        mObservable = observable;
        return this;
    }

    public rx.Subscription getSubscription() {
        return mSubscription;
    }

    public SubscribeInfo setSubscription(rx.Subscription subscription) {
        mSubscription = subscription;
        return this;
    }
}
