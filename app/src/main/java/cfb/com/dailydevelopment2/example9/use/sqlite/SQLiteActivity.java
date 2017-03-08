package cfb.com.dailydevelopment2.example9.use.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfb.com.dailydevelopment2.R;

public class SQLiteActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;

    @BindView(R.id.create_database)
    public Button createDatabase;

    @BindView(R.id.add_data)
    public Button addData;

    @BindView(R.id.update_data)
    public Button updateData;

    @BindView(R.id.query_data)
    public Button queryData;

    @BindView(R.id.delete_data)
    public Button deleteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 2);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.create_database)
    public void createDatabase() {
        // 会发现只有在App第一次运行的时候才会执行一次建表语句
        dbHelper.getWritableDatabase();
    }

    // 删除数据的操作
    @OnClick(R.id.delete_data)
    public void deleteData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Book", "pages > ?", new String[] { "500" });
    }
    // 增加数据的操作
    @OnClick(R.id.add_data)
    public void addData() {
        // 向表中增加数据的方法
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        // 插入第一条数据
        values.put("name", "The Da Vinci Code");
        values.put("author", "Dan Brown");
        values.put("pages", 454);
        values.put("price", 16.96);
        db.insert("book", null, values);

        values.clear();

        values.put("name", "The Lost Symbol");
        values.put("author", "Dan Brown");
        values.put("pages", 510);
        values.put("price", 19.95);
        db.insert("Book", null, values); // 插入第二条数据
    }

    // 修改数据库的操作
    @OnClick(R.id.update_data)
    public void updateData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("price", 10.99);
        db.update("Book", values, "name = ?", new String[] { "The Da Vinci Code" });
    }

    // 查询数据
    @OnClick(R.id.query_data)
    public void queryData() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // 查询Book表中所有的数据
        Cursor cursor = db.query("Book", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 遍历Cursor对象，取出数据并打印
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String author = cursor.getString(cursor.getColumnIndex("author"));
                int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                double price = cursor.getDouble(cursor.getColumnIndex("price"));
                Log.d("MainActivity", "book name is " + name);
                Log.d("MainActivity", "book author is " + author);
                Log.d("MainActivity", "book pages is " + pages);
                Log.d("MainActivity", "book price is " + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}
