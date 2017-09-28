package com.china_liantong.navigationcontrol.fragment;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.china_liantong.navigationcontrol.widgets.LtGridAdapter;

/**
 * Created by Randal on 2017-09-26.
 */

public class ContentViewInfo {
    public ContentViewInfo() {}
    public int marginRight;
    public int fadingWidth;
    public BuiltInAdapter builtInAdapter;
    public LtGridAdapter customAdapter;
    public View customView;

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
    }
}
