package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 一个视图容器控件
 * 阻止拦截onTouch事件传递给其子控件
 * Created by fengbincao on 2017/2/26.
 */

public class ObserverHScrollViewIntercept extends LinearLayout {

    public ObserverHScrollViewIntercept(Context context) {
        this(context,null);
    }

    public ObserverHScrollViewIntercept(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ObserverHScrollViewIntercept(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    // 直接拦截onTouche事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
