package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cfb.com.dailydevelopment2.R;

/**
 * 左边一栏数据的Adapter
 * Created by caofengbin on 2017/2/12.
 */

public class LeftAdapter extends ArrayAdapter<Product> {

    List<Product> objects;

    public LeftAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();

    }

    class ViewHolder {
        TextView tv_item;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            view = View.inflate(getContext(), R.layout.item_left,null);
            viewHolder.tv_item = (TextView) view.findViewById(R.id.tv_item);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item.setText(objects.get(position).getName());
        return view;
    }
}
