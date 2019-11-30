package com.haiyangwang.summer;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import com.haiyangwang.summer.HomePage.FilmDataKey;
import com.haiyangwang.summer.HomePage.FilmDataReformer;
import com.haiyangwang.summer.HomePage.HomePageApiManager;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;
import com.haiyangwang.summer.NetWork.VVURLResponse;
import com.haiyangwang.summer.SearchBar.SearchBar;
import com.haiyangwang.summer.SearchBar.SearchView;
import com.haiyangwang.summer.ShufflingView.ShufflingItem;
import com.haiyangwang.summer.ShufflingView.ShufflingView;


import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ApiManagerResultCallBackDelegate{

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

        // loadData
        getHomePageApiManager().loadData();

    }


    @Override
    public void managerCallApiDidSuccess(VVBaseApiManager manager) {
        if (manager == homePageApiManager) {
            Map<String, Object> dataMap = (Map<String, Object>) manager.fetchResponseDataWithReformer(new FilmDataReformer());

            List filmList = (List) dataMap.get(FilmDataKey.FilmInfo);
            // test
            Map<String, String> filmInfo = (Map<String, String>) filmList.get(0);
            String filmName = filmInfo.get(FilmDataKey.FilmName);
            String filmId = filmInfo.get(FilmDataKey.FilmID);
            String filmImageAdr = filmInfo.get(FilmDataKey.FilmImageAddress);


            Log.d(TAG, "managerCallApiDidSuccess: 影片信息："+"\n"+"name:"+filmName+"\n"
                    +"id:"+filmId+"\n"
                    +"imageAddress:"+filmImageAdr+"\n");


        }
    }

    @Override
    public void managerCallApiManagerFaild(VVBaseApiManager manager) {
        if (manager == homePageApiManager) {
            String faildReason = manager.fetchLog();
            String faildType = manager.fetchFailureType();
            Log.d(TAG, "managerCallApiManagerFaild: "+faildReason);
        }
    }


    // Home Api Manager
    public HomePageApiManager getHomePageApiManager() {
        if (homePageApiManager == null) {
            homePageApiManager = new HomePageApiManager();
            homePageApiManager.setDelegate(this);
        }
        return  homePageApiManager;
    }
}

/*   mShufflingView = findViewById(R.id.shuffling);
        mSearchBar = findViewById(R.id.search_bar);

        mSearchView = new SearchView(this);
        mSearchBar.setSearchUI(new WeakReference<>(mSearchView));
        mSearchBar.setSearchLogic(new WeakReference<>(this));

        mShufflingView.setDataSource(new WeakReference<>(this));
        mShufflingView.loadData();
        mShufflingView.setDelegate(new WeakReference<>(this));*/