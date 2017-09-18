package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;
import com.china_liantong.navigationcontrol.widgets.LtGridView;

import java.util.List;

/**
 * Created by randal on 2017/9/14.
 */

public class SubNavigationBar extends FrameLayout {
    private Context mActivity;
    private DataHolder mDataHolder;

    public SubNavigationBar(Context context) {
        this(context, null);
    }

    public SubNavigationBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDataHolder(Activity activity, DataHolder holder) {
        if (activity != null && holder != null) {
            mActivity = activity;
            mDataHolder = holder;
            initView();
        }
    }

    private void initView() {
        if (mDataHolder.pairs == null || mDataHolder.pairs.size() == 0) {
            return;
        }

        LtGridView gridView = new LtGridView(mActivity);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(gridView, params);
        gridView.setBackgroundColor(Color.CYAN);

        asyncRelayout();
    }

    private void asyncRelayout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams mainParams = getLayoutParams();
                mainParams.width = mDataHolder.width;
                setLayoutParams(mainParams);

            }
        }, 200);
    }

    private class GridViewAdapter extends LtGridAdapter {
        private Context mContext;

        GridViewAdapter(Context context) {
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
//            return LayoutInflater.from(mContext).inflate(R.layout.item_grid_settings, null);
            return new View(mActivity);
        }

        @Override
        protected View getItemView(int indexPage, int position,
                                   View convertView, ViewGroup parent) {
//            ImageView imageView = (ImageView) convertView.findViewById(R.id.mytv_item_grid_settings_image);
//            TextView title = (TextView) convertView.findViewById(R.id.mytv_item_grid_settings_title);
//            TextView content = (TextView) convertView.findViewById(R.id.mytv_item_grid_settings_content);
//
//            imageView.setBackgroundResource(mThumbIds[position]);
//            title.setText(mTitles[position]);
//
//            if (position == 0) {
//                int mbyte = mRecordManger.getSpaceWarningSize();
//                content.setText(mByte2ReadableString(mbyte));
//            } else if (position == 1) {
//                content.setTextColor(getResources().getColor(R.color.settings_cover_center_content_text_color));
//                content.setText(getResources().getString(R.string.fragment_record_settings_cover_center_content));
//            } else if (position == 2) {
//                int seconds = mRecordManger.getMaxRecordTime();
//                content.setText(second2ReadableString(seconds));
//            }

            return convertView;
        }

        @Override
        protected int getPageCount() {
            return 1;
        }

        @Override
        protected int getItemCount(int indexPage) {
            return 3;
        }

        @Override
        protected int getNumRows() {
            return 1;
        }

        @Override
        protected int getNumColumns() {
            return 1;
        }

        @Override
        protected int getRowSpacing() {
            return mDataHolder.rowSpacing;
        }

        @Override
        protected int getColumnSpacing() {
            return 0;
        }

        @Override
        protected int getItemStartIndex(int pageIndex, int position) {
            int retVal = 0;
            switch (position) {
                case 0:
                    retVal = 0;
                    break;
                case 1:
                    retVal = 1;
                    break;
                case 2:
                    retVal = 2;
                    break;
            }
            return retVal;
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

    public static class DataHolder {
        private List<Pair<String, Drawable>> pairs;
        private int width;
        private int itemHeight;
        private int iconWidth;
        private int iconHeight;
        private int textSize;
        private int textColor;
        private int rowSpacing;

        public DataHolder() {
        }

        public DataHolder subPairs(List<Pair<String, Drawable>> p) {
            pairs = p;
            return this;
        }

        public DataHolder width(int w) {
            width = w;
            return this;
        }

        public DataHolder itemHeight(int h) {
            itemHeight = h;
            return this;
        }

        public DataHolder iconWidth(int width) {
            iconWidth = width;
            return this;
        }

        public DataHolder iconHeight(int height) {
            iconHeight = height;
            return this;
        }

        public DataHolder textSize(int size) {
            textSize = size;
            return this;
        }

        public DataHolder textColor(int color) {
            textColor = color;
            return this;
        }

        public DataHolder rowSpacing(int spacing) {
            rowSpacing = spacing;
            return this;
        }
    }
}
