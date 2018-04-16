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

package com.xuexiang.rxbusdemo.fragment;

import com.xuexiang.rxbus.annotation.Bus;
import com.xuexiang.rxbus.bus.RxBusUtils;
import com.xuexiang.rxbusdemo.entity.Event;
import com.xuexiang.rxbusdemo.entity.EventKey;

import rx.functions.Action1;

/**
 * 注册了三个事件：EVENT_HAVE_DATA，EVENT_ONE_BY_ONE，EVENT_CLEAR
 * @author xuexiang
 * @date 2018/3/3 下午4:49
 */
public class RxBusTestFragment1 extends BaseRxBusTestFragment {

    @Override
    protected void initViews() {
        setBackgroundColor(android.R.color.holo_green_light);

    }

    @Override
    protected void initListener() {
        super.initListener();
//        RxBusUtils.get().onMainThread(EventKey.EVENT_HAVE_DATA, new Action1<Event>() {
//            @Override
//            public void call(Event event) {
//                showContent(EventKey.EVENT_HAVE_DATA, event.toString());
//            }
//        });
        RxBusUtils.get().onMainThread(EventKey.EVENT_ONE_BY_ONE, new Action1<String>() {
            @Override
            public void call(String eventName) {
                showContent(EventKey.EVENT_ONE_BY_ONE, "   EventName:" + eventName);
            }
        });
    }

    @Bus(id = EventKey.EVENT_HAVE_DATA_ID, thread = Bus.UI)
    public void handleHaveDataEvent(Event event) {
        showContent(EventKey.EVENT_HAVE_DATA, "EventID" + EventKey.EVENT_HAVE_DATA_ID + "\n" + event.toString());
    }

    @Override
    protected void onCancelEvent() {
//        RxBusUtils.get().unregisterAll(EventKey.EVENT_HAVE_DATA);
        RxBusUtils.get().unregisterAll(EventKey.EVENT_ONE_BY_ONE);
        RxBusUtils.get().unregister(EventKey.EVENT_CLEAR, mSubscribeInfo);

    }


}
