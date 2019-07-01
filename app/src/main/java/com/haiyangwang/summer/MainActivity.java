package com.haiyangwang.summer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Map<String,String > cache = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        cache.put("DATA","i want a apple");
        cache.put("CACHETIME","1");
        cache.put("UPDATETIME","12");

        try{
            JSONObject json = new JSONObject(cache);

            Log.d(TAG,"json 数据  = \t"+json);
        } catch (Exception e) {
            Log.d(TAG,"json 数据  = \t"+e.getMessage());
        }

    }
}
