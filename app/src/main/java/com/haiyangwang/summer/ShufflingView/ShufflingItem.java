package com.haiyangwang.summer.ShufflingView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShufflingItem extends RelativeLayout {

    // default item
    private static final String ShufflingItem_Default_Identifier = "ShufflingItem_Default_Identifier";
    private Context context;
    private String mIdentifier = ShufflingItem_Default_Identifier;
    private TextView textView;
    public ShufflingItem(Context context) {
        super(context);
        this.context = context;
        initDefaultItem();
    }

    public ShufflingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initDefaultItem();
    }

    public ShufflingItem(Context context, String identifier) {
        super(context);
        this.context = context;
        this.mIdentifier = identifier;
        initDefaultItem();
    }

    /* Private methods*/
    private void initDefaultItem() {

//        if (mIdentifier != ShufflingItem_Default_Identifier) {
//            return;
//        }
        // only a label text
        if (textView == null) {
            textView = new TextView(context);
            textView.setText(ShufflingItem_Default_Identifier);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(CENTER_VERTICAL);
            this.addView(textView,layoutParams);
        }
        textView.setText(mIdentifier);
    }

    /* Setter*/
    public void setIdentifier(String identifier) {
        this.mIdentifier = identifier;
        initDefaultItem();
    }

    public String getIdentifier() {
        return mIdentifier;
    }
}
