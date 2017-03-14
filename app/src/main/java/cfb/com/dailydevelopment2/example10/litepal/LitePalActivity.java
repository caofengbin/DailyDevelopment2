package cfb.com.dailydevelopment2.example10.litepal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import cfb.com.dailydevelopment2.R;

/**
 * 使用LitePal框架进行数据增删查改的核心实现类
 */
public class LitePalActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lite_pal);

		// (1)使用LitePal进行建立数据库的操作
		Button createDatabase = (Button) findViewById(R.id.create_database);
		createDatabase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Connector.getDatabase();
			}
		});

		// (2)使用LitePal进行向数据库添加数据的操作
		Button addData = (Button) findViewById(R.id.add_data);
		addData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Book book = new Book();
				book.setName("The Da Vinci Code");
				book.setAuthor("Dan Brown");
				book.setPages(454);
				book.setPrice(16.96);
				book.setPress("Unknow");
				// 插入数据的核心方法
				book.save();
				Toast.makeText(LitePalActivity.this,"插入数据成功",Toast.LENGTH_LONG).show();
			}
		});

		// (3)使用LitePal更新数据的操作
		Button updateData = (Button) findViewById(R.id.update_data);
		updateData.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Book book = new Book();
				book.setPrice(14.95);
				book.setPress("Anchor");
				book.updateAll("name = ? and author = ?", "The Lost Symbol", "Dan Brown");
			}
		});

		// (4)使用LitePal删除数据的操作
		Button deleteButton = (Button) findViewById(R.id.delete_data);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DataSupport.deleteAll(Book.class,"price < ?", "15");
			}
		});

		// (5)使用LitePal查询数据的操作
		Button queryButton = (Button) findViewById(R.id.query_data);
		queryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<Book> books = DataSupport.findAll(Book.class);
				for(Book book : books) {
					Log.d("MainActivity", "book name is " + book.getName());
					Log.d("MainActivity", "book author is " + book.getAuthor());
					Log.d("MainActivity", "book pages is " + book.getPages());
					Log.d("MainActivity", "book price is " + book.getPrice());
					Log.d("MainActivity", "book press is " + book.getPress());
				}
			}
		});
	}
}
