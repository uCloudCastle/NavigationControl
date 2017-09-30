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

import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.SubMenu;
import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.PageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by randal on 2017/9/18.
 */

public class NavigationFragment extends Fragment {
    private Activity mActivity;
    private DataHolder mDataHolder;
    private List<ContentFragment> mFragmentList = new ArrayList<>();
    private NavigationControl.OnItemClickListener mItemClickListener;

    private int mPagePos;
    private int mSubPagePos;
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
        int frameId = CommonUtils.generateViewId();
        mContentLayout.setId(frameId);
        RelativeLayout.LayoutParams framelp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        framelp.addRule(RelativeLayout.ABOVE, pageViewId);
        framelp.addRule(RelativeLayout.RIGHT_OF, subMenuId);
        mainLayout.addView(mContentLayout, framelp);

        if (mDataHolder.infoList != null && mDataHolder.infoList.size() > 0) {
            FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
            for (int i = 0; i < mDataHolder.infoList.size(); ++i) {
                ContentFragment fragment = new ContentFragment();
                fragment.init(mActivity, mPageView, mDataHolder.infoList.get(i));
                mFragmentList.add(fragment);
                fragTransaction.add(frameId, fragment);
            }
            fragTransaction.commit();
        }
        showContentByPos(0);

        mSubMenu.setSubMenuListener(new SubMenu.SubMenuListener() {
            @Override
            public void onItemGetFocus(int newPos) {
                LogUtils.d("submenu get Focus : " + newPos);
                mSubPagePos = newPos;
                showContentByPos(newPos);

                if (mItemClickListener != null) {
                    mItemClickListener.onPageChanged(mPagePos, mSubPagePos);
                }
            }
        });

//        mGridView.setOnItemClickListener(new LtAdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(LtAdapterView<?> parent, View view, int position, long id) {
//                LogUtils.d(view.toString() + " " + position + " " + id);
//                mItemClickListener.onItemClick(view, mPagePos, mSubPagePos, position);
//            }
//        });
        return mainLayout;
    }

    public void setPagePos(int pos) {
        if (pos >= 0) {
            mPagePos = pos;
        }
    }

    public void setOnItemClickListener(NavigationControl.OnItemClickListener l) {
        if (l != null) {
            mItemClickListener = l;
        }
    }

    private void showContentByPos(int pos) {
        if (pos >= mFragmentList.size()) {
            return;
        }

        FragmentTransaction fragTransaction = mActivity.getFragmentManager().beginTransaction();
        for (int i = 0; i < mFragmentList.size(); ++i) {
            if (i != pos) {
                fragTransaction.hide(mFragmentList.get(i));
            }
        }
        fragTransaction.show(mFragmentList.get(pos)).commitAllowingStateLoss();
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class DataHolder {
        private List<ContentViewProxy> infoList;
        private SubMenu.DataHolder subHolder;
        private int subMenuWidth;
        private int subMenuMarginRight;

        public DataHolder() {
        }

        public DataHolder infoList(List<ContentViewProxy> list) {
            infoList = list;
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
