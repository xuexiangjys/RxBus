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

package com.xuexiang.rxbusdemo.util;

import com.xuexiang.rxbus.annotation.Bus;
import com.xuexiang.rxbus.annotation.BusRegister;
import com.xuexiang.rxbus.annotation.BusUnRegister;
import com.xuexiang.rxbus.logs.RxLog;
import com.xuexiang.rxbusdemo.entity.EventKey;

/**
 * @author xuexiang
 * @date 2018/4/17 下午10:32
 */
public class RxEventTest {


    public RxEventTest() {
        register();
    }

    @BusRegister
    private void register() {

    }


    @Bus(id = EventKey.EVENT_TEST_ID)
    public void getRxEventTest() {
        RxLog.e("收到了RxEventTest消息");
    }

    @BusUnRegister
    public void unRegister() {

    }

}
