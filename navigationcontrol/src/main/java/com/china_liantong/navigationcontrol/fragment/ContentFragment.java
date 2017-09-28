package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.widgets.LtGridView;

/**
 * Created by randal on 2017/9/28.
 */

public class ContentFragment extends Fragment {
    private Activity mActivity;
    private LtGridView mGridView;
    private RelativeLayout mRootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = new RelativeLayout(mActivity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mRootLayout.setLayoutParams(lp);

        // **** gridView
        mGridView = new LtGridView(getActivity());
        mGridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_HORIZONTAL);
        mGridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
        mGridView.setFocusDrawable(getResources().getDrawable(R.drawable.app_selected));
        mGridView.setFadingEdgeEnabled(true);
        mGridView.setFadingEdgeDrawable(getResources().getDrawable(R.drawable.gridview_shading));
        mGridView.setFocusScaleAnimEnabled(false);
        mGridView.setSelectPadding((int)getResources().getDimension(R.dimen.gridview_select_padding_left),
                (int)getResources().getDimension(R.dimen.gridview_select_padding_top),
                (int)getResources().getDimension(R.dimen.gridview_select_padding_right),
                (int)getResources().getDimension(R.dimen.gridview_select_padding_bottom));
        RelativeLayout.LayoutParams gvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRootLayout.addView(mGridView, gvlp);


        return mRootLayout;
    }

    public RelativeLayout getRootLayout() {
        return mRootLayout;
    }
}
