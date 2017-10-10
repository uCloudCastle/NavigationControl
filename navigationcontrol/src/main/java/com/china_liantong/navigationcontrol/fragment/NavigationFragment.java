package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.NavigationControlListener;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.SubMenu;
import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.PageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by randal on 2017/9/18.
 */

public class NavigationFragment extends Fragment implements ContentFragment.HierarchyChangeListener {
    private Activity mActivity;
    private DataHolder mDataHolder;
    private List<ContentFragment> mContentList = new ArrayList<>();
    private Stack<ContentFragment> mHierarchyStack = new Stack<>();
    private NavigationControlListener mClientListener;

    private int mFrameId;
    private int mPagePos = 0;
    private int mSubPagePos = 0;
    private PageView mPageView;
    private SubMenu mSubMenu;
    private FrameLayout mContentLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout mainLayout = new RelativeLayout(mActivity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayout.setLayoutParams(lp);

        // **** pageView
        mPageView = new PageView(mActivity);
        int pageViewId = CommonUtils.generateViewId();
        mPageView.setId(pageViewId);
        RelativeLayout.LayoutParams pvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        pvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        pvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        pvlp.setMargins(0, (int) getResources().getDimension(R.dimen.fragment_pageview_margin_top),
                (int) getResources().getDimension(R.dimen.fragment_pageview_margin_right), 0);
        mainLayout.addView(mPageView, pvlp);

        // **** subMenu
        mSubMenu = new SubMenu(mActivity);
        int subMenuId = CommonUtils.generateViewId();
        mSubMenu.setId(subMenuId);
        mSubMenu.setDataHolder(mActivity, mDataHolder.subHolder);
        RelativeLayout.LayoutParams snlp = new RelativeLayout.LayoutParams(
                mDataHolder.subMenuWidth,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ABOVE, pageViewId);
        snlp.setMargins(0, 0, mDataHolder.subMenuMarginRight, 0);
        mainLayout.addView(mSubMenu, snlp);

        // content fragment
        mContentLayout = new FrameLayout(mActivity);
        mFrameId = CommonUtils.generateViewId();
        mContentLayout.setId(mFrameId);
        RelativeLayout.LayoutParams framelp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        framelp.addRule(RelativeLayout.ABOVE, pageViewId);
        framelp.addRule(RelativeLayout.RIGHT_OF, subMenuId);
        mainLayout.addView(mContentLayout, framelp);

        if (mDataHolder.proxyList != null && mDataHolder.proxyList.size() > 0) {
            FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
            for (int i = 0; i < mDataHolder.proxyList.size(); ++i) {
                ContentFragment fragment = new ContentFragment();
                fragment.init(mActivity, mPageView, mDataHolder.proxyList.get(i), mClientListener);
                fragment.setHierarchyChangeListener(this);
                mContentList.add(fragment);
                fragTransaction.add(mFrameId, fragment);
            }
            fragTransaction.commit();
        }
        showContent(0);

        mSubMenu.setSubMenuListener(new SubMenu.SubMenuListener() {
            @Override
            public void onItemGetFocus(int newPos) {
                LogUtils.d("submenu get Focus : " + newPos);
                mSubPagePos = newPos;
                showContent(newPos);

                if (mClientListener != null) {
                    mClientListener.onPageChanged(mPagePos, mSubPagePos);
                }
            }
        });
        return mainLayout;
    }

    public void resetSubMenuIfExist() {
        if (mSubMenu != null) {
            mSubMenu.reset();
        }
    }


    public void setPagePos(int pos) {
        if (pos >= 0) {
            mPagePos = pos;
        }
    }

    public void setClientListener(NavigationControlListener l) {
        if (l != null) {
            mClientListener = l;
        }
    }

    private void showContent(int pos) {
        if (pos >= mContentList.size()) {
            return;
        }

        FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
        for (int i = 0; i < mContentList.size(); ++i) {
            if (i != pos) {
                fragTransaction.hide(mContentList.get(i));
            }
        }
        fragTransaction.show(mContentList.get(pos)).commitAllowingStateLoss();
        mPageView.setTotalPage(mContentList.get(pos).getPageCount());
    }

    private void addHierarchy(ContentFragment fragment) {
        FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
        if (mHierarchyStack.size() == 0) {
            fragTransaction.hide(mContentList.get(mSubPagePos));
        } else {
            fragTransaction.hide(mHierarchyStack.peek());
        }
        fragTransaction.show(fragment).commitAllowingStateLoss();
        mHierarchyStack.push(fragment);
        mPageView.setTotalPage(fragment.getPageCount());
    }

    public NavigationFragment() {
        super();
    }

    @Override
    public void onEnterNextHierarchy(ContentViewProxy proxy) {
        ContentFragment fragment = new ContentFragment();
        fragment.init(mActivity, mPageView, proxy, mClientListener);
        fragment.setHierarchyChangeListener(this);
        mActivity.getFragmentManager().beginTransaction().add(mFrameId, fragment).commit();
        proxy.setHierarchy(mHierarchyStack.size() + 1);
        addHierarchy(fragment);
    }

    @Override
    public void onReturnPreHierarchy() {
        if (mHierarchyStack.size() == 0) {
            return;
        }

        FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
        fragTransaction.remove(mHierarchyStack.pop());
        if (mHierarchyStack.size() > 0) {
            fragTransaction.show(mHierarchyStack.peek());
            mPageView.setTotalPage(mHierarchyStack.peek().getPageCount());
        } else {
            showContent(mSubPagePos);
        }
        fragTransaction.commitAllowingStateLoss();
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class DataHolder {
        private List<ContentViewProxy> proxyList;
        private SubMenu.DataHolder subHolder;
        private int subMenuWidth;
        private int subMenuMarginRight;

        public DataHolder() {
        }

        public DataHolder infoList(List<ContentViewProxy> list) {
            proxyList = list;
            return this;
        }

        public DataHolder subHolder(SubMenu.DataHolder holder) {
            subHolder = holder;
            return this;
        }

        public DataHolder subMenuWidth(int width) {
            subMenuWidth = width;
            return this;
        }

        public DataHolder subMenuMarginRight(int margin) {
            subMenuMarginRight = margin;
            return this;
        }
    }
}
