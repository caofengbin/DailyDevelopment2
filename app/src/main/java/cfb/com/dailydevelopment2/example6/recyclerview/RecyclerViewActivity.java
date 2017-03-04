package cfb.com.dailydevelopment2.example6.recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cfb.com.dailydevelopment2.R;

public class RecyclerViewActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String[] mainItems;
    private ListView mMainListView;
    private ArrayAdapter<String> itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        mainItems = getResources().getStringArray(R.array.recycler_view_string_array);
        mMainListView = (ListView) findViewById(R.id.recycler_view_list);
        itemAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, mainItems);

        mMainListView.setAdapter(itemAdapter);
        mMainListView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        switch (position) {
            case 0:
                startIntent(UseRecyclerViewActivity.class);
                break;
            case 1:
                startIntent(UseRecyclerViewActivity2.class);
                break;
            case 2:
                startIntent(UseRecyclerViewActivity3.class);
                break;
        }
    }

    private void startIntent(Class class1){
        Intent intent = new Intent(RecyclerViewActivity.this,class1);
        startActivity(intent);
    }
}
