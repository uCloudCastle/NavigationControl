package com.china_liantong.navigationcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.widgets.LtAdapterView;
import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;
import com.china_liantong.navigationcontrol.widgets.LtGridView;
import com.china_liantong.navigationcontrol.widgets.SlantedTextView;

import java.util.List;

import static com.china_liantong.navigationcontrol.SubMenu.DataHolder.SUBMENU_ITEM_STYLE_ICON_LEFT;

/**
 * Created by randal on 2017/9/14.
 */

public class SubMenu extends FrameLayout {
    private static final float SQUARE_ROOT_OF_TWO = 1.41421356f;

    private Activity mActivity;
    private DataHolder mDataHolder;
    private SubMenuListener mListener;

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

    public void setSubMenuListener(SubMenuListener l) {
        if (l != null) mListener = l;
    }

    private void initView() {
        if (mDataHolder.pairs == null || mDataHolder.pairs.size() == 0) {
            return;
        }

        final LtGridView gridView = new LtGridView(mActivity);
        gridView.setScrollOrientation(LtGridView.ScrollOrientation.SCROLL_VERTICAL);
        gridView.setScrollMode(LtGridView.ScrollMode.SCROLL_MODE_PAGE);
        gridView.setAdapter(new SubMenuAdapter(mActivity));
        gridView.setFocusDrawable(getResources().getDrawable(R.drawable.app_selected));
        gridView.setSelectPadding(15, 15, 16, 13);

        gridView.setFadingEdgeEnabled(true);
        gridView.setVerticalPageSpacing(mDataHolder.rowSpacing);
        gridView.setShadowBottom(mDataHolder.fadingWidth);
        gridView.setFadingEdgeDrawable(getResources().getDrawable(R.drawable.gridview_shading));
        gridView.setFocusScaleAnimEnabled(false);

        gridView.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean b) {
                hasFocus = b;
                if (lastSelectedView != null) {
                    //if (hasFocus) {
                    //    lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_blue));
                    //} else {
                    //    lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_dark));
                    //}
                }
            }
        });
        gridView.setOnItemSelectedListener(new LtAdapterView.OnItemSelectedListener() {
            @Override
            public void onItemGoingTo(View view, int position) {
                if (view == null) {
                    return;
                }
                View bg = view.findViewById(R.id.item_submenu_background);
                if (lastSelectedView == bg) {
                    return;
                }

                if (lastSelectedView != null) {
                    lastSelectedView.setBackground(getResources().getDrawable(R.drawable.app_bg_dark));
                }
                //if (hasFocus) {
                    bg.setBackground(getResources().getDrawable(R.drawable.app_bg_blue));
                //}
                lastSelectedView = bg;
            }

            @Override
            public void onItemSelected(LtAdapterView<?> parent, View view, int position, long id) {
                mListener.onItemGetFocus(position);
            }

            @Override
            public void onNothingSelected(LtAdapterView<?> parent) {}
        });

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(gridView, params);
    }

    private class SubMenuAdapter extends LtGridAdapter {
        private Context mContext;

        SubMenuAdapter(Context context) {
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
            if (mDataHolder.itemStyle == SUBMENU_ITEM_STYLE_ICON_LEFT) {
                return LayoutInflater.from(mContext).inflate(R.layout.item_submenu_icon_left, null);
            } else {
                return LayoutInflater.from(mContext).inflate(R.layout.item_submenu_icon_top, null);
            }
        }

        @Override
        protected View getItemView(int indexPage, int position,
                                   View convertView, ViewGroup parent) {
            View icon = convertView.findViewById(R.id.item_submenu_icon);
            TextView text = (TextView) convertView.findViewById(R.id.item_submenu_text);
            SlantedTextView tag= (SlantedTextView) convertView.findViewById(R.id.item_submenu_tag);

            int actualIndex = indexPage * mDataHolder.fullDisplayNumber + position;
            text.setText(mDataHolder.pairs.get(actualIndex).first);
            icon.setBackground(mDataHolder.pairs.get(actualIndex).second);
            text.setTextSize(mDataHolder.textSize);
            text.setTextColor(mDataHolder.textColor);

            int itemHeight = (getHeight() - mDataHolder.fadingWidth) / mDataHolder.fullDisplayNumber - mDataHolder.rowSpacing;
            int itemWidth = getWidth();
            int min = itemHeight < itemWidth ? itemHeight : itemWidth;

            if (mDataHolder.tagList != null
                    && mDataHolder.tagList.size() > actualIndex
                    && mDataHolder.tagList.get(actualIndex) != null
                    && !mDataHolder.tagList.get(actualIndex).isEmpty()) {
                RelativeLayout.LayoutParams taglp = (RelativeLayout.LayoutParams) tag.getLayoutParams();
                taglp.width = min / 2;
                taglp.height = min / 2;
                tag.setLayoutParams(taglp);
                tag.setSlantedLength((int)(min * SQUARE_ROOT_OF_TWO / 4));
                tag.setSlantedBackgroundColor(getResources().getColor(R.color.submenu_item_slanted_textview_background));
                tag.setText(mDataHolder.tagList.get(actualIndex));
            }

            RelativeLayout.LayoutParams iconlp = (RelativeLayout.LayoutParams) icon.getLayoutParams();
            if (mDataHolder.itemStyle == SUBMENU_ITEM_STYLE_ICON_LEFT) {
                int unit = itemHeight / 8;
                iconlp.setMargins(unit * 3, unit, 0, 0);
                iconlp.width = 6 * unit;
                iconlp.height = 6 * unit;
            } else {
                int unit = itemWidth / 9;
                iconlp.setMargins(unit * 3, unit, 0, 0);
                iconlp.width = 3 * unit;
                iconlp.height = 3 * unit;
            }
            icon.setLayoutParams(iconlp);
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

    public interface SubMenuListener {
        void onItemGetFocus(int newPos);
    }

    public static class DataHolder {
        public static final int SUBMENU_ITEM_STYLE_ICON_LEFT = 0;
        public static final int SUBMENU_ITEM_STYLE_ICON_TOP = 1;

        private List<Pair<String, Drawable>> pairs;
        private int fullDisplayNumber;
        private int textSize;
        private int textColor;
        private int rowSpacing;
        private List<String> tagList;
        private int fadingWidth;
        private int itemStyle;

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

        public DataHolder tagList(List<String> list) {
            tagList = list;
            return this;
        }

        public DataHolder fadingWidth(int pixel) {
            fadingWidth = pixel;
            return this;
        }

        public DataHolder itemStyle(int style) {
            itemStyle = style;
            return this;
        }
    }
}
