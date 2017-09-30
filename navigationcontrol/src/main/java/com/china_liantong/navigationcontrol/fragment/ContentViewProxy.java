package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.adapt.ContentAdapt;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.PageView;

/**
 * Created by Randal on 2017-09-26.
 */

public class ContentViewProxy {
    public interface DataLoadNotifier {
        void onDataLoadStart();
        void onDataLoadDone(boolean success);
        void onPageChanged(int newPage);
    }

    public abstract class CustomViewHolder {
        abstract View getLoadingView();
        abstract View getLoadFailView();
        abstract View getContentView();
        abstract int getTotalPage();
    }

    // *************** BuiltIn Construction
    public ContentViewProxy(BuiltInAdapter adapter) {
        builtInAdapter = adapter;
    }

    // *************** Custom Construction
    public ContentViewProxy(CustomViewHolder holder) {
        customViewHolder = holder;
        mNotifier = new DataLoadNotifier() {
            @Override
            public void onDataLoadStart() {
                updateContentView(customViewHolder.getLoadingView());
            }

            @Override
            public void onDataLoadDone(boolean success) {
                if (success) {
                    mPageView.setTotalPage(customViewHolder.getTotalPage());
                    updateContentView(customViewHolder.getContentView());
                } else {
                    updateContentView(customViewHolder.getLoadFailView());
                }
            }

            @Override
            public void onPageChanged(int newPage) {
                if (mPageView != null) {
                    mPageView.setCurrentPage(newPage);
                }
            }
        };
    }

    public DataLoadNotifier getNotifier() {
        return mNotifier;
    }

    void init(Activity activity, PageView pageView) {
        mPageView = pageView;
        if (builtInAdapter != null) {
            LtGridView gridView = new LtGridView(activity);
            gridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_HORIZONTAL);
            gridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
            gridView.setFocusDrawable(activity.getResources().getDrawable(R.drawable.app_selected));
            gridView.setFadingEdgeEnabled(true);
            gridView.setFadingEdgeDrawable(activity.getResources().getDrawable(R.drawable.gridview_shading));
            gridView.setFocusScaleAnimEnabled(false);
            gridView.setSelectPadding((int) activity.getResources().getDimension(R.dimen.gridview_select_padding_left),
                    (int) activity.getResources().getDimension(R.dimen.gridview_select_padding_top),
                    (int) activity.getResources().getDimension(R.dimen.gridview_select_padding_right),
                    (int) activity.getResources().getDimension(R.dimen.gridview_select_padding_bottom));

            mPageView.setTotalPage(builtInAdapter.pageCount);
            gridView.setPageSpacing(builtInAdapter.columnSpacing);
            gridView.setAdapter(new ContentAdapt(activity, builtInAdapter));
            updateContentView(gridView);
        }
    }

    void setContentUpdateListener(ContentUpdateListener l) {
        if (l != null) {
            mUpdateListener = l;
        }
    }

    View getContentView() {
        return mRootView;
    }

    private void updateContentView(View view) {
        mRootView = view;
        if (mUpdateListener != null) {
            mUpdateListener.onContentViewUpdate();
        }
    }

    private DataLoadNotifier mNotifier;
    private View mRootView;
    private BuiltInAdapter builtInAdapter;
    private CustomViewHolder customViewHolder;
    private ContentUpdateListener mUpdateListener;
    private PageView mPageView;

    public static class BuiltInAdapter {
        public static final int CONTENT_ITEM_STYLE_BACKGROUND_COVERED = 0;
        public static final int CONTENT_ITEM_STYLE_ICON_TOP = 1;
        public static final int CONTENT_ITEM_STYLE_ICON_LEFT = 2;
        public static final int CONTENT_ITEM_STYLE_ONLY_TEXT = 3;

        public int pageCount;
        public int[] perPageStyle;
        public int[] perPageItemCount;
        public Drawable[][] pictures;
        public String[][] titles;
        public String[][] subtitles;
        public int titleSize;
        public int titleColor;
        public int subtitleSize;
        public int subtitleColor;
        public int rows;
        public int columns;
        public int rowSpacing;
        public int columnSpacing;
        public int itemStartIndex[][];
        public int itemRowSize[][];
        public int itemColumnSize[][];
        public int fadingWidth;
        public int marginRight;

        public BuiltInAdapter pageCount(int i) {
            pageCount = i;
            return this;
        }

        public BuiltInAdapter perPageStyle(int[] array) {
            perPageStyle = array;
            return this;
        }

        public BuiltInAdapter perPageItemCount(int[] array) {
            perPageItemCount = array;
            return this;
        }

        public BuiltInAdapter pictures(Drawable[][] array) {
            pictures = array;
            return this;
        }

        public BuiltInAdapter titles(String[][] array) {
            titles = array;
            return this;
        }

        public BuiltInAdapter subtitles(String[][] array) {
            subtitles = array;
            return this;
        }

        public BuiltInAdapter titleSize(int i) {
            titleSize = i;
            return this;
        }

        public BuiltInAdapter titleColor(int i) {
            titleColor = i;
            return this;
        }

        public BuiltInAdapter subtitleSize(int i) {
            subtitleSize = i;
            return this;
        }

        public BuiltInAdapter subtitleColor(int i) {
            subtitleColor = i;
            return this;
        }

        public BuiltInAdapter rows(int i) {
            rows = i;
            return this;
        }

        public BuiltInAdapter columns(int i) {
            columns = i;
            return this;
        }

        public BuiltInAdapter rowSpacing(int i) {
            rowSpacing = i;
            return this;
        }

        public BuiltInAdapter columnSpacing(int i) {
            columnSpacing = i;
            return this;
        }

        public BuiltInAdapter itemStartIndex(int[][] array) {
            itemStartIndex = array;
            return this;
        }

        public BuiltInAdapter itemRowSize(int[][] array) {
            itemRowSize = array;
            return this;
        }

        public BuiltInAdapter itemColumnSize(int[][] array) {
            itemColumnSize = array;
            return this;
        }

        public BuiltInAdapter marginRight(int margin) {
            marginRight = margin;
            return this;
        }

        public BuiltInAdapter fadingWidth(int width) {
            fadingWidth = width;
            return this;
        }
    }

    interface ContentUpdateListener {
        void onContentViewUpdate();
    }
}
