package com.china_liantong.navigationcontrol.item;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.china_liantong.navigationcontrol.R;

/**
 * Created by randal on 2017/9/19.
 */

public class SubMenuItemIconLeft extends BaseItem {
    public View icon;
    public TextView textView;

    public SubMenuItemIconLeft(Context context) {
        this(context, null);
    }

    public SubMenuItemIconLeft(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_submenu_iconleft, this, true);
        icon = findViewById(R.id.item_submenu_iconleft_icon);
        textView = (TextView) findViewById(R.id.item_submenu_iconleft_text);
    }
}
