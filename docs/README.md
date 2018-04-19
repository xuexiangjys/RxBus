# RxBus

[![RxBus][rxSvg]][rx]  [![api][apiSvg]][api]

一个简易的Android事件通知库，使用RxJava和Javassist设计，拒绝使用反射，保证性能高效稳定。

## 特征

* 支持多事件定义。

* 支持自定义数据携带。

* 支持全局和局部的事件订阅和注销。

* 支持指定事件订阅响应的线程。

* 支持使用@Bus注解进行事件订阅和注销。

## 如何使用

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

## 联系方式

[![](https://img.shields.io/badge/点击一键加入QQ群-602082750-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=9922861ef85c19f1575aecea0e8680f60d9386080a97ed310c971ae074998887)

[rxSvg]: https://img.shields.io/badge/RxBus-v1.0.0-brightgreen.svg
[rx]: https://github.com/xuexiangjys/RxBus
[apiSvg]: https://img.shields.io/badge/API-14+-brightgreen.svg
[api]: https://android-arsenal.com/api?level=14