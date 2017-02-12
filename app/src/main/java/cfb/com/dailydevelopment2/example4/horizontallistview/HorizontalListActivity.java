package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cfb.com.dailydevelopment2.R;

public class HorizontalListActivity extends AppCompatActivity {

    private static final String TAG = "HorizontalListActivity";

    private MySyncHorizontalScrollView rightTitleHorscrollView;
    private MySyncHorizontalScrollView rightContentHorscrollView;
    private MyListView contentListViewLeft;
    private MyListView contentListViewRight;
    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    private TextView mTextView;

    private ArrayList<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_list);

        // initviews
        rightTitleHorscrollView = (MySyncHorizontalScrollView)findViewById(R.id.rightTitleHorscrollView);
        rightContentHorscrollView = (MySyncHorizontalScrollView)findViewById(R.id.rightContentHorscrollView);
        contentListViewLeft = (MyListView)findViewById(R.id.contentListViewLeft);
        contentListViewRight = (MyListView)findViewById(R.id.contentListViewRight);

        mTextView = (TextView) findViewById(R.id.tv_item);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "onClick: 加载更多数据");
                for (Product product : mProducts) {
                    leftAdapter.add(product);
                    rightAdapter.add(product);
                }
                leftAdapter.notifyDataSetChanged();
                rightAdapter.notifyDataSetChanged();
            }
        });

        //相互引用
        rightTitleHorscrollView.setSyncView(rightContentHorscrollView);
        rightContentHorscrollView.setSyncView(rightTitleHorscrollView);

        //setadapter
        leftAdapter = new LeftAdapter(this, 0, new ArrayList<Product>());
        contentListViewLeft.setAdapter(leftAdapter);
        rightAdapter = new RightAdapter(this, 0, new ArrayList<Product>());
        contentListViewRight.setAdapter(rightAdapter);

        //get data
        new Thread(){
            @Override
            public void run() {
                super.run();

                List<Product> productList = DataUtil.getData(HorizontalListActivity.this);
                Message message = handler.obtainMessage();
                message.what = 0;
                message.obj = productList;
                message.sendToTarget();
            }
        }.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    mProducts = (ArrayList<Product>)msg.obj;
                    for(int i = 0 ; i < 18; i ++) {
                        leftAdapter.add(mProducts.get(i));
                        rightAdapter.add(mProducts.get(i));
                    }

                    break;
            }

        }
    };
}
