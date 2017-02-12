package cfb.com.dailydevelopment2.example4.horizontallistview;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 从本地的json文件中获取数据的工具类
 * Created by caofengbin on 2017/2/12.
 */

public class DataUtil {

    public static List<Product> getData (Context context) {
        try {
            InputStream in = context.getResources().getAssets().open("data.json");
            String res = convertStreamToString(in);
            Gson gson = new Gson();
            List<Product> list = gson.fromJson(res, new TypeToken<ArrayList<Product>>(){}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String convertStreamToString(InputStream is) {
        /*
        * To convert the InputStream to String we use the BufferedReader.readLine()
        * method. We iterate until the BufferedReader return null which means
        * there's no more data to read. Each line will appended to a StringBuilder
        * and returned as String.
        */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}
