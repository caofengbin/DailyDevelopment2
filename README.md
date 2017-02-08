# DailyDevelopment2
该项目主要记录平时学习过程中的一些零碎知识点(2017年2月相关)，具体目录如下：

## 1.ImageLoader的使用

&emsp;&emsp;

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

回顾一下一个重要的属性
| 属性名      					| 取值    		|  描述  |
| --------   					| -----:   		| :----: |
|android:indeterminate|true		| false			|启用indeterminate（中文：不定的，不明确）模式。设置为true。将导致启用进度条的动画。|

    