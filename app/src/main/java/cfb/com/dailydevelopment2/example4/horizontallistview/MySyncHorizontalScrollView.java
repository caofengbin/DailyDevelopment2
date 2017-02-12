package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * 自定义的HorizontalScrollView，可以用来设置联动的对象
 * Created by caofengbin on 2017/2/12.
 */

public class MySyncHorizontalScrollView extends HorizontalScrollView {

    // 联动的View对象
    private View mSyncView;

    public MySyncHorizontalScrollView(Context context) {
        super(context);
    }

    public MySyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySyncHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     *
     * @param l                   Current horizontal scroll origin.
     * @param t                   Current vertical scroll origin.
     * @param oldHorizontal       Previous horizontal scroll origin.
     * @param oldVertical         Previous vertical scroll origin.
     */
    protected void onScrollChanged(int l, int t, int oldHorizontal, int oldVertical) {
        super.onScrollChanged(l, t, oldHorizontal, oldVertical);
        // 让需要联动的View跟着联动
        if(mSyncView != null) {
            mSyncView.scrollTo(l,t);
        }
    }

    public View getMySyncView() {
        return mSyncView;
    }

    public void setSyncView(View view) {
        mSyncView = view;
    }
}
