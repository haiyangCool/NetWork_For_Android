package com.haiyangwang.summer;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.haiyangwang.summer.HomePage.FilmDataKey;
import com.haiyangwang.summer.HomePage.FilmDataReformer;
import com.haiyangwang.summer.HomePage.HomePageApiManager;
import com.haiyangwang.summer.HomePage.HomeServiceErrorReformer;
import com.haiyangwang.summer.NetWork.InterfaceDefines.ApiManagerResultCallBackDelegate;
import com.haiyangwang.summer.NetWork.InterfaceDefines.VVPublicDefines;
import com.haiyangwang.summer.NetWork.VVBaseApiManager;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        ApiManagerResultCallBackDelegate, View.OnClickListener {

    private static final String TAG = "MainActivity";
    private HomePageApiManager homePageApiManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.load);
        btn.setOnClickListener(this);

        // loadData
//        getHomePageApiManager().loadData();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.load) {
            getHomePageApiManager().loadData();
        }
    }

    @Override
    public void managerCallApiDidSuccess(VVBaseApiManager manager) {
        if (manager == homePageApiManager) {
            List<Map<String, String>> filmList = (List<Map<String, String>>) manager.fetchResponseDataWithReformer(new FilmDataReformer());

            for (Map<String, String> filmInfo: filmList) {
                String filmName = filmInfo.get(FilmDataKey.FilmName);
                String filmId = filmInfo.get(FilmDataKey.FilmID);
                String filmImageAdr = filmInfo.get(FilmDataKey.FilmImageAddress);
                Log.d(TAG, "managerCallApiDidSuccess: \n"+filmName
                        +"\n"+filmImageAdr
                        +"\n"+filmId
                        +"\n");
            }

        }
    }

    @Override
    public void managerCallApiManagerFaild(VVBaseApiManager manager) {
        if (manager == homePageApiManager) {
            String faildLog = manager.fetchLog();
            VVPublicDefines.RequestFailureType faildType = manager.fetchFailureType();
            Map<String, String> errorInfo = (Map<String, String>) manager.fetchFaildResponseDataWithReformer(new HomeServiceErrorReformer());
            Log.d(TAG, "managerCallApiManagerFaild: \n 错误类型 ："+faildType.rawValue() +"\n 错误信息" + errorInfo + "\nLog :"+faildLog);
        }
    }


    // Home Api Manager
    public HomePageApiManager getHomePageApiManager() {
        if (homePageApiManager == null) {
            homePageApiManager = new HomePageApiManager();
            homePageApiManager.setDelegate(new WeakReference<>(this));
        }
        return  homePageApiManager;
    }
}

/*

    private ShufflingView mShufflingView;
    private SearchBar mSearchBar;
    private SearchView mSearchView;
    private SearchView tSearchView;

    mShufflingView = findViewById(R.id.shuffling);
    mSearchBar = findViewById(R.id.search_bar);

    mSearchView = new SearchView(this);
    mSearchBar.setSearchUI(new WeakReference<>(mSearchView));
    mSearchBar.setSearchLogic(new WeakReference<>(this));

    mShufflingView.setDataSource(new WeakReference<>(this));
    mShufflingView.loadData();
    mShufflingView.setDelegate(new WeakReference<>(this));*/