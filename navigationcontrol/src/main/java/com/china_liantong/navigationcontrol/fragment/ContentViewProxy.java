package com.china_liantong.navigationcontrol.fragment;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.china_liantong.navigationcontrol.NavigationControlListener;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.adapt.ContentAdapt;
import com.china_liantong.navigationcontrol.widgets.LtAdapterView;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.PageView;

/**
 * Created by Randal on 2017-09-26.
 */

public class ContentViewProxy {
    private NavigationControlListener mClientListener;
    private View mSelectedGridViewItem;
    private int mSelectedPosition;
    private boolean gridViewHasFocus = false;

    public interface InfoChangeNotifier {
        void notifyDataLoadStart();
        void notifyDataLoadDone(boolean success);
        void notifyPageChanged(int curPage, int totalPage);
        void notifyEnterNextHierarchy(ContentViewProxy proxy);
        void notifyReturnPreHierarchy();
    }

    // *************** BuiltIn Construction
    public ContentViewProxy(BuiltInAdapter adapter) {
        this();
        builtInAdapter = adapter;
    }

    // *************** Custom Construction
    public ContentViewProxy(CustomViewHolder holder) {
        this();
        customViewHolder = holder;
        if (customViewHolder != null && customViewHolder.getContentView() != null) {
            updateContentView(customViewHolder.getContentView());
        }
    }

    public void setBuiltInAdapter(BuiltInAdapter adapter) {
        builtInAdapter = adapter;
    }

    public BuiltInAdapter getBuiltInAdapter() {
        return builtInAdapter;
    }

    public void setCustomViewHolder(CustomViewHolder holder) {
        customViewHolder = holder;
    }

    public CustomViewHolder getCustomViewHolder() {
        return customViewHolder;
    }

    public InfoChangeNotifier getNotifier() {
        return mNotifier;
    }

    private static final int HANDLER_ACTION_SHOW_LOADING = 0;
    private static final int HANDLER_ACTION_SHOW_LOAD_SUCCESS = 1;
    private static final int HANDLER_ACTION_SHOW_LOAD_FAIL = 2;
    private static final int HANDLER_ACTION_SET_PAGE = 3;
    private InfoChangeNotifier mNotifier;
    private View mRootView;
    private BuiltInAdapter builtInAdapter;
    private CustomViewHolder customViewHolder;
    private ContentUpdateListener mUpdateListener;
    private PageView mPageView;
    private Activity mActivity;
    private LtGridView mGridView;
    private int mHierarchy;

    void init(Activity activity, PageView pageView, NavigationControlListener listener) {
        mActivity = activity;
        mPageView = pageView;
        mClientListener = listener;
        if (builtInAdapter != null) {
            FrameLayout layout = createBuiltInGridView();
            updateContentView(layout);
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

    void setHierarchy(int h) {
        mHierarchy = h;
        if (h > 0 && mGridView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mGridView.requestFocus();
                    mGridView.setFocusToPosition(0);
                }
            });
        }
    }

    void resumeGridViewFocus() {
        if (mGridView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mGridView.requestFocus();
                }
            });
        }
    }

    private FrameLayout createBuiltInGridView() {
        mGridView = new LtGridView(mActivity);
        mGridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_HORIZONTAL);
        mGridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
        mGridView.setFocusDrawable(mActivity.getResources().getDrawable(R.drawable.app_selected));
        mGridView.setFadingEdgeEnabled(true);
        mGridView.setFadingEdgeDrawable(mActivity.getResources().getDrawable(R.drawable.gridview_shading));
        mGridView.setFocusScaleAnimEnabled(false);
        mGridView.setSelectPadding((int) mActivity.getResources().getDimension(R.dimen.gridview_select_padding_left),
                (int) mActivity.getResources().getDimension(R.dimen.gridview_select_padding_top),
                (int) mActivity.getResources().getDimension(R.dimen.gridview_select_padding_right),
                (int) mActivity.getResources().getDimension(R.dimen.gridview_select_padding_bottom));

        mGridView.setPageSpacing(builtInAdapter.columnSpacing);
        mGridView.setAdapter(new ContentAdapt(mActivity, builtInAdapter));
        mGridView.setShadowRight(builtInAdapter.fadingWidth);
        mGridView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                gridViewHasFocus = hasFocus;
                if (hasFocus && mSelectedGridViewItem != null) {
                    mClientListener.onBuiltInItemGetFocus(mSelectedGridViewItem, mSelectedPosition, mHierarchy);
                }
            }
        });

        mGridView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP
                        && keyCode == KeyEvent.KEYCODE_BACK
                        && mHierarchy > 0) {
                    mUpdateListener.onReturnPreHierarchy();
                    return true;
                }
                return false;
            }
        });

        mGridView.setOnItemSelectedListener(new LtAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(LtAdapterView<?> parent, View view, int position, long id) {
                mSelectedGridViewItem = view;
                mSelectedPosition = position;
                if (!gridViewHasFocus) {
                    return;
                }

                if (mClientListener != null) {
                    mClientListener.onBuiltInItemGetFocus(view, position, mHierarchy);
                }
            }

            @Override
            public void onNothingSelected(LtAdapterView<?> parent) {}

            @Override
            public void onItemGoingTo(View view, int position) {}
        });

        mGridView.setOnPageChangeListener(new LtAdapterView.OnPageChangeListener() {
            @Override
            public void onPageChanged(int curPage) {
                mPageView.setCurrentPage(curPage + 1);
            }
        });

        mGridView.setOnItemClickListener(new LtAdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(LtAdapterView<?> parent, View view, int position, long id) {
                if (mClientListener != null) {
                    mClientListener.onBuiltInItemClick(view, position, mHierarchy);
                }
            }
        });

        FrameLayout gridLayout = new FrameLayout(mActivity);
        FrameLayout.LayoutParams gridParams = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        gridParams.setMargins(0, 0, builtInAdapter.marginRight, 0);
        gridLayout.addView(mGridView, gridParams);
        return gridLayout;
    }

    private void updateContentView(View view) {
        mRootView = view;
        if (mUpdateListener != null) {
            mUpdateListener.onContentViewUpdate();
        }
    }

    public static class CustomViewHolder {
        View mLoadingView;
        View mLoadFailView;
        View mContentView;

        public CustomViewHolder(View loadingView,
                                View loadFailView, View contentView) {
            mLoadingView = loadingView;
            mLoadFailView = loadFailView;
            mContentView = contentView;
        }

        public View getLoadingView() {
            return mLoadingView;
        }

        public View getLoadFailView() {
            return mLoadFailView;
        }

        public View getContentView() {
            return mContentView;
        }

        public void setLoadingView(View view) {
            if (view != null) {
                mLoadingView = view;
            }
        }

        public void setLoadFailView(View view) {
            if (view != null) {
                mLoadFailView = view;
            }
        }

        public void setContentView(View view) {
            if (view != null) {
                mContentView = view;
            }
        }
    }

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

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_ACTION_SHOW_LOADING: {
                    if (customViewHolder == null) {
                        return;
                    }
                    View view = customViewHolder.getLoadingView();
                    if (view != null) {
                        updateContentView(view);
                    }
                    break;
                }
                case HANDLER_ACTION_SHOW_LOAD_SUCCESS: {
                    View view = null;
                    if (builtInAdapter != null) {
                        view = createBuiltInGridView();
                    } else if (customViewHolder != null) {
                        view = customViewHolder.getContentView();
                    }
                    if (view != null) {
                        updateContentView(view);
                    }
                    break;
                }
                case HANDLER_ACTION_SHOW_LOAD_FAIL: {
                    if (customViewHolder == null) {
                        return;
                    }
                    View view = customViewHolder.getLoadFailView();
                    if (view != null) {
                        updateContentView(view);
                    }
                    break;
                }
                case HANDLER_ACTION_SET_PAGE: {
                    if (mPageView != null) {
                        Bundle bundle = msg.getData();
                        int totalPage = bundle.getInt("totalPage");
                        int curPage = bundle.getInt("curPage");
                        mPageView.setTotalPage(totalPage);
                        mPageView.setCurrentPage(curPage);
                    }
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    // *************** Default Construction
    public ContentViewProxy() {
        mNotifier = new InfoChangeNotifier() {
            @Override
            public void notifyDataLoadStart() {
                mHandler.removeMessages(HANDLER_ACTION_SHOW_LOADING);
                Message msg = mHandler.obtainMessage(HANDLER_ACTION_SHOW_LOADING);
                mHandler.sendMessage(msg);
            }

            @Override
            public void notifyDataLoadDone(boolean success) {
                if (success) {
                    mHandler.removeMessages(HANDLER_ACTION_SHOW_LOAD_SUCCESS);
                    Message msg = mHandler.obtainMessage(HANDLER_ACTION_SHOW_LOAD_SUCCESS);
                    mHandler.sendMessage(msg);
                } else {
                    mHandler.removeMessages(HANDLER_ACTION_SHOW_LOAD_FAIL);
                    Message msg = mHandler.obtainMessage(HANDLER_ACTION_SHOW_LOAD_FAIL);
                    mHandler.sendMessage(msg);
                }
            }

            @Override
            public void notifyPageChanged(int curPage, int totalPage) {
                mHandler.removeMessages(HANDLER_ACTION_SET_PAGE);
                Message msg = mHandler.obtainMessage(HANDLER_ACTION_SET_PAGE);
                Bundle bundle = new Bundle();
                bundle.putInt("totalPage", totalPage);
                bundle.putInt("curPage", curPage);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
            }

            @Override
            public void notifyEnterNextHierarchy(ContentViewProxy proxy) {
                mUpdateListener.onEnterNextHierarchy(proxy);
            }

            @Override
            public void notifyReturnPreHierarchy() {
                mUpdateListener.onReturnPreHierarchy();
            }
        };
    }

    interface ContentUpdateListener {
        void onContentViewUpdate();
        void onEnterNextHierarchy(ContentViewProxy proxy);
        void onReturnPreHierarchy();
    }
}
