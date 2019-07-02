package com.haiyangwang.summer.ShufflingView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/* 轮播图*/
public class ShufflingView extends FrameLayout implements
        ViewPager.OnPageChangeListener{

    public interface ShufflingViewDataSource {
        /* item 数量*/
        int getItemCount();
        /* item View*/
        ShufflingItem getItemView(int itemIndex, @Nullable ShufflingItem item);
    }

    public interface ShufflingViewDelegate {
        void itemBeClickedAtIndex(int index);
    }

    private static final String TAG = "ShufflingView";
    private WeakReference<ShufflingViewDataSource> mDataSource;
    private WeakReference<ShufflingViewDelegate> mDelegate;
    /* 轮播容器*/
    private ViewPager mViewPager;
    /* Left item*/
    private ShufflingItem mLeftItem;
    /* Center item*/
    private ShufflingItem mCenterItem;
    /* Right item*/
    private ShufflingItem mRightItem;


    /* 当前选中的itemIndex 默认选中第一个*/
    private int mCurrentItemIndex = 0;
    /* ViewPager id*/
    private int mViewPagerId = 1011;
    /* 是否需要刷新*/
    private boolean mIsNeedRefresh = true;
    /* ViewGroup list*/
    private List<ViewGroup> mItemList = new ArrayList<>();
    /* itemCount*/
    private int mItemCount = 0;

    /* 自动切换item*/
    private Timer mTimer;
    /* 延时刷新*/
    private Timer mRefreshTime;

    private Context context;
    public ShufflingView(Context context) {
        super(context);
        this.context = context;
        configureShuffling();
    }

    public ShufflingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        configureShuffling();
    }

    /* Listener methods*/

    //OnPageChangeListener
    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    @Override
    public void onPageSelected(int i) {

        if (!mIsNeedRefresh) {return;}
        // >>>> i > 1
        if (i > 1) {
            mCurrentItemIndex += 1;
        }
        // <<<< i < 1
        if (i < 1) {
            mCurrentItemIndex -= 1;
        }
        // warning
        if (mCurrentItemIndex == mItemCount) {mCurrentItemIndex = 0;}
        if (mCurrentItemIndex < 0) {mCurrentItemIndex = mItemCount-1;}


        delayRefresh();
//        mIsNeedRefresh = false;
//        refreshShowData();
//        mIsNeedRefresh = true;
//        startTimer();
    }

    @Override
    public void onPageScrollStateChanged(int i) {
        stopAutoTimer();
    }

    /* Public methods*/
    public void loadData() {
        configureShuffling();
    }


    public void stopTime() {
      stopAutoTimer();
      stopRefreshTimer();
    }

    public void setDataSource(WeakReference<ShufflingViewDataSource> mDataSource) {
        this.mDataSource = mDataSource;
    }

    public void setDelegate(WeakReference<ShufflingViewDelegate> mDelegate) {
        this.mDelegate = mDelegate;
    }

    public void setIsNeedRefresh(boolean mIsNeedRefresh) {
        this.mIsNeedRefresh = mIsNeedRefresh;
    }
    /* Private methods*/

    /* refresh data*/
    private void refreshShowData() {

        int leftPageIndex = 0;
        int rightPageIndex = 1;

        // get left Index
        leftPageIndex = (mCurrentItemIndex + mItemCount-1) % mItemCount;
        // get right index
        rightPageIndex = (mCurrentItemIndex + 1) % mItemCount;

        mLeftItem = mDataSource.get().getItemView(leftPageIndex,mLeftItem);
        mCenterItem = mDataSource.get().getItemView(mCurrentItemIndex,mCenterItem);
        mRightItem = mDataSource.get().getItemView(rightPageIndex,mRightItem);


        // 重置center item
        mViewPager.setCurrentItem(1,false);

    }

    private void configureShuffling() {

        if (mDataSource != null) {
           mItemCount = mDataSource.get().getItemCount();
           if (mItemCount <= 0) {
               return;
           }

           int leftIndex = 0, centerIndex = 0, rightIndex = 0;
           if (mItemCount == 1) {
               leftIndex = 0;
               centerIndex = 0;
               rightIndex = 0;
           }
           if (mItemCount == 2) {
               leftIndex = 1;
               centerIndex = 0;
               rightIndex = 1;
           }

           if (mItemCount > 2) {
               leftIndex = mItemCount-1;
               centerIndex = 0;
               rightIndex = 1;
           }
            // 超过2条数据
           mLeftItem = mDataSource.get().getItemView(leftIndex,mLeftItem);
           mCenterItem = mDataSource.get().getItemView(centerIndex,mCenterItem);
           mRightItem = mDataSource.get().getItemView(rightIndex,mRightItem);
           initUI();
        }
    }

    /* View Pager */
    private void initUI() {

        mItemList.add(mLeftItem);
        mItemList.add(mCenterItem);
        mItemList.add(mRightItem);

        ShufflingViewAdapter adapter = new ShufflingViewAdapter(mItemList);

        mViewPager = new ViewPager(context);
        mViewPager.setId(mViewPagerId);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        this.addView(mViewPager,layoutParams);

        // set center item be showing always
        mViewPager.setCurrentItem(1,false);

    }


    private void startTimer() {

        if (mTimer == null) {
            mTimer = new Timer();
            mTimer.schedule(new ShufflingAutoChangeTask(new WeakReference<>(this)),4*1000,200);
        }
    }

    private void delayRefresh() {
        stopRefreshTimer();
        if (mRefreshTime == null) {
            mRefreshTime = new Timer();
            mRefreshTime.schedule(new ShufflingDelayFreshTask(new WeakReference<>(this)),200);
        }

    }

    private void stopAutoTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    private void stopRefreshTimer() {
        if (mRefreshTime != null) {
            mRefreshTime.cancel();
            mRefreshTime = null;
        }
    }

    private static class ShufflingAutoChangeTask extends TimerTask {
        private WeakReference<ShufflingView> mWeakReference;
        public ShufflingAutoChangeTask(WeakReference<ShufflingView> weakReference) {
            this.mWeakReference = weakReference;
        }

        @Override
        public void run() {

            if (mWeakReference != null) {
                ShufflingView view = mWeakReference.get();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.mViewPager.setCurrentItem(2,true);
                    }
                });
            }

        }
    }

    private static class ShufflingDelayFreshTask extends TimerTask {

        private WeakReference<ShufflingView> mWakReference;
        public ShufflingDelayFreshTask(WeakReference<ShufflingView> weakReference) {
            this.mWakReference = weakReference;
        }

        @Override
        public void run() {

            if (mWakReference != null) {
                ShufflingView view = mWakReference.get();
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.setIsNeedRefresh(false);
                        view.refreshShowData();
                        view.setIsNeedRefresh(true);
                        view.startTimer();
                    }
                });
            }
        }
    }
}
