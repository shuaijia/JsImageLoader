# JsImageLoader

使用HttpUrlConnection封装图片请求库

![image](https://raw.githubusercontent.com/shuaijia/JsImageLoader/master/img/p.png)

## 使用
 
### 1、添依赖

```
allprojects {
  repositories {
    ...
    maven { url 'https://www.jitpack.io' }
  }
}

dependencies {
  compile 'com.github.shuaijia:JsImageLoader:v1.0'
}
```

### 2、添权限
```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

### 3、继承JsApplication

### 4、请求
```
JsLoader.with(this)
    .load("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=699359866,1092793192&fm=27&gp=0.jpg")
    .defaultImg(R.mipmap.default)
    .errorImg(R.mipmap.error)
    .into(imageView);
```
