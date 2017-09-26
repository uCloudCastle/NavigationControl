package com.china_liantong.navigationcontrol.widgets;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class LtGridAdapter extends BaseAdapter {
    protected abstract View createItemView(int indexPage,
                                           int position, ViewGroup parent);

    //
//        /
//           @brief
//           Get the View associated with the specified position in the data set.
//          /
    protected abstract View getItemView(int indexPage,
                                        int position, View convertView,
                                        ViewGroup parent);

    //        /
//            If your provider's data is invalid, you should update your item in this method
//            There has two method to trigger this method called:
//               1. You call LtGridView.refreshData
//               2. When page changed
//           /
    void updateItemViewForInvalid(int indexPage, int position, View pItemView) {}

    public abstract int getPageCount();

    protected abstract int getItemCount(int indexPage);

    protected abstract int getNumRows();

    protected abstract int getNumColumns();

    protected abstract int getRowSpacing();

    public abstract int getColumnSpacing();

    protected abstract int getItemStartIndex(int pageIndex, int position);

    protected abstract int getItemRowSize(int pageIndex, int position);

    protected abstract int getItemColumnSize(int pageIndex, int position);

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    public int getCount() {
        int count = 0;
        for (int i = 0; i < getPageCount(); i++) {
            count += getItemCount(i);
        }
        return count;
    }

    void updateItemWidgetForInvalid(int indexPage, int position, View convertView) {
    }

    boolean getPageIndexAndPosInPage(int position, AtomicInteger pageIndex, AtomicInteger posInPage) {
        boolean bFound = false;
        int calcPos = 0;
        for (int i = 0; !bFound && i < getPageCount(); i++) {
            for (int j = 0; !bFound && j < getItemCount(i); j++) {
                if (calcPos == position) {
                    bFound = true;
                    pageIndex.set(i);
                    posInPage.set(j);
                }
                calcPos++;
            }
        }
        return bFound;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link android.view.LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        AtomicInteger pageIndex = new AtomicInteger(-1);
        AtomicInteger posInPage = new AtomicInteger(-1);
        boolean success = getPageIndexAndPosInPage(position, pageIndex, posInPage);
        if (!success) {
            return null;
        }
        View result = null;
        if (convertView == null) {
            View view = createItemView(pageIndex.get(), posInPage.get(), parent);
            if (view != null) {
                result = getItemView(pageIndex.get(), posInPage.get(), view, parent);
            }
        } else {
            result = getItemView(pageIndex.get(), posInPage.get(), convertView, parent);
        }
        return result;
    }

    /**
     * only for cell_mode,return page count in service
     *
     * @return page count
     */
    public int getOriginalPageCount() {
        return 1;
    }

    /**
     * notify the current positon of adapter when move item
     *
     * @param position current position of move action
     */
    public void updateCurrentPosition(int position) {
    }

    /**
     * for decide whether or not to calculate columnWidth and rowHeight in LtGridView onMeasure method.
     *
     * @return true when need to calculate
     * false when no need to calculate
     * when return false, need to call setColumnWidth and setRowHeight method in LtGridView.
     */
    public boolean isNeedToCalculateWH() {
        return true;
    }

    class ItemCells {
        int mStartRow;
        int mStartCol;
        int mEndRow;
        int mEndCol;
    }
}
