package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by randal on 2017/9/14.
 */

public class NavigationControl extends LinearLayout {
    Context mContext;
    private NavigationControl.DataHolder mControlHolder;
    private NavigationBar.DataHolder mBarHolder;
    private List<NavigationFragment.DataHolder> mFragmentHolders;

    public NavigationControl(Context context) {
        this(context, null);
    }

    public NavigationControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setOrientation(VERTICAL);
    }

    public void show() {
        NavigationBar navBar = new NavigationBar(mContext);
        navBar.setDataHolder(mBarHolder);
        LinearLayout.LayoutParams nblp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mControlHolder.navigationBarHeight);
        addView(navBar, nblp);

        FragmentContainer container = new FragmentContainer(mContext);
        container.setDataHolder(mControlHolder.activity, mFragmentHolders);
        LinearLayout.LayoutParams fclp = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        fclp.setMargins(0, mControlHolder.fragmentMarginTop, 0, 0);
        addView(container, fclp);

        navBar.setNavigationBarListener(container);
    }

    public NavigationControl navigationControlHolder(NavigationControl.DataHolder holder) {
        if (holder != null) {
            mControlHolder = holder;
        }
        return this;
    }

    public NavigationControl navigationBarHolder(NavigationBar.DataHolder holder) {
        if (holder != null) {
            mBarHolder = holder;
        }
        return this;
    }

    public NavigationControl navigationFragmentHolder(List<NavigationFragment.DataHolder> holders) {
        if (holders != null) {
            mFragmentHolders = holders;
        }
        return this;
    }

    public static class DataHolder {
        private Activity activity;
        private int navigationBarHeight;
        private int fragmentMarginTop;

        public DataHolder() {}

        public DataHolder activity(Activity a) {
            activity = a;
            return this;
        }

        public DataHolder navigationBarHeight(int h) {
            navigationBarHeight = h;
            return this;
        }

        public DataHolder fragmentMarginTop(int t) {
            fragmentMarginTop = t;
            return this;
        }
    }
}
