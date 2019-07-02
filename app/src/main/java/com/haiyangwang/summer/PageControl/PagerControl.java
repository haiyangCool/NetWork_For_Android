package com.haiyangwang.summer.PageControl;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haiyangwang.summer.R;

import java.util.ArrayList;

/*
 * XML 配置
 * 代码 配置
 * 考虑到一般页码控制都在10个以内，所以页码控制只显示在一行 30dp
 * example:
 * 1\PagerControl pagerControl = new PagerControl(context);
 * 2\<PagerControl>
 *     ……
 * </PagerControl>
 * */
public class PagerControl extends RelativeLayout implements View.OnClickListener {
    public interface PagerControlDelegate {
        // be selected page(currentPage) index
        public void pagerControl(PagerControl pagerControl,int index);
    }

    private Context context;
    // indicator id began
    private static final int indicatorIdBegan = 985;
    // indicator item width
    private static final int itemWidth = 30;
    // indicator item height
    private static final int itemHeight = 30;
    // indicator item gap
    private static final int itemGap = 10;

    // center line
    private ImageView centerLine;

    /* 总页码*/
    private int numberOfPages = 0;
    /* 当前页码*/
    private int currentPage = 0;
    /* 仅有一个时是否隐藏*/
    private boolean hiddenForSinglePage;

    /* 页码颜色*/
    private int pageIndicatorTintColor;

    /* 当前选中的页码颜色*/
    private int currentPageIndicatorTintColor;

    /* 当前被选中的页码*/
    private Button currentIndicator;
    /* indicator list*/
    private ArrayList<Button> indicatorList = new ArrayList<>();
    /* delegate*/
    private PagerControlDelegate delegate;

    public PagerControl(Context context) {
        super(context);
        this.context = context;

        // Default configure set
        defaultConfigure();

    }

    public PagerControl(Context context, AttributeSet attrs) {
        super(context,attrs);
        this.context = context;
        defaultConfigure();
        // XML
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,R.styleable.PagerControl,0,0);
        setAttrs(typedArray);
    }

    /** View.OnClickListener Methods*/
    @Override
    public void onClick(View v) {

        int indicatorId = v.getId();
        if (indicatorId < 0) {return;}

        updateSelectedPage(indicatorId);
        if (delegate != null) {
            delegate.pagerControl(this,indicatorId-indicatorIdBegan);
        }
    }

    /******************************
     * Private method*/
    // init indicator
    private void initIndicator() {
        if (numberOfPages == 0) {
            this.setVisibility(GONE);
            return;
        }
        if (numberOfPages == 1 && hiddenForSinglePage) {
            this.setVisibility(GONE);
            return;
        }
        if (numberOfPages == 1) {
            // indicator at center
            Button indicator = new Button(context);
            indicator.setId(indicatorIdBegan);
            indicator.setBackgroundColor(currentPageIndicatorTintColor);
            currentIndicator = indicator;
            LayoutParams layoutParams = new LayoutParams(itemWidth,itemHeight);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            this.addView(indicator,layoutParams);

        }else {
            //
            for (int i = 0; i < numberOfPages; i++) {
                Button indicator = new Button(context);
                indicator.setBackgroundColor(pageIndicatorTintColor);
                indicator.setId(indicatorIdBegan+i);
                LayoutParams layoutParams = new LayoutParams(itemWidth,itemHeight);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                layoutParams.setMargins((itemWidth+itemGap)*i,0,0,0);
                this.addView(indicator,layoutParams);
                if (indicator.getId()-indicatorIdBegan == currentPage) {
                    indicator.setBackgroundColor(currentPageIndicatorTintColor);
                    currentIndicator = indicator;
                }
                indicator.setOnClickListener(this);
                indicatorList.add(indicator);

            }
        }
    }

    // update selected(current) page
    private void updateSelectedPage(int pageId) {

        if (currentIndicator == null) { return; }
        currentIndicator.setBackgroundColor(pageIndicatorTintColor);
        Button newCi = findViewById(pageId);
        currentIndicator = newCi;
        currentIndicator.setBackgroundColor(currentPageIndicatorTintColor);
    }

    // update pageIndicator color
    private void updateIndicatorColor() {

        for (Button btn:indicatorList) {
            if (currentIndicator == null){return;}
            if (btn.getId() != currentIndicator.getId()) {
                btn.setBackgroundColor(pageIndicatorTintColor);
            }
        }
    }

    // update current pageIndicator color
    private void updateCurrentIndicatorColor() {

        if (currentIndicator == null) {return;}
        currentIndicator.setBackgroundColor(currentPageIndicatorTintColor);
    }
    // default Configure
    private void defaultConfigure() {
        numberOfPages = 0;
        currentPage = 0;
        hiddenForSinglePage = false;
        pageIndicatorTintColor = Color.parseColor("#ABABAB");
        currentPageIndicatorTintColor = Color.parseColor("#FFFFFF");
    }

    /**
     * XML setter*/
    private void setAttrs(TypedArray typedArray) {
        for (int i = 0; i < typedArray.getIndexCount() ; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.PagerControl_NumberOfPages:
                    int num = typedArray.getInteger(attr,0);
                    setNumberOfPages(num);
                    break;
                case R.styleable.PagerControl_CurrentPage:
                    int cn = typedArray.getInteger(attr,0);
                    setCurrentPage(cn);
                    break;
                case R.styleable.PagerControl_HiddenForSinglePage:
                    boolean hfsp = typedArray.getBoolean(attr,false);
                    setHiddenForSinglePage(true);
                    setHiddenForSinglePage(hfsp);
                    break;
                case R.styleable.PagerControl_PageIndicatorTintColor:
                    String colorStr = typedArray.getString(attr);
                    int color = Color.parseColor(colorStr);
                    setPageIndicatorTintColor(color);
                    break;
                case R.styleable.PagerControl_CurrentPageIndicatorTintColor:
                    String cpic = typedArray.getString(attr);
                    int cpcc = Color.parseColor(cpic);
                    setCurrentPageIndicatorTintColor(cpcc);
                    break;

            }
        }
    }

    /******************************
     *  Getter and Setter*/
    // getter
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageIndicatorTintColor() {
        return pageIndicatorTintColor;
    }

    public int getCurrentPageIndicatorTintColor() {
        return currentPageIndicatorTintColor;
    }

    // setter

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
        initIndicator();
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;

        updateSelectedPage(currentPage+indicatorIdBegan);
    }

    public void setHiddenForSinglePage(boolean hiddenForSinglePage) {
        this.hiddenForSinglePage = hiddenForSinglePage;
        if (numberOfPages == 1) {
            this.setVisibility(GONE);
        }
    }

    public void setPageIndicatorTintColor(int pageIndicatorTintColor) {
        this.pageIndicatorTintColor = pageIndicatorTintColor;
        updateIndicatorColor();
    }

    public void setCurrentPageIndicatorTintColor(int currentPageIndicatorTintColor) {
        this.currentPageIndicatorTintColor = currentPageIndicatorTintColor;
        updateCurrentIndicatorColor();
    }

}
