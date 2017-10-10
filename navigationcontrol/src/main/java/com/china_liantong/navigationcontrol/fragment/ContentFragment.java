package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.NavigationControlListener;
import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.PageView;

/**
 * Created by randal on 2017/9/28.
 */

public class ContentFragment extends Fragment {
    private Activity mActivity;
    private RelativeLayout mRootLayout;
    private ContentViewProxy mContentProxy;
    private HierarchyChangeListener mHyChangeListener;

    public void init(Activity activity, PageView pageView, ContentViewProxy proxy, NavigationControlListener listener) {
        if (activity == null || pageView == null || proxy == null) {
            LogUtils.e("param error");
            return;
        }
        mActivity = activity;
        mContentProxy = proxy;
        mContentProxy.init(activity, pageView, listener);
    }

    void setHierarchyChangeListener(HierarchyChangeListener l) {
        if (l != null) {
            mHyChangeListener = l;
        }
    }

    ContentViewProxy getProxy() {
        return mContentProxy;
    }

    int getPageCount() {
        if (mContentProxy.getBuiltInAdapter() != null) {
            return mContentProxy.getBuiltInAdapter().pageCount;
        } else {
            return 0;
        }
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

            @Override
            public void onEnterNextHierarchy(ContentViewProxy proxy) {
                if (mHyChangeListener != null) {
                    mHyChangeListener.onEnterNextHierarchy(proxy);
                }
            }

            @Override
            public void onReturnPreHierarchy() {
                if (mHyChangeListener != null) {
                    mHyChangeListener.onReturnPreHierarchy();
                }
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

    interface HierarchyChangeListener {
        void onEnterNextHierarchy(ContentViewProxy proxy);
        void onReturnPreHierarchy();
    }
}
