package com.haiyangwang.summer;

import android.nfc.Tag;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.haiyangwang.summer.HomePage.HomePageApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.ShufflingView.ShufflingItem;
import com.haiyangwang.summer.ShufflingView.ShufflingView;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ApiManagerResultCallBackDelegate,
        ShufflingView.ShufflingViewDataSource {

    private static final String TAG = "MainActivity";
    private HomePageApiManager homePageApiManager;


    private ShufflingView mShufflingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getHomePageApiManager().loadData();

        mShufflingView = findViewById(R.id.shuffling);
        mShufflingView.setDataSource(new WeakReference<>(this));
        mShufflingView.loadData();

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public ShufflingItem getItemView(int itemIndex, @Nullable ShufflingItem item) {

        ShufflingItem shufflingItem = null;
        if (item != null) {
            shufflingItem = item;
        }else {
            shufflingItem = new ShufflingItem(this);
        }
        shufflingItem.setIdentifier("我的数据是"+itemIndex);
        return shufflingItem;
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
