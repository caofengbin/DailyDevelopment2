package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的滚动控件
 * 重载了onScrollChange(滚动条变化)，监听每次的变化通知给观察者
 * 对外提供AddOnScrollChangedListener 来订阅本控件的滚动条变化
 * Created by fengbincao on 2017/2/26.
 */

public class ObserverHScrollView extends HorizontalScrollView {

    ScrollViewObserver mScrollViewObserver = new ScrollViewObserver();

    public ObserverHScrollView(Context context) {
        this(context,null);
    }

    public ObserverHScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ObserverHScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    /**
     * 监听控件的滑动变化
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        /*
		 * 当滚动条移动后，引发 滚动事件。通知给观察者，观察者会传达给其他的。
		 */
        if (mScrollViewObserver != null ) {
            mScrollViewObserver.notifyOnScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /*
	 * 订阅 本控件 的 滚动条变化事件
	 * */
    public void addOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.addOnScrollChangedListener(listener);
    }

    /*
     * 取消 订阅 本控件 的 滚动条变化事件
     * */
    public void removeOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.removeOnScrollChangedListener(listener);
    }

    // 观察者，监听滚动事件
    public static class ScrollViewObserver {

        List<OnScrollChangedListener> mList;

        public ScrollViewObserver() {
            super();
            mList = new ArrayList<>();
        }

        /**
         * 订阅 本控件 的 滚动条变化事件
         */
        public void addOnScrollChangedListener(OnScrollChangedListener listener) {
            mList.add(listener);
        }

        /**
         * 取消 订阅 本控件 的 滚动条变化事件
         */
        public void removeOnScrollChangedListener(
                OnScrollChangedListener listener) {
            mList.remove(listener);
        }

        public void notifyOnScrollChanged(int l, int t, int oldl, int oldt) {
            if (mList == null || mList.size() == 0) {
                return;
            }
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i) != null) {
                    mList.get(i).onScrollChanged(l, t, oldl, oldt);
                }
            }
        }
    }

    // 当发生了滚动事件时，调用这个接口
    public static interface OnScrollChangedListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
