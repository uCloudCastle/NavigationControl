package com.china_liantong.navigationcontrol.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by randal on 2017/9/21.
 */

public class PageView extends TextView implements LtAdapterView.OnPageChangeListener{
    private static final String SLASH = " / ";
    private String mCurrentPage = "1";
    private String mTotalPage = "1";

    public PageView(Context context) {
        this(context, null);
    }

    public PageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVisibility(INVISIBLE);
    }

    public void setTotalPage(int page) {
        mTotalPage = String.valueOf(page);
        setText(mCurrentPage + SLASH + mTotalPage);
        if (page > 1) {
            setVisibility(VISIBLE);
        } else {
            setVisibility(INVISIBLE);
        }
    }

    public void setCurrentPage(int page) {
        mCurrentPage = String.valueOf(page);
        setText(mCurrentPage + SLASH + mTotalPage);
    }

    @Override
    public void onPageChanged(int curPage) {
        setCurrentPage(curPage + 1);
    }
}
