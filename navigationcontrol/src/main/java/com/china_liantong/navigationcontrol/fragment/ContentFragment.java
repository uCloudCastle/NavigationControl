package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.PageView;

/**
 * Created by randal on 2017/9/28.
 */

public class ContentFragment extends Fragment {
    private Activity mActivity;
    private RelativeLayout mRootLayout;
    private ContentViewProxy mContentProxy;

    public void init(Activity activity, PageView pageView, ContentViewProxy proxy) {
        if (activity == null || pageView == null || proxy == null) {
            LogUtils.e("param error");
            return;
        }
        mActivity = activity;
        mContentProxy = proxy;
        mContentProxy.init(activity, pageView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout = new RelativeLayout(mActivity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mRootLayout.setLayoutParams(lp);
        mContentProxy.setContentUpdateListener(new ContentViewProxy.ContentUpdateListener() {
            @Override
            public void onContentViewUpdate() {
                setContentView(mContentProxy.getContentView());
            }
        });

        View view = mContentProxy.getContentView();
        setContentView(view);
        return mRootLayout;
    }

    private void setContentView(View content) {
        if (content == null) {
            return;
        }
        if (content.getParent() != null) {
            ((ViewGroup) content.getParent()).removeView(content);
        }

        mRootLayout.removeAllViews();
        mRootLayout.addView(content, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT));
    }
}
