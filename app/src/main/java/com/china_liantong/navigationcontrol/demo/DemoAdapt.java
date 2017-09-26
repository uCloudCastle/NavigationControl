package com.china_liantong.navigationcontrol.demo;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;

/**
 * Created by randal on 2017/9/26.
 */

public class DemoAdapt extends LtGridAdapter {
    private Context mContext;

    public DemoAdapt(Context context) {
        mContext = context;
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
        return LayoutInflater.from(mContext).inflate(R.layout.item_demo, null);
    }

    @Override
    protected View getItemView(int indexPage, int position,
                               View convertView, ViewGroup parent) {
        TextView textView = (TextView) convertView.findViewById(R.id.item_demo_textview);
        String str = textView.getText().toString() + " " + String.valueOf(position);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        textView.setText(str);

        return convertView;
    }

    @Override
    protected int getPageCount() {
        return 1;
    }

    @Override
    protected int getItemCount(int indexPage) {
        return 16;
    }

    @Override
    protected int getNumRows() {
        return 4;
    }

    @Override
    protected int getNumColumns() {
        return 4;
    }

    @Override
    protected int getRowSpacing() {
        return 10;
    }

    @Override
    protected int getColumnSpacing() {
        return 10;
    }

    @Override
    protected int getItemStartIndex(int pageIndex, int position) {
        return position;
    }

    @Override
    protected int getItemRowSize(int pageIndex, int position) {
        return 1;
    }

    @Override
    protected int getItemColumnSize(int pageIndex, int position) {
        return 1;
    }
}
