package com.haiyangwang.summer.ShufflingView;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/* A simple adapter*/
public class ShufflingViewAdapter extends PagerAdapter implements View.OnClickListener {

    public interface ShufflingViewAdapterDelegate {
        void itemBeClicked();
    }

    private WeakReference<ShufflingViewAdapterDelegate> mDelegate;
    private List<ShufflingItem> mList = new ArrayList<>();
    public ShufflingViewAdapter(List<ShufflingItem> list) {
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ShufflingItem item = mList.get(position);
        container.addView(item);
        item.setOnClickListener(this);
        return mList.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView(mList.get(position));
    }

    @Override
    public void onClick(View v) {
        if (mDelegate != null) {
            mDelegate.get().itemBeClicked();
        }
    }


    public void setDelegate(WeakReference<ShufflingViewAdapterDelegate> mDelegate) {
        this.mDelegate = mDelegate;
    }
}
