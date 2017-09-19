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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout layout = new RelativeLayout(getActivity());
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(lp);

        SubMenu sub = new SubMenu(mActivity);
        sub.setDataHolder(mActivity, mDataHolder.subHolder);
        RelativeLayout.LayoutParams snlp = new RelativeLayout.LayoutParams(
                mDataHolder.subMenuWidth,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layout.addView(sub, snlp);

        return layout;
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class DataHolder {
        private SubMenu.DataHolder subHolder;
        private int subMenuWidth;

        public DataHolder() {
        }

        public DataHolder subHolder(SubMenu.DataHolder holder) {
            subHolder = holder;
            return this;
        }

        public DataHolder subMenuWidth(int width) {
            subMenuWidth = width;
            return this;
        }
    }
}
