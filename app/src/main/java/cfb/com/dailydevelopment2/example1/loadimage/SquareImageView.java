package cfb.com.dailydevelopment2.example1.loadimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义的一个正方形的ImageView，为了实际在照片墙中显示的美观
 * Created by fengbincao on 2017/2/7.
 */

public class SquareImageView extends ImageView {

    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 这里直接重写onMeasure，
    // 是图片的宽和高相等
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
