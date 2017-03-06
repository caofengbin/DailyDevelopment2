package cfb.com.dailydevelopment2.example8.storedata;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cfb.com.dailydevelopment2.R;

public class StoreDataActivity extends AppCompatActivity {

    @BindView(R.id.edit)
    public EditText edit;

    @BindView(R.id.save_data)
    public Button saveDataButton;

    @BindView(R.id.restore_data)
    public Button restoreDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_data);

        ButterKnife.bind(this);

        String inputText = load();
        if(!TextUtils.isEmpty(inputText)) {
            edit.setText(inputText);
            edit.setSelection(inputText.length());
            Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
        }


    }

    @OnClick(R.id.save_data)
    public void saveDataToSharedPreference() {
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putString("name","Tom");
        editor.putInt("age",28);
        editor.putBoolean("married",false);
        editor.apply();
        Toast.makeText(this,"保存到SharedPreference成功",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.restore_data)
    public void getDataToSharedPreference() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String name = pref.getString("name", "");
        int age = pref.getInt("age", 0);
        boolean married = pref.getBoolean("married", false);
        Log.d("MainActivity", "name is " + name);
        Log.d("MainActivity", "age is " + age);
        Log.d("MainActivity", "married is " + married);
        Toast.makeText(this,"从SharedPreference读取成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 在界面销毁时存储相关的数据
        String inputText = edit.getText().toString();
        save(inputText);
    }

    /**
     * 存储数据的主要方法
     * @param inputText
     */
    public void save(String inputText) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取数据的主要方法
     * @return
     */
    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}
