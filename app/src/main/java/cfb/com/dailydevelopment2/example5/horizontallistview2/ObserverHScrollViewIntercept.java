package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 *一个视图容器控件
 * 阻止 拦截 ontouch事件传递给其子控件
 * Created by willkong on 2016/11/15.
 */

public class ObserverHScrollViewIntercept extends LinearLayout{
    public ObserverHScrollViewIntercept(Context context) {
        this(context,null);
    }

    public ObserverHScrollViewIntercept(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ObserverHScrollViewIntercept(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
