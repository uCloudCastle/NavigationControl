package com.china_liantong.navigationcontrol.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by randal on 2017/9/14.
 */

public abstract class BaseItem extends RelativeLayout {

    public BaseItem(Context context) {
        this(context, null);
    }

    public BaseItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
