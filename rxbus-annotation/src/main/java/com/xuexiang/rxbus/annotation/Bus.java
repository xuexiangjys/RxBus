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

package com.xuexiang.rxbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>利用javassist根据bus注解自动添加注册和反注册代码，目前仅支持Activity和Fragment中使用</p>
 * <p>在其他地方使用 需要配合@BusRegister以及@BusUnRegister一起使用，才能自动生成注册和反注册代码</p>
 *
 * 不按照规则使用，编译器间会直接报错
 * @author xuexiang
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Bus {
    int DEFAULT = -1; //事件发出时所在的线程
    int UI = 0; //UI线程
    int IO = 1; //子线程

    /**
     * 事件id
     *
     * @return
     */
    int id();

    /**
     * 事件订阅的线程
     *
     * @return
     */
    int thread() default DEFAULT;

}
