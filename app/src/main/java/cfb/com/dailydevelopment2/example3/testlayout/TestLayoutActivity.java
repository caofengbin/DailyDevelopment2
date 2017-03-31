package cfb.com.dailydevelopment2.example3.testlayout;

import android.os.Bundle;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import cfb.com.dailydevelopment2.R;

public class TestLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percent_layout);

        PercentRelativeLayout container = (PercentRelativeLayout) findViewById(R.id.myContainer);

        LayoutInflater layoutInflater = LayoutInflater.from(this);

        View myLayout = layoutInflater.inflate(R.layout.holder_layout, null);
        PercentRelativeLayout.LayoutParams params1 = new PercentRelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        PercentLayoutHelper.PercentLayoutInfo info = params1.getPercentLayoutInfo();
        info.widthPercent = 0.30f;

        container.addView(myLayout,params1);

    }
}
