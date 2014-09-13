# ![Logo](https://github.com/xiaopansky/Spear/raw/master/app/src/main/res/drawable-mdpi/ic_launcher.png) Spear

Spear是Android上的一个图片加载器，目的是为了帮助开发者从本地或网络读取图片，然后处理并显示在ImageView上

Spear is an image loader for Android, the purpose is to help the developers to read a picture from a local or network, and then processed and displayed on the ImageView.

![sample](https://github.com/xiaopansky/Spear/raw/master/docs/sample.jpg)

###Features
>* ``多种URI支持``。支持``http://``、``https://``、``assets://``、``content://``、``file://``、``drawable://``等6种URI。
>* ``异步加载``。采用线程池来处理每一个请求，并且网络加载和本地加载会放在不同的线程池中执行，保证不会因为网络加载而堵塞本地加载。
>* ``支持缓存``。采用Lru算法在本地和内存中缓存图片，本地缓存可设置``过期``时间。
>* ``支持ViewHolder``。即使你在ListView中使用了ViewHolder也依然可以使用ImageLoader来加载图片，并且图片显示绝对不会混乱。
>* ``重复下载过滤``。如果两个请求的图片地址一样的话，第二个就会等待，一直到第一个下载成功后才会继续处理。
>* ``即时取消无用请求``，在onDetachedFromWindow或重复利用的时候会取消无用的请求。
>* ``支持进度回调``，通过progressCallback()方法即可设置并开启进度回调。
>* ``防止加载过大Bitmap``,默认最大Bitmap限制为当前屏幕宽高的1.5倍，这样可以有效防止加载过大图片到内存中。
>* ``重新处理图片尺寸``，可自定义加载到内存的图片的尺寸，使用display()方法显示图片的时候还会自动根据ImageView的宽高来重新处理。
>* ``自带RequestOptions管理器``。你可以通过Spear.putOptions(Enum<?>, RequestOptions)存储RequestOptions，然后在使用的时候指定名称即可。
>* ``提供SpearImageView``。让加载图片更加简单。
>* ``额外提供load()和download()``。如果你不是要显示图片只是想要加载然后用作其他用途，那么你可以使用load()方法。
>* ``强大的自定义功能``。可自定义请求分发与执行、缓存、解码、处理、显示、默认图片、失败图片等。

### Sample App
>* [Get it on Google Play](https://play.google.com/store/apps/details?id=me.xiaoapn.android.imageloader)
>* [Download APK](https://github.com/xiaopansky/Spear/raw/master/releases/HappyImageLoader-2.4.0.apk)

###Usage guide

####显示图片
Spear支持以下六种URI：
>* "http://b.zol-img.com.cn/desk/bizhi/image/4/1366x768/1387347695254.jpg"; // from Web
>* "https://b.zol-img.com.cn/desk/bizhi/image/4/1366x768/1387347695254.jpg"; // from Web
>* "file:///mnt/sdcard/image.png"; // from SD card
>* "content://media/external/audio/albumart/13"; // from content provider
>* "assets://image.png"; // from assets
>* "drawable://" + R.drawable.image; // from drawable resource

**Image from http or https**
```java
String uri = "http://www.huabian.com/uploadfile/2013/1222/20131222054754556.jpg";
Spear.with(context).display(uri, imageView).fire();
```

**Image from file**
```java
Spear.with(context).display("file:///mnt/sfs.png", imageView).fire();

or

Spear.with(context).display(new File("/mnt/sfs.png"), imageView).fire();
```
        
**Image from content provider**
```java
Spear.with(context).display("content://media/external/audio/albumart/13", imageView).fire();

or

Uri uri = ...;
Spear.with(context).display(uri, imageView).fire();
```

        
**Image from drawable resource**
```java
Spear.with(context).display("drawable://"+R.drawable.ic_launcher, imageView).fire();

or

Spear.with(context).display(R.drawable.ic_launcher, imageView).fire();
```

        
**Image from assets**
```java
Spear.with(context).display("assets://test.png", imageView).fire();

or

String uri = Scheme.ASSETS.createUri("test.png");
Spear.with(context).display(uri, imageView).fire();
```
*一定要记得最后要调用fire()方法哦*

####配置显示选项：
```java
Spear.with(getBaseContext())
    .display("http://b.zol-img.com.cn/desk/bizhi/image/4/1366x768/1387347695254.jpg", imageView)
    .loadingDrawable(R.drawable.image_loading)    // 设置正在加载的时候显示的图片
    .loadFailedDrawable(R.drawable.image_load_fail)   // 设置当加载失败的时候显示的图片
    .disableDiskCache() // 禁用磁盘缓存
    .disableMemoryCache()   // 禁用内存缓存
    .diskCachePeriodOfValidity(60 * 1000) // 设置磁盘缓存有效期，单位毫秒，默认为0（永久有效）
    .maxsize(1000, 800) // 设置最大尺寸，用来解码Bitmap时计算inSampleSize，防止加载过大的图片到内存中，默认为当前屏幕的1.5倍
    .resize(300, 300)   // 重新定义图片宽高，将原始图片加载到内存中之后会使用ImageProcessor根据原始图片创建一张新的300x300的图片，如果ImageView的宽高是固定的，那么就会使用ImageView的宽高作为resize
    .displayer(new OriginalFadeInImageDisplayer())  // 设置图片显示器，在最后一步会使用ImageDisplayer来显示图片
    .processor(new CircleImageProcessor())  // 设置图片处理器
    .scaleType(ImageView.ScaleType.FIT_START)   // 设置图片显示模式，在使用ImageProcessor处理图片的时候会用到此参数，默认为ImageView的ScaleType
    .listener(new DisplayListener() {   // 设置监听器
        @Override
        public void onStarted() {
            
        }

        @Override
        public void onCompleted(String uri, ImageView imageView, BitmapDrawable drawable) {

        }

        @Override
        public void onFailed(FailureCause failureCause) {

        }

        @Override
        public void onCanceled() {

        }
    })
    .progressCallback(new ProgressCallback() {  // 设置进度监听器
        @Override
        public void onUpdateProgress(long totalLength, long completedLength) {
            
        }
    })
    .fire();
```

####你可能还感兴趣的功能：
>* [使用``RequestOptions``定义属性模板来简化属性设置操作](https://github.com/xiaopansky/Spear/wiki/RequestOptions)
>* [监听加载``开始``、``成功``、``失败``以及``进度``]()
>* [使用```SpearImageView```]()
>* [将默认图也使用BitmapProcessor处理一下]()
>* [使用``load()``或``download()``来单独``加载``或``下载``图片](https://github.com/xiaopansky/Spear/wiki/use-load-and-download-method)
>* [自定义``InSampleSize``计算规则或``自定义图片解码器``（ImageDecoder）](https://github.com/xiaopansky/Spear/wiki/ImageDecoder)
>* [自定义display时``Maxsize``和``resize``计算规则]()
>* [使加载到内存中的图片的尺寸``同ImageView的尺寸一样``，这样可最大限度的``节省内存``]()
>* [将图片处理成``圆形``的、``椭圆形``的或者加上``倒影效果``以及``自定义图片处理器``（ImageProcessor）](https://github.com/xiaopansky/Spear/wiki/ImageProcessor)
>* [以``渐变``或``缩放``的形式显示图片以及``自定义图片显示器``（ImageDisplayer）](https://github.com/xiaopansky/Spear/wiki/ImageDisplayer)
>* [自定义``请求执行顺序``或``线程池大小``以及``自定义请求执行器``（RequestExecutor）](https://github.com/xiaopansky/Spear/wiki/RequestExecutor)
>* [设置下载``失败重试次数``、``超时时间``以及``自定义图片下载器``（ImageDownloader）](https://github.com/xiaopansky/Spear/wiki/ImageDownloader)
>* [设置``内存缓存大小``或``自定义内存缓存器``（MemoryCache）](https://github.com/xiaopansky/Spear/wiki/MemoryCache)
>* [设置``磁盘缓存目录``或``自定义磁盘缓存器``（DiskCache）](https://github.com/xiaopansky/Spear/wiki/DiskCache)

###Downloads
>* [spear-1.0.0.jar](https://github.com/xiaopansky/Spear/raw/master/releases/spear-1.0.0.jar)
>* [spear-1.0.0-sources.zip](https://github.com/xiaopansky/Spear/raw/master/releases/spear-1.0.0-sources.zip)

###Change log
####1.0.0
Spear脱胎换骨，全新出发

[Browse more](https://github.com/xiaopansky/Spear/wiki/Change-log)

###License
```java
/*
 * Copyright (C) 2013 Peng fei Pan <sky@xiaopan.me>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```