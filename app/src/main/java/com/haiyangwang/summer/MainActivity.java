package com.haiyangwang.summer;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.haiyangwang.summer.HomePage.HomePageApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.SearchBar.SearchBar;
import com.haiyangwang.summer.SearchBar.SearchView;
import com.haiyangwang.summer.ShufflingView.ShufflingItem;
import com.haiyangwang.summer.ShufflingView.ShufflingView;


import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements
        ApiManagerResultCallBackDelegate,
        ShufflingView.ShufflingViewDataSource,
        ShufflingView.ShufflingViewDelegate,
        SearchBar.SearchLogic {

    private static final String TAG = "MainActivity";
    private HomePageApiManager homePageApiManager;


    private ShufflingView mShufflingView;
    private SearchBar mSearchBar;
    private SearchView mSearchView;
    private SearchView tSearchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getHomePageApiManager().loadData();

        mShufflingView = findViewById(R.id.shuffling);
        mSearchBar = findViewById(R.id.search_bar);

        mSearchView = new SearchView(this);
        mSearchBar.setSearchUI(new WeakReference<>(mSearchView));
        mSearchBar.setSearchLogic(new WeakReference<>(this));

        mShufflingView.setDataSource(new WeakReference<>(this));
        mShufflingView.loadData();
        mShufflingView.setDelegate(new WeakReference<>(this));


    }

    @Override
    public int getItemCount() {
        return 9;
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
    public void itemBeClickedAtIndex(int index) {
        Log.d(TAG,"item 选中了"+index);
    }

    @Override
    public void doSearchWithText(String text) {
        Log.d(TAG,"搜搜"+text);
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
