# RxBus

[![RxBus][rxSvg]][rx]  [![api][apiSvg]][api]

一个简易的Android事件通知库，使用RxJava和Javassist设计，拒绝使用反射，保证性能高效稳定。

> 该项目是从[RxUtil](https://github.com/xuexiangjys/RxUtil)中分离出RxBus相关，并进行功能增强。如果你对RxJava的使用还不满足于RxBus， 你可以移步[RxUtil](https://github.com/xuexiangjys/RxUtil)和[RxUtil2](https://github.com/xuexiangjys/RxUtil2)。

## 关于我

[![github](https://img.shields.io/badge/GitHub-xuexiangjys-blue.svg)](https://github.com/xuexiangjys)   [![csdn](https://img.shields.io/badge/CSDN-xuexiangjys-green.svg)](http://blog.csdn.net/xuexiangjys)

## 特征

* 支持多事件定义。

* 支持自定义数据携带。

* 支持全局和局部的事件订阅和注销。

* 支持指定事件订阅响应的线程。

* 支持使用@Bus注解进行事件订阅和注销。

## 1、演示（请star支持）

![](https://github.com/xuexiangjys/RxUtil/blob/master/img/rxbus.gif)

## 2、如何使用
目前支持主流开发工具AndroidStudio的使用，直接配置build.gradle，增加依赖即可.

### 2.1、添加Gradle依赖

1.先在项目根目录的 build.gradle 的 repositories 添加:
```
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

2.再在项目根目录的 build.gradle 的 dependencies 添加Rxbus插件：

```
buildscript {
    ···
    dependencies {
        ···
        classpath 'com.github.xuexiangjys.RxBus:rxbus-plugin:1.0.0'
    }
}
```

3.在项目的 build.gradle 中增加依赖并引用Rxbus插件

```

apply plugin: 'com.xuexiang.rxbus'  //引用Rxbus插件

dependencies {
   ...
   implementation 'io.reactivex:rxjava:1.3.6'
   implementation 'io.reactivex:rxandroid:1.2.1'
   implementation 'com.github.xuexiangjys.RxBus:rxbus-runtime:1.0.0'
}
```

### 2.2、事件注册订阅

方法一：使用RxBusUtils.get().onMainThread方法注册事件，并指定订阅发生在主线程。

```
RxBusUtils.get().onMainThread(EventKey.EVENT_HAVE_DATA, new Action1<Event>() {
    @Override
    public void call(Event event) {
        showContent(EventKey.EVENT_HAVE_DATA, event.toString());
    }
});
```

方法二：使用RxBusUtils.get().on方法注册事件，订阅所在线程为事件发生线程，也可指定订阅发生的线程。

```
RxBusUtils.get().on(EventKey.EVENT_BACK_NORMAL, new Action1<String>() {
    @Override
    public void call(String eventName) {
        final String msg = "事件Key:" + EventKey.EVENT_BACK_NORMAL + "\n   EventName:" + eventName + ", 当前线程状态： " + Event.getLooperStatus();
        showContent(msg);
    }
});
```

方法三：使用@Bus注解修饰方法进行事件注册。

`id`为注册事件的ID，`thread`为订阅方法指定的线程。

`thread`的值：

* DEFAULT:事件发生的线程（线程不变）

* UI:UI线程（主线程）

* IO:IO线程（子线程）

```
@Bus(id = EventKey.EVENT_HAVE_DATA_ID, thread = Bus.UI)
private void handleHaveDataEvent(Event event) {
    showContent(EventKey.EVENT_HAVE_DATA, "EventID" + EventKey.EVENT_HAVE_DATA_ID + "\n" + event.toString());
}


@Bus(id = EventKey.EVENT_NO_DATA_ID, thread = Bus.UI)
private void handleNoDataEvent() {
    showContent(String.valueOf(EventKey.EVENT_NO_DATA_ID), "没有数据！");
}

```

### 2.3、事件发送

方法一：使用RxBusUtils.get().post(Object eventName)发送不带数据的事件。

```
RxBusUtils.get().post(EventKey.EVENT_NO_DATA);
```

方法二：使用RxBusUtils.get().post(Object eventName, Object content)发送携带数据的事件。

```
RxBusUtils.get().post(EventKey.EVENT_HAVE_DATA, new Event(EventKey.EVENT_HAVE_DATA, "这里携带的是数据"));
RxBusUtils.get().post(EventKey.EVENT_HAVE_DATA, true);
```

方法三：使用@Bus进行事件注册和注销的需使用RxEventUtils进行事件方式。

* 发送不带数据的事件

```
RxEventUtils.post(EventKey.EVENT_NO_DATA_ID);

```

* 发送携带数据的事件

```
RxEventUtils.post(EventKey.EVENT_HAVE_DATA_ID, new Event(EventKey.EVENT_HAVE_DATA, "这里携带的是数据"));

```

### 2.4、事件注销

1.使用RxBusUtils.get().unregisterAll(Object eventName)取消事件的所有订阅并注销事件。

```

RxBusUtils.get().unregisterAll(EventKey.EVENT_HAVE_DATA);

```

2.使用RxBusUtils.get().unregister(Object eventName, SubscribeInfo subscribeInfo)取消事件的某个指定订阅。

SubscribeInfo是事件注册订阅后返回的订阅信息。如果在取消该订阅后，该事件如无其他订阅，便自动注销该事件。

```

RxBusUtils.get().unregister(EventKey.EVENT_CLEAR, mSubscribeInfo);

```

3.使用@Bus注册的事件，如果是在Activity或者Fragment中，框架会自动进行事件注销。

当然你也可以主动进行事件注销：

```
RxEventUtils.unregisterAll(EventKey.EVENT_HAVE_DATA_ID);

RxEventUtils.unregisterAll(EventKey.EVENT_NO_DATA_ID);

```

### 2.5、非activity和fragment使用@BusRegister和@BusUnRegister进行事件的注册和注销。

```
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
```


## 联系方式

[![](https://img.shields.io/badge/%E7%82%B9%E6%88%91%E4%B8%80%E9%94%AE%E5%8A%A0%E5%85%A5QQ%E7%BE%A4-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=1e1f4bcfd8775a55e6cf6411f6ff0e7058ff469ef87c4d1e67890c27f0c5a390)

![](https://github.com/xuexiangjys/XPage/blob/master/img/qq_group.jpg)

[rxSvg]: https://img.shields.io/badge/RxBus-v1.0.0-brightgreen.svg
[rx]: https://github.com/xuexiangjys/RxBus
[apiSvg]: https://img.shields.io/badge/API-14+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=14