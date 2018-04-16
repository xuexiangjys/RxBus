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

package com.xuexiang.rxbus.subsciber;


import com.xuexiang.rxbus.exception.RxException;
import com.xuexiang.rxbus.exception.RxExceptionHandler;
import com.xuexiang.rxbus.logs.RxLog;

import rx.Subscriber;

/**
 * 基础订阅者
 * @author xuexiang
 * @date 2018/3/9 上午9:00
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    public BaseSubscriber() {

    }

    @Override
    public void onCompleted() {
        RxLog.i("-->Subscriber is Complete");
    }

    @Override
    public void onStart() {
        super.onStart();
        RxLog.i("-->Subscriber is Start");
    }

    @Override
    public final void onError(Throwable e) {
        RxLog.e("-->Subscriber is onError");
        try {
            if (e instanceof RxException) {
                RxLog.e("--> e instanceof RxException, message:" + e.getMessage());
                onError((RxException) e);
            } else {
                RxLog.e("e !instanceof RxException, message:" + e.getMessage());
                onError(RxExceptionHandler.handleException(e));
            }
        } catch (Throwable throwable) {  //防止onError中执行又报错导致rx.exceptions.OnErrorFailedException: Error occurred when trying to propagate error to Observer.onError问题
            e.printStackTrace();
            RxLog.e(e);
        }
    }

    /**
     * 出错
     * @param e
     */
    public abstract void onError(RxException e);

}
