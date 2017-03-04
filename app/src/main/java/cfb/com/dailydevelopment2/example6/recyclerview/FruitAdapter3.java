package cfb.com.dailydevelopment2.example6.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cfb.com.dailydevelopment2.R;

/**
 * 瀑布流布局使用的Adapter
 * Created by fengbincao on 2017/3/4.
 */

public class FruitAdapter3 extends RecyclerView.Adapter<FruitAdapter3.ViewHolder> {

    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            fruitImage = (ImageView) view.findViewById(R.id.fruit_image);
            fruitName = (TextView) view.findViewById(R.id.fruit_name);
        }
    }

    public FruitAdapter3(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }

    // 实现一个RecyclerView.Adapter必须实现的方法1
    @Override
    public FruitAdapter3.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item3, parent, false);
        final FruitAdapter3.ViewHolder holder = new FruitAdapter3.ViewHolder(view);

        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "you clicked image " + fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    // 实现一个RecyclerView.Adapter必须实现的方法2
    @Override
    public void onBindViewHolder(FruitAdapter3.ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    // 实现一个RecyclerView.Adapter必须实现的方法3
    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}
