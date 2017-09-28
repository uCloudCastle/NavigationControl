package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.NavigationControl;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.SubMenu;
import com.china_liantong.navigationcontrol.adapt.ContentAdapt;
import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.LtAdapterView;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.PageView;

import java.util.List;

/**
 * Created by randal on 2017/9/18.
 */

public class NavigationFragment extends Fragment {
    private Activity mActivity;
    private DataHolder mDataHolder;
    private NavigationControl.OnItemClickListener mItemClickListener;
    private int mPagePos;
    private int mSubPagePos;

    private PageView mPageView;
    private SubMenu mSubMenu;
    private LtGridView mGridView;

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
        mGridView.setOnPageChangeListener(mPageView);
        RelativeLayout.LayoutParams gvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        gvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        gvlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        gvlp.addRule(RelativeLayout.ABOVE, pageViewId);
        gvlp.addRule(RelativeLayout.RIGHT_OF, subMenuId);
        mainLayout.addView(mGridView, gvlp);
        showGridViewByPos(0);

        mSubMenu.setSubMenuListener(new SubMenu.SubMenuListener() {
            @Override
            public void onItemGetFocus(int newPos) {
                LogUtils.d("submenu get Focus : " + newPos);
                mSubPagePos = newPos;
                showGridViewByPos(newPos);
            }
        });

        mGridView.setOnItemClickListener(new LtAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(LtAdapterView<?> parent, View view, int position, long id) {
                LogUtils.d(view.toString() + " " + position + " " + id);
                mItemClickListener.onItemClick(view, mPagePos, mSubPagePos, position);
            }
        });
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

    private void showGridViewByPos(int pos) {
        if (mDataHolder.infoList != null && mDataHolder.infoList.size() > pos
                && mDataHolder.infoList.get(pos) != null) {

            mGridView.setShadowRight(mDataHolder.infoList.get(pos).fadingWidth);
            RelativeLayout.LayoutParams gvlp = (RelativeLayout.LayoutParams) mGridView.getLayoutParams();
            gvlp.setMargins(0, 0, mDataHolder.infoList.get(pos).marginRight, 0);
            mGridView.setLayoutParams(gvlp);

            if (mDataHolder.infoList.get(pos).builtInAdapter != null) {
                mPageView.setTotalPage(mDataHolder.infoList.get(pos).builtInAdapter.pageCount);
                mGridView.setPageSpacing(mDataHolder.infoList.get(pos).builtInAdapter.columnSpacing);
                mGridView.setAdapter(new ContentAdapt(mActivity, mDataHolder.infoList.get(pos).builtInAdapter));
            } else {
                if (mDataHolder.infoList.get(pos).customAdapter != null) {
                    mPageView.setTotalPage(mDataHolder.infoList.get(pos).customAdapter.getPageCount());
                    mGridView.setPageSpacing(mDataHolder.infoList.get(pos).customAdapter.getColumnSpacing());
                    mGridView.setAdapter(mDataHolder.infoList.get(pos).customAdapter);
                }
            }
        }
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class DataHolder {
        private List<ContentViewInfo> infoList;
        private SubMenu.DataHolder subHolder;
        private int subMenuWidth;
        private int subMenuMarginRight;

        public DataHolder() {
        }

        public DataHolder infoList(List<ContentViewInfo> list) {
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
