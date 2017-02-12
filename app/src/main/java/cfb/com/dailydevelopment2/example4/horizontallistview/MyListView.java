package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ScrollView中嵌套ListView,达到使ListView自适应高度
 * 否则直接用ListView会出现ListView只显示一行的问题
 * Created by caofengbin on 2017/2/12.
 */

public class MyListView extends ListView {

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 重写该方法，ScrollView中嵌套ListView
     * 达到使ListView自适应高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int myHeightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, myHeightMeasureSpec);
    }
}
