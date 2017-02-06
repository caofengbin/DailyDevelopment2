package cfb.com.dailydevelopment2.example1.loadimage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_image);
    }
}
