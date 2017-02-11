package cfb.com.dailydevelopment2.example1.loadimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cfb.com.dailydevelopment2.R;
import cfb.com.myfoundation.imageloader.ImageLoader;

/**
 * 用来测试加载大量的图片
 */
public class LoadImageActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "MainActivity";

    // 存放图片的数组
    private List<String> mUrlList = new ArrayList<>();
    ImageLoader mImageLoader;
    private GridView mImageGridView;
    private BaseAdapter mImageAdapter;

    private boolean mIsGridViewIdle = true;
    private int mImageWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        initData();
        initView();

        mImageLoader = ImageLoader.build(LoadImageActivity.this);
    }

    // 初始化待加载的图片url数组
    private void initData() {
        for(String url : URLArrays.imageUrls) {
            mUrlList.add(url);
        }
        int screenWidth = MyUtils.getScreenMetrics(this).widthPixels;        // 获得屏幕的宽度
        int space = (int)MyUtils.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 2;
    }

    private void initView() {
        mImageGridView = (GridView) findViewById(R.id.gridView1);
        mImageAdapter = new ImageAdapter(this);
        mImageGridView.setAdapter(mImageAdapter);
        mImageGridView.setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            mIsGridViewIdle = true;
            mImageAdapter.notifyDataSetChanged();
        } else {
            mIsGridViewIdle = false;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    // 显示图片的Adapter
    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater mInflater;
        private Drawable mDefaultBitmapDrawable;

        private ImageAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mDefaultBitmapDrawable = context.getResources().getDrawable(R.drawable.image_default);
        }

        @Override
        public int getCount() {
            return mUrlList.size();
        }

        @Override
        public Object getItem(int i) {
            return mUrlList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            // 提升ListView运行效率的典型写法
            View view;
            ViewHolder viewHolder;

            if(convertView == null) {
                // 没有视图复用，从布局文件中加载视图
                view = mInflater.inflate(R.layout.image_list_item, parent, false);

                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) view.findViewById(R.id.image);

                view.setTag(viewHolder);
            } else{
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            ImageView imageView = viewHolder.imageView;

            final String tag = (String) imageView.getTag();
            final String url = (String) getItem(i);

            // 防止出现乱序加载图片的关键
            if(!url.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }

            // 示例1，原书给出的示例代码
            if(mIsGridViewIdle) {
                imageView.setTag(url);
                mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
            }

            // 示例2，去掉滑动时停止加载，如下代码所示，
            // 快速滑动，会出现在同一个View区域先后展示多张图片的问题
            // 原因分析，执行到这里，先set了TAG，提交加载任务
            // 之所以出现了在同一个View区域先后展示多张图片的问题
            // 是因为一旦制定到了这里，通多bindBitmap方法就一定给指定的Imageiew提交了一个加载图片
            // 的任务，这个任务完成时，就一定会加载图片
//            imageView.setTag(url);
//            mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);


            // 示例3，这种方式一开始只加载一张图，必须进行轻微的触碰才可以把所有图片都加载出来
//            imageView.setTag(url);
//            if(url.equals(tag)) {
//                mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
//            }

            // 示例4，对上面的那个问题的一个逻辑判断
//            imageView.setTag(url);
//            if(url.equals(tag) || tag == null) {
//                mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
//            }

            return view;
        }
    }

    // 定义内部类ViewHolder用于对控件的实例进行缓存
    private static class ViewHolder {
        public ImageView imageView;
    }
}
