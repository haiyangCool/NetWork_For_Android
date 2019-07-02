package com.haiyangwang.summer.SearchBar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.haiyangwang.summer.R;

import java.lang.ref.WeakReference;

/** 通过组合searchUI 和 searchLogic 组成searchBar
 * 实现任意搜索UI和搜索逻辑的组合*/
public class SearchBar extends FrameLayout {
    private static final String TAG = "SearchBar";

    /** any obj 实现该接口都可以作为searchUI*/
    public interface SearchUI {

        // 获取UI
        ViewGroup getSearchUI();
        // 获取搜索值
        String getSearchText();
        /* 在实现搜索UI的obj中，保留SearchBar的弱引用，可以通过 search(text)进行实时搜索*/
        void searchReference(WeakReference<SearchBar> searchBarWeakReference);
    }

    /** any obj 实现该协议即可作为具体的搜索逻辑
     * example: 本地搜索逻辑or服务器搜索逻辑*/
    public interface SearchLogic {
        void doSearchWithText(String text);
    }

    private WeakReference<SearchUI> mSearchUI;
    private WeakReference<SearchLogic> mSearchLogic;
    private Context context;
    private int contentViewID = 1001;
    private int searchBtnID = 1002;
    // Bar 容器
    private RelativeLayout contentView;
    private Button searchBtn;
    public SearchBar(Context context) {
        super(context);
        this.context = context;
        initUI();
    }

    public SearchBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initUI();
    }

    /** Public methods*/
    public void setSearchUI(WeakReference<SearchUI> searchUI) {
        this.mSearchUI = searchUI;

        mSearchUI.get().searchReference(new WeakReference<>(this));
        ViewGroup searchV = mSearchUI.get().getSearchUI();
        Log.d(TAG,"searchView is = "+searchV);
        RelativeLayout.LayoutParams searchVLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        searchVLayout.addRule(RelativeLayout.ALIGN_PARENT_LEFT,contentView.getId());
        searchVLayout.addRule(RelativeLayout.START_OF,searchBtn.getId());
        searchVLayout.setMargins(10,4,10,4);


        contentView.addView(searchV,searchVLayout);
    }

    public void setSearchLogic(WeakReference<SearchLogic> mSearchLogic) {
        this.mSearchLogic = mSearchLogic;
    }

    public void search(String text) {
        if (mSearchLogic != null) {
            mSearchLogic.get().doSearchWithText(text);
        }
    }

    /** Private methods*/
    private void initUI() {

        contentView = new RelativeLayout(context);
        contentView.setId(contentViewID);
        contentView.setBackgroundResource(R.color.colorAccent);
        RelativeLayout.LayoutParams contentViewLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(contentView,contentViewLayout);

        searchBtn = new Button(context);
        searchBtn.setId(searchBtnID);
        searchBtn.setText("搜索");
        RelativeLayout.LayoutParams searchBrnLayout = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        searchBrnLayout.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,contentView.getId());
        contentView.addView(searchBtn,searchBrnLayout);

        searchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSearchLogic != null) {
                    if (mSearchUI != null) {
                        mSearchLogic.get().doSearchWithText(mSearchUI.get().getSearchText());
                    }
                }
            }
        });
    }

}
