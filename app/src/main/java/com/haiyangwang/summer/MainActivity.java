package com.haiyangwang.summer;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.haiyangwang.summer.HomePage.HomePageApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ApiManagerResultCallBackDelegate {

    private static final String TAG = "MainActivity";
    private HomePageApiManager homePageApiManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getHomePageApiManager().loadData();

    }

    @Override
    public void managerCallApiDidSuccess(VVBaseApiManager manager) {

        if (manager.equals(HomePageApiManager.class)) {
            Log.d(TAG,"success"+manager.fetchResponseDataWithReformer(null));
        }
    }

    @Override
    public void managerCallApiManagerFaild(VVBaseApiManager manager) {

        if (manager.getClass().equals(HomePageApiManager.class)) {
            Log.d(TAG,"HomePageApiManager faild"+manager);
        }

        if (manager.getClass().equals(HomePageApiManager.class)) {
            Log.d(TAG,"HomePageApiManager faild"+manager.fetchFailureReason());
        }
    }

    public HomePageApiManager getHomePageApiManager() {
        if (homePageApiManager == null) {
            homePageApiManager = new HomePageApiManager();
            homePageApiManager.setmDelegate(new WeakReference<>(this));
        }
        return  homePageApiManager;
    }
}
