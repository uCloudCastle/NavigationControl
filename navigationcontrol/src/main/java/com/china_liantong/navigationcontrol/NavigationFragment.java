package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.adapt.ContentAdapt;
import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.PageView;

import java.util.List;

/**
 * Created by randal on 2017/9/18.
 */

public class NavigationFragment extends Fragment {
    private Activity mActivity;
    private DataHolder mDataHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RelativeLayout mainLayout = new RelativeLayout(mActivity);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mainLayout.setLayoutParams(lp);

        // **** pageView
        final PageView pageView = new PageView(mActivity);
        int pageViewId = CommonUtils.generateViewId();
        pageView.setId(pageViewId);
        RelativeLayout.LayoutParams pvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        pvlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        pvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        pvlp.setMargins(0, (int) getResources().getDimension(R.dimen.fragment_pageview_margin_top),
                (int) getResources().getDimension(R.dimen.fragment_pageview_margin_right), 0);
        pageView.setTextSize(getResources().getDimension(R.dimen.fragment_pageview_textsize));
        mainLayout.addView(pageView, pvlp);

        // **** subMenu
        SubMenu subMenu = new SubMenu(mActivity);
        int subMenuId = CommonUtils.generateViewId();
        subMenu.setId(subMenuId);
        subMenu.setDataHolder(mActivity, mDataHolder.subHolder);
        RelativeLayout.LayoutParams snlp = new RelativeLayout.LayoutParams(
                mDataHolder.subMenuWidth,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        snlp.addRule(RelativeLayout.ABOVE, pageViewId);
        snlp.setMargins(0, 0, mDataHolder.subMenuMarginRight, 0);
        mainLayout.addView(subMenu, snlp);

        // **** gridView
        final LtGridView gridView = new LtGridView(getActivity());
        gridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_HORIZONTAL);
        gridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
        gridView.setFocusDrawable(getResources().getDrawable(R.drawable.app_selected));
        gridView.setFadingEdgeEnabled(true);
        gridView.setFadingEdgeDrawable(getResources().getDrawable(R.drawable.gridview_shading));
        gridView.setFocusScaleAnimEnabled(false);
        gridView.setSelectPadding(18, 17, 19, 16);
        gridView.setOnPageChangeListener(pageView);
        RelativeLayout.LayoutParams gvlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        gvlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        gvlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        gvlp.addRule(RelativeLayout.ABOVE, pageViewId);
        gvlp.addRule(RelativeLayout.RIGHT_OF, subMenuId);

        if (mDataHolder.infoList != null && mDataHolder.infoList.size() > 0) {
            if (mDataHolder.infoList.get(0).pageCount > 1) {
                gridView.setPageSpacing(mDataHolder.infoList.get(0).columnSpacing);
                gridView.setShadowRight(mDataHolder.infoList.get(0).fadingWidth);
                pageView.setTotalPage(mDataHolder.infoList.get(0).pageCount);
            }
            gridView.setAdapter(new ContentAdapt(mActivity, mDataHolder.infoList.get(0)));
            gvlp.setMargins(0, 0, mDataHolder.infoList.get(0).marginRight, 0);
        }
        mainLayout.addView(gridView, gvlp);

        subMenu.setSubMenuListener(new SubMenu.SubMenuListener() {
            @Override
            public void onItemGetFocus(int newPos) {
                LogUtils.d("submenu get Focus : " + newPos);
                if (mDataHolder.infoList != null && mDataHolder.infoList.size() > newPos
                        && mDataHolder.infoList.get(newPos) != null) {

                    pageView.setTotalPage(mDataHolder.infoList.get(newPos).pageCount);
                    gridView.setShadowRight(mDataHolder.infoList.get(newPos).fadingWidth);
                    if (mDataHolder.infoList.get(newPos).pageCount > 1) {
                        gridView.setPageSpacing(mDataHolder.infoList.get(newPos).columnSpacing);
                    }
                    RelativeLayout.LayoutParams gvlp = (RelativeLayout.LayoutParams)gridView.getLayoutParams();
                    gvlp.setMargins(0, 0, mDataHolder.infoList.get(newPos).marginRight, 0);
                    gridView.setLayoutParams(gvlp);
                    gridView.setAdapter(new ContentAdapt(mActivity, mDataHolder.infoList.get(newPos)));
                }
            }
        });
        return mainLayout;
    }

    public void setDataHolder(Activity activity, NavigationFragment.DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
        }
    }

    public static class GridViewInfo {
        public static final int CONTENT_ITEM_STYLE_BACKGROUND_COVERED = 0;
        public static final int CONTENT_ITEM_STYLE_ICON_TOP = 1;
        public static final int CONTENT_ITEM_STYLE_ICON_LEFT = 2;
        public static final int CONTENT_ITEM_STYLE_ONLY_TEXT = 3;

        public GridViewInfo() {}

        public LtGridAdapter customAdapter;
        public int[] perPageStyle;
        public Drawable[][] pictures;
        public String[][] titles;
        public String[][] subtitles;
        public int titleSize;
        public int titleColor;
        public int subtitleSize;
        public int subtitleColor;
        public int pageCount;
        public int[] perPageItemCount;
        public int rows;
        public int columns;
        public int rowSpacing;
        public int columnSpacing;
        public int itemStartIndex[][];
        public int itemRowSize[][];
        public int itemColumnSize[][];
        public int fadingWidth;
        public int marginRight;
    }

    public static class DataHolder {
        private List<GridViewInfo> infoList;
        private SubMenu.DataHolder subHolder;
        private int subMenuWidth;
        private int subMenuMarginRight;

        public DataHolder() {
        }

        public DataHolder infoList(List<GridViewInfo> list) {
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
