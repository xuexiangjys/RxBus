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

import java.util.Arrays;
import java.util.Objects;

/**
 * 万能的事件对象[用于注解注册]
 *
 * @author xuexiang
 * @date 2018/3/26 下午11:01
 */
public class RxEvent {

    /**
     * 事件的Id（必须）
     */
    public int id;

    /**
     * 事件的类型
     */
    public Object type;
    /**
     * 事件携带的数据
     */
    public Object data;

    /**
     * 构造方法
     *
     * @param id 事件的Id
     */
    public RxEvent(int id) {
        this.id = id;
    }

    /**
     * 构造方法
     *
     * @param id   事件的Id
     * @param data 事件携带的数据
     */
    public RxEvent(int id, Object data) {
        this.id = id;
        this.data = data;
    }

    /**
     * 构造方法
     *
     * @param id   事件的Id
     * @param type 事件的类型
     * @param data 事件携带的数据
     */
    public RxEvent(int id, Object type, Object data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public RxEvent setId(int id) {
        this.id = id;
        return this;
    }

    public Object getType() {
        return type;
    }

    public RxEvent setType(Object type) {
        this.type = type;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RxEvent setData(Object value) {
        data = value;
        return this;
    }

    @Override
    public String toString() {
        return "[RxEvent] { \n" +
                "  id:" + id + "\n" +
                "  type:" + RxEvent.toString(type) + "\n" +
                "  data:" + RxEvent.toString(data) + "\n" +
                "}";
    }

    /**
     * 判断是否是指定的事件
     *
     * @param eventType
     * @return
     */
    public boolean isEvent(Object eventType) {
        return Objects.equals(type, eventType);
    }


    /**
     * 将对象转化为String
     *
     * @param object
     * @return
     */
    static String toString(Object object) {
        if (object == null) {
            return "null";
        }
        if (!object.getClass().isArray()) {
            return object.toString();
        }
        if (object instanceof boolean[]) {
            return Arrays.toString((boolean[]) object);
        }
        if (object instanceof byte[]) {
            return Arrays.toString((byte[]) object);
        }
        if (object instanceof char[]) {
            return Arrays.toString((char[]) object);
        }
        if (object instanceof short[]) {
            return Arrays.toString((short[]) object);
        }
        if (object instanceof int[]) {
            return Arrays.toString((int[]) object);
        }
        if (object instanceof long[]) {
            return Arrays.toString((long[]) object);
        }
        if (object instanceof float[]) {
            return Arrays.toString((float[]) object);
        }
        if (object instanceof double[]) {
            return Arrays.toString((double[]) object);
        }
        if (object instanceof Object[]) {
            return Arrays.deepToString((Object[]) object);
        }
        return "Couldn't find a correct type for the object";
    }
}
