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

package com.xuexiang.rxbus.util

/**
 * 常量
 * @author xuexiang
 */
final class Consts {

    final static String BusAnnotation = "com.xuexiang.rxbus.annotation.Bus"
    final static String BusRegisterAnnotation = "com.xuexiang.rxbus.annotation.BusRegister"
    final static String BusUnRegisterAnnotation = "com.xuexiang.rxbus.annotation.BusUnRegister"

    final static String RxBusUtils = "com.xuexiang.rxbus.bus.RxBusUtils"
    final static String RxEventUtils = "com.xuexiang.rxbus.bus.rxevent.RxEventUtils"
    final static String RxEvent = "com.xuexiang.rxbus.bus.rxevent.RxEvent"
    final static String Action = "com.xuexiang.rxbus.bus.rxevent.Action"

    static def ON_CREATE = ['onCreate', "onActivityCreated"] as String[]
    static def ON_DESTROY = 'onDestroy'

    static String BusErrInfo = "【注意】：非Activity和Fragment中使用@BusRegister必须和@BusUnRegister一起使用，才能自动生成注册和反注册代码！"

    static def Activity_OnCreate = "\n" +
            "    protected void onCreate(Bundle savedInstanceState) {\n" +
            "        super.onCreate(savedInstanceState);\n"

    static def Fragment_OnCreate = "\n" +
            "    public void onActivityCreated(Bundle savedInstanceState) {\n" +
            "        super.onActivityCreated(savedInstanceState);"

    static def Pre_OnDestroy = "\n" +
            "    protected void onDestroy() {\n" +
            "        super.onDestroy();\n"

    static def Pre_Switch_Str = "\n" +
            "    public void call(RxEvent rxEvent) {\n" +
            "        switch(rxEvent.id) {\n"



}
