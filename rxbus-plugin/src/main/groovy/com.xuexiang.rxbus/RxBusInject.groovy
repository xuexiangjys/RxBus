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

package com.xuexiang.rxbus

import com.xuexiang.rxbus.entity.RxBusInfo
import com.xuexiang.rxbus.util.Consts
import com.xuexiang.rxbus.util.RxBusHelper
import com.xuexiang.rxbus.util.Utils
import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import javassist.bytecode.DuplicateMemberException
import org.gradle.api.Project

import java.lang.annotation.Annotation

/**
 * RxBus动态注入修改的方法
 */
class RxBusInject {
    private final static ClassPool pool = ClassPool.getDefault()

    /**
     * 动态注入RxBus的注册代码
     * @param path 扫描
     * @param packageName 扫描代码的包名
     * @param project 项目
     */
    static void injectDir(String path, String packageName, Project project) {
        pool.appendClassPath(path)
        //project.android.bootClasspath 加入android.jar，否则找不到android相关的所有类
        pool.appendClassPath(project.android.bootClasspath[0].toString())
        Utils.importBaseClass(pool)

        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->
                String filePath = file.absolutePath//确保当前文件是class文件，并且不是系统自动生成的class文件
                if (filePath.endsWith(".class") && !filePath.contains('R$') && !filePath.contains('$')//代理类
                        && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")) {
                    // 判断当前目录是否是在我们的应用包里面
                    int index = filePath.indexOf(packageName)
                    if (index != -1) {

                        String className = Utils.getClassNameByClassFilePath(index, filePath)
                        //获取类的类名
                        CtClass c = pool.getCtClass(className)
                        if (c.isFrozen()) { // 如果类被冻结，无法修改就解冻，使其可以修改
                            c.defrost()
                        }

                        //记录RxBus的注册信息
                        RxBusInfo rxBusInfo = new RxBusInfo()
                        rxBusInfo.setProject(project)
                        rxBusInfo.setClazz(c)
                        if (c.name.endsWith("Activity") || c.superclass.name.endsWith("Activity")) {
                            rxBusInfo.setIsActivity(true)
                        }

                        //getDeclaredMethods获取自己申明的方法，c.getMethods()会把所有父类的方法都加上
                        for (CtMethod ctMethod : c.declaredMethods) {
                            String methodName = Utils.getMethodSimpleName(ctMethod)
                            if (Consts.ON_CREATE.contains(methodName)) {
                                rxBusInfo.setOnCreateMethod(ctMethod)
                            }
                            if (Consts.ON_DESTROY.contains(methodName)) {
                                rxBusInfo.setOnDestroyMethod(ctMethod)
                            }
                            ctMethod.annotations.each { Annotation annotation ->
                                if (annotation.annotationType().canonicalName == Consts.BusRegisterAnnotation) {
                                    project.logger.error "find @BusRegister method:" + c.getName() + " - " + ctMethod.getName()
                                    rxBusInfo.setBusRegisterMethod(ctMethod)
                                }
                                if (annotation.annotationType().canonicalName == Consts.BusUnRegisterAnnotation) {
                                    project.logger.error "find @BusUnRegister method:" + c.getName() + " - " + ctMethod.getName()
                                    rxBusInfo.setBusUnRegisterMethod(ctMethod)
                                }
                                if (annotation.annotationType().canonicalName == Consts.BusAnnotation) {
                                    project.logger.error "find @Bus method:" + c.getName() + " - " + ctMethod.getName()
                                    rxBusInfo.methods.add(ctMethod)
                                    rxBusInfo.annotations.add(annotation)
//                                    if (!isAnnotatedByBus) isAnnotatedByBus = true
                                }
                            }
                        }

                        if ((rxBusInfo.BusRegisterMethod != null && rxBusInfo.BusUnRegisterMethod == null)
                                || (rxBusInfo.BusRegisterMethod == null && rxBusInfo.BusUnRegisterMethod != null)) {
                            assert false: Consts.BusErrInfo
                            //发现@BusRegister和@BusUnRegister使用不匹配，直接中断操作
                        }

                        if (rxBusInfo != null && rxBusInfo.methods != null && rxBusInfo.methods.size() > 0) {
                            try {
                                RxBusHelper.initRxBus(rxBusInfo, path)
                            } catch (DuplicateMemberException e) {
                                e.printStackTrace()
                            }
                        }
                        c.detach()//用完一定记得要卸载，否则pool里的永远是旧的代码
                    }
                }
            }
        }
    }
}