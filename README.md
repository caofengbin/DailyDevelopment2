# DailyDevelopment2
该项目主要记录平时学习过程中的一些零碎知识点(2017年2月)，具体目录如下：

## 1.ImageLoader的使用

&emsp;&emsp;本部分主要参照任玉刚--《Android开发艺术探索》第12章的内容，模拟实现一个ImageLoader的过程。主要涉及的技术包括：

> * BitmapFactory加载图片的四种方式；
> * 使用BitmapFactory.Option进行图片的高效加载；
> * LRU算法基本原理
> * LruCache类的详细使用
> * DiskLruCache的详细使用
> * ImageLoader的大致实现细节
> * ImageLoader**处理列表显示错误的细节**



## 2.ProgressBar的使用

[ProgressBar的官方文档链接](https://developer.android.com/reference/android/widget/ProgressBar.html)

实现了如下的几个效果，

<center>
![progressBar效果演示](http://occl9k36n.bkt.clouddn.com/2017_02_08_progressbar_show.gif)
</center>

###（1）最基本的效果

``` java
	<ProgressBar
        android:id="@+id/progress_bar1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

###（2）水平进度条效果效果

&emsp;&emsp;动画中已经演示了具体的效果，关键技术：

``` java
style="?android:attr/progressBarStyleHorizontal"
android:max="100"
```

指定相应的ProgressBar的style。并设置进度的最大值。在代码中动态的设置进度值就可以实现相应的效果：

``` java
	private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = mProgressBar2.getProgress();
            progress = progress + new Random().nextInt(25) + 5;
            mProgressBar2.setProgress(progress);
        }
    };
```

### (3)自定义进度条效果

demo中给出了具体的实现效果。
代码为

```	java
<ProgressBar
        android:id="@+id/progress_bar3"
        android:layout_width="50dp"
        android:layout_height="50dp"
        android:layout_centerInParent="true"
        android:indeterminate="true"
        android:indeterminateDrawable="@drawable/self_define_progressbar"
        android:layout_below="@id/text2"
        android:layout_marginTop="15dp"/>

```

``` java
<?xml version="1.0" encoding="utf-8"?>
<rotate xmlns:android="http://schemas.android.com/apk/res/android"
        android:fromDegrees="0"
        android:pivotX="50%"
        android:pivotY="50%"
        android:toDegrees="360" >

    <shape
        android:innerRadiusRatio="3"
        android:shape="ring"
        android:thicknessRatio="8"
        android:useLevel="false" >
        <gradient
            android:centerColor="#FF303d4b"
            android:centerY="0.50"
            android:endColor="#FF2973c5"
            android:startColor="#FF1d508a"
            android:type="sweep"
            android:useLevel="false" />
    </shape>

</rotate>

```

回顾一下比较重要的属性

> * (1)android:indeterminate,取值为true| false，作用：启用indeterminate（中文：不定的，不明确）模式。设置为true。将导致启用进度条的动画。
> * (2)android:indeterminateBehavior，取值为"repeat"|"cycle"，作用：indeterminate模式下，当进度条达到最大值时的动画处理行为："repeat"= Animation.RESTART，动画将从头开始执行。"cycle" = Animation.REVERSE，动画将反向从头开始执行。
> * (3)android:indeterminateDrawable，indeterminate模式下使用的Drawable对象。该对象如果是动画对象（继承自Animatable），
将会在onDraw方法中被启动动画。

更多自定义ProgressBar的参考链接

[Android的ProgressBar自定义入门](http://blog.csdn.net/zenip/article/details/8575498)

[简单自定义ProgressBar](http://www.jianshu.com/p/1afc30ba2811)
    