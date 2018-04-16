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

import javassist.ClassPool
import javassist.CtMethod

/**
 * @author xuexiang
 * @date 2018/4/15 下午6:21
 */
public final class Utils {

    /**
     * 事先载入相关类
     *
     * @param pool
     */
    static void importBaseClass(ClassPool pool) {
        pool.importPackage(Consts.BusAnnotation)
        pool.importPackage(Consts.BusRegisterAnnotation)
        pool.importPackage(Consts.BusUnRegisterAnnotation)
        pool.importPackage("android.os.Bundle")
//        pool.importPackage("rx.functions.Action1")

        pool.importPackage(Consts.Action)
        pool.importPackage(Consts.RxEvent)
        pool.importPackage(Consts.RxBusUtils)
        pool.importPackage(Consts.RxEventUtils)


    }


    /**
     * 获取方法的SimpleName
     *
     * @param ctMethod
     * @return
     */
    static String getMethodSimpleName(CtMethod ctMethod) {
        String methodName = ctMethod.getName()
        return methodName.substring(
                methodName.lastIndexOf('.') + 1, methodName.length())
    }

    /**
     * 根据class文件的路径获取类的ClassName
     *
     * @param index
     * @param filePath
     * @return
     */
    static String getClassNameByClassFilePath(int index, String filePath) {
        int end = filePath.length() - 6 // .class = 6
        return filePath.substring(index, end).replace('\\', '.').replace('/', '.')
    }

}
