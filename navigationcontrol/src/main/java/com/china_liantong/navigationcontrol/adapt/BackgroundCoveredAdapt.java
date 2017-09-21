package com.china_liantong.navigationcontrol.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.NavigationFragment;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;

/**
 * Created by randal on 2017/9/21.
 */

public class BackgroundCoveredAdapt extends LtGridAdapter {
    private Context mContext;
    private NavigationFragment.GridViewInfo mInfo;

    public BackgroundCoveredAdapt(Context context, NavigationFragment.GridViewInfo info) {
        mContext = context;
        mInfo = info;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected View createItemView(int indexPage, int position,
                                  ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.item_content_background_covered, null);
    }

    @Override
    protected View getItemView(int indexPage, int position,
                               View convertView, ViewGroup parent) {
        View background = convertView.findViewById(R.id.item_content_background);
        TextView text = (TextView) convertView.findViewById(R.id.item_content_text);

        background.setBackground(mInfo.pictures[indexPage][position]);
        text.setText(mInfo.titles[indexPage][position]);
        text.setTextColor(mInfo.titleColor);
        text.setTextSize(mInfo.titleSize);
        return convertView;
    }

    @Override
    protected int getPageCount() {
        return mInfo.pageCount;
    }

    @Override
    protected int getItemCount(int indexPage) {
        return mInfo.perPageItemCount[indexPage];
    }

    @Override
    protected int getNumRows() {
        return mInfo.rows;
    }

    @Override
    protected int getNumColumns() {
        return mInfo.columns;
    }

    @Override
    protected int getRowSpacing() {
        return mInfo.rowSpacing;
    }

    @Override
    protected int getColumnSpacing() {
        return mInfo.columnSpacing;
    }

    @Override
    protected int getItemStartIndex(int pageIndex, int position) {
        return mInfo.itemStartIndex[pageIndex][position];
    }

    @Override
    protected int getItemRowSize(int pageIndex, int position) {
        return mInfo.itemRowSize[pageIndex][position];
    }

    @Override
    protected int getItemColumnSize(int pageIndex, int position) {
        return mInfo.itemColumnSize[pageIndex][position];
    }
}
