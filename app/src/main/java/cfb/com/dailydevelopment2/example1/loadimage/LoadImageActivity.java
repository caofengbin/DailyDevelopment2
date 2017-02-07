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
import cfb.com.dailydevelopment2.URLArrays;
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
            ViewHolder viewHolder = null;
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.image_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            } else{
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ImageView imageView = viewHolder.imageView;

            final String tag = (String) imageView.getTag();
            final String url = (String) getItem(i);

            // 防止出现乱序加载图片的关键
            if(!url.equals(tag)) {
                imageView.setImageDrawable(mDefaultBitmapDrawable);
            }

            imageView.setTag(url);
            if(url.equals(tag)) {
                mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
            }

//            if(mIsGridViewIdle) {
//                imageView.setTag(url);
//                mImageLoader.bindBitmap(url,imageView,mImageWidth,mImageWidth);
//            }
            return convertView;
        }
    }

    private static class ViewHolder {
        public ImageView imageView;
    }
}
