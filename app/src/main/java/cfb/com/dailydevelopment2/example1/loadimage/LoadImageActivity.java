package cfb.com.dailydevelopment2.example1.loadimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import cfb.com.dailydevelopment2.R;
import cfb.com.myfoundation.imageloader.ImageLoader;

/**
 * 用来测试加载大量的图片
 */
public class LoadImageActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private List<String> mUrlList = new ArrayList<>();
    ImageLoader mImageLoader;
    private GridView mImageGridView;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);

        imageView = (ImageView) findViewById(R.id.myImageView);
        mImageLoader = ImageLoader.build(this);

        String url = "http://ww4.sinaimg.cn/large/610dc034jw1f9mp3xhjdhj20u00u0ta9.jpg";
        mImageLoader.bindBitmap(url,imageView);
    }
}
