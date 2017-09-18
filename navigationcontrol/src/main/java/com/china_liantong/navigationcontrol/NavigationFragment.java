package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by randal on 2017/9/18.
 */

public class NavigationFragment extends Fragment {
    private Activity mActivity;
    private DataHolder mDataHolder;

    //private SubNavigationBar mSubNavBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(lp);

        SubNavigationBar sn = new SubNavigationBar(mActivity);
        sn.setDataHolder(mActivity, mDataHolder.subHolder);
        RelativeLayout.LayoutParams snlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layout.addView(sn, snlp);

        return layout;
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class DataHolder {
        private SubNavigationBar.DataHolder subHolder;

        public DataHolder() {
        }

        public DataHolder subHolder(SubNavigationBar.DataHolder holder) {
            subHolder = holder;
            return this;
        }
    }
}
