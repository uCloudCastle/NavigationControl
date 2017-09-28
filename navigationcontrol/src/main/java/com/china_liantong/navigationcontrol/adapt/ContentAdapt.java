package com.china_liantong.navigationcontrol.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.fragment.ContentViewInfo;
import com.china_liantong.navigationcontrol.R;
import com.china_liantong.navigationcontrol.utils.CommonUtils;
import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;
import com.china_liantong.navigationcontrol.widgets.SquareMaskView;

import static com.china_liantong.navigationcontrol.fragment.ContentViewInfo.BuiltInAdapter.CONTENT_ITEM_STYLE_BACKGROUND_COVERED;
import static com.china_liantong.navigationcontrol.fragment.ContentViewInfo.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_LEFT;
import static com.china_liantong.navigationcontrol.fragment.ContentViewInfo.BuiltInAdapter.CONTENT_ITEM_STYLE_ICON_TOP;
import static com.china_liantong.navigationcontrol.fragment.ContentViewInfo.BuiltInAdapter.CONTENT_ITEM_STYLE_ONLY_TEXT;


/**
 * Created by Randal on 2017-09-21.
 */

public class ContentAdapt extends LtGridAdapter {
    private Context mContext;
    private ContentViewInfo.BuiltInAdapter mInfo;

    public ContentAdapt(Context context, ContentViewInfo.BuiltInAdapter info) {
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
        if (mInfo.perPageStyle == null || mInfo.perPageStyle.length <= indexPage) {
            return null;
        }

        switch (mInfo.perPageStyle[indexPage]) {
            case CONTENT_ITEM_STYLE_BACKGROUND_COVERED:
                return LayoutInflater.from(mContext).inflate(R.layout.item_content_background_covered, null);
            case CONTENT_ITEM_STYLE_ICON_TOP:
                return LayoutInflater.from(mContext).inflate(R.layout.item_content_icon_top, null);
            case CONTENT_ITEM_STYLE_ICON_LEFT:
                return LayoutInflater.from(mContext).inflate(R.layout.item_content_icon_left, null);
            case CONTENT_ITEM_STYLE_ONLY_TEXT:
                return LayoutInflater.from(mContext).inflate(R.layout.item_content_only_text, null);
            default:
                return null;
        }
    }

    @Override
    protected View getItemView(int indexPage, int position,
                               View convertView, ViewGroup parent) {
        View picture = convertView.findViewById(R.id.item_content_picture);
        TextView title = (TextView) convertView.findViewById(R.id.item_content_title);
        TextView subtitle = (TextView) convertView.findViewById(R.id.item_content_subtitle);
        View icon = convertView.findViewById(R.id.item_content_icon);

        if (picture != null && mInfo.pictures != null
                && mInfo.pictures.length > indexPage
                && mInfo.pictures[indexPage].length > position
                && mInfo.pictures[indexPage][position] != null) {
            SquareMaskView view = CommonUtils.safeTypeConvert(picture, SquareMaskView.class);
            if (view != null) {
                view.setForegroundImage(mInfo.pictures[indexPage][position]);
            } else {
                picture.setBackground(mInfo.pictures[indexPage][position]);
            }
        }
        if (title != null && mInfo.titles != null
                && mInfo.titles.length > indexPage
                && mInfo.titles[indexPage].length > position
                && mInfo.titles[indexPage][position] != null
                && !mInfo.titles[indexPage][position].isEmpty()) {
            title.setVisibility(View.VISIBLE);
            title.setText(mInfo.titles[indexPage][position]);
            title.setTextColor(mInfo.titleColor);
            title.setTextSize(mInfo.titleSize);
        }
        if (subtitle != null && mInfo.subtitles != null
                && mInfo.subtitles.length > indexPage
                && mInfo.subtitles[indexPage].length > position
                && mInfo.subtitles[indexPage][position] != null
                && !mInfo.subtitles[indexPage][position].isEmpty()) {
            if (icon != null) {
                icon.setVisibility(View.VISIBLE);
            }
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(mInfo.subtitles[indexPage][position]);
            subtitle.setTextColor(mInfo.subtitleColor);
            subtitle.setTextSize(mInfo.subtitleSize);
        }

        return convertView;
    }

    @Override
    public int getPageCount() {
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
    public int getColumnSpacing() {
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
