package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.fragment.NavigationFragment;

import java.util.List;

/**
 * Created by randal on 2017/9/14.
 */

public class NavigationControl extends LinearLayout {
    private Context mContext;
    private FragmentContainer mFragmentContainer;

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

    public void init() {
        NavigationBar navBar = new NavigationBar(mContext);
        navBar.setDataHolder(mBarHolder);
        LinearLayout.LayoutParams nblp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                mControlHolder.navigationBarHeight);
        addView(navBar, nblp);

        mFragmentContainer = new FragmentContainer(mContext);
        mFragmentContainer.setDataHolder(mControlHolder.activity, mFragmentHolders);
        LinearLayout.LayoutParams fclp = new LinearLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        fclp.setMargins(0, mControlHolder.fragmentMarginTop, 0, 0);
        addView(mFragmentContainer, fclp);
        navBar.setNavigationBarListener(mFragmentContainer);
    }

    public boolean setOnItemClickListener(OnItemClickListener l) {
        if (mFragmentContainer != null && l != null) {
            mFragmentContainer.setOnItemClickListener(l);
            return true;
        }
        return false;
    }

    public interface OnItemClickListener {
        void onItemClick(View focusView, int page, int subpage, int position);
        void onPageChanged(int newPage);
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
