package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cfb.com.dailydevelopment2.R;

/**
 * Created by caofengbin on 2017/2/12.
 */

public class RightAdapter extends ArrayAdapter<Product> {

    List<Product> objects;

    public RightAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(getContext(), R.layout.item_right, null);
            viewHolder.tv_item1 = (TextView)view.findViewById(R.id.tv_item1);
            viewHolder.tv_item2 = (TextView)view.findViewById(R.id.tv_item2);
            viewHolder.tv_item3 = (TextView)view.findViewById(R.id.tv_item3);
            viewHolder.tv_item4 = (TextView)view.findViewById(R.id.tv_item4);
            viewHolder.tv_item5 = (TextView)view.findViewById(R.id.tv_item5);
            viewHolder.tv_item6 = (TextView)view.findViewById(R.id.tv_item6);
            viewHolder.tv_item7 = (TextView)view.findViewById(R.id.tv_item7);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)convertView.getTag();
        }
        Product product = objects.get(position);
        viewHolder.tv_item1.setText(NumberUtil.beautifulDouble(product.getLast()));
        viewHolder.tv_item2.setText(NumberUtil.beautifulDouble(product.getSell() - product.getLastClose()));
        viewHolder.tv_item3.setText(NumberUtil.beautifulDouble(product.getUpDownRate()));//(product.getSell() - product.getLastClose()) * 100 / product.getSell() + ""
        viewHolder.tv_item4.setText(NumberUtil.beautifulDouble(product.getOpen()));
        viewHolder.tv_item5.setText(NumberUtil.beautifulDouble(product.getHigh()));
        viewHolder.tv_item6.setText(NumberUtil.beautifulDouble(product.getLow()));
        viewHolder.tv_item7.setText(NumberUtil.beautifulDouble(product.getLastClose()));
        return view;
    }

    class ViewHolder {
        TextView tv_item1;
        TextView tv_item2;
        TextView tv_item3;
        TextView tv_item4;
        TextView tv_item5;
        TextView tv_item6;
        TextView tv_item7;
    }

}
