package cfb.com.dailydevelopment2.example5.horizontallistview2;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义的 滚动控件
 * 重载了 onScrollChanged（滚动条变化）,监听每次的变化通知给 观察(此变化的)观察者
 * 可使用 AddOnScrollChangedListener 来订阅本控件的 滚动条变化
 * Created by willkong on 2016/11/15.
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
        if (mScrollViewObserver != null /*&& (l != oldl || t != oldt)*/) {
            mScrollViewObserver.NotifyOnScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    /*
	 * 订阅 本控件 的 滚动条变化事件
	 * */
    public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.AddOnScrollChangedListener(listener);
    }

    /*
     * 取消 订阅 本控件 的 滚动条变化事件
     * */
    public void RemoveOnScrollChangedListener(OnScrollChangedListener listener) {
        mScrollViewObserver.RemoveOnScrollChangedListener(listener);
    }


    /**
     * 观察者
     */
    public static class ScrollViewObserver{
        List<OnScrollChangedListener> mList;
        public ScrollViewObserver(){
            super();
            mList = new ArrayList<OnScrollChangedListener>();

        }

        /**
         * 订阅 本控件 的 滚动条变化事件
         * @param listener
         */
        public void AddOnScrollChangedListener(OnScrollChangedListener listener) {
            mList.add(listener);
        }

        /**
         * 取消 订阅 本控件 的 滚动条变化事件
         * @param listener
         */
        public void RemoveOnScrollChangedListener(
                OnScrollChangedListener listener) {
            mList.remove(listener);
        }

        public void NotifyOnScrollChanged(int l, int t, int oldl, int oldt) {
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

    /*
	 * 当发生了滚动事件时,调用这个接口
	 */
    public static interface OnScrollChangedListener {
        public void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private int mLastX;
    private int mLastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean shouldConsume = false;

        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if(deltaX > deltaY)
                    shouldConsume = true;
                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
            default:
                break;
        }

        if(shouldConsume)
            Log.e("test","水平滑动View消耗了事件");
        else {
            Log.e("test","水平滑动View不消耗事件");
        }
        mLastX = x;
        mLastY = y;
        return super.onTouchEvent(event);
    }
}
