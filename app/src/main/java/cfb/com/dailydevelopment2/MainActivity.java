package cfb.com.dailydevelopment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cfb.com.dailydevelopment2.example1.loadimage.LoadImageActivity;
import cfb.com.dailydevelopment2.example10.litepal.LitePalActivity;
import cfb.com.dailydevelopment2.example2.progressbar.ProgressBarActivity;
import cfb.com.dailydevelopment2.example3.testlayout.TestLayoutActivity;
import cfb.com.dailydevelopment2.example4.horizontallistview.HorizontalListActivity;
import cfb.com.dailydevelopment2.example5.horizontallistview2.HorizontalListActivity2;
import cfb.com.dailydevelopment2.example6.recyclerview.RecyclerViewActivity;
import cfb.com.dailydevelopment2.example7.local.refresh.ChatFrameActivity;
import cfb.com.dailydevelopment2.example8.storedata.StoreDataActivity;
import cfb.com.dailydevelopment2.example9.use.sqlite.SQLiteActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] mainItems;
    private ListView mMainListView;
    private ArrayAdapter<String> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainItems = getResources().getStringArray(R.array.main_view_string_array);
        mMainListView = (ListView) findViewById(R.id.main_activity_listView);
        itemAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mainItems);

        mMainListView.setAdapter(itemAdapter);
        mMainListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                startIntent(LoadImageActivity.class);
                break;
            case 1:
                startIntent(ProgressBarActivity.class);
                break;
            case 2:
                startIntent(TestLayoutActivity.class);
                break;
            case 3:
                startIntent(HorizontalListActivity.class);
                break;
            case 4:
                startIntent(HorizontalListActivity2.class);
                break;
            case 5:
                startIntent(RecyclerViewActivity.class);
                break;
            case 6:
                startIntent(ChatFrameActivity.class);
                break;
            case 7:
                startIntent(StoreDataActivity.class);
                break;
            case 8:
                startIntent(SQLiteActivity.class);
                break;
            case 9:
                startIntent(LitePalActivity.class);
                break;
        }
    }

    private void startIntent(Class class1){
        Intent intent = new Intent(MainActivity.this,class1);
        startActivity(intent);
    }
}
