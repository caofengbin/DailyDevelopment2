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

该实例中方式出现图片显示错位的关键代码：

``` java

if(!url.equals(tag)) {
	imageView.setImageDrawable(mDefaultBitmapDrawable);
}
  
if(mIsGridViewIdle) {
	imageView.setTag(url);
	mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
}
              
```

关于优化列表卡顿的几个实践：
> * (1)不要在getView中做太多的耗时操作，加载图片的过程要异步进行；
> * (2)特殊情况下，可以考虑开启硬件加速，通过设置android:hardwareAcceletate="true"，使相应的Activity开启硬件加速；
> * (3)控制异步任务的执行频率，简单的说就是通过在列表滑动过程中，停止提交加载图片的任务，列表滑动结束再进行图片加载操作；

``` java

	@Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

	if(mIsGridViewIdle) {
		imageView.setTag(url);
		mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
	}

```

通常情况下，通过这三条优化方式，列表就不会有卡顿现象。

## 2.ProgressBar的使用

[ProgressBar的官方文档链接](https://developer.android.com/reference/android/widget/ProgressBar.html)

实现了如下的几个效果，

![progressBar效果演示](http://occl9k36n.bkt.clouddn.com/2017_02_08_progressbar_show.gif)

### (1)最基本的效果

``` java
	<ProgressBar
        android:id="@+id/progress_bar1"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

### (2)水平进度条效果效果

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

## 3.百分比布局的使用

&emsp;&emsp;在之前，只有LinearLayout可以使用layout:weight属性，来实现按比例指定控件大小的功能。因此安卓引入一种全新的布局方式来解决这个问题--百分比布局。

> * PercentFrameLayout
> * PercentRelativeLayout

使用前需要在build文件中引入：

``` java
compile 'com.android.support:percent:24.2.1'
```

一个完整的布局使用示例代码可以看具体的文件中使用，尝试采用了PeercentFrameLayout。
    
## 4.实现一个可以同时左右滑动和上下滑动的ListView

&emsp;&emsp;本实现方案中，通过左右两个ListView方式来实现，并将这两个ListView包在同一个ScrollView中，可以比较好的解决ListVIew联动导致数据错乱的问题，但是他的缺点也很明显，就是用了两个ListView，导致两边的点击事件很难进行同步响应。另外就是由于ListVIew加在了ScrollView中，导致一个很坑的问题就是add onScrollListener会失效，没法监听响应，这样想监听整个ListVIew滑动到哪里时是没法做到的。另外就是addFooterView时，会出现很坑的问题就是整个FooterVIew会跟着右边的ListView左右滑动。实际上很不采用这种方式来实现这个效果。运行截图如下所示：

![水平滑动ListView效果1](http://occl9k36n.bkt.clouddn.com/2017_03_01_horizontal_list_view1.png)


## 5.实现一个可以同时左右滑动和上下滑动的ListView--方案2

本例中的方案只使用一个LsitView,通过拦截下面ListView的onTouch事件，并交给顶部的HorizontalView来处理，实现上面的滑动和下面的一起联动的效果。

![水平滑动ListView效果2](http://occl9k36n.bkt.clouddn.com/2017_03_01_horizontal_list_view2.png)

## 6.关于RecyclerView的使用

实现一个基本的RecyclerView的模板代码如下：

``` java
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapter(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }

    // 实现一个RecyclerView.Adapter必须实现的方法1
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    // 实现一个RecyclerView.Adapter必须实现的方法2
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    // 实现一个RecyclerView.Adapter必须实现的方法3
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}

```

必须要实现的三个方法：

> * (1)onCreateViewHolder,该方法主要用于创建ViewHolder实例，需要将布局加载出来，并利用该View创建ViewHolder实例；
> * (2)对RecyclerView子项的数据进行赋值，会在每个子项被滚动到屏幕内时执行；
> * (3)返回子项的数目；

实现纵向布局RecyclerView的核心：

``` java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.first_recycler_list);
LinearLayoutManager layoutManager = new LinearLayoutManager(this);
recyclerView.setLayoutManager(layoutManager);
FruitAdapter fruitAdapter = new FruitAdapter(fruitList);
recyclerView.setAdapter(fruitAdapter);
```

实现横向布局RecyclerView的核心：

``` java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.first_recycler_list2);
LinearLayoutManager layoutManager = new LinearLayoutManager(this);
layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
recyclerView.setLayoutManager(layoutManager);
FruitAdapter2 fruitAdapter = new FruitAdapter2(fruitList);
recyclerView.setAdapter(fruitAdapter);
```

实现瀑布流RecyclerView的核心：

``` java
RecyclerView recyclerView = (RecyclerView) findViewById(R.id.first_recycler_list3);
StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
recyclerView.setLayoutManager(layoutManager);
FruitAdapter3 fruitAdapter = new FruitAdapter3(fruitList);
recyclerView.setAdapter(fruitAdapter);
```

## 7.简易的聊天室应用界面实现

主要涉及知识点包括:

> * (1)9-patch图的基本制作与使用方式；
> * (2)RecyclerView中多item类型的使用；
> * (3)RecyclerView的局部刷新方式；

实现局部刷新的关键代码：

``` java
	adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); 
                }
            }
        });
```

## 8.数据持久化技术

Android中主要提供了三种方案：

> * (1)文件存储；
> * (2)SharedPreferences存储；
> * (3)数据库存储；

&emsp;&emsp;文件存储中，主要就是利用Context类提供的openFileInput()和openFileOutput()方法，再结合基本的java的各种流操作技术即可以。

&emsp;&emsp;**SharedPreferences使用的是键值对存储数据的，当保存一条数据的时候，需要给这条数据提供一个对应的键**，这样在读取数据的时候通过相应的键就可以把值读出来。而且SharedPreferences还支持多种不同的数据类型。

## 9.SQLite的增删查改操作

&emsp;&emsp;演示原生的SQLite的基本操作--增删查改数据库的实现。

## 10.LitePal的增删查改操作

&emsp;&emsp;演示开源项目LitePal的基本操作--增删查改数据库的实现。

[1.LitePal的Github源地址](https://github.com/LitePalFramework/LitePal)

[2.LitePal 1.5版本发布，你想要的都在这里](http://mp.weixin.qq.com/s?__biz=MzA5MzI3NjE2MA==&mid=2650238794&idx=1&sn=f105f8100d62a202f5695f9496425063&chksm=88639e25bf1417330f0ffb1cf6c10ea07359ff25774313fac5addfb25541f7969d1d178e17cc&mpshare=1&scene=23&srcid=03081ZddhZdGviU3aanJkBkL#rd)

[3.LitePal的CSDN专栏地址](http://blog.csdn.net/column/details/android-database-pro.html)
