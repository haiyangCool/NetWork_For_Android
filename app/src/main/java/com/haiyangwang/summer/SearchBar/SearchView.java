package com.haiyangwang.summer.SearchBar;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.haiyangwang.summer.R;

import java.lang.ref.WeakReference;

public class SearchView extends FrameLayout implements
        SearchBar.SearchUI, TextWatcher {
    private Context context;
    private EditText mEditText;
    private String mWaitSearchText;
    private WeakReference<SearchBar> mSearchBarWeakReference;
    public SearchView(Context context) {
        super(context);
        this.context = context;
        mWaitSearchText = "";

        initUI();
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mWaitSearchText = "";
        initUI();

    }

    /** Delegate methods*/
    //SearchUI
    @Override
    public ViewGroup getSearchUI() {
        return this;
    }

    @Override
    public String getSearchText() {
        return mWaitSearchText;
    }

    @Override
    public void searchReference(WeakReference<SearchBar> searchBarWeakReference) {
        this.mSearchBarWeakReference = searchBarWeakReference;
    }

    //TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        this.mWaitSearchText = s.toString();
        if (mSearchBarWeakReference != null) {
            mSearchBarWeakReference.get().search(mWaitSearchText);
        }
    }

    /**Public methods*/
    private void initUI() {
        LayoutInflater.from(context).inflate(R.layout.search_view,this);
        mEditText = findViewById(R.id.edit_text);
        mEditText.addTextChangedListener(this);

    }

    public void setWaitSearchText(String mWaitSearchText) {
        this.mWaitSearchText = mWaitSearchText;
    }
}
