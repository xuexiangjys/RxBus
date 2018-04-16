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

package com.xuexiang.rxbus.bus.rxevent;

import android.support.annotation.NonNull;

import com.xuexiang.rxbus.bus.RxBus;
import com.xuexiang.rxbus.bus.RxBusUtils;
import com.xuexiang.rxbus.bus.SubscribeInfo;
import com.xuexiang.rxbus.subsciber.SimpleThrowableAction;

import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * RxEvent
 * @author xuexiang
 */
public final class RxEventUtils {

    private final static String TAG = "RxEventUtils";

    /**
     * RxEvent注入监听（订阅发生在主线程）
     *
     * @param eventId 事件Id
     * @param action 订阅动作
     */
    public static SubscribeInfo onUI(int eventId, final Action action) {
        return RxBusUtils.get().onMainThread(eventId, new Action1<RxEvent>() {
            @Override
            public void call(RxEvent rxEvent) {
                if (action != null) {
                    action.call(rxEvent);
                }
            }
        }, new SimpleThrowableAction(TAG));
    }

    /**
     * RxBus注入监听（订阅发生在IO线程）
     *
     * @param eventId 事件Id
     * @param action 订阅动作
     */
    public static SubscribeInfo onIO(int eventId, final Action action) {
        return RxBusUtils.get().on(eventId, Schedulers.io(), new Action1<RxEvent>() {
            @Override
            public void call(RxEvent rxEvent) {
                if (action != null) {
                    action.call(rxEvent);
                }
            }
        });
    }

    /**
     * RxBus注入监听（订阅线程不变）
     *
     * @param eventId 事件Id
     * @param action 订阅动作
     */
    public static SubscribeInfo on(int eventId, final Action action) {
        return RxBusUtils.get().on(eventId, new Action1<RxEvent>() {
            @Override
            public void call(RxEvent rxEvent) {
                if (action != null) {
                    action.call(rxEvent);
                }
            }
        });
    }

    /**
     * 取消事件的所有订阅并注销事件
     *
     * @param eventId 事件Id
     */
    public static void unregisterAll(int eventId) {
        RxBusUtils.get().unregisterAll(eventId);
    }

    /**
     * 发送指定的事件（携带数据）
     *
     * @param rxEvent   rxEvent
     */
    private static void post(@NonNull RxEvent rxEvent) {
        RxBus.get().post(rxEvent.getId(), rxEvent);
    }

    /**
     * 发送指定的事件（携带数据）
     *
     * @param eventId   事件的id
     * @param data   事件携带的数据
     */
    public static void post(int eventId,  Object data) {
        post(new RxEvent(eventId, data));
    }

    /**
     * 发送指定的事件（携带数据）
     *
     * @param eventId   事件的id
     */
    public static void post(int eventId) {
        post(new RxEvent(eventId));
    }
}
