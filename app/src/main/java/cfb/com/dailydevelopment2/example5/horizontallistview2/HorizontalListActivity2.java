package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cfb.com.dailydevelopment2.R;
import cfb.com.dailydevelopment2.example4.horizontallistview.DataUtil;
import cfb.com.dailydevelopment2.example4.horizontallistview.Product;

/**
 * 实现水平滑动ListView的第二种方式
 * Created by fengincao on 2017/2/26.
 */

public class HorizontalListActivity2 extends AppCompatActivity {

    private static final String TAG = "HorizontalListActivity";

    /**
     * 列表表头容器
     **/
    private RelativeLayout mListViewHead;

    /**
     * 列表ListView
     **/
    private ListView mListView;

    /**
     * 列表ListView水平滚动条
     **/
    private HorizontalScrollView mHorizontalScrollView;

    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_list2);
        initView();
    }

    private void initView() {
        //初始化列表表头
        mListViewHead = (RelativeLayout) findViewById(R.id.activity_horizontal_list2_listView1_head);
        //下面这个两个属性必须同时设置，不然点击头部是不能滑动的
        mListViewHead.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        mListViewHead.setClickable(true);//设置为true时，表明控件可以点击

        mHorizontalScrollView = (HorizontalScrollView) mListViewHead.findViewById(R.id.horizontalScrollView1);

        //初始化ListView
        mListView = (ListView) findViewById(R.id.activity_horizontal_list2_listView1);
        //准备数据
        myAdapter = new MyAdapter(this,mListViewHead);


        new Thread(){
            @Override
            public void run() {
                super.run();

                List<Product> productList = DataUtil.getData(HorizontalListActivity2.this);
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = productList;
                message.sendToTarget();
            }
        }.start();
    }

    private ArrayList<Product> mProducts;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mProducts = (ArrayList<Product>)msg.obj;
                    for(int i = 0 ; i < 18; i ++) {
                        mProducts.add(mProducts.get(i));
                    }
                    myAdapter.updateDates(mProducts);
                    break;
            }

        }
    };
}
