package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cfb.com.dailydevelopment2.R;
import cfb.com.dailydevelopment2.example4.horizontallistview.NumberUtil;
import cfb.com.dailydevelopment2.example4.horizontallistview.Product;

/**
 * Created by Administrator on 2017/2/26.
 */

public class MyAdapter extends BaseAdapter {

    /**列表表头容器**/
    private RelativeLayout mListviewHead;
    private Context mContext;
    private List<Product> objects;

    public MyAdapter(Context context, RelativeLayout mListViewHead){
        this.mContext = context;
        this.mListviewHead = mListViewHead;
        objects = new ArrayList<>();
    }

    public MyAdapter(Context context, RelativeLayout mListViewHead, List<Product> objects) {
        this.mContext = context;
        this.mListviewHead = mListViewHead;
        this.objects = objects;
    }

    public void updateDates(List<Product> objects) {
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
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
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.activity_horizontal_listview_head, null);
            viewHolder.tv_item = (TextView)view.findViewById(R.id.tv_item);
            viewHolder.tv_item1 = (TextView)view.findViewById(R.id.tv_item1);
            viewHolder.tv_item2 = (TextView)view.findViewById(R.id.tv_item2);
            viewHolder.tv_item3 = (TextView)view.findViewById(R.id.tv_item3);
            viewHolder.tv_item4 = (TextView)view.findViewById(R.id.tv_item4);
            viewHolder.tv_item5 = (TextView)view.findViewById(R.id.tv_item5);
            viewHolder.tv_item6 = (TextView)view.findViewById(R.id.tv_item6);
            viewHolder.tv_item7 = (TextView)view.findViewById(R.id.tv_item7);

            //列表水平滚动条
            ObserverHScrollView scrollView = (ObserverHScrollView) view.findViewById(R.id.horizontalScrollView1);
            viewHolder.scrollView = (ObserverHScrollView) view.findViewById(R.id.horizontalScrollView1);
            //列表表头滚动条
            ObserverHScrollView headScrollView = (ObserverHScrollView) mListviewHead.findViewById(R.id.horizontalScrollView1);
            headScrollView.addOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView));
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Product product = objects.get(position);
        viewHolder.tv_item.setText(objects.get(position).getName());
        viewHolder.tv_item1.setText(NumberUtil.beautifulDouble(product.getLast()));
        viewHolder.tv_item2.setText(NumberUtil.beautifulDouble(product.getSell() - product.getLastClose()));
        viewHolder.tv_item3.setText(NumberUtil.beautifulDouble(product.getUpDownRate()));
        viewHolder.tv_item4.setText(NumberUtil.beautifulDouble(product.getOpen()));
        viewHolder.tv_item5.setText(NumberUtil.beautifulDouble(product.getHigh()));
        viewHolder.tv_item6.setText(NumberUtil.beautifulDouble(product.getLow()));
        viewHolder.tv_item7.setText(NumberUtil.beautifulDouble(product.getLastClose()));
        return view;
    }

    /**
     * 实现接口，获得滑动回调
     */
    private class OnScrollChangedListenerImp implements ObserverHScrollView.OnScrollChangedListener {
        ObserverHScrollView mScrollViewArg;

        public OnScrollChangedListenerImp(ObserverHScrollView scrollView) {
            mScrollViewArg = scrollView;
        }

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
            mScrollViewArg.smoothScrollTo(l, t);
        }
    }

    class ViewHolder {
        TextView tv_item;
        TextView tv_item1;
        TextView tv_item2;
        TextView tv_item3;
        TextView tv_item4;
        TextView tv_item5;
        TextView tv_item6;
        TextView tv_item7;
        HorizontalScrollView scrollView;
    }
}
