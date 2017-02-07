# DailyDevelopment2
该项目主要记录平时学习过程中的一些零碎知识点(2017年2月相关)，具体目录如下：

## 1.ImageLoader的使用

&emsp;&emsp;

## 2.ProgressBar的使用

[ProgressBar的官方文档链接](https://developer.android.com/reference/android/widget/ProgressBar.html)

实现了如下的几个效果，

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
