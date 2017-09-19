package com.china_liantong.navigationcontrol.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.china_liantong.navigationcontrol.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LtGridView extends LtAbsSpinner {
    private static final String LOG_TAG = "LtGridView";

    private Activity mActivity;
    private Scroller mScroller;
    private Scroller mScrollerFocusMoveAnim;
    private boolean mIsScrolling;
    private boolean mIsDrawFocusMoveAnim;
    private float mFocusMoveAnimScale;
    private float mSelectedScaleValue;

    private ScrollOrientation mScrollOrientation;
    private ScrollMode mScrollMode;

    private int mFocusFrameLeft;
    private int mFocusFrameTop;
    private int mFocusFrameRight;
    private int mFocusFrameBottom;

    private int mFadingSpaceLeft;
    private int mFadingSpaceRight;
    private int mFadingSpaceBottom;

    private DrawLayout mDrawLayout;
    private Drawable mDrawableFocus;
    private Drawable mFadingDrawer;
    private Drawable mFadingArrowLeft;
    private Drawable mFadingArrowRight;

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mColumnWidth;
    private int mRowHeight;
    private boolean mReceivedInvokeKeyDown;

    private View mSelectedItem;
    private int mIndexLayoutPage;
    private int mIndexNextLayoutPage;

    private boolean mAdapterChanged;
    private LtSelectionAnimNotifier mSelectionAnimNotifier;
    private static Map<Context, DrawLayout> msMapDrawLayout = new HashMap<>() ;
    private Map<Integer, PositionRecord> mMapRecordPosition = new HashMap<>();

    private int mShadowLeft;
    private int mShadowRight;
    private int mPageSpacing;
    private int mShadowBottom;
    private int mVerticalPageSpacing;

    private boolean mIsNeedLayerTypeToSoftWare;
    private boolean mIsNeedFocusScaleAnim;
    private boolean mFadingEdgeEnabled;

    private boolean mIsScrollToSpecPage = false;
    private int mScrollSpecPageIndex = -1;

    LtGridAdapter mAdapter;

    class PositionRecord {
        int posUp = INVALID_POSITION;
        int posDown  = INVALID_POSITION;
        int posLeft = INVALID_POSITION;
        int posRight = INVALID_POSITION;
    }

    public enum Direction {
        LEFT, UP, RIGHT, DOWN
    }

    /**
     * scroll direction enum.
     */
    public enum FocusDirection {
        LEFT, UP, RIGHT, DOWN
    }

    /**
     * flip direction enum.
     */
    public enum ScrollOrientation {
        SCROLL_HORIZONTAL, SCROLL_VERTICAL
    }

    /**
     * scroll mode enum.
     */
    public enum ScrollMode {
        SCROLL_MODE_PAGE, SCROLL_MODE_CELL
    }

    public LtGridView(Context context) {
        this(context, null);
    }

    public LtGridView(Activity activity) {
        super(activity);

        init(activity, null, 0);
        setActivity(activity);
        addDrawLayout();
    }

    public LtGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LtGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, 0);
        addDrawLayout();
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
        addDrawLayout();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mScroller = new Scroller(context);
        mScrollOrientation = ScrollOrientation.SCROLL_VERTICAL;
        mScrollMode = ScrollMode.SCROLL_MODE_PAGE;

        // NOTE: LtGridView item focus image focus_2.9.png
        mFocusFrameLeft = 32;    // the upper left part of .9 picture, width
        mFocusFrameTop = 22;     // the upper left part of .9 picture, height
        mFocusFrameRight = 32;   // the lower right part of .9 picture, width
        mFocusFrameBottom = 43;  // the lower right part of .9 picture, height

        mFadingSpaceLeft = 0;
        mFadingSpaceRight = 0;
        mFadingSpaceBottom = 0;
        mFadingDrawer = null;
        mFadingArrowLeft = null;
        mFadingArrowRight = null;

        mDrawLayout = null;
        mSelectedItem = null;
        mScrollerFocusMoveAnim = new Scroller(context);
        mIsScrolling = false;
        mIsDrawFocusMoveAnim = false;
        mFocusMoveAnimScale = 1;
        mDrawableFocus = null;
        mHorizontalSpacing = 5;
        mVerticalSpacing = 5;
        mReceivedInvokeKeyDown = false;
        mIndexLayoutPage = 0;
        mIndexNextLayoutPage = 0;
        mAdapterChanged = false;
        mSelectionAnimNotifier = null;
        mIndexNextLayoutPage = 0;
        mIndexLayoutPage = 0;
        mSelectedScaleValue = 1.08f;
        mIsNeedLayerTypeToSoftWare = true;
        mIsNeedFocusScaleAnim = true;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LtGridView, defStyle, 0);
        setAttributeSet(typedArray);
        typedArray.recycle();
    }

    private void setAttributeSet(TypedArray typedArray) {
        if (typedArray == null) {
            return;
        }
        int value = typedArray.getInteger(R.styleable.LtGridView_scrollOrientation, 1);
        ScrollOrientation orientation = ScrollOrientation.SCROLL_VERTICAL;
        if (value == 0) {
            orientation = ScrollOrientation.SCROLL_HORIZONTAL;
        }
        setScrollOrientation(orientation);
        ScrollMode mode = ScrollMode.SCROLL_MODE_PAGE;
        value = typedArray.getInteger(R.styleable.LtGridView_scrollMode, 0);
        if (value == 1) {
            mode = ScrollMode.SCROLL_MODE_CELL;
        }
        setScrollMode(mode);
        final Drawable drawable = typedArray.getDrawable(R.styleable.LtGridView_focusDrawable);
        if (drawable != null) {
            this.setFocusDrawable(drawable);
        }
        int shadowLeft = (int) typedArray.getDimension(R.styleable.LtGridView_shadowLeft,0);
        int shadowRight = (int) typedArray.getDimension(R.styleable.LtGridView_shadowRight,0);
        if(shadowLeft >= 0) {
            mShadowLeft = shadowLeft;
            mFadingSpaceLeft = shadowLeft;
        }
        if(shadowRight >= 0){
            mShadowRight = shadowRight;
            mFadingSpaceRight = shadowRight;
        }

        mPageSpacing = typedArray.getDimensionPixelOffset(R.styleable.LtGridView_pageSpacing,0);
        setFadingEdgeDrawable(typedArray.getDrawable(R.styleable.LtGridView_fadingEdgeDrawable));
        setFadingEdgeArrowLeft(typedArray.getDrawable(R.styleable.LtGridView_fadingEdgeArrowLeft));
        setFadingEdgeArrowRight(typedArray.getDrawable(R.styleable.LtGridView_fadingEdgeArrowRight));
        mIsNeedLayerTypeToSoftWare = typedArray.getBoolean(R.styleable.LtGridView_isNeedLayerTypeToSoftWare, true);
        mIsNeedFocusScaleAnim = typedArray.getBoolean(R.styleable.LtGridView_isNeedFocusScaleAnim, true);
        setFadingEdgeEnabled(typedArray.getBoolean(R.styleable.LtGridView_fadingEdgeEnable, false));
    }

    public void setFadingEdgeEnabled(boolean fadingEdgeEnabled) {
        mFadingEdgeEnabled = fadingEdgeEnabled;
    }

    public void setScrollOrientation(ScrollOrientation orientation) {
        mScrollOrientation = orientation;
    }

    public void setScrollMode(ScrollMode mode) {
        mScrollMode = mode;
    }

    /**
     * Set the Layer Type when scroll
     *
     * @param isNeed set the layer type, true is need open the layer type to software
     */
    public void setLayerTypeToSoftWare(boolean isNeed) {
        mIsNeedLayerTypeToSoftWare = isNeed;
    }

    /**
     * Set the focus scale animation enable
     * @param isNeed set the animation status, true is need focus scale animation
     */
    public void setFocusScaleAnimEnabled(boolean isNeed) {
        mIsNeedFocusScaleAnim = isNeed;
    }

    /**
     * Sets the Drawable of the grid's edge when it has focus.
     *
     * @param focusDrawable Drawable The focused grid's edge Drawable.
     */
    public void setFocusDrawable(Drawable focusDrawable) {
        mDrawableFocus = focusDrawable;
    }

    public int getPageCount() {
        return mAdapter.getPageCount();
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalSpacing = horizontalSpacing;
    }

   /**
    * Set the amount of vertical (y) spacing to place between each item
    * in the grid.
    *
    * @param verticalSpacing The amount of vertical space between items,
    * in pixels.
    */
   public void setVerticalSpacing(int verticalSpacing) {
       mVerticalSpacing = verticalSpacing;
   }

   /**
    * Set the width of columns in the grid.
    *
    * @param columnWidth The column width, in pixels.
    */
   public void setColumnWidth(int columnWidth) {
       mColumnWidth = columnWidth;
   }

   /**
    * Set the height of rows in the grid.
    *
    * @param rowHeight The row height, in pixels.
    */
   public void setRowHeight(int rowHeight) {
       mRowHeight = rowHeight;
   }

    /**
     * Set the scale size of the selected view in the grid
     *
     * @param value the scale size
     */
   public void setSelectedScaleValue(int value) {
       if (value >= 1) {
           mSelectedScaleValue = value;
       }
   }

    /**
     * set the padding left of grid
     * @param value the left padding in pixels
     */
    public void setShadowLeft(int value){
        if(value >= 0){
            mShadowLeft = value;
            mFadingSpaceLeft = value;
        }
    }

    /**
     * set the padding right of grid
     * @param value the right padding in pixels
     */
    public void setShadowRight(int value){
        if(value >=0){
            mShadowRight = value;
            mFadingSpaceRight = value;
        }
    }

    /**
     * set the padding right of grid
     * @param value the right padding in pixels
     */
    public void setShadowBottom(int value){
        if(value >=0){
            mShadowBottom = value;
            mFadingSpaceBottom = value;
        }
    }

    public void setVerticalPageSpacing(int value){
        if(value >=0){
            mVerticalPageSpacing = value;
        }
    }

    public void setFadingEdgeDrawable(Drawable drawable) {
        if (drawable != null) {
            mFadingDrawer = drawable;
        }
    }

    public void setFadingEdgeArrowLeft(Drawable drawable) {
        if (drawable != null) {
            mFadingArrowLeft = drawable;
        }
    }

    public void setFadingEdgeArrowRight(Drawable drawable) {
        if (drawable != null) {
            mFadingArrowRight = drawable;
        }
    }

    public void setAdapter(LtGridAdapter adapter) {
       if (adapter != null) {
           mRecycler.setViewTypeCount(adapter.getViewTypeCount());
       }
       super.setAdapter(adapter);
       mAdapter = adapter;
       onAdapterChanged();
    }

    public int getCurrentPageIndex() {
        return mIndexLayoutPage;
    }

    public View getSelectedItem() {
        return mSelectedItem;
    }

   private void addDrawLayout() {
       DrawLayout drawlayout = msMapDrawLayout.get(mActivity);
       if (drawlayout == null && mActivity != null) {
           mDrawLayout = new DrawLayout(mActivity, this);
           mActivity.getWindow().addContentView(mDrawLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
           msMapDrawLayout.put(mActivity, mDrawLayout);
       } else {
           mDrawLayout = drawlayout;
       }

       if (null != mDrawLayout) {
           mDrawLayout.setSelectPadding(mFocusFrameLeft, mFocusFrameTop, mFocusFrameRight, mFocusFrameBottom);
       }
   }

   public void setSelectPadding(int left, int top, int right, int bottom) {
       mFocusFrameLeft = left;
       mFocusFrameTop = top;
       mFocusFrameRight = right;
       mFocusFrameBottom = bottom;

       if (null != mDrawLayout) {
           mDrawLayout.setSelectPadding(mFocusFrameLeft, mFocusFrameTop, mFocusFrameRight, mFocusFrameBottom);
       }
   }

    void refreshData() {
       if (mDataChanged || mIndexLayoutPage != mIndexNextLayoutPage) {
           return;
       }
       int count = mAdapter.getItemCount(mIndexLayoutPage);
       for (int posInPage = 0; posInPage < count; posInPage++) {
           int position = getPosition(mIndexLayoutPage, posInPage);
           int idx = position - mFirstPosition;
           if (idx >= 0 && idx < getChildCount()) {
               View child = getChildAt(idx);
               if (child != null) {
                    mAdapter.updateItemViewForInvalid(mIndexLayoutPage, posInPage, child);
               }
           }
       }
   }

    private int getPosition(int indexPage, int posInPage) {
        if (mAdapter == null) {
            return 0;
        }
        int position = 0;
        for (int i = 0; i < indexPage; i++) {
            position += mAdapter.getItemCount(i);
        }
        position += posInPage;
        return position;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mDataChanged) {
            handleDataChanged();
        }

        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        Log.e(LOG_TAG, ">>>>>>>>>>>>>>onMeasure widthSize = " + widthSize + " heightSize = " + heightSize);

        if (mAdapter != null) {
            if (mAdapter.isNeedToCalculateWH()) {
                mVerticalSpacing = mAdapter.getColumnSpacing();
                mHorizontalSpacing = mAdapter.getRowSpacing();
                if (mAdapter.getNumColumns() <= 0) {
                    mColumnWidth = widthSize;
                } else {
                    if (mScrollMode == ScrollMode.SCROLL_MODE_CELL && mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
                        mColumnWidth = (widthSize
                                - (mAdapter.getNumColumns() / mAdapter.getOriginalPageCount() - 1) * mAdapter.getColumnSpacing()
                                - (mShadowLeft + mShadowRight))
                                / (mAdapter.getNumColumns() / mAdapter.getOriginalPageCount());
                    } else {
                        mColumnWidth = (widthSize
                                - (mAdapter.getNumColumns() - 1) * mAdapter.getColumnSpacing()
                                - (mShadowLeft + mShadowRight) - (mFadingEdgeEnabled ? mPageSpacing * 2 : 0))
                                / (mAdapter.getNumColumns());
                    }
                    Log.e(LOG_TAG, ">>>>>>>>>>>>>>onMeasure mColumnWidth = " + mColumnWidth);
                }
                if (mAdapter.getNumRows() <= 0) {
                    mRowHeight = heightSize;
                } else {
                    if (mScrollMode == ScrollMode.SCROLL_MODE_CELL && mScrollOrientation == ScrollOrientation.SCROLL_VERTICAL) {
                        mRowHeight = (heightSize
                                - (mAdapter.getNumRows() / mAdapter.getOriginalPageCount() - 1) * mAdapter.getRowSpacing()
                                - (mShadowBottom))
                                / (mAdapter.getNumRows() / mAdapter.getOriginalPageCount());
                    } else {
                        mRowHeight = (heightSize
                                - (mAdapter.getNumRows() - 1) * mAdapter.getRowSpacing()
                                - (mShadowBottom) - (mFadingEdgeEnabled ? mVerticalPageSpacing * 2 : 0))
                                / mAdapter.getNumRows();
                    }
                    Log.e(LOG_TAG, ">>>>>>>>>>>>>>onMeasure mRowHeight = " + mRowHeight);
                }
            }
        }
        setMeasuredDimension(widthSize, heightSize);
        mWidthMeasureSpec = widthMeasureSpec;
        mHeightMeasureSpec = heightMeasureSpec;
    }

    /**
     * Subclasses should NOT override this method but
     *  {@link #layout(int, boolean)} instead.
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layout(0, false);
    }

    @Override
    void layout(int delta, boolean animate) {
        // TODO Auto-generated method stub
        mInLayout = true;
        if (mDataChanged) {
            mMapRecordPosition.clear();
            handleDataChanged();
        }

        // Handle an empty LTGrid by removing all views.
        if ((mItemCount == 0) || (mAdapter == null)) {
            resetList();
            return;
        }

        // Update to the new selected position.
        if (mNextSelectedPosition >= 0) {
            if (mNextSelectedPosition > getCount() - 1) {
                mNextSelectedPosition = getCount() - 1;
            }
            setSelectedPositionInt(mNextSelectedPosition);
        }

        // All views go in recycler while we are in layout
        if (mDataChanged) {
            mRecycler.scrapActiveViews();
            recycleAllViewsToScrap();
        } else {
            recycleAllViewsToActive();
        }

        // Clear out old views
        detachAllViewsFromParent();
        if (mIndexNextLayoutPage > 0 && mFadingEdgeEnabled) {
            mFirstPosition = getPosition(mIndexNextLayoutPage - 1, 0);
            layout(mIndexNextLayoutPage - 1);
        } else {
            int indexLayout = Math.min(mIndexLayoutPage, mIndexNextLayoutPage);
            mFirstPosition = getPosition(indexLayout, 0);
        }
        layout(mIndexNextLayoutPage);
        if (mIndexNextLayoutPage + 1 < getPageCount() && mFadingEdgeEnabled) {
            layout(mIndexNextLayoutPage + 1);
        }
        checkSelectionChanged();
        setNextSelectedPositionInt(mSelectedPosition);
        invalidate();
        mDataChanged = false;
        updateSelectedItemMetadata();
        mInLayout = false;
    }

    void layout(int position) {
        if (mAdapter == null) {
            return;
        }
        int indexLayout = Math.min(mIndexLayoutPage, mIndexNextLayoutPage);
        if (mFadingEdgeEnabled) {
            indexLayout = position;
        }
        int firstPos = getPosition(indexLayout, 0);
        for (int index = 0; index < mAdapter.getItemCount(indexLayout); index++) {
            makeAndAddView(firstPos + index, 0, false, 0, false, 0);
        }
        if (mIndexLayoutPage != mIndexNextLayoutPage && !mFadingEdgeEnabled) {
            indexLayout = Math.max(mIndexLayoutPage, mIndexNextLayoutPage);
            firstPos = getPosition(indexLayout, 0);
            for (int index = 0; index < mAdapter.getItemCount(indexLayout); index++) {
                makeAndAddView(firstPos + index, 0, false, 0, false, 0);
            }
        }
    }

   private int getPageWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
   }

   private int getPageHeight() {
       return getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
   }

    private int getScrollDistance() {
        if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
            if (mFadingEdgeEnabled) {
                return getPageWidth() - mFadingSpaceLeft - mFadingSpaceRight - mPageSpacing;
            } else {
                return getPageWidth() + mVerticalSpacing;
            }
        } else {
            if (mFadingEdgeEnabled) {
                return getPageHeight() - mFadingSpaceBottom - mVerticalPageSpacing;
            } else {
                return getPageHeight() + mHorizontalSpacing;
            }
        }
    }

    /**
     * get item view position (left, top, right, bottom)
     * @param indexPage Index for page
     * @param posInPage The location of the view
     * @param startRow left
     * @param startCol top
     * @param endRow right
     * @param endCol bottom
     * @return whether get position success
     */
    private boolean getRowAndColInPage(int indexPage, int posInPage,
            AtomicInteger startRow, AtomicInteger startCol, AtomicInteger endRow, AtomicInteger endCol) {
        if (mAdapter == null) {
            return false;
        }

        if (indexPage < 0 || posInPage < 0 || (indexPage > (mAdapter.getPageCount() - 1))
                || (posInPage > (mAdapter.getItemCount(indexPage) - 1))) {
            return false;
        }
        int numRows = mAdapter.getNumRows();
        int numCols = mAdapter.getNumColumns();
        int startIndex = mAdapter.getItemStartIndex(indexPage, posInPage);
        int itemRows = mAdapter.getItemRowSize(indexPage, posInPage);
        int itemCols = mAdapter.getItemColumnSize(indexPage, posInPage);
        if (numRows <= 0 || numCols <= 0) {
            return false;
        }

        if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
            startCol.set(startIndex / numRows);
            startRow.set(startIndex % numRows);
        } else {
            startRow.set(startIndex / numCols);
            startCol.set(startIndex % numCols);
        }
        endRow.set(startRow.get() + itemRows - 1);
        endCol.set(startCol.get() + itemCols - 1);
        return true;
    }

    void onAdapterChanged() {
        mAdapterChanged = true;
        mMapRecordPosition.clear();
        if (mDrawLayout != null && mDrawLayout.getGridView() == this) {
            mDrawLayout.onDismissFocus();
        }
    }

    void updateSelectedItemMetadata() {
        View oldSelectedChild = mSelectedItem;

        View child;
        int selectedIndex = mSelectedPosition - mFirstPosition;
        if ((selectedIndex >= 0) && (selectedIndex < getChildCount())) {
            child = mSelectedItem = getChildAt(selectedIndex);
        } else {
            child = mSelectedItem = null;
        }

        if (child != null) {
            if (hasFocus()) {
                child.setSelected(true);
            } else {
                child.setSelected(false);
            }
        }

        // We not focus the old child down here so the above hasFocus check
        // returns true
        if (oldSelectedChild != null && oldSelectedChild != child) {
            // Make sure its drawable state doesn't contain 'selected'
            oldSelectedChild.setSelected(false);

            // Make sure it is not focusable anymore, since otherwise arrow keys
            // can make this one be focused
            // Workaround for Focus selector issue
            // oldSelectedChild.setFocusable(false);
        }
        if (mAdapterChanged && (mDrawLayout != null)) {
            mAdapterChanged = false;
            mDrawLayout.startFocusAnim();
        }
    }

    private boolean scrollToPosition(int position) {
        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        boolean success = mAdapter.getPageIndexAndPosInPage(position, pageIndex, posInPage);
        if (success) {
            if (pageIndex.get() == mIndexLayoutPage || pageIndex.get() == mIndexNextLayoutPage) {
                return false;
            }
            int distance;
            mIsScrolling = true;
            setLayerType(View.LAYER_TYPE_NONE, null);
            if (mScrollOrientation == ScrollOrientation.SCROLL_VERTICAL) {
                distance = (pageIndex.get() * getScrollDistance()) - mScroller.getCurrY();
                mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, distance, 0);
            } else {
                distance = (pageIndex.get() * getScrollDistance()) - mScroller.getCurrX();
                mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), distance, 0, 0);
            }
            mIndexLayoutPage = pageIndex.get();
            mIndexNextLayoutPage = pageIndex.get();
            invalidate();
        }
        return success;
    }

    private void scrollToUp() {
        if (mScroller == null) {
            return;
        }
        mIndexNextLayoutPage = mIndexLayoutPage - 1;
        if (mIndexNextLayoutPage < 0) {
            mIndexNextLayoutPage = 0;
        }
        mIsScrolling = true;
        mDrawLayout.onDismissFocus();
        setLayerType(View.LAYER_TYPE_NONE, null);
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, 0 - getScrollDistance());
        postInvalidate();
    }

    private void scrollToDown() {
        if (mScroller == null) {
            return;
        }
        mIndexNextLayoutPage = mIndexLayoutPage + 1;
        if (mIndexNextLayoutPage > mAdapter.getPageCount() -1) {
            mIndexNextLayoutPage = mAdapter.getPageCount() -1;
        }
        mIsScrolling = true;
        mDrawLayout.onDismissFocus();
        setLayerType(View.LAYER_TYPE_NONE, null);
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, getScrollDistance());
        postInvalidate();
    }

    private void scrollToLeft() {
        if (mScroller == null) {
            return;
        }
        mIndexNextLayoutPage = mIndexLayoutPage - 1;
        if (mIndexNextLayoutPage < 0) {
            mIndexNextLayoutPage = 0;
        }
        mIsScrolling = true;
        mDrawLayout.onDismissFocus();
        setLayerType(View.LAYER_TYPE_NONE, null);
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0 - getScrollDistance(), 0);
        postInvalidate();
    }

    private void scrollToRight() {
        if (mScroller == null) {
            return;
        }
        mIndexNextLayoutPage = mIndexLayoutPage + 1;
        if (mIndexNextLayoutPage > mAdapter.getPageCount() -1) {
            mIndexNextLayoutPage = mAdapter.getPageCount() -1;
        }
        mIsScrolling = true;
        mDrawLayout.onDismissFocus();
        setLayerType(View.LAYER_TYPE_NONE, null);
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), getScrollDistance(), 0);
        postInvalidate();
    }

    private void trackScroll() {
        layout(0, false);
    }

    /**
     * Obtain the view and add it to our list of children. The view can be made
     * fresh, converted from an unused view, or used as is if it was in the
     * recycle bin.
     *
     * @param position Logical position in the list
     * @param y Top or bottom edge of the view to add
     * @param flow If flow is true, align top edge to y. If false, align bottom
     *        edge to y.
     * @param childrenLeft Left edge where children should be positioned
     * @param selected Is this position selected?
     * @param where unused
     * @return View that was added
     */
    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft,
            boolean selected, int where) {
        View child;

        if (!mDataChanged) {
            // Try to use an existing view for this position
            child = mRecycler.getActiveView(position);
            if (child != null) {
                // Found it -- we're using an existing child
                // This just needs to be positioned
                setupChild(child, position, y, flow, childrenLeft, selected, true);
                return child;
            }
        }

        // Make a new view for this position, or convert an unused view if possible
        child = obtainView(position, mIsScrap);

        // This needs to be positioned and measured
        setupChild(child, position, y, flow, childrenLeft, selected, mIsScrap[0]);

        return child;
    }

    /**
     * Add a view as a child and make sure it is measured (if necessary) and
     * positioned properly.
     *
     * @param child The view to add
     * @param position The position of this child
     * @param y The y position relative to which this view will be positioned
     * @param flow If true, align top edge to y. If false, align bottom
     *        edge to y.
     * @param childrenLeft Left edge where children should be positioned
     * @param selected Is this position selected?
     * @param recycled Has this view been pulled from the recycle bin? If so it
     *        does not need to be remeasured.
     */
    private void setupChild(View child, int position, int y, boolean flow, int childrenLeft,
            boolean selected, boolean recycled ) {
        boolean isSelected = selected;
        final boolean updateChildSelected = isSelected != child.isSelected();
        final boolean isPressed = false;
        final boolean updateChildPressed = isPressed != child.isPressed();
        
        boolean needToMeasure = !recycled || child.isLayoutRequested();

        // Respect layout params that are already in the view. Otherwise make some up...
        // noinspection unchecked
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (LayoutParams) generateDefaultLayoutParams();
        }
        p.viewType = mAdapter.getItemViewType(position);

        if (recycled) {
            attachViewToParent(child, -1, p);
        } else {
            addViewInLayout(child, -1, p, true);
        }
        if (updateChildSelected) {
            child.setSelected(isSelected);
            if (isSelected) {
                requestFocus();
            }
        }
        if (updateChildPressed) {
            child.setPressed(isPressed);
        }
        AtomicInteger indexPage = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        mAdapter.getPageIndexAndPosInPage(position,indexPage,posInPage);
        int startCoordinate = indexPage.get() * getScrollDistance() + mShadowLeft; // 该item的x轴左边空白间隔
        AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);

        int childWidthSpec;
        int childHeightSpec;
        boolean success = getRowAndColInPage(indexPage.intValue(), posInPage.intValue(), startRow, startCol, endRow, endCol);
        if (success) {
            int numCols = endCol.get() - startCol.get() + 1;
            int numRows =  endRow.get() - startRow.get() + 1;
            childWidthSpec = View.MeasureSpec.makeMeasureSpec(mColumnWidth * numCols + (numCols - 1) * mVerticalSpacing, View.MeasureSpec.EXACTLY);
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(mRowHeight * numRows + (numRows - 1) * mHorizontalSpacing, View.MeasureSpec.EXACTLY);
            child.measure(childWidthSpec, childHeightSpec);

            int leftItem, topItem, rightItem, bottomItem;
            if (mScrollOrientation == ScrollOrientation.SCROLL_VERTICAL) {
                topItem = startCoordinate + startRow.get() * (mRowHeight + mHorizontalSpacing)
                        + (mFadingEdgeEnabled ? mVerticalPageSpacing : 0);
                leftItem = startCol.get() * (mColumnWidth + mVerticalSpacing);
            } else {
                leftItem = startCoordinate + startCol.get() * (mColumnWidth + mVerticalSpacing)
                + (mFadingEdgeEnabled ? mPageSpacing: 0);
                topItem = startRow.get() * (mRowHeight + mHorizontalSpacing);
            }

            rightItem = leftItem + child.getMeasuredWidth();
            bottomItem = topItem + child.getMeasuredHeight();

            child.layout(leftItem, topItem, rightItem, bottomItem);
        }
    }

    /**
     * Returns the view at the specified position in the group.
     *
     * @param position the position at which to get the view from
     * @return the view at the specified position or null if the position
     *         does not exist within the group
     */
    private View getChildView(int position) {
        View child = null;
        int indexChild = position - mFirstPosition;
        if (indexChild >= 0 && indexChild < getChildCount()) {
            child = getChildAt(indexChild);
            return child;
        }
        return child;
    }

    /**
     * Judge the view at the specified position in the group whether
     * hide a part
     * @param position the position at which to get the view from
     * @return the result the view whether hide a part
     */
    private boolean isHalfVisibleChild(int position) {
        View child = getChildView(position);
        if (child != null) {
            Rect ret = new Rect();
            boolean isVisible = child.getLocalVisibleRect(ret);
            if (isVisible && (ret.width() < child.getWidth())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Judge the view at the specified position in the group whether visible
     * @param position the position at which to get the view from
     * @return the result the view whether visible
     */
    private boolean isVisibleChild(int position) {
        View child = getChildView(position);
        if (child != null) {
            Rect ret = new Rect();
            return child.getLocalVisibleRect(ret);
        }
        return false;
    }

    private boolean scrollToPositionByCell(int position) {
        if (mScrollMode != ScrollMode.SCROLL_MODE_CELL || mScroller == null) {
            return false;
        }
        boolean isVisible = isVisibleChild(position);
        boolean isHalfVisible = isHalfVisibleChild(position);

        if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
            int desX;
            int left = 0;
            View child = null;
            if (isHalfVisible) {
                child = getChildView(position);
                if (child == null) {
                    return false;
                }
                left = child.getLeft();
            } else if (!isVisible) {
                child = getChildView(position);
                if (child == null) {
                    return false;
                }
                left = child.getLeft();
            }

            // set item view in the middle
            int width = getWidth();
            int distance = width - child.getWidth() - mAdapter.getColumnSpacing();
            desX = Math.max(0, left - distance / 2);
            int lastDistanceX = desX;
            int lastPosition = mAdapter.getCount() - 1;
            View lastChild = getChildView(lastPosition);
            if (lastChild != null) {
                int lastLeft = lastChild.getLeft();
                distance = width - lastChild.getWidth() - mAdapter.getColumnSpacing();
                lastDistanceX = Math.max(0, lastLeft - distance);
            }
            Log.e(LOG_TAG, ">>>>>>>>>>>>>>> scrollToPositionByCell desX = " + desX + " lastDistanceX = " + lastDistanceX);
            desX = Math.min(lastDistanceX ,desX);

            mIsScrolling = true;
            mDrawLayout.onDismissFocus();
            setLayerType(View.LAYER_TYPE_NONE, null);
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), desX - mScroller.getCurrX(), 0);
            postInvalidate();
            return true;
        } else {
            int desY;
            int top = 0;
            View child = null;
            if (isHalfVisible) {
                child = getChildView(position);
                if (child == null) {
                    return false;
                }
                top = child.getTop();
            } else if (!isVisible) {
                child = getChildView(position);
                if (child == null) {
                    return false;
                }
                top = child.getTop();
            }
            int height = getHeight();
            int distance = height - child.getWidth() - mAdapter.getRowSpacing();
            desY = Math.max(0, top - distance / 2);
            int lastDistanceY = desY;
            int lastPosition = mAdapter.getCount() - 1;
            View lastChild = getChildView(lastPosition);
            if (lastChild != null) {
                int lastTop = lastChild.getTop();
                distance = height - lastChild.getWidth() - mAdapter.getRowSpacing();
                lastDistanceY = Math.max(0, lastTop - distance);
            }
            Log.e(LOG_TAG, ">>>>>>>>>>>>>>> scrollToPositionByCell desY= " + desY + " lastDistanceY = " + lastDistanceY);
            desY = Math.min(lastDistanceY, desY);
            if (desY != 0) {
                mIsScrolling = true;
                mDrawLayout.onDismissFocus();
                setLayerType(View.LAYER_TYPE_NONE, null);
                mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, desY - mScroller.getCurrX());
                postInvalidate();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (!handled) {
            // Our workaround
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                return onKeyDown(event.getKeyCode(), event);
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                return onKeyUp(event.getKeyCode(), event);
            }
        }
        return handled;
    }

    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mFadingEdgeEnabled && mFadingDrawer != null) {
            if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
                canvas.translate(getScrollX(), getScrollY());
                float density = getContext().getResources().getDisplayMetrics().density;
                if (mIndexNextLayoutPage > 0) {
                    mFadingDrawer.setBounds(0, 0, mFadingSpaceLeft, getHeight());
                    mFadingDrawer.draw(canvas);
                    if (mFadingArrowLeft != null) {
                        int width = (int) (mFadingArrowLeft.getIntrinsicWidth() / density);
                        int height = (int) (mFadingArrowLeft.getIntrinsicHeight() / density);
                        mFadingArrowLeft.setBounds(mFadingSpaceLeft - width,
                                (getHeight() - height) / 2,
                                mFadingSpaceLeft, (getHeight() + height) / 2);
                        mFadingArrowLeft.draw(canvas);
                    }
                }
                if (mAdapter != null && mIndexNextLayoutPage < mAdapter.getPageCount() - 1) {
                    mFadingDrawer.setBounds(getWidth() - mFadingSpaceRight, 0, getWidth(), getHeight());
                    mFadingDrawer.draw(canvas);
                    if (mFadingArrowRight != null) {
                        int width = (int) (mFadingArrowRight.getIntrinsicWidth() / density);
                        int height = (int) (mFadingArrowRight.getIntrinsicHeight() / density);
                        mFadingArrowRight.setBounds(getWidth() - mFadingSpaceRight,
                                (getHeight() - height) / 2,
                                getWidth() - mFadingSpaceRight + width,
                                (getHeight() + height) / 2);
                        mFadingArrowRight.draw(canvas);
                    }
                }
            } else {
                canvas.translate(getScrollX(), getScrollY());
                if (mAdapter != null && mIndexNextLayoutPage < mAdapter.getPageCount() - 1) {
                    mFadingDrawer.setBounds(0, getHeight() - mFadingSpaceBottom, getWidth(), getHeight());
                    mFadingDrawer.draw(canvas);
                }
            }
        }
        if (mDrawLayout != null && mDrawLayout.getGridView() != null) {
            mDrawLayout.invalidate();
        }
    }

    public void setCurrentPageSelectPosition(int pos) {
        if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
            int selectPos = pos;
            if (mIndexLayoutPage != 0) {
                selectPos = getPosition(mIndexLayoutPage, pos);
            }
            setSelection(selectPos);
        }
    }

    public void scrollToSpecPage(int pageIndex) {
        if (pageIndex < 0 || pageIndex > mAdapter.getPageCount() - 1) {
            return;
        }

        if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
            if (mIndexLayoutPage != pageIndex) {
                mScrollSpecPageIndex = pageIndex;
                mSelectedPosition = 0;
                mSelectedItem = getChildAt(0);
                setSelection(0, false);
                mIsScrollToSpecPage = true;
                if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
                    if (pageIndex < mIndexLayoutPage) {
                        scrollToLeft();
                    } else {
                        scrollToRight();
                    }
                } else {
                    if (pageIndex < mIndexLayoutPage) {
                        scrollToUp();
                    } else {
                        scrollToDown();
                    }
                }
            } else {
                mSelectedPosition = 0;
                mSelectedItem = getChildAt(0);
                setSelection(0, false);
            }
        }
    }
    /**
     * only invalid by SCROLL_CELL mode
     * @param position position
     */
    public void setFocusToPosition(int position) {
        Log.e(LOG_TAG, ">>>>>>>>>>>>>>>> setFocusToPosition mSelectedPosition = " + mSelectedPosition + " position = " + position);
        if (mSelectedPosition != position) {
            mSelectedPosition = position;
            mSelectedItem = getChildAt(position);
            setSelection(position, false);
            if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
                if (position == 0) {
                    if (mIndexLayoutPage != 0) {
                        mIndexLayoutPage = 0;
                        int distance = 0 - mScroller.getCurrX();
                        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), distance, 0, 300);
                    }
                } else if (position == mAdapter.getCount() - 1) {
                    if (mIndexLayoutPage != mAdapter.getPageCount() - 1) {
                        int scrollDistance = mAdapter.getPageCount() - 1 - mIndexLayoutPage;
                        mIndexLayoutPage = mAdapter.getPageCount() - 1;
                        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), getScrollDistance() * scrollDistance, 0, 300);
                    }
                }
            } else {
                boolean isVisible = isVisibleChild(position);
                boolean isHalfVisible = isHalfVisibleChild(position);
                Log.e(LOG_TAG, ">>>>>>>>>>>>>>>> setFocusToFirst isVisible = " + isVisible + " isHalfVisible = " + isHalfVisible);
                if (!isVisible || isHalfVisible) {
                    scrollToPositionByCell(position);
                }
            }
        }
    }

    protected void onFocusChanged (boolean gainFocus, int 
            direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        /*
         * The LTGrid shows focus by focusing the selected item. So, give
         * focus to our selected item instead. We steal keys from our
         * selected item elsewhere.
         */
        mDrawLayout.setGridView(this);
        if (gainFocus) {
            mDrawLayout.bringToFront();
        }
        if (mSelectedItem != null) {
            if (gainFocus) {
                // Workaround for Focus selector issue
                // mSelectedItem.requestFocus(direction);
                mSelectedItem.setSelected(true);
            } else {
                mSelectedItem.setSelected(false);
            }
            if (gainFocus && !mInLayout && mIsNeedFocusScaleAnim) {
                mDrawLayout.startFocusAnim();
            }
        }
        if (!gainFocus) {
            setLayerType(View.LAYER_TYPE_NONE, null);
            mDrawLayout.onDismissFocus();
        }
        if (gainFocus) {
            if (mOnAnimationChangeListener != null) {
                mOnAnimationChangeListener.onAnimationStop();
            }
        } else {
            if (mOnAnimationChangeListener != null) {
                mOnAnimationChangeListener.onAnimationStart();
            }
        } 
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
          case KeyEvent.KEYCODE_DPAD_LEFT:
            if (moveLeft()) {
                // playSoundEffect(SoundEffectConstants.NAVIGATION_LEFT);
                return true;
            }
            break;

          case KeyEvent.KEYCODE_DPAD_UP:
            if (moveUp()) {
                // playSoundEffect(SoundEffectConstants.NAVIGATION_UP);
                return true;
            }
            break;

          case KeyEvent.KEYCODE_DPAD_RIGHT:
            if (moveRight()) {
                // playSoundEffect(SoundEffectConstants.NAVIGATION_RIGHT);
                return true;
            }
            break;

          case KeyEvent.KEYCODE_DPAD_DOWN:
            if (moveDown()) {
                // playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
                return true;
            }
            break;

          case KeyEvent.KEYCODE_DPAD_CENTER:
          case KeyEvent.KEYCODE_ENTER:
            mReceivedInvokeKeyDown = true;
            break;
            // fallthrough to default handling
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
          case KeyEvent.KEYCODE_DPAD_CENTER:
          case KeyEvent.KEYCODE_ENTER: {
            if (mReceivedInvokeKeyDown) {
                if ((mAdapter != null) && (mSelectedItem != null)) {
                    performItemClick(mSelectedItem, mSelectedPosition, mSelectedItem.getId());
                }
            }
            // Clear the flag
            mReceivedInvokeKeyDown = false;
            return true;
          }
        }
        return super.onKeyUp(keyCode, event);
    }

    private void startFocusMoveAnim() {
        setLayerType(View.LAYER_TYPE_NONE, null);
        mIsDrawFocusMoveAnim = true;
        if (mOnAnimationChangeListener != null) {
            mOnAnimationChangeListener.onAnimationStart();
        }
        mScrollerFocusMoveAnim.startScroll(0, 0, 100, 100, 167);
        invalidate();
    }

    void selectionChanged() {
        super.selectionChanged();
        if (mDrawLayout != null) {
            mDrawLayout.onDismissFocus();
        }
        selectionAnimNotifier();
    }

    void selectionAnimNotifier() {
        if (mInLayout || mIsScrolling) {
            if (mSelectionAnimNotifier == null) {
                mSelectionAnimNotifier = new LtSelectionAnimNotifier(this);
            }
            Message msg = Message.obtain();
            mSelectionAnimNotifier.sendMessage(msg);
        } else {
            fireOnSelectedAnim();
        }
    }

    void fireOnSelectedAnim() {
        updateSelectedItemMetadata();
    }


    private boolean moveLeft() {
        Log.i("LtGridView", "moveLeft: ================================");
        if (mAdapter == null) {
            Log.e(LOG_TAG, "mAdapter is null in moveLeft");
            return false;
        }
        if (mIsScrolling) {
            return true;
        }
        if (mIsDrawFocusMoveAnim) {
            mScrollerFocusMoveAnim.abortAnimation();
            computeScroll();
        }
        int pos = findNextSelectedPosition(Direction.LEFT);
        if (pos == INVALID_POSITION) {
            return false;
        }
        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        boolean success = mAdapter.getPageIndexAndPosInPage(pos, pageIndex, posInPage);
        if (success) {
            setNextSelectedPositionInt(pos);
            if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
                if (pageIndex.get() != mIndexLayoutPage) {
                    scrollToLeft();
                } else {
                    startFocusMoveAnim();
                }
            } else {
                boolean isVisible = isVisibleChild(pos);
                boolean isHalfVisible  = isHalfVisibleChild(pos);
                if (isHalfVisible || !isVisible) {
                    return scrollToPositionByCell(pos);
                } else {
                    startFocusMoveAnim();
                }
            }
            return true;
        }
        return false;
    }

    private boolean moveRight() {
        Log.i("LtGridView", "moveRight: ================================");
        if (mAdapter == null) {
            Log.e(LOG_TAG, "mAdapter is null in moveRight");
            return false;
        }
        if (mIsScrolling) {
            return true;
        }
        if (mIsDrawFocusMoveAnim) {
            mScrollerFocusMoveAnim.abortAnimation();
            computeScroll();
        }
        int pos = findNextSelectedPosition(Direction.RIGHT);
        if (pos == INVALID_POSITION) {
            return false;
        }
        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        boolean success = mAdapter.getPageIndexAndPosInPage(pos, pageIndex, posInPage);
        if (success) {
            setNextSelectedPositionInt(pos);
            if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
                if (pageIndex.get() != mIndexLayoutPage) {
                    scrollToRight();
                } else {
                    startFocusMoveAnim();
                }
            } else {
                boolean isVisible = isVisibleChild(pos);
                boolean isHalfVisible  = isHalfVisibleChild(pos);
                if (isHalfVisible || !isVisible) {
                    return scrollToPositionByCell(pos);
                } else {
                    startFocusMoveAnim();
                }
            }
            return true;
        }
        return false;
    }

    private boolean moveUp() {
        Log.i("LtGridView", "moveUp: ================================");
        if (mAdapter == null) {
            Log.e(LOG_TAG, "mAdapter is null in moveUp");
            return false;
        }
        if (mIsScrolling) {
            return true;
        }
        if (mIsDrawFocusMoveAnim) {
            mScrollerFocusMoveAnim.abortAnimation();
            computeScroll();
        }
        int pos = findNextSelectedPosition(Direction.UP);
        if (pos == INVALID_POSITION) {
            return false;
        }
        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        boolean success = mAdapter.getPageIndexAndPosInPage(pos, pageIndex, posInPage);
        if (success) {
            setNextSelectedPositionInt(pos);
            if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
                if (pageIndex.get() != mIndexLayoutPage) {
                    scrollToUp();
                } else {
                    startFocusMoveAnim();
                }
            } else {
                boolean isVisible = isVisibleChild(pos);
                boolean isHalfVisible  = isHalfVisibleChild(pos);
                if (isHalfVisible || !isVisible) {
                    return scrollToPositionByCell(pos);
                } else {
                    startFocusMoveAnim();
                }
            }
            return true;
        }
        return false;
    }

    private boolean moveDown() {
        Log.i("LtGridView", "moveDown: ================================");
        if (mAdapter == null) {
            Log.e(LOG_TAG, "mAdapter is null in moveDown");
            return false;
        }
        if (mIsScrolling) {
            return true;
        }
        if (mIsDrawFocusMoveAnim) {
            mScrollerFocusMoveAnim.abortAnimation();
            computeScroll();
        }
        int pos = findNextSelectedPosition(Direction.DOWN);
        if (pos == INVALID_POSITION) {
            return false;
        }
        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        boolean success = mAdapter.getPageIndexAndPosInPage(pos, pageIndex, posInPage);
        if (success) {
            setNextSelectedPositionInt(pos);
            // mNextSelectedPosition = pos;
            if (mScrollMode == ScrollMode.SCROLL_MODE_PAGE) {
                if (pageIndex.get() != mIndexLayoutPage) {
                    scrollToDown();
                } else {
                    // setNextSelectedPositionInt(pos);
                    startFocusMoveAnim();
                }
            } else {
                boolean isVisible = isVisibleChild(pos);
                boolean isHalfVisible  = isHalfVisibleChild(pos);
                if (isHalfVisible || !isVisible) {
                    return scrollToPositionByCell(pos);
                } else {
                    startFocusMoveAnim();
                }
            }
            return true;
        }
        return false;
    }

    public int findNextSelectedPosition(Direction direct) {
        switch (direct) {
            case LEFT: {
                return findLeftPosition(mSelectedPosition);
            }
            case UP: {
                return findUpPosition(mSelectedPosition);
            }
            case RIGHT: {
                return findRightPosition(mSelectedPosition);
            }
            case DOWN: {
                return findDownPosition(mSelectedPosition);
            }
            default:
                break;
        }
        return INVALID_POSITION;
    }

    private int findPosition(int indexPage, int row, int col) {
        if (indexPage == INVALID_POSITION || row == INVALID_POSITION || col == INVALID_POSITION) {
            return INVALID_POSITION;
        }
        int result = INVALID_POSITION;
        AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);
        for (int j = 0; j < mAdapter.getItemCount(indexPage); j++) {
            boolean success = getRowAndColInPage(indexPage, j, startRow, startCol, endRow, endCol);
            if (!success) {
                continue;
            }
            if (row >= startRow.get() && row <= endRow.get() && col >= startCol.get() && col <= endCol.get()) {
                result = getPosition(indexPage, j);
                break;
            }
        }
        return result;
    }

    private int findLeftPosition(int position) {
        PositionRecord record = mMapRecordPosition.get(position);
        
        int nextPosition = INVALID_POSITION;
        if (record != null) {
            nextPosition = record.posLeft;
            if (nextPosition != INVALID_POSITION) {
                PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
                if (nextRecord != null) {
                    nextRecord.posRight = position;
                } else {
                    nextRecord = new PositionRecord();
                    nextRecord.posRight = position;
                    mMapRecordPosition.put(nextPosition, nextRecord);
                }
                return nextPosition;
            }
        }

        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        int nextPageIndex = INVALID_POSITION;
        boolean sucess = false;
        if (mAdapter != null) {
            sucess = mAdapter.getPageIndexAndPosInPage(position, pageIndex, posInPage);
        }
        if (!sucess) {
            return INVALID_POSITION;
        }
        AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);
        boolean success = getRowAndColInPage(pageIndex.get(), posInPage.get(), startRow, startCol, endRow, endCol);
        if (!success) {
            return INVALID_POSITION;
        }
        if (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation) {
            if (startCol.get() == 0) {
                return INVALID_POSITION;
            }
        } else {
            if (startCol.get() == 0 && pageIndex.get() <= 0) {
                return INVALID_POSITION;
            }
        }
        boolean bFound = false;
        for (int i = startRow.get(); !bFound && i <= endRow.get(); i++) {
            for (int j = startCol.get() - 1; !bFound && j >=0; j--) {
                int findPos = findPosition(pageIndex.get(), i, j);
                if (INVALID_POSITION != findPos) {
                    bFound = true;
                    nextPosition = findPos;
                }
            }
        }
        if (INVALID_POSITION == nextPosition && pageIndex.get() > 0 &&
                (ScrollOrientation.SCROLL_HORIZONTAL == mScrollOrientation)) {
            nextPageIndex = pageIndex.get() - 1;
            int nextRow = mAdapter.getNumRows() - 1;
            int nextCol = mAdapter.getNumColumns() - 1;
            nextPosition = findPosition(nextPageIndex, nextRow, nextCol);
        }

        PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
        if (nextRecord != null) {
            nextRecord.posRight = position;
        } else {
            nextRecord = new PositionRecord();
            nextRecord.posRight = position;
            mMapRecordPosition.put(nextPosition, nextRecord);
        }
        return nextPosition;
    }

    private int findRightPosition(int position) {
        PositionRecord record = mMapRecordPosition.get(position);
        int nextPosition = INVALID_POSITION;
        if (record != null) {
            nextPosition = record.posRight;
            if (nextPosition != INVALID_POSITION) {
                PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
                if (nextRecord != null) {
                    nextRecord.posLeft = position;
                } else {
                    nextRecord = new PositionRecord();
                    nextRecord.posLeft = position;
                    mMapRecordPosition.put(nextPosition, nextRecord);
                }
                return nextPosition;
            }
        }

        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        int nextPageIndex = INVALID_POSITION;
        boolean sucess = false;
        if (mAdapter != null) {
            sucess = mAdapter.getPageIndexAndPosInPage(position, pageIndex, posInPage);
        }
        if (!sucess) {
            return INVALID_POSITION;
        }
        AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);
        boolean success = getRowAndColInPage(pageIndex.get(), posInPage.get(), startRow, startCol, endRow, endCol);
        if (!success) {
            return INVALID_POSITION;
        }
        if (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation) {
            if (endCol.get() == (mAdapter.getNumColumns() - 1)) {
                   return INVALID_POSITION;
               }
        } else {
           if ((endCol.get() == (mAdapter.getNumColumns() - 1)) && (pageIndex.get() >= (mAdapter.getPageCount() - 1))) {
                    return INVALID_POSITION;
            }
        }
        // foreach every item view to find the next view position
        boolean bFound = false;
        for (int i = startRow.get(); !bFound && i <= endRow.get(); i++) {
            for (int j = endCol.get() + 1; !bFound && j < mAdapter.getNumColumns(); j++) {
                int findPos = findPosition(pageIndex.get(), i, j);
                if (INVALID_POSITION != findPos) {
                    bFound = true;
                    nextPosition = findPos;
                }
            }
        }
        if (INVALID_POSITION == nextPosition && (pageIndex.get() < (mAdapter.getPageCount() - 1)) &&
                (ScrollOrientation.SCROLL_HORIZONTAL == mScrollOrientation)) {
            nextPageIndex = pageIndex.get() + 1;
            nextPosition = findPosition(nextPageIndex, 0, 0);
        }
        PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
        if (nextRecord != null) {
            nextRecord.posLeft = position;
        } else {
            nextRecord = new PositionRecord();
            nextRecord.posLeft = position;
            mMapRecordPosition.put(nextPosition, nextRecord);
        }
        return nextPosition;
    }

    private int findUpPosition(int position) {
        PositionRecord record = mMapRecordPosition.get(position);
        int nextPosition = INVALID_POSITION;
        if (record != null) {
            nextPosition = record.posUp;
            if (nextPosition != INVALID_POSITION) {
                PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
                if (nextRecord != null) {
                    nextRecord.posDown = position;
                } else {
                    nextRecord = new PositionRecord();
                    nextRecord.posDown = position;
                    mMapRecordPosition.put(nextPosition, nextRecord);
                }
                return nextPosition;
            }
        }

        AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
        AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
        int nextPageIndex = INVALID_POSITION;
        boolean sucess = false;
        if (mAdapter != null) {
            sucess = mAdapter.getPageIndexAndPosInPage(position, pageIndex, posInPage);
        }
        if (!sucess) {
            return INVALID_POSITION;
        }
        AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
        AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);
        boolean success = getRowAndColInPage(pageIndex.get(), posInPage.get(), startRow, startCol, endRow, endCol);
        if (!success) {
            return INVALID_POSITION;
        }
        if (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation) {
           if (startRow.get() == 0 && pageIndex.get() <= 0) {
               return INVALID_POSITION;
           }
        } else {
            if (startRow.get() == 0) {
                return INVALID_POSITION;
            }
        }
        boolean bFound = false;
        for (int i = startCol.get(); !bFound && i <= endCol.get(); i++) {
            for (int j = startRow.get() - 1; !bFound && j >=0; j--) {
                int findPos = findPosition(pageIndex.get(), j, i);
                if (INVALID_POSITION != findPos) {
                    bFound = true;
                    nextPosition = findPos;
                }
            }
        }
        if (INVALID_POSITION == nextPosition && pageIndex.get() > 0 &&
                (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation)) {
            nextPageIndex = pageIndex.get() - 1;
            int nextRow = mAdapter.getNumRows() - 1;
            int nextCol = mAdapter.getNumColumns() - 1;
            nextPosition = findPosition(nextPageIndex, nextRow, nextCol);
        }
        PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
        if (nextRecord != null) {
            nextRecord.posDown = position;
        } else {
            nextRecord = new PositionRecord();
            nextRecord.posDown = position;
            mMapRecordPosition.put(nextPosition, nextRecord);
        }
        return nextPosition;
    }

    private int findDownPosition(int position) {
            PositionRecord record = mMapRecordPosition.get(position);
            int nextPosition = INVALID_POSITION;
            if (record != null) {
                nextPosition = record.posDown;
                if (nextPosition != INVALID_POSITION) {
                    PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
                    if (nextRecord != null) {
                        nextRecord.posUp = position;    
                    } else {
                        nextRecord = new PositionRecord();
                        nextRecord.posUp = position;
                        mMapRecordPosition.put(nextPosition, nextRecord);
                    }
                    return nextPosition;
                }
            }

            AtomicInteger pageIndex = new AtomicInteger(INVALID_POSITION);
            AtomicInteger posInPage = new AtomicInteger(INVALID_POSITION);
            int nextPageIndex = INVALID_POSITION;
            boolean sucess = false;
            if (mAdapter != null) {
                sucess = mAdapter.getPageIndexAndPosInPage(position, pageIndex, posInPage);
            }

            if (!sucess) {
                return INVALID_POSITION;
            }
            AtomicInteger startRow = new AtomicInteger(INVALID_POSITION);
            AtomicInteger startCol = new AtomicInteger(INVALID_POSITION);
            AtomicInteger endRow = new AtomicInteger(INVALID_POSITION);
            AtomicInteger endCol = new AtomicInteger(INVALID_POSITION);
            boolean success = getRowAndColInPage(pageIndex.get(), posInPage.get(), startRow, startCol, endRow, endCol);
            if (!success) {
                return INVALID_POSITION;
            }
            if (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation) {
                if (endRow.get() == (mAdapter.getNumRows() - 1) && (pageIndex.get() >= (mAdapter.getPageCount() - 1))) {
                   return INVALID_POSITION;
               }
            } else {
                if (endRow.get() == (mAdapter.getNumRows() - 1)) {
                    return INVALID_POSITION;
                }
            }
            boolean bFound = false;
            for (int i = startCol.get(); !bFound && i <= endCol.get(); i++) {
                for (int j = endRow.get() + 1; !bFound && j < mAdapter.getNumRows(); j++) {
                    int findPos = findPosition(pageIndex.get(), j, i);
                    if (INVALID_POSITION != findPos) {
                        bFound = true;
                        nextPosition = findPos;
                    }
                }
            }
            if (INVALID_POSITION == nextPosition && (pageIndex.get() < (mAdapter.getPageCount() - 1)) && (ScrollOrientation.SCROLL_VERTICAL == mScrollOrientation)) {
                nextPageIndex = pageIndex.get() + 1;
                nextPosition = findPosition(nextPageIndex, 0, 0);
            }
            PositionRecord nextRecord = mMapRecordPosition.get(nextPosition);
            if (nextRecord != null) {
                nextRecord.posUp = position;
            } else {
                nextRecord = new PositionRecord();
                nextRecord.posUp = position;
                mMapRecordPosition.put(nextPosition, nextRecord);
            }
            return nextPosition;
    }

    @Override
    public void computeScroll () {
        if (mScroller.computeScrollOffset()) {
            trackScroll();
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            if (mAdapter != null) {
                mAdapter.updateCurrentPosition(mSelectedPosition);
            }
            if (mIsScrolling) {
                mIsScrolling = false;
                mIndexLayoutPage = mIndexNextLayoutPage;
                checkSelectionChanged();
                setNextSelectedPositionInt(mSelectedPosition);
                refreshData();
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageChanged(mIndexLayoutPage);
                }
                setLayerType(mIsNeedLayerTypeToSoftWare ? View.LAYER_TYPE_SOFTWARE : View.LAYER_TYPE_NONE, null);
                postInvalidate();
            }
            if (mIsScrollToSpecPage && mScrollSpecPageIndex >= 0) {
                if (mScrollOrientation == ScrollOrientation.SCROLL_HORIZONTAL) {
                    if (mScrollSpecPageIndex < mIndexNextLayoutPage) {
                        scrollToLeft();
                    } else if (mScrollSpecPageIndex > mIndexNextLayoutPage) {
                        scrollToRight();
                    }
                } else {
                    if (mScrollSpecPageIndex < mIndexNextLayoutPage) {
                        scrollToUp();
                    } else if (mScrollSpecPageIndex > mIndexNextLayoutPage) {
                        scrollToDown();
                    }
                }
                if (mScrollSpecPageIndex == mIndexNextLayoutPage) {
                    mIsScrollToSpecPage = false;
                    mScrollSpecPageIndex = -1;
                }
            }
        }
        if (mScrollerFocusMoveAnim.computeScrollOffset()) {
            if (mIsDrawFocusMoveAnim) {
                mFocusMoveAnimScale = ((float)(mScrollerFocusMoveAnim.getCurrX()))/100;
            }
            postInvalidate();
        } else {
            if (mIsDrawFocusMoveAnim) {
                mIsDrawFocusMoveAnim = false;
                setSelectedPositionInt(mNextSelectedPosition);
                checkSelectionChanged();
                if (mOnAnimationChangeListener != null) {
                    mOnAnimationChangeListener.onAnimationStop();
                }
                setLayerType(mIsNeedLayerTypeToSoftWare ? View.LAYER_TYPE_SOFTWARE : View.LAYER_TYPE_NONE, null);
                postInvalidate();
            }
        }
    }

    class LtSelectionAnimNotifier extends Handler {
        LtSelectionAnimNotifier(LtGridView gridView) {
            mGridView = gridView;
        }

        public void handleMessage(Message msg) {
            if (mGridView == null) {
                return;
            }
            if (mGridView.mInLayout || mGridView.mIsScrolling) {
                // Data has changed between when this SelectionNotifier
                // was posted and now. We need to wait until the AdapterView
                // has been synched to the new data.
                mGridView.selectionAnimNotifier();
            } else {
                mGridView.fireOnSelectedAnim();
            }
        }

        private LtGridView mGridView;
    }

    public class DrawLayout extends ViewGroup {
        private Activity mActivity;
        private LtGridView mGridView;
        private Scroller mScroller;
        private boolean mIsDrawGetFocusAnim;

        private int mLeftFocusBoundWidth;
        private int mTopFocusBoundWidth;
        private int mRightFocusBoundWidth;
        private int mBottomFocusBoundWidth;

        private float mScaleX;
        private float mScaleY;

        public DrawLayout(Activity activity, LtGridView gridView) {
            super(activity);

            mActivity = activity;
            mGridView = gridView;
            mScroller = new Scroller(mActivity);
            mIsDrawGetFocusAnim = false;
            mLeftFocusBoundWidth = 0;
            mTopFocusBoundWidth = 0;
            mRightFocusBoundWidth = 0;
            mBottomFocusBoundWidth = 0;
            mScaleX = 0;
            mScaleY = 0;
        }

        public LtGridView getGridView() {
            return mGridView;
        }

        public void setGridView(LtGridView grid) {
            mGridView = grid;
        }

        public void setSelectPadding(int left, int top, int right, int bottom) {
            mLeftFocusBoundWidth = left;
            mTopFocusBoundWidth = top;
            mRightFocusBoundWidth = right;
            mBottomFocusBoundWidth = bottom;
        }

        public void startFocusAnim() {
            if (mGridView != null) {
                mGridView.setLayerType(View.LAYER_TYPE_NONE, null);
                View v = null;
                int indexChild = mGridView.mSelectedPosition - mGridView.mFirstPosition;
                if (indexChild >= 0 && indexChild < mGridView.getChildCount()) {
                    v = mGridView.getChildAt(indexChild);
                }
                if (v != null) {
                    mIsDrawGetFocusAnim = true;
                    mScroller.startScroll(0, 0, 100, 100, 300);
                    
                    DrawHandler handler = new DrawHandler();
                    Message msg = Message.obtain();
                    handler.sendMessage(msg);
                }
            }
        }

        public void onDismissFocus() {
            mIsDrawGetFocusAnim = false;
            if (mGridView!= null) {
                mGridView.invalidate();
            }
            invalidate();
        }

        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            if (mGridView != null && mGridView.hasFocus()) {
                drawGetFocusScaleAnim(canvas);
                drawFocusMoveAnim(canvas);
                drawFocus(canvas);
            } 
        }

        boolean compute() {
            if (mScroller.computeScrollOffset()) {
                mScaleX = ((mSelectedScaleValue - 1) * mScroller.getCurrX()) / 100 + 1;
                mScaleY = ((mSelectedScaleValue - 1) * mScroller.getCurrY()) / 100 + 1;
                if (mGridView != null) {
                    mGridView.invalidate();
                }
                return true;
            } else {
                if (mIsDrawGetFocusAnim) {
                    mIsDrawGetFocusAnim = false;
                    if (mGridView != null) {
                        mGridView.setLayerType(mIsNeedLayerTypeToSoftWare ? View.LAYER_TYPE_SOFTWARE : View.LAYER_TYPE_NONE, null);
                        mGridView.invalidate();
                    }
                }
                return false;
            }
        }

        void drawFocus(Canvas canvas) {
            if (mGridView != null && mGridView.hasFocus() && !mGridView.mIsDrawFocusMoveAnim && !mIsDrawGetFocusAnim) {
                View itemView = mGridView.getChildAt(mGridView.mSelectedPosition - mGridView.mFirstPosition);

                if (itemView != null) {
                    int itemLocation[] = new int[2];
                    itemView.getLocationInWindow(itemLocation);

                    int itemWidth = itemView.getWidth();
                    int itemHeight = itemView.getHeight();
                    float itemPositionX = itemLocation[0] - (mSelectedScaleValue - 1) / 2 * itemWidth;
                    float itemPositionY = itemLocation[1] - (mSelectedScaleValue - 1) / 2 * itemHeight;

                    // draw focus image
                    if (mGridView != null && mGridView.mDrawableFocus != null) {
                        int drawLayoutWidth = itemWidth + mLeftFocusBoundWidth + mRightFocusBoundWidth;
                        int drawLayoutHeight = itemHeight + mTopFocusBoundWidth + mBottomFocusBoundWidth;
                        float drawLayoutPositionX = itemPositionX - mSelectedScaleValue * mLeftFocusBoundWidth;
                        float drawLayoutPositionY = itemPositionY - mSelectedScaleValue * mTopFocusBoundWidth;

                        canvas.save();
                        canvas.translate(drawLayoutPositionX, drawLayoutPositionY);
                        canvas.scale(mSelectedScaleValue, mSelectedScaleValue, 0, 0);
                        mGridView.mDrawableFocus.setBounds(0, 0, drawLayoutWidth, drawLayoutHeight);
                        mGridView.mDrawableFocus.draw(canvas);
                        canvas.restore();
                    }

                    // draw item view
                    canvas.save();
                    canvas.translate(itemPositionX, itemPositionY);
                    canvas.scale(mSelectedScaleValue, mSelectedScaleValue, 0, 0);
                    itemView.draw(canvas);
                    canvas.restore();
                }
            }
        }

        void drawGetFocusScaleAnim(Canvas canvas) {
            if (mGridView != null && mGridView.hasFocus() && mIsDrawGetFocusAnim) {
                View itemView = null;

                int indexChild = mGridView.mSelectedPosition - mGridView.mFirstPosition;
                if (indexChild >= 0 && indexChild < mGridView.getChildCount()) {
                    itemView = mGridView.getChildAt(indexChild);
                }

                if (itemView == null) {
                    return;
                }

                int itemWidth = itemView.getWidth();
                int itemHeight = itemView.getHeight();

                int location[] = new int[2];
                itemView.getLocationInWindow(location);

                int locationDrawLayout[] = new int[2];
                this.getLocationInWindow(locationDrawLayout);

                // draw focus image
                if (mGridView.mDrawableFocus != null) {
                    int focusWidth = itemWidth + mLeftFocusBoundWidth + mRightFocusBoundWidth;
                    int focusHeight = itemHeight + mTopFocusBoundWidth + mBottomFocusBoundWidth;

                    canvas.save();
                    canvas.translate(location[0] - mLeftFocusBoundWidth, location[1] - locationDrawLayout[1] - mTopFocusBoundWidth);
                    canvas.scale(mScaleX, mScaleY, itemWidth / 2, itemHeight / 2);

                    mGridView.mDrawableFocus.setBounds(0, 0, focusWidth, focusHeight);
                    mGridView.mDrawableFocus.draw(canvas);
                    canvas.restore();
                }

                // draw item view
                canvas.save();
                canvas.translate(location[0], location[1]);
                canvas.scale(mScaleX, mScaleY, itemWidth / 2, itemHeight / 2);
                itemView.draw(canvas);
                canvas.restore();
            }
        }

        void drawFocusMoveAnim(Canvas canvas) {
            if (mGridView != null && mGridView.mIsDrawFocusMoveAnim) {
                View nextView = mGridView.getChildAt(mGridView.mNextSelectedPosition - mGridView.mFirstPosition);
                View curView = mGridView.getChildAt(mGridView.mSelectedPosition - mGridView.mFirstPosition);

                if ((nextView != null) && (curView != null)) {
                    int locationDrawLayout[] = new int[2];
                    this.getLocationInWindow(locationDrawLayout);

                    int location[] = new int[2];
                    nextView.getLocationInWindow(location);
                    int nextLeft = location[0];
                    int nextTop = location[1] - locationDrawLayout[1];
                    int nextWidth = nextView.getWidth();
                    int nextHeight = nextView.getHeight();

                    curView.getLocationInWindow(location);
                    int curLeft = location[0];
                    int curTop = location[1] - locationDrawLayout[1];
                    int curWidth = curView.getWidth();
                    int curHeight = curView.getHeight();

                    float animScale = mGridView.mFocusMoveAnimScale;
                    float focusLeft = curLeft + (nextLeft - curLeft) * animScale;
                    float focusTop = curTop + (nextTop - curTop) * animScale;
                    float focusWidth = curWidth + (nextWidth - curWidth) * animScale;
                    float focusHeight = curHeight + (nextHeight - curHeight) * animScale;

                    // draw focus image
                    if (mGridView.mDrawableFocus != null) {
                        canvas.save();
                        canvas.translate(
                                focusLeft - (mSelectedScaleValue - 1) / 2 * focusWidth,
                                focusTop - (mSelectedScaleValue - 1) / 2 * focusHeight);
                        canvas.scale(mSelectedScaleValue, mSelectedScaleValue, 0, 0);

                        mGridView.mDrawableFocus.setBounds(
                                0 - mLeftFocusBoundWidth,
                                0 - mTopFocusBoundWidth,
                                (int) (focusWidth + mRightFocusBoundWidth),
                                (int) (focusHeight + mBottomFocusBoundWidth));
                        mGridView.mDrawableFocus.draw(canvas);
                        canvas.restore();
                    }

                    // draw next item view
                    canvas.save();
                    canvas.translate(
                            focusLeft - (mSelectedScaleValue - 1) / 2 * focusWidth,
                            focusTop - (mSelectedScaleValue - 1) / 2 * focusHeight);
                    canvas.scale(
                            (mSelectedScaleValue * focusWidth) / nextWidth,
                            (mSelectedScaleValue * focusHeight) / nextHeight,
                            0,
                            0);
                    canvas.saveLayerAlpha(new RectF(0, 0, this.getWidth(), this.getHeight()), (int) (0xFF * animScale), Canvas.ALL_SAVE_FLAG);
                    nextView.draw(canvas);
                    canvas.restore();
                    canvas.restore();

                    // draw current item view
                    canvas.save();
                    canvas.translate(
                            focusLeft - (mSelectedScaleValue - 1) / 2 * focusWidth,
                            focusTop - (mSelectedScaleValue - 1) / 2 * focusHeight);
                    canvas.scale(
                            (mSelectedScaleValue * focusWidth) / curWidth,
                            (mSelectedScaleValue * focusHeight) / curHeight,
                            0,
                            0);
                    canvas.saveLayerAlpha(new RectF(0, 0, this.getWidth(), this.getHeight()), (int) (0xFF * (1 - animScale)), Canvas.ALL_SAVE_FLAG);
                    curView.draw(canvas);
                    canvas.restore();
                    canvas.restore();
                }
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
        }

        public class DrawHandler extends Handler {
            public void handleMessage(Message msg) {
                if (compute()) {
                    Message message = Message.obtain();
                    sendMessageDelayed(message, 50);
                }
            }
        }
    }
}