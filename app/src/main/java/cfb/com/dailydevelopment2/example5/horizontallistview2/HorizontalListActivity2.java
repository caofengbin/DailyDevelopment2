package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cfb.com.dailydevelopment2.R;

public class HorizontalListActivity2 extends AppCompatActivity {

    private MyAdapter mAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_list4);
        initView();
    }

    private void initView() {
        //初始化列表表头
        mListViewHead = (RelativeLayout) findViewById(R.id.head);
        //下面这个两个属性必须同时设置，不然点击头部是不能滑动的
        mListViewHead.setFocusable(true);//将控件设置成可获取焦点状态,默认是无法获取焦点的,只有设置成true,才能获取控件的点击事件
        mListViewHead.setClickable(true);//设置为true时，表明控件可以点击
        mListViewHead.setBackgroundColor(ContextCompat.getColor(this,R.color.table_header));
        mListViewHead.setOnTouchListener(mHeadListTouchListener);//头部设置触摸事件同时把触摸事件传递给水平滑动控件
        mHorizontalScrollView = (HorizontalScrollView) mListViewHead.findViewById(R.id.horizontalScrollView1);

        //初始化ListView
        mListView = (ListView) findViewById(R.id.listView1);
        //准备数据
        //设置适配器
        mAdapter = new MyAdapter(this, mListViewHead);
        mListView.setOnTouchListener(mHeadListTouchListener);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 列头/Listview触摸事件监听器<br>
     * 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
     */
    private View.OnTouchListener mHeadListTouchListener = new View.OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mHorizontalScrollView.onTouchEvent(event);
            return false;
        }
    };

    class MyAdapter extends BaseAdapter {

        /**列表表头容器**/
        private RelativeLayout mListViewHead;
        private Context mContext;

        public MyAdapter(Context context,RelativeLayout mListViewHead){
            this.mContext = context;
            this.mListViewHead = mListViewHead;
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 查找控件
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_hvscorll_listview_head, null);
                holder = new ViewHolder();

                holder.txt1 = (TextView) convertView.findViewById(R.id.textView1);
                holder.txt2 = (TextView) convertView.findViewById(R.id.textView2);
                holder.txt3 = (TextView) convertView.findViewById(R.id.textView3);
                holder.txt4 = (TextView) convertView.findViewById(R.id.textView4);
                holder.txt5 = (TextView) convertView.findViewById(R.id.textView5);
                holder.txt6 = (TextView) convertView.findViewById(R.id.textView6);
                holder.txt7 = (TextView) convertView.findViewById(R.id.textView7);
                holder.txt8 = (TextView) convertView.findViewById(R.id.textView8);
                holder.txt9 = (TextView) convertView.findViewById(R.id.textView9);
                holder.txt10 = (TextView) convertView.findViewById(R.id.textView10);
                holder.txt11 = (TextView) convertView.findViewById(R.id.textView11);
                holder.txt12 = (TextView) convertView.findViewById(R.id.textView12);
                holder.txt13 = (TextView) convertView.findViewById(R.id.textView13);
                holder.txt14 = (TextView) convertView.findViewById(R.id.textView14);
                holder.txt15 = (TextView) convertView.findViewById(R.id.textView15);
                holder.txt16 = (TextView) convertView.findViewById(R.id.textView16);
                holder.txt17 = (TextView) convertView.findViewById(R.id.textView17);
                holder.txt18 = (TextView) convertView.findViewById(R.id.textView18);
                holder.txt19 = (TextView) convertView.findViewById(R.id.textView19);
                holder.txt20 = (TextView) convertView.findViewById(R.id.textView20);
                holder.txt21 = (TextView) convertView.findViewById(R.id.textView21);
                holder.txt22 = (TextView) convertView.findViewById(R.id.textView22);
                //列表水平滚动条
                ObserverHScrollView scrollView1 = (ObserverHScrollView) convertView.findViewById(R.id.horizontalScrollView1);
                holder.scrollView = (ObserverHScrollView) convertView.findViewById(R.id.horizontalScrollView1);
                //列表表头滚动条
                ObserverHScrollView headScrollView = (ObserverHScrollView) mListViewHead.findViewById(R.id.horizontalScrollView1);
                headScrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txt1.setText("国债");
            holder.txt2.setText("简称");
            holder.txt3.setText("000000");
            holder.txt4.setText("财政部");
            holder.txt5.setText("2016-11-15");
            holder.txt6.setText("100.00");
            holder.txt7.setText("200.00");
            holder.txt8.setText("附息式固定利率");
            holder.txt9.setText("12");
            holder.txt10.setText("2.4200");
            holder.txt11.setText("--");
            holder.txt12.setText("--");
            holder.txt13.setText("--");
            holder.txt14.setText("--");
            holder.txt15.setText("--");
            holder.txt16.setText("--");
            holder.txt17.setText("--");
            holder.txt18.setText("--");
            holder.txt19.setText("--");
            holder.txt20.setText("--");
            holder.txt21.setText("--");
            holder.txt22.setText("结束");

            return convertView;
        }
    }

    /**
     * 实现接口，获得滑动回调
     */
    private class OnScrollChangedListenerImp implements ObserverHScrollView.OnScrollChangedListener {
        ObserverHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(ObserverHScrollView scrollViewar) {
            mScrollViewArg = scrollViewar;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }

    private class ViewHolder {
        TextView txt1;
        TextView txt2;
        TextView txt3;
        TextView txt4;
        TextView txt5;
        TextView txt6;
        TextView txt7;
        TextView txt8;
        TextView txt9;
        TextView txt10;
        TextView txt11;
        TextView txt12;
        TextView txt13;
        TextView txt14;
        TextView txt15;
        TextView txt16;
        TextView txt17;
        TextView txt18;
        TextView txt19;
        TextView txt20;
        TextView txt21;
        TextView txt22;
        HorizontalScrollView scrollView;
    }
}
