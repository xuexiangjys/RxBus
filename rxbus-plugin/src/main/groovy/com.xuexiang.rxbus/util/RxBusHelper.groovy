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

import com.xuexiang.rxbus.entity.RxBusInfo
import javassist.CtClass
import javassist.CtMethod
import javassist.CtNewMethod
import javassist.bytecode.AnnotationsAttribute
import javassist.bytecode.MethodInfo
import javassist.bytecode.annotation.IntegerMemberValue

import java.lang.annotation.Annotation

/**
 * RxBus自动注册辅助类
 * @author xuexiang
 */
final class RxBusHelper {

    /**
     * 处理BusInfo
     * @param rxBusInfo
     * @param path
     */
    static void initRxBus(RxBusInfo rxBusInfo, String path) {
        if (rxBusInfo.clazz.isFrozen()) rxBusInfo.clazz.defrost()//解冻

        if (rxBusInfo.BusRegisterMethod != null) {//有被BusRegister注解的方法
            rxBusInfo.project.logger.quiet "BusRegisterMethod != null"
            rxBusInfo.BusRegisterMethod.insertAfter(getRegisterRxBusMethodCode(rxBusInfo))
        } else if (rxBusInfo.OnCreateMethod == null) {//没有OnCreateMethod，创建并加上新代码
            rxBusInfo.project.logger.quiet "OnCreateMethod == null , isActivity: " + rxBusInfo.isActivity
            String pre_create_str = rxBusInfo.isActivity ? Consts.Activity_OnCreate : Consts.Fragment_OnCreate
            String method = pre_create_str + getRegisterRxBusMethodCode(rxBusInfo) + "    }"
            rxBusInfo.project.logger.quiet method
            CtMethod registerRxBusMethod = CtNewMethod.make(method, rxBusInfo.clazz)
            rxBusInfo.clazz.addMethod(registerRxBusMethod)
        } else {//有OnCreateMethod，直接插入新代码
            rxBusInfo.project.logger.quiet "OnCreateMethod != null"
            rxBusInfo.OnCreateMethod.insertAfter(getRegisterRxBusMethodCode(rxBusInfo))
        }
        if (rxBusInfo.BusUnRegisterMethod != null) {//有被BusUnRegister注解的方法
            rxBusInfo.project.logger.quiet "BusUnRegisterMethod != null"
            rxBusInfo.BusUnRegisterMethod.insertAfter(getUnRegisterRxBusMethodCode(rxBusInfo))
        } else if (rxBusInfo.OnDestroyMethod == null) {
            rxBusInfo.project.logger.quiet "OnDestroyMethod == null"
            String method = Consts.Pre_OnDestroy + getUnRegisterRxBusMethodCode(rxBusInfo) + "    }"
            rxBusInfo.project.logger.quiet method
            CtMethod unregisterRxBusMethod = CtNewMethod.make(method, rxBusInfo.clazz)
            rxBusInfo.clazz.addMethod(unregisterRxBusMethod)
        } else {
            rxBusInfo.project.logger.quiet "OnDestroyMethod != null"
            rxBusInfo.OnDestroyMethod.insertAfter(getUnRegisterRxBusMethodCode(rxBusInfo))
        }

        rxBusInfo.clazz.writeFile(path)
    }

    /**
     * 获取注册RxBus方法的代码
     * @param mBusInfo 事件信息
     * @return
     */
    static String getRegisterRxBusMethodCode(RxBusInfo rxBusInfo) {
        String registerCode = "\n"
        rxBusInfo.clazz.addInterface(rxBusInfo.clazz.classPool.get(Consts.Action))//为当前的类添加事件处理的接口
        for (int i = 0; i < rxBusInfo.getMethods().size(); i++) {
            CtMethod method = rxBusInfo.getMethods().get(i)
            MethodInfo methodInfo = method.getMethodInfo()

            Annotation mAnnotation = rxBusInfo.getAnnotations().get(i)
            AnnotationsAttribute attribute = methodInfo.getAttribute(AnnotationsAttribute.visibleTag)
            //获取注解属性
            javassist.bytecode.annotation.Annotation annotation = attribute.getAnnotation(mAnnotation.annotationType().canonicalName)
            //获取注解
            int eventId = ((IntegerMemberValue) annotation.getMemberValue("id")).getValue()
            rxBusInfo.eventIds.add(eventId)
            //获取注解的值
            int thread = -1
            if (annotation.getMemberValue("thread") != null) {
                thread = ((IntegerMemberValue) annotation.getMemberValue("thread")).getValue()
            }
            String registerMethod = ""
            switch (thread) {
                case -1: registerMethod = "on"; break;   //Bus.DEFAULT
                case 0: registerMethod = "onUI"; break; //Bus.UI
                case 1: registerMethod = "onIO"; break; //Bus.IO
            }

            registerCode += "        RxEventUtils." + registerMethod + "(" + eventId + ", (Action)this);\n"
        }
        initEventDispatch(rxBusInfo)

        rxBusInfo.project.logger.quiet "Add RxBus Register Code:"
        return registerCode
    }

    /**
     * 生成event事件分发的逻辑代码
     * @param rxBusInfo
     * @return
     */
    static initEventDispatch(RxBusInfo rxBusInfo) {
        String SwitchStr = Consts.Pre_Switch_Str
        for (int i = 0; i < rxBusInfo.eventIds.size(); i++) {
            CtMethod method = rxBusInfo.getMethods().get(i)
            CtClass[] parameterTypes = method.getParameterTypes()
            boolean oneParam = parameterTypes.length == 1
            boolean isBaseType = false
            String paramStr = ""
            if (oneParam) {
                String parameterTypeName = parameterTypes[0].name
                if (parameterTypeName == Consts.RxEvent) {
                    rxBusInfo.clazz.classPool.importPackage(parameterTypeName)
                    paramStr =  "(" + parameterTypeName + ")rxEvent"
                } else {
                    switch (parameterTypeName) {
                    //Primitive Types（原始型）	Reference Types(Wrapper Class)（引用型，（包装类））
                        case "boolean": parameterTypeName = "Boolean"; isBaseType = true; break;
                        case "byte": parameterTypeName = "Byte"; isBaseType = true; break;
                        case "char": parameterTypeName = "Character"; isBaseType = true; break;
                        case "float": parameterTypeName = "Float"; isBaseType = true; break;
                        case "int": parameterTypeName = "Integer"; isBaseType = true; break;
                        case "long": parameterTypeName = "Long"; isBaseType = true; break;
                        case "short": parameterTypeName = "Short"; isBaseType = true; break;
                        case "double": parameterTypeName = "Double"; isBaseType = true; break;
                    }
                    rxBusInfo.project.logger.quiet "name:" + parameterTypeName
                    String packageName = isBaseType ? "java.lang." + parameterTypeName : parameterTypeName
                    rxBusInfo.clazz.classPool.importPackage(packageName)

                    //如果是基本数据类型，需要手动拆箱，否则会报错
                    paramStr = isBaseType ? ("((" + packageName + ")rxEvent.data)." +
                            parameterTypes[0].name + "Value()") : ("(" + packageName + ")rxEvent.data")
                }
            }


            SwitchStr += "            case " + rxBusInfo.eventIds.get(i) + ": \n" +
                    "                " + method.getName() + "(" + (oneParam ? paramStr : "") + ");\n" +
                    "                break;\n"
        }
        String method = SwitchStr + "        }\n" +
                "    }"
        rxBusInfo.project.logger.quiet "Add EventDispatch Code:"
        rxBusInfo.project.logger.quiet method
        CtMethod dispatchEventMethod = CtMethod.make(method, rxBusInfo.clazz)
        rxBusInfo.clazz.addMethod(dispatchEventMethod)
    }

    /**
     * 生成取消事件注册的代码
     * @param mBusInfo
     */
    static String getUnRegisterRxBusMethodCode(RxBusInfo rxBusInfo) {
        String unregisterCode = "\n"
        rxBusInfo.eventIds.each { eventId -> unregisterCode += "        RxEventUtils.unregisterAll(" + eventId + ");\n" }

        rxBusInfo.project.logger.quiet "Add RxBus UnRegister Code:"
        return unregisterCode;
    }
}
