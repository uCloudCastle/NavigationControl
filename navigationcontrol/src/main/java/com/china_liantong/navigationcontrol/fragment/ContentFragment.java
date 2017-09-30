package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.PageView;

/**
 * Created by randal on 2017/9/28.
 */

public class ContentFragment extends Fragment {
    private Activity mActivity;
    private LtGridView mGridView;
    private PageView mPageView;

    private RelativeLayout mRootLayout;
    private ContentViewProxy mContentProxy;

    public void init(Activity activity, PageView pageView, ContentViewProxy proxy) {
        if (activity == null || pageView == null || proxy == null) {
            LogUtils.e("param error");
            return;
        }
        mActivity = activity;
        mPageView = pageView;
        mContentProxy = proxy;
        mContentProxy.init(activity, pageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = new RelativeLayout(mActivity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        //mRootLayout.setBackgroundColor(Color.CYAN);
        mRootLayout.setLayoutParams(lp);

        mContentProxy.setContentUpdateListener(new ContentViewProxy.ContentUpdateListener() {
            @Override
            public void onContentViewUpdate() {
                setContentView(mContentProxy.getContentView());
            }
        });

//        // **** gridView
//        if (mViewInfo.builtInAdapter != null) {
//            mGridView = new LtGridView(getActivity());
//            mGridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_HORIZONTAL);
//            mGridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
//            mGridView.setFocusDrawable(getResources().getDrawable(R.drawable.app_selected));
//            mGridView.setFadingEdgeEnabled(true);
//            mGridView.setFadingEdgeDrawable(getResources().getDrawable(R.drawable.gridview_shading));
//            mGridView.setFocusScaleAnimEnabled(false);
//            mGridView.setSelectPadding((int) getResources().getDimension(R.dimen.gridview_select_padding_left),
//                    (int) getResources().getDimension(R.dimen.gridview_select_padding_top),
//                    (int) getResources().getDimension(R.dimen.gridview_select_padding_right),
//                    (int) getResources().getDimension(R.dimen.gridview_select_padding_bottom));
//
//            mPageView.setTotalPage(mViewInfo.builtInAdapter.pageCount);
//            mGridView.setPageSpacing(mViewInfo.builtInAdapter.columnSpacing);
//            mGridView.setAdapter(new ContentAdapt(mActivity, mViewInfo.builtInAdapter));
//
//            RelativeLayout.LayoutParams gvlp = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.MATCH_PARENT,
//                    RelativeLayout.LayoutParams.MATCH_PARENT);
//            mRootLayout.addView(mGridView, gvlp);
//        }
        View content = mContentProxy.getContentView();
        setContentView(content);
        return mRootLayout;
    }

    private void setContentView(View content) {
        if (content == null) {
            LogUtils.d("content == null");
            return;
        }

        mRootLayout.removeAllViews();
        mRootLayout.addView(content, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
    }
}
