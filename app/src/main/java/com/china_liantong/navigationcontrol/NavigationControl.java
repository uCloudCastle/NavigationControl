package com.china_liantong.navigationcontrol;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * Created by randal on 2017/9/14.
 */

public class NavigationControl extends RelativeLayout {
    Context mContext;
    private NavigationControl.DataHolder mNavigationControlHolder;
    private NavigationBar.DataHolder mNavigationBarHolder;
    private NavigationBar mNavBar;

    public NavigationControl(Context context) {
        this(context, null);
    }

    public NavigationControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        //setBackgroundColor(Color.BLUE);
    }

    public void show() {
        mNavBar = new NavigationBar(mContext);
        mNavBar.setDataHolder(mNavigationBarHolder);
        RelativeLayout.LayoutParams nblp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                mNavigationControlHolder.navigationBarHeight);
        nblp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        nblp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        addView(mNavBar, nblp);

        Button btn = new Button(mContext);
        btn.setText("我是一个按钮");
        RelativeLayout.LayoutParams btnlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        btnlp.setMargins(0, 200, 0, 0);
        addView(btn, btnlp);
    }

    public NavigationControl navigationControlHolder(NavigationControl.DataHolder holder) {
        if (holder != null) {
            mNavigationControlHolder = holder;
        }
        return this;
    }

    public NavigationControl navigationBarHolder(NavigationBar.DataHolder holder) {
        if (holder != null) {
            mNavigationBarHolder = holder;
        }
        return this;
    }

    public static class DataHolder {
        private int navigationBarHeight;

        public DataHolder() {}

        public DataHolder navigationBarHeight(int h) {
            navigationBarHeight = h;
            return this;
        }
    }
}
