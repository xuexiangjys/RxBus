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

package com.xuexiang.rxbus.entity

import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.annotation.Annotation
import org.gradle.api.Project

/**
 * 扫描到的RxBus注册信息
 */
class RxBusInfo {

    Project project                                     //保留当前工程的引用
    CtClass clazz                                       //当前处理的class
    List<CtMethod> methods = new ArrayList<>()          //带有Bus注解的方法列表
    List<Annotation> annotations = new ArrayList<>()    //带有Bus注解的注解列表
    List<Integer> eventIds = new ArrayList<>()        //带有Bus注解的事件名集合
    boolean isActivity = false                          //是否是在Activity
    CtMethod OnCreateMethod                             //Activity或Fragment的初始化方法
    CtMethod OnDestroyMethod                            //Activity或Fragment的销毁方法
    CtMethod BusRegisterMethod                          //被Register注解标注的初始化方法
    CtMethod BusUnRegisterMethod                        //被UnRegister注解标注的销毁方法
}
