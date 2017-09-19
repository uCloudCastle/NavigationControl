package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.utils.LogUtils;
import com.china_liantong.navigationcontrol.widgets.LtAdapterView;
import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;
import com.china_liantong.navigationcontrol.widgets.LtGridView;

import java.util.List;

/**
 * Created by randal on 2017/9/14.
 */

public class SubMenu extends FrameLayout {
    private static final int CONTENT_SHADOW_BOTTOM = 25;

    private Activity mActivity;
    private DataHolder mDataHolder;
    private View lastSelectedView;
    private boolean hasFocus;

    public SubMenu(Context context) {
        this(context, null);
    }

    public SubMenu(Context context, AttributeSet attrs) {
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

        final LtGridView gridView = new LtGridView(mActivity);
        gridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_VERTICAL);
        gridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
        gridView.setAdapter(new GridViewAdapter(mActivity));
        gridView.setFocusDrawable(getResources().getDrawable(R.drawable.app_selected));
        gridView.setSelectPadding(15, 15, 16, 13);

        gridView.setFadingEdgeEnabled(true);
        gridView.setVerticalPageSpacing(mDataHolder.rowSpacing);
        gridView.setShadowBottom(CONTENT_SHADOW_BOTTOM);
        gridView.setFadingEdgeDrawable(getResources().getDrawable(R.drawable.gridview_shading));
        gridView.setFocusScaleAnimEnabled(false);

//        public static final int KEYCODE_DPAD_DOWN = 20;
//        public static final int KEYCODE_DPAD_LEFT = 21;
//        public static final int KEYCODE_DPAD_RIGHT = 22;
//        public static final int KEYCODE_DPAD_UP = 19;
        gridView.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.KEYCODE_DPAD_UP:
                        LogUtils.d(gridView.getSelectedItemId() + " " + gridView.getSelectedItemPosition());
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

        gridView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                hasFocus = b;
                if (lastSelectedView != null) {
                    if (hasFocus) {
                        lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_blue));
                    } else {
                        lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_dark));
                    }
                }
            }
        });
        gridView.setOnItemSelectedListener(new LtAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemGoingTo(View view, int position) {
                if (view == null) {
                    return;
                }
                View bg = view.findViewById(R.id.item_submenu_iconleft_background);
                if (lastSelectedView == bg) {
                    return;
                }

                if (lastSelectedView != null) {
                    lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_dark));
                }
                if (hasFocus) {
                    bg.setBackground(getResources().getDrawable(R.drawable.app_bg_blue));
                }
                lastSelectedView = bg;
            }

            @Override
            public void onItemSelected(LtAdapterView<?> parent, View view, int position, long id) {}

            @Override
            public void onNothingSelected(LtAdapterView<?> parent) {}
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(gridView, params);
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
            return LayoutInflater.from(mContext).inflate(R.layout.item_submenu_iconleft, null);
        }

        @Override
        protected View getItemView(int indexPage, int position,
                                   View convertView, ViewGroup parent) {

            View icon = convertView.findViewById(R.id.item_submenu_iconleft_icon);
            TextView text = (TextView) convertView.findViewById(R.id.item_submenu_iconleft_text);

            text.setText(mDataHolder.pairs.get(indexPage * mDataHolder.fullDisplayNumber + position).first);
            text.setTextSize(mDataHolder.textSize);
            text.setTextColor(mDataHolder.textColor);

            int itemHeight = (getHeight() - CONTENT_SHADOW_BOTTOM) / mDataHolder.fullDisplayNumber - mDataHolder.rowSpacing;
            int unit = itemHeight / 8;
            RelativeLayout.LayoutParams iconlp = (RelativeLayout.LayoutParams) icon.getLayoutParams();
            iconlp.setMargins(unit * 3, unit, 0, unit);
            iconlp.width = 6 * unit;
            iconlp.height = 6 * unit;
            icon.setLayoutParams(iconlp);
            icon.setBackground(mDataHolder.pairs.get(indexPage * mDataHolder.fullDisplayNumber + position).second);
            return convertView;
        }

        @Override
        protected int getPageCount() {
            return (mDataHolder.pairs.size() / mDataHolder.fullDisplayNumber) + 1;
        }

        @Override
        protected int getItemCount(int indexPage) {
            int size = mDataHolder.pairs.size();
            int retVal = size - indexPage * mDataHolder.fullDisplayNumber;
            return retVal > mDataHolder.fullDisplayNumber ? mDataHolder.fullDisplayNumber : retVal;
        }

        @Override
        protected int getNumRows() {
            return mDataHolder.fullDisplayNumber;
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

    public static class DataHolder {
        private List<Pair<String, Drawable>> pairs;
        private int fullDisplayNumber;
        private int textSize;
        private int textColor;
        private int rowSpacing;

        public DataHolder() {
        }

        public DataHolder subPairs(List<Pair<String, Drawable>> p) {
            pairs = p;
            return this;
        }

        public DataHolder fullDisplayNumber(int number) {
            fullDisplayNumber = number;
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
